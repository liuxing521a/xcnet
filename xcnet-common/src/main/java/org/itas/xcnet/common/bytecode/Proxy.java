/**
 * 
 */
package org.itas.xcnet.common.bytecode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.itas.xcnet.common.utils.ClassHelper;
import org.itas.xcnet.common.utils.ReflectUtils;

/**
 * 代理
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月11日上午10:10:25
 */
public abstract class Proxy 
{
	/**
	 * package name
	 */
	private static final String PACKAGE_NAME = Proxy.class.getPackage().getName();

	/**
	 * proxy suffix
	 */
	private static final AtomicInteger PROXY_CLASS_COUNT = new AtomicInteger(0);
	
	
	public static final InvocationHandler RETURN_NULL_INVOKER = new InvocationHandler() 
	{
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
		{
			return null;
		}
	};
	
	public static final InvocationHandler THROW_UNSUPPORTED_INVOKER = new InvocationHandler()
	{
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
		{
			throw new UnsupportedOperationException("Method [" + ReflectUtils.getName(method) + "] unimplemented.");
		}
	};
	
	private static final Map<ClassLoader, Map<String, Object>> PROXY_CACHE_MAP = new WeakHashMap<>();
	
	private static final Object PendingGenerationMarker = new Object();
	
	public static Proxy getProxy(Class<?>...interfaces)
	{
		return getProxy(ClassHelper.getClassLoader(interfaces[0]), interfaces);
	}
	
	public static Proxy getProxy(ClassLoader loader, Class<?>[] interfaces)
	{
		if (interfaces.length > Short.MAX_VALUE)
		{
			throw new IllegalArgumentException("interface limit exceeded");
		}
		
		StringBuilder sb = new StringBuilder();
		for (Class<?> infc : interfaces) 
		{
			if (!infc.isInterface())
			{
				throw new RuntimeException(infc.getName() + " is not a interface");
			}
			
			Class<?> tmp = null;
			try
			{
				tmp = Class.forName(infc.getName(), false, loader);
			}
			catch (ClassNotFoundException e)
			{
			}
			
			if (tmp != infc)
			{
				throw new IllegalArgumentException(infc + "is not visible from class loader");
			}
			
			sb.append(infc.getName()).append(';');
		}
		
		// user interface class name list as key
		String key = sb.toString();
		
		// get cache by class loader
		Map<String, Object> cache = PROXY_CACHE_MAP.get(loader);
		if (cache == null)
		{
			cache = new HashMap<>();
			PROXY_CACHE_MAP.putIfAbsent(loader, cache);
			cache = PROXY_CACHE_MAP.get(loader);
		}
		
		Proxy proxy = null;
		synchronized (cache) 
		{
			do
			{
				Object value = cache.get(key);
				if (value instanceof Reference<?>)
				{
					proxy = (Proxy)((Reference<?>)value).get();
					if (proxy != null) 
					{
						return proxy;
					}
				}
				
				if (value == PendingGenerationMarker)
				{
					try 
					{
						cache.wait();
					}
					catch(InterruptedException e)
					{
					}
				}
				else
				{
					cache.put(key, PendingGenerationMarker);
					break;
				}
			}
			while (true);
		}
		
		int id = PROXY_CLASS_COUNT.getAndIncrement();
		String pkg = null;
		
		ClassGenerator ccp = null, ccm = null;
		try
		{
			ccp = ClassGenerator.newInstance(loader);
			
			Set<String> worked = new HashSet<>();
			List<Method> methods = new ArrayList<>();
			
			for (Class<?> infc : interfaces)
			{
				if (!Modifier.isPublic(infc.getModifiers()))
				{
					String npkg = infc.getPackage().getName();
					if (pkg == null)
					{
						pkg = npkg;
					}
					else
					{
						if (!pkg.equals(npkg))
						{
							throw new IllegalArgumentException("non-public interfaces from different packages");
						}
					}
				}
				ccp.addInterface(infc);
				
				for (Method method : infc.getMethods())
				{
					String desc = ReflectUtils.getDesc(method);
					if (worked.contains(desc))
					{
						continue;
					}
					worked.add(desc);
					
					int ix = methods.size();
					Class<?> returnType = method.getReturnType();
					Class<?>[] paramsTypes = method.getParameterTypes();
					
					StringBuilder code = new StringBuilder();
					code.append("Object[] args = new Object[").append(paramsTypes.length).append("];");
					for (int i = 0; i < paramsTypes.length; i++)
					{
						code.append("args[").append(i).append("] = ($w)$").append(i + 1).append(';');
					}
					code.append("Object ret = handler.invoke(this, methods[").append(ix).append("], args);");
					
					if (!Void.TYPE.equals(returnType))
					{
						code.append("return ").append(asArgument(returnType, "ret")).append(';');
					}
					
					methods.add(method);
					ccp.addMethod(method.getName(), method.getModifiers(), 
							returnType, paramsTypes, method.getExceptionTypes(), code.toString());
				}
			}
			
			if (pkg == null)
			{
				pkg = PACKAGE_NAME;
			}
			
			// create ProxyInstance class
			String pcn = pkg + ".proxy" + id;
			ccp.setClassName(pcn);
			ccp.addField("public static java.lang.reflect.Method[] methods;");
			ccp.addField("private " + InvocationHandler.class.getName() + " handler;");
			ccp.addConstructor(Modifier.PUBLIC, new Class<?>[]{InvocationHandler.class}, new Class<?>[0], "handler=$1;");
			Class<?> clazz = ccp.toClass();
			clazz.getField("methods").set(null, methods.toArray(new Method[0]));
			
			// create Proxy class
			String fcn = Proxy.class.getName() + id;
			ccm = ClassGenerator.newInstance(loader);
			ccm.setClassName(fcn);
			ccm.addDefaultConstructor();
			ccm.setSuperClass(Proxy.class);
			ccm.addMethod("public Object newInstance(" + InvocationHandler.class.getName() +
				" h) {  return new " + pcn + "($1); }");
			Class<?> pc = ccm.toClass();
			proxy = (Proxy)pc.newInstance();
			
			return proxy;
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e.getMessage(), e);
		} 
		finally
		{
			if (ccp != null)
			{
				ccp.release();
			}
			if (ccm != null)
			{
				ccm.release();
			}
			
			synchronized (cache) 
			{
				if (proxy == null)
				{
					cache.remove(key);
				}
				else
				{
					cache.put(key, new WeakReference<>(proxy));
				}
			
				cache.notify();
			}
		}
	}
	
	protected Proxy()
	{
	}
	
	
	public abstract Object newInstance(InvocationHandler handler);
	
	public Object newInstance()
	{
		return newInstance(THROW_UNSUPPORTED_INVOKER);
	}
	
	private static String asArgument(Class<?> cls, String name)
	{
		if (cls.isPrimitive())
		{
			if (Boolean.TYPE == cls)
			{
				return String.format("%s == null ? false : ((Boolean)%s).booleanValue()", name, name);
			}
			else if (Byte.TYPE == cls)
			{
				return String.format("%s == null ? (byte)0 : ((Byte)%s).byteValue()", name, name);
			}
			else if (Character.TYPE == cls)
			{
				return String.format("%s == null ? (char)0 : ((Character)%s).charValue()", name, name);
			}
			else if (Double.TYPE == cls)
			{
				return String.format("%s == null ? 0D : ((Double)%s).doubleValue()", name, name);
			}
			else if (Float.TYPE == cls)
			{
				return String.format("%s == null ? 0F : ((Float)%s).floatValue()", name, name);
			}
			else if (Integer.TYPE == cls)
			{
				return String.format("%s == null ? 0 : ((Integer)%s).intValue()", name, name);
			}
			else if (Long.TYPE == cls)
			{
				return String.format("%s == null ? 0L : ((Long)%s).longValue()", name, name);
			}
			else if (Short.TYPE == cls)
			{
				return String.format("%s == null ? (short)0 : ((Short)%s).shortValue()", name, name);
			}
			else
			{
				throw new RuntimeException(name  + "is unkown primitive type");
			}
		} 
		else
		{
			return String.format("(%s)%s", ReflectUtils.getName(cls), name);
		}
		
	}
	
}
