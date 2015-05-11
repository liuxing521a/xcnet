package org.itas.xcnet.common.bytecode;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.itas.xcnet.common.utils.ReflectUtils;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

/**
 * 类生成器
 * 
 * @author liuzhen
 *
 */
public class ClassGenerator 
{
	public static interface DC
	{
		// dynamic class tag interface.
	} 
	
	private static final AtomicLong CLASS_NAME_COUNT = new AtomicLong(0L);
	
	private static final String SIMPLE_NAME_TAG = "<init>";
	
	private static final Map<ClassLoader, ClassPool> POOL_MAP = new ConcurrentHashMap<>();
	
	public static ClassGenerator newInstance()
	{
		return new ClassGenerator(getClassPool(null));
	}
	
	public static ClassGenerator newInstance(ClassLoader loader)
	{
		return new ClassGenerator(getClassPool(loader));
	}
	
	public static boolean isDynamicClass(Class<?> clazz)
	{
		return DC.class.isAssignableFrom(clazz);
	}
	
	private static ClassPool getClassPool(ClassLoader loader)
	{
		if (loader == null)
		{
			return ClassPool.getDefault();
		}
		
		ClassPool pool = POOL_MAP.get(loader);
		if (pool == null)
		{
			pool = new ClassPool(true);
			pool.appendClassPath(new LoaderClassPath(loader));
			POOL_MAP.putIfAbsent(loader, pool);
			pool = POOL_MAP.get(loader);
		}
		
		return pool;
	}
	
	private ClassPool mPool;
	
	private CtClass mCtc;
	
	private String mClassName, mSuperClass;
	
	private Set<String> mInterfaces;
	
	private List<String> mFields, mMethods, mConstructors;
	
	// <method desc,method instance>
	private Map<String, Method> mCopyMethods;
	
	// <constructor desc,constructor instance>
	private Map<String, Constructor<?>> mCopyConstructors;
	
	private boolean mDefaultConstructor;
	
	private ClassGenerator(ClassPool pool)
	{
		mPool = pool;
		mDefaultConstructor = false;
	}
	
	public String getClassName()
	{
		return mClassName;
	}
	
	public ClassGenerator setClassName(String name)
	{
		mClassName = name;
		return this;
	}
	
	public ClassGenerator addInterface(String infa)
	{
		if (mInterfaces == null)
		{
			mInterfaces = new HashSet<String>();
		}
		
		mInterfaces.add(infa);
		return this;
	}
	
	public ClassGenerator addInterface(Class<?> cls)
	{
		return addInterface(cls.getName());
	}
	
	public ClassGenerator setSuperClass(String cls)
	{
		mSuperClass = cls;
		return this;
	}
	
	public ClassGenerator setSuperClass(Class<?> cls)
	{
		return setSuperClass(cls.getName());
	}
	
	/**
	 * <p>添加属性定义</p>
	 * 
	 * @param code example [private String username;|private int age;];
	 * @return
	 */
	public ClassGenerator addField(String code)
	{
		if (mFields == null)
		{
			mFields = new ArrayList<String>();
		}
		
		mFields.add(code);
		return this;
	}
	
	public ClassGenerator addField(String name, int mod, Class<?> type, String defValue)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(modify(mod)).append(' ').append(ReflectUtils.getName(type)).append(name);
		if (defValue != null && defValue.length() > 0)
		{
			sb.append('=');
			sb.append(defValue);
		}
		sb.append(';');
		
		return addField(sb.toString());
	}
	
	/**
	 * <p>添加方法</p>
	 * 
	 * @param code example [public String getName(){return name;}]
	 * @return
	 */
	public ClassGenerator addMethod(String code)
	{
		if (mMethods == null)
		{
			mMethods = new ArrayList<String>();
		}
		
		mMethods.add(code);
		return this;
	}
	
	public ClassGenerator addMethod(String name, int mod, 
		Class<?> returnType, Class<?>[] paramTypes, Class<?>[] throwsTypes, String body)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(modify(mod)).append(' ').append(ReflectUtils.getName(returnType)).append(' ').append(name);
		sb.append('(');
		
		for (int i = 0; i < paramTypes.length; i++) 
		{
			if (i > 0)
			{
				sb.append(',');
			}
			
			sb.append(ReflectUtils.getName(paramTypes[i])).append(" arg").append(i);
		}
		sb.append(')');
		
		if (throwsTypes != null && throwsTypes.length > 0)
		{
			sb.append(" throws ");
			for (int i = 0; i < throwsTypes.length; i++) 
			{
				if (i > 0)
				{
					sb.append(',');
				}
				
				sb.append(ReflectUtils.getName(throwsTypes[i]));
			}
		}
		
		sb.append('{').append(body).append('}');
		return addMethod(sb.toString());
	}
	
	public ClassGenerator addMethod(Method method)
	{
		return addMethod(method.getName(), method);
	}
	
	public ClassGenerator addMethod(String name, Method method)
	{
		String desc = name + ReflectUtils.getDescWithoutMethodName(method);
		addMethod(':' + desc);
		
		if (mCopyMethods == null)
		{
			mCopyMethods = new ConcurrentHashMap<String, Method>();
		}
		
		mCopyMethods.put(name, method);
		return this;
	}
	
	public ClassGenerator addConstructor(String code)
	{
		if (mConstructors == null)
		{
			mConstructors = new ArrayList<String>(4);
		}
		
		mConstructors.add(code);
		return this;
	}
	
	public ClassGenerator addConstructor(int mod, Class<?>[] paramTypes, String body)
	{
		return addConstructor(mod, paramTypes, null, body);
	}
	
	public ClassGenerator addConstructor(int mod, Class<?>[] paramTypes, Class<?>[] throwTypes, String body)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(modify(mod)).append(' ').append(SIMPLE_NAME_TAG);
		sb.append('(');
		for (int i = 0; i < paramTypes.length; i++) 
		{
			if (i > 0)
			{
				sb.append(',');	
			}
			sb.append(ReflectUtils.getName(paramTypes[i])).append(" arg").append(i);
		}
		sb.append(')');
		
		if (throwTypes != null && throwTypes.length > 0)
		{
			sb.append(" throws ");
			for (int i = 0; i < throwTypes.length; i++) 
			{
				if (i > 0)
				{
					sb.append(',');
				}
				sb.append(ReflectUtils.getName(throwTypes[i]));
			}
		}
		sb.append('{').append(body).append('}');
		
		return addConstructor(sb.toString());
		
	}
	
	public ClassGenerator addConstructor(Constructor<?> c)
	{
		String desc = ReflectUtils.getDesc(c);
		addConstructor(':' + desc);
		if (mCopyConstructors == null)
		{
			mCopyConstructors = new ConcurrentHashMap<String, Constructor<?>>();
		}
		
		mCopyConstructors.put(desc, c);
		return this;
	}
	
	public ClassGenerator addDefaultConstructor()
	{
		mDefaultConstructor = true;
		return this;
	}
	
	public Class<?> toClass()
	{
		if (mCtc != null)
		{
			mCtc.detach();
		}
		
		long clsId = CLASS_NAME_COUNT.getAndIncrement();
		try
		{
			CtClass ctcs = null;
			if(mSuperClass != null)
			{
				ctcs = mPool.get(mSuperClass);
			}
			
			if (mClassName == null)
			{
				mClassName = ((mSuperClass == null || !javassist.Modifier.isPublic(ctcs.getModifiers()))
						? ClassGenerator.class.getName() : mSuperClass + "$sc") + clsId;
			}
			mCtc = mPool.makeClass(mClassName);
			
			// add super class
			if (mSuperClass != null)
			{
				mCtc.setSuperclass(ctcs);
			}
			
			// add interface
			mCtc.addInterface(mPool.get(DC.class.getName()));
			if (mInterfaces != null)
			{
				for (String infa : mInterfaces) 
				{
					mCtc.addInterface(mPool.get(infa));
				}
			}
			
			// add Fields
			if (mFields != null)
			{
				for (String code : mFields) 
				{
					mCtc.addField(CtField.make(code, mCtc));
				}
			}
			
			// add Method
			if (mMethods != null)
			{
				for (String code : mMethods) 
				{
					if( code.charAt(0) == ':' )
					{
						mCtc.addMethod(CtNewMethod.copy(getCtMethod(mCopyMethods.get(code.substring(1))), code.substring(1, code.indexOf('(')), mCtc, null));
					}
					else
					{
						mCtc.addMethod(CtNewMethod.make(code, mCtc));
					}
				}
			}
			
			// add constructor
			if (mDefaultConstructor)
			{
				mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
			}
			if (mConstructors != null)
			{
				for (String code : mConstructors) 
				{
					if (code.charAt(0) == ':')
					{
						CtConstructor ctConstructor = getCtConstructor(mCopyConstructors.get(code.substring(1)));
						mCtc.addConstructor(CtNewConstructor.copy(ctConstructor, mCtc, null));
					}
					else
					{
						String[] sn = mCtc.getSimpleName().split("\\$");
						mCtc.addConstructor(CtNewConstructor.make(code.replaceFirst(SIMPLE_NAME_TAG, sn[sn.length-1]), mCtc));
					}
				}
			}
			
			try {
				mCtc.writeFile("D:/");// TODO
			} catch (IOException e) {
				e.printStackTrace();
			}
			return mCtc.toClass();
		} 
		catch (RuntimeException|NotFoundException|CannotCompileException e) 
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public void release()
	{
		if( mCtc != null ) 
		{
			mCtc.detach();
		}
		if( mInterfaces != null )
		{
			mInterfaces.clear();
		}
		if( mFields != null )
		{
			mFields.clear();
		}
		if( mMethods != null )
		{
			mMethods.clear();
		}
		if( mConstructors != null )
		{
			mConstructors.clear();
		}
		if( mCopyMethods != null )
		{
			mCopyMethods.clear();
		}
		if( mCopyConstructors != null )
		{
			mCopyConstructors.clear();
		}
	}
	
	private CtClass getCtClass(Class<?> c) throws NotFoundException
	{
		return mPool.get(c.getName());
	}
	
	private CtMethod  getCtMethod(Method m) throws NotFoundException
	{
		CtClass ctClass = getCtClass(m.getDeclaringClass());
		return ctClass.getMethod(m.getName(), ReflectUtils.getDescWithoutMethodName(m));
	}
	
	private CtConstructor getCtConstructor(Constructor<?> c) throws NotFoundException
	{
		CtClass ctClass = getCtClass(c.getDeclaringClass());
		return ctClass.getConstructor(ReflectUtils.getDesc(c));
	}
	
	private static String modify(int mod)
	{
		if (Modifier.isPrivate(mod))
		{
			return "private";
		}
		else if (Modifier.isProtected(mod))
		{
			return "protected";
		} 
		else if (Modifier.isPublic(mod))
		{
			return "public";
		}
		else
		{
			return "";
		}
	}
	
}
