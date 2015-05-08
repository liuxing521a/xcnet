/**
 * 
 */
package org.itas.xcnet.common.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * class tools
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月8日下午4:56:42
 */
public class ClassHelper 
{
	 /** 
	  * Suffix for array class names: "[]" 
	  */
	public static final String ARRAY_SUFFIX = "[]";
	
	/** 
	 * Prefix for internal array class names: "[L" 
	 */
	public static final String INTERNAL_ARRAY_PREFIX = "[L";
	
	 /**
     * Map with primitive type name as key and corresponding primitive type as
     * value, <br/>
     * for example: "int" -> "int.class".
     */
	private static final Map<String, Class<?>> primitiveTypeNameMap;
	
	static
	{
		Set<Class<?>> primitiveTypeNames = new HashSet<Class<?>>(16);
		primitiveTypeNames.addAll(Arrays.asList(new Class<?>[] 
		{ 	
    		boolean.class, 
    		byte.class, 
    		char.class, 
    		double.class,
    		float.class, 
    		int.class, 
    		long.class, 
    		short.class 
		}));
		        
		primitiveTypeNames.addAll(Arrays.asList(new Class<?>[] 
		{ 	
    		boolean[].class, 
    		byte[].class, 
    		char[].class, 
    		double[].class,
    		float[].class, 
    		int[].class, 
    		long[].class, 
    		short[].class 
		}));
		        
		Map<String, Class<?>> map = new HashMap<>(primitiveTypeNames.size());
        for (Class<?> primitiveClass : primitiveTypeNames) 
        {
        	map.put(primitiveClass.getName(), primitiveClass);
        }
        
        primitiveTypeNameMap = Collections.unmodifiableMap(map);
	}
	
	/**
	 * 获取类加载器
	 * 
	 * @return 类加载器
	 */
	public static ClassLoader getClassLoader()
	{
		return getClassLoader(ClassHelper.class);
	}
	
	/**
	 * 获取类加载器
	 * 
	 * @param c 
	 * @return 类加载器
	 */
	public static ClassLoader getClassLoader(Class<?> c)
	{
		try
		{
			return Thread.currentThread().getContextClassLoader();
		} 
		catch (Throwable e)
		{
			return c.getClassLoader();
		}
	}
	
    public static Class<?> forName(String name) throws ClassNotFoundException 
    {
        return forName(name, getClassLoader());
    }
	
	public static Class<?> forName(String name, ClassLoader cl) throws ClassNotFoundException
	{
		Class<?> clazz = resolvePrimitiveClassName(name);
		if (clazz != null)
		{
			return clazz;
		}
		
		// "java.lang.String[]" style arrays
		if (name.endsWith(ARRAY_SUFFIX))
		{
			String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
			Class<?> elementClass = forName(elementClassName, cl);
			return Array.newInstance(elementClass, 0).getClass();
		}
		
		// "[Ljava.lang.String;" style arrays
		int internalArrayMarker = name.indexOf(INTERNAL_ARRAY_PREFIX);
		if (internalArrayMarker != -1 && name.endsWith(";"))
		{
			String elementClassName;
			if (internalArrayMarker == 0)
			{
				 elementClassName = name.substring(INTERNAL_ARRAY_PREFIX.length(), name.length() - 1);
			}
			else
			{
				elementClassName = name.substring(1);
			}
			
			Class<?> elementClass = forName(elementClassName, cl);
            return Array.newInstance(elementClass, 0).getClass();
		}
		
		ClassLoader classLoaderToUse = cl;
		if (classLoaderToUse == null)
		{
			classLoaderToUse = getClassLoader();
		}
		
		return classLoaderToUse.loadClass(name);
	}
	
	/**
     * 分析并获取java基础类型<p>
     * 
     * @param name 可能基础类型名称
     * @return java基础类型, or <code>null</code>
     */
	public static Class<?> resolvePrimitiveClassName(String name)
	{
		if (name == null || name.length() > 8)
		{
			return null;
		}
		else
		{
			return primitiveTypeNameMap.get(name);
		}
	}
	
	public static String toShortString(Object o)
	{
		if (o == null)
		{
			return "null";
		}
		else
		{
			return String.format("%s@%s", o.getClass().getSimpleName(), System.identityHashCode(o));
		}
	}
	
}
