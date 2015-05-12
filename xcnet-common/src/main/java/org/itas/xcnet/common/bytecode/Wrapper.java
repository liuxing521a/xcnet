/**
 * 
 */
package org.itas.xcnet.common.bytecode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;

import org.itas.xcnet.common.utils.ClassHelper;
import org.itas.xcnet.common.utils.ReflectUtils;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月11日下午3:02:04
 */
public abstract class Wrapper 
{
	private static AtomicLong WRAPPER_CLASS_COUNT = new AtomicLong();
	
	private static final Map<Class<?>, Wrapper> WRAPPER_MAP = new ConcurrentHashMap<>();
	
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	
	private static final String[] OBJECT_METHODS = new String[]{"getClass", "hashCode", "toString", "equals"};
	
	private static final Wrapper OBJECT_WRAPPER = new Wrapper() 
	{

		@Override
		public String[] getPropertynames() 
		{
			return EMPTY_STRING_ARRAY;
		}

		@Override
		public Class<?> getPropertyType(String name) 
		{
			return null;
		}

		@Override
		public boolean hasProperty(String name) 
		{
			return false;
		}

		@Override
		public Object getPropertyValue(Object instance, String name) 
		{
			throw new NoSuchPropertyException("Property [" + name + "] not found.");
		}

		@Override
		public void setPropertyValue(Object instance, String name, Object value) 
		{
			 throw new NoSuchPropertyException("Property [" + name + "] not found.");
		}

		@Override
		public String[] getMethodNames() 
		{
			return OBJECT_METHODS;
		}

		@Override
		public String[] getDeclaredMethodNames() 
		{
			return OBJECT_METHODS;
		}

		@Override
		public Object invokeMethod(Object instance, String mn, Class<?>[] types, Object[] args) throws NoSuchMethodException, InvocationTargetException 
		{
			if ("getclass".equals(mn))
			{
				return instance.getClass();
			}
			else if ("hashCode".equals(mn))
			{
				return instance.hashCode();
			}
			else if ("toString".equals(mn))
			{
				return instance.toString();
			}
			else if ("eqauls".endsWith(mn))
			{
				if (args.length == 1)
				{
					return instance.equals(args[0]);
				} 
				else
				{
					throw new IllegalArgumentException("Invoke method [" + mn + "] argument number error.");
				}
			}
			else
			{
				throw new NoSuchMethodException("Method [" + mn + "] not found.");
			}
		}
	};
	
	/**
	 * 获取wrapper
	 * 
	 * @param cls class实体
	 * @return Wrapper instance(not null).
	 */
	public static Wrapper getWrapper(Class<?> cls)
	{
		for ( ; ClassGenerator.isDynamicClass(cls);)
		{
			cls = cls.getSuperclass();
		}
		
		if (cls == Object.class)
		{
			return OBJECT_WRAPPER;
		}
		
		Wrapper ret = WRAPPER_MAP.get(cls);
		if (ret == null)
		{
			ret = makeWrapper(cls);
			WRAPPER_MAP.put(cls, ret);
		}
		
		return ret;
	}
	
	/**
	 * get property name array.<p>
	 * 
	 * @return property name array.
	 */
	public abstract String[] getPropertynames();
	
	/**
	 * get property type.<p>
	 * 
	 * @param name property name.
	 * @return Property type or nul.
	 */
	public abstract Class<?> getPropertyType(String name);
	
	/**
	 * has property
	 * 
	 * @param name property name.
	 * @return has or has not.
	 */
	public abstract boolean hasProperty(String name);
	
	/**
	 * get property value.<p>
	 * 
	 * @param instance instance.
	 * @param name property name.
	 * @return value.
	 */
	public abstract Object getPropertyValue(Object instance, String name);
	
	/**
	 * set property value.<p>
	 * 
	 * @param instance instance
	 * @param name property name.
	 * @param value pv property value.
	 */
	public abstract void setPropertyValue(Object instance, String name, Object value);
	
	/**
	 * get property value.<p>
	 * 
	 * @param instance instance
	 * @param names property name array.
	 * @return value array.
	 */
	public Object[] getPropertyValues(Object instance, String[] names)
	{
		Object[] ret = new Object[names.length];
		for (int i = 0; i < names.length; i++) 
		{
			ret[i] = getPropertyValue(instance, names[i]);
		}
		
		return ret;
	}
	
	/**
	 * set property value.
	 * 
	 * @param instance instance.
	 * @param names property name array.
	 * @param vs property value array.
	 */
	public void setPropertyValues(Object instance, String[] names, Object[] values) throws NoSuchPropertyException, IllegalArgumentException
	{
		if (names.length != values.length)
		{
			throw new IllegalArgumentException("names.length != values.length");
		}
		
		for (int i = 0; i < values.length; i++) 
		{
			setPropertyValue(instance, names[i], values[i]);
		}
	}
	
	/**
	 * get method name array<p>
	 * 
	 * @return method name array
	 */
	public abstract String[] getMethodNames();
	
	/**
	 * get method name array.<p>
	 * 
	 * @return method name array.
	 */
	public abstract String[] getDeclaredMethodNames();
	
	/**
	 * has method<p>
	 * 
	 * @param name method name
	 * @return has or not
	 */
	public boolean hasMethod(String name)
	{
		for (String mn : getMethodNames()) 
		{
			if (mn.equals(name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * invoke method.<p>
	 * 
	 * @param instance instance.
	 * @param mn method name.
	 * @param types 
	 * @param args argument array.
	 * @return return value.
	 */
	public abstract Object invokeMethod(Object instance, String mn, Class<?>[] types, Object[] args)throws NoSuchMethodException, InvocationTargetException;;
	
	private static Wrapper makeWrapper(Class<?> cls)
	{
		if (cls.isPrimitive())
		{
			throw new IllegalArgumentException("can not create wrapper for primitve type:" + cls);
		}
		
		String name = cls.getName();
		ClassLoader cl = ClassHelper.getClassLoader(cls);
		
		StringBuilder c1 = new StringBuilder("public void setPropertyValue(Object o, String n, Object v){");
		StringBuilder c2 = new StringBuilder("public Object getPropertyValue(Object o, String n){");
		StringBuilder c3 = new StringBuilder("public Object invokeMethod(Object o, String n, Class[] p, Object[] v) throws " + InvocationTargetException.class.getName()).append("{");
	
		c1.append(name).append(" w; try{ w = ((").append(name).append(")$1); } catch(Throwable e){ throw new IllegalArgumentException(e); }");
		c2.append(name).append(" w; try{ w = ((").append(name).append(")$1); } catch(Throwable e){ throw new IllegalArgumentException(e); }");
		c3.append(name).append(" w; try{ w = ((").append(name).append(")$1); } catch(Throwable e){ throw new IllegalArgumentException(e); } try{");
		
		Map<String, Class<?>> pts = new HashMap<>();	// <property name, property types>
		Map<String, Method> ms = new LinkedHashMap<>();	// <method desc, Method instance>
		List<String> mns = new ArrayList<>();	// method names
		List<String> dmns = new ArrayList<>();	// declaring method names.
		
		// get all public field
		for (Field f : cls.getFields())
		{
			if (Modifier.isStatic(f.getModifiers()) || 
				Modifier.isTransient(f.getModifiers()))
			{
				continue;
			}

			String fn = f.getName();
			Class<?> ft = f.getType();
			c1.append(" if ($2.equals(\"").append(fn).append("\")){ w.").append(fn).append('=').append(arg(ft, "$3")).append("; return; }");
			c2.append(" if ($2.equals(\"").append(fn).append("\")){ return ($w)w.").append(fn).append("; }");
			pts.put(fn, ft);
		}
		
		// get all public method
		Method[] methods = cls.getMethods();
		for (Method m : methods) 
		{
			if (m.getDeclaringClass() == Object.class)
			{
				continue;
			}
			
			String mn = m.getName();
			c3.append(" if (\"").append(mn).append("\".equals($2)");
			
			boolean override = false;
			for (Method m2 : methods) 
			{
				if (m != m2 && m.getName().equals(m2.getName()))
				{
					override = true;
					break;
				}
			}
			
			if (override)
			{
				int len = m.getParameterTypes().length;
				c3.append(" && ").append("$3.length == ").append(len);
				if (len > 0)
				{
					for (int i = 0; i < len; i++) 
					{
						c3.append(" && ").append("$3[").append(i).append("] == ")
							.append(m.getParameterTypes()[i].getName()).append(".class");
					}
				}
			}
			c3.append(") {");
			
			if (m.getReturnType() == Void.TYPE)
			{
				c3.append(" w.").append(mn).append('(').append(args(m.getParameterTypes(), "$4")).append("); return null;");
			}
			else
			{
				c3.append(" return ($w)w.").append(mn).append('(').append(args(m.getParameterTypes(), "$4")).append(");");
			}
			c3.append('}');
			
			mns.add(mn);
			if (m.getDeclaringClass() == cls)
			{
				dmns.add(mn);
			}
			ms.put(ReflectUtils.getDesc(m), m);
		}
		c3.append(" } catch(Throwable e) {");
		c3.append("     throw new java.lang.reflect.InvocationTargetException(e);");
		c3.append(" }");
		c3.append(" throw new " + NoSuchMethodException.class.getName() + "(\"Not found method \\\"\"+$2+\"\\\" in class " + cls.getName() + ".\"); }");
	
		// deal with get/set method.
		Matcher matcher;
		for (Map.Entry<String, Method> entry : ms.entrySet()) 
		{
			String md = entry.getKey();
			Method method = entry.getValue();
			if ((matcher = ReflectUtils.GETTER_METHOD_DESC_PATTERN.matcher(md)).matches())
			{
				String pn = propertyName(matcher.group(1));
				c2.append(" if($2.equals(\"").append(pn).append("\")) { return ($w)w.").append(method.getName()).append("(); }");
				pts.put(pn, method.getReturnType());
			}
			else if ((matcher = ReflectUtils.IS_HAS_CAN_METHOD_DESC_PATTERN.matcher(md)).matches())
			{
				String pn = propertyName(matcher.group(1));
				c2.append(" if($2.equals(\"").append(pn).append("\")) { return ($w)w.").append(method.getName()).append("(); }");
				pts.put(pn, method.getReturnType());
			}
			else if ((matcher = ReflectUtils.SETTER_METHOD_DESC_PATTERN.matcher(md)).matches())
			{
				Class<?> pt = method.getParameterTypes()[0];
				String pn = propertyName(matcher.group(1));
				c1.append(" if($2.equals(\"").append(pn).append("\")){ w.").append(method.getName()).append("(").append(arg(pt, "$3")).append("); return; }");
				pts.put(pn, pt);
			}
		}
		c1.append(" throw new ").append(NoSuchPropertyException.class.getName()).append("(\"Not found property \\\"\"+$2+\"\\\" filed or setter method in class " + cls.getName() + ".\"); }");
		c2.append(" throw new ").append(NoSuchPropertyException.class.getName()).append("(\"Not found property \\\"\"+$2+\"\\\" filed or setter method in class " + cls.getName() + ".\"); }");
		
		// make class
		long id = WRAPPER_CLASS_COUNT.getAndIncrement();
		ClassGenerator cc = ClassGenerator.newInstance(cl);
		cc.setClassName((Modifier.isPublic(cls.getModifiers()) ? Wrapper.class.getName() : cls.getName() + "$sw") + id);
		cc.setSuperClass(Wrapper.class);
		
		cc.addDefaultConstructor();
		cc.addField("public static String[] pns;");// property name array.
		cc.addField("public static " + Map.class.getName() + " pts;");// property type map.
		cc.addField("public static String[] mns;");// all method name array.
		cc.addField("public static String[] dmns;");// declared method name array.
		
		for (int i = 0, len = ms.size(); i < len; i++) 
		{
			cc.addField("public static Class[] mts" + i + ";");
		}
		
		cc.addMethod("public String[] getPropertyNames(){ return pns; }");
		cc.addMethod("public boolean hasProperty(String n){ return pts.containsKey($1); }");
		cc.addMethod("public Class getPropertyType(String n){ return (Class)pts.get($1); }");
		cc.addMethod("public String[] getMethodNames(){ return mns; }");
		cc.addMethod("public String[] getDeclaredMethodNames(){ return dmns; }");
		cc.addMethod(c1.toString());
		cc.addMethod(c2.toString());
		cc.addMethod(c3.toString());
		
		try
		{
			Class<?> wc = cc.toClass();
			// setup static field.
			wc.getField("pts").set(null, pts);
			wc.getField("pns").set(null, pts.keySet().toArray(new String[0]));
			wc.getField("mns").set(null, mns.toArray(new String[0]));
			wc.getField("dmns").set(null, dmns.toArray(new String[0]));
			
			int ix = 0;
			for (Method m : ms.values()) 
			{
				wc.getField("mts" + ix ++).set(null, m.getParameterTypes());
			}
			
			return (Wrapper)wc.newInstance();
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new RuntimeException(e.getMessage(), e);
		}
		finally
		{
			cc.release();
			ms.clear();
			mns.clear();
			dmns.clear();
		}
	}
	
	private static String arg(Class<?> cls, String name)
	{
		if (cls.isPrimitive())
		{
			if (Boolean.TYPE == cls)
			{
				return String.format("((Boolean)%s).booleanValue()", name);
			}
			else if (Byte.TYPE == cls)
			{
				return String.format("((Byte)%s).byteValue()", name);
			}
			else if (Character.TYPE == cls)
			{
				return String.format("((Character)%s).charValue()", name);
			}
			else if (Double.TYPE == cls)
			{
				return String.format("((Double)%s).doubleValue()", name);
			}
			else if (Float.TYPE == cls)
			{
				return String.format("((Float)%s).floatValue()", name);
			}
			else if (Integer.TYPE == cls)
			{
				return String.format("((Integer)%s).intValue()", name);
			}
			else if (Long.TYPE == cls)
			{
				return String.format("((Long)%s).longValue()", name);
			}
			else if (Short.TYPE == cls)
			{
				return String.format("((Short)%s).shortValue()", name);
			}
			else
			{
				throw new RuntimeException("unkown primitive type:" + cls);
			}
		}
		else
		{
			return String.format("(%s)%s", ReflectUtils.getName(cls), name);
		}
	}
	
	private static String args(Class<?>[] cs, String name)
	{
		int len = cs.length;
		if (len == 0)
		{
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) 
		{
			if (i > 0)
			{
				sb.append(',');
			}
			sb.append(arg(cs[i], name + "[" + i + "]"));
		}
		return sb.toString();
	}
	
	private static String propertyName(String pn)
	{
		return pn.length() == 1 || 
				Character.isLowerCase(pn.charAt(1)) ? Character.toLowerCase(pn.charAt(0)) + pn.substring(1) : pn;
	}
	
}
