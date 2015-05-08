package org.itas.xcnet.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.itas.xcnet.common.logger.Logger;
import org.itas.xcnet.common.logger.LoggerFactory;

public final class ReflectUtils 
{
	private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
	/**
	 * void(V)
	 */
	public static final char JVM_VOID = 'V';
	
	/**
	 * boolean(Z)
	 */
	public static final char JVM_BOOLEAN = 'Z';

	/**
	 * byte(B)
	 */
	public static final char JVM_BYTE = 'B';

	/**
	 * char(C)
	 */
	public static final char JVM_CHAR = 'C';

	/**
	 * short(S)
	 */
	public static final char JVM_SHORT = 'S';
	
	/**
	 * int(I)
	 */
	public static final char JVM_INT = 'I';
	
	/**
	 * long(J)
	 */
	public static final char JVM_LONG = 'J';
	
	/**
	 * float(F)
	 */
	public static final char JVM_FLOAT = 'F';
	
	/**
	 * double(D)
	 */
	public static final char JVM_DOUBLE = 'D';
	
	public static final Class<?> [] EMPTY_CLASS_ARRAY = new Class<?>[0];
	
	public static final String JAVA_IDENT_REGEX = "(?:[_$a-zA-Z][_$z-zA-Z0-9])";
	
	public static final String JAVA_NAME_REGEX = "(?:" + JAVA_IDENT_REGEX + "(?:\\." + JAVA_IDENT_REGEX + ")*)";
	
	public static final String CLASS_DESC = "(?:L" + JAVA_IDENT_REGEX   + "(?:\\/" + JAVA_IDENT_REGEX + ")*;)";
	
	public static final String ARRAY_DESC = "(?:\\[+(?:(?:[VZBCDFIJS])|" + CLASS_DESC + "))";

	public static final String DESC_REGEX = "(?:(?:[VZBCDFIJS])|" + CLASS_DESC + "|" + ARRAY_DESC + ")";
	
	public static final Pattern DESC_PATTERN = Pattern.compile(DESC_REGEX);
	
	/**
	 * is compatible.
	 * 
	 * @param c class.
	 * @param o instance.
	 * @return compatible or not.
	 */
	public static boolean isCompatible(Class<?> c, Object o)
	{
		boolean isCompatible = c.isPrimitive();
		if (o == null)
		{
			return !isCompatible;
		}
		
		if (isCompatible)
		{
			if (c == boolean.class)
			{
				c = Boolean.class;
			} 
			else if (c == byte.class)
			{
				c = Byte.class;
			}
			else if (c == char.class)
			{
				c = Character.class;
			}
			else if (c == short.class)
			{
				c = Short.class;
			}
			else if (c == int.class)
			{
				c = Integer.class;
			}
			else if (c == long.class)
			{
				c = Long.class;
			}
			else if (c == float.class)
			{
				c = Float.class;
			}
			else if (c == long.class)
			{
				c = Double.class;
			}
		}
		
		if (c == o.getClass())
		{
			return true;
		} 
		else
		{
			return c.isInstance(o);
		}
	}
	
	/**
	 * is compatible.
	 * 
	 * @param cs class array.
	 * @param os object array.
	 * @return compatible or not.
	 */
	public static boolean isCompatible(Class<?>[] cs, Object[] os)
	{
		final int len = cs.length;
		if (len != os.length)
		{
			return false;
		}

		if (len == 0)
		{
			return true;
		}
		
		for (int i = 0; i < len; i++) 
		{
			if (!isCompatible(cs[i], os[i]))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static String getCodeBase(Class<?> c)
	{
		if (c == null)
		{
			return null;
		}
		
		ProtectionDomain domain = c.getProtectionDomain();
		if (domain == null)
		{
			return null;
		}
		
		CodeSource source = domain.getCodeSource();
		if (source == null)
		{
			return null;
		}
		
		URL location = source.getLocation();
		if (location == null)
		{
			return null;
		}
		
		return location.getFile();
	}
	
	/**
	 * get name.
	 * java.lang.Object[][].class => "java.lang.Object[][]"
	 * 
	 * @param c class.
	 * @return name.
	 */
	public static String getName(Class<?> c)
	{
		if (!c.isArray())
		{
			return c.getName();
		}
		
		StringBuilder sb = new StringBuilder();
		do
		{
			sb.append("[]");
			c = c.getComponentType();
		} 
		while (c.isArray());
		
		return c.getName() + sb.toString();
	}
	
	/**
	 * <p>get method name.</p>
	 * "void do(int)", "void do()", "int do(java.lang.String,boolean)"
	 * 
	 * @param m method.
	 * @return name.
	 */
	public static String getName(Method m)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(getName(m.getReturnType())).append(' ');
		sb.append(m.getName()).append('(');
		
		Class<?>[] types = m.getParameterTypes();
		for (int i = 0; i < types.length; i++) 
		{
			if (i > 0)
			{
				sb.append(',');
			}
			sb.append(getName(types[i]));
		}
		sb.append(')');
		
		return sb.toString();
	}
	
	/**
	 * <p>get method signature</p>
	 * "do(int)", "do()", "do(java.lang.String,boolean)"
	 * 
	 * @param methodNamem 方法名.
	 * @param parameterTypes 参数列表.
	 * @return name.
	 */
	public static String getSignature(String methodName, Class<?>[] parameterTypes)
	{
		StringBuilder sb = new StringBuilder(methodName);
		sb.append('(');
		if (parameterTypes == null || parameterTypes.length == 0)
		{
			sb.append(')');
			return sb.toString();
		}
		
		for (int i = 0; i < parameterTypes.length; i++) 
		{
			if (i > 0)
			{
				sb.append(',');
			}
			sb.append(parameterTypes[i].getName());
		}
		sb.append(')');
		
		return sb.toString();
	}
	
	/**
	 * <p>get constructor name.</p>
	 * "()", "(java.lang.String,int)"
	 * 
	 * @param c constructor.
	 * @return name.
	 */
	public static String getName(Constructor<?> c)
	{
		StringBuilder sb = new StringBuilder("(");
		
		Class<?>[] types = c.getParameterTypes();
		for (int i = 0; i < types.length; i++) 
		{
			if (i > 0)
			{
				sb.append(',');
			}
			sb.append(getName(types[i]));
		}
		sb.append(')');
		
		return sb.toString();
	}
	
	/**
	 * <p>get class desc.</p>
	 * boolean[].class => "[Z"
	 * Object.class => "Ljava/lang/Object;"
	 * 
	 * @param c class.
	 * @return desc.
	 * @throws NotFoundException 
	 */
	public static String getDesc(Class<?> c)
	{
		StringBuilder sb = new StringBuilder();
		
		for ( ; c.isArray(); )
		{
			sb.append('[');
			c = c.getComponentType();
		}
		
		if (c.isPrimitive())
		{
			if ("void".equals(c.getName()))
			{
				sb.append(JVM_VOID);
			}
			else if ("boolean".equals(c.getName()))
			{
				sb.append(JVM_BOOLEAN);
			}
			else if ("byte".equals(c.getName()))
			{
				sb.append(JVM_BYTE);
			}
			else if ("char".equals(c.getName()))
			{
				sb.append(JVM_CHAR);
			}
			else if ("short".equals(c.getName()))
			{
				sb.append(JVM_SHORT);
			}
			else if ("int".equals(c.getName()))
			{
				sb.append(JVM_INT);
			}
			else if ("long".equals(c.getName()))
			{
				sb.append(JVM_LONG);
			}
			else if ("float".equals(c.getName()))
			{
				sb.append(JVM_FLOAT);
			}
			else if ("double".equals(c.getName()))
			{
				sb.append(JVM_DOUBLE);
			}
		} 
		else
		{
			sb.append('L');
			sb.append(c.getName().replace('.', '/'));
			sb.append(';');
		}
		
		return sb.toString();
	}
	
	/**
	 * <p>get class array desc.</p>
	 * [int.class, boolean[].class, Object.class] => "I[ZLjava/lang/Object;"
	 * 
	 * @param cs class array.
	 * @return desc.
	 * @throws NotFoundException 
	 */
	public static String getDesc(Class<?>[] cs)
	{
		if (cs.length == 0)
		{
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		for (Class<?> c : cs) 
		{
			sb.append(getDesc(c));
		}
		
		return sb.toString();
	}
	
	/**
	 * <p>get method desc.</p>
	 * int do(int arg1) => "do(I)I"
	 * void do(String arg1,boolean arg2) => "do(Ljava/lang/String;Z)V"
	 * 
	 * @param m method.
	 * @return desc.
	 */
	public static String getDesc(Method m)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(m.getName());
		sb.append('(');
		
		Class<?>[] types = m.getParameterTypes();
		for (Class<?> type : types) 
		{
			sb.append(getDesc(type));
		}
		sb.append(')');
		sb.append(getDesc(m.getReturnType()));
		
		return sb.toString();
	}
	
	/**
	 * <p>get constructor desc.</p>
	 * "()V", "(Ljava/lang/String;I)V"
	 * 
	 * @param c constructor.
	 * @return desc
	 */
	public static String getDesc(Constructor<?> c)
	{
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		
		Class<?>[] types = c.getParameterTypes();
		for (Class<?> type : types) 
		{
			sb.append(getDesc(type));
		}
		sb.append(')');
		sb.append('V');
		
		return sb.toString();
	}
	
	/**
	 * <p>get method desc.</p>
	 * "(I)I", "()V", "(Ljava/lang/String;Z)V"
	 * 
	 * @param m method.
	 * @return desc.
	 */
	public static String getDescWithoutMethodName(Method m)
	{
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		
		Class<?>[] types = m.getParameterTypes();
		for (Class<?> type : types) 
		{
			sb.append(getDesc(type));
		}
		sb.append(')');
		sb.append(getDesc(m.getReturnType()));
		
		return sb.toString();
	}
	
	/**
	 * <p>get class desc.</p>
	 * Object.class => "Ljava/lang/Object;"
	 * boolean[].class => "[Z"
	 * 
	 * @param c class.
	 * @return desc.
	 * @throws NotFoundException 
	 */
	public static String getDesc(CtClass c) throws NotFoundException
	{
		StringBuilder sb = new StringBuilder();
		if (c.isArray())
		{
			sb.append('[');
			sb.append(getDesc(c.getComponentType()));
		}
		else if (c.isArray())
		{
			if ("void".equals(c.getName()))
			{
				sb.append(JVM_VOID);
			}
			else if ("boolean".equals(c.getName()))
			{
				sb.append(JVM_BOOLEAN);
			}
			else if ("byte".equals(c.getName()))
			{
				sb.append(JVM_BYTE);
			}
			else if ("char".equals(c.getName()))
			{
				sb.append(JVM_CHAR);
			}
			else if ("short".equals(c.getName()))
			{
				sb.append(JVM_SHORT);
			}
			else if ("int".equals(c.getName()))
			{
				sb.append(JVM_INT);
			}
			else if ("long".equals(c.getName()))
			{
				sb.append(JVM_LONG);
			}
			else if ("float".equals(c.getName()))
			{
				sb.append(JVM_FLOAT);
			}
			else if ("double".equals(c.getName()))
			{
				sb.append(JVM_DOUBLE);
			}
		}
		else
		{
			sb.append('L');
			sb.append(c.getName().replace('.', '/'));
			sb.append(';');
		}
		
		return sb.toString();
	}
	
	/**
	 * <p>get method desc.</p>
	 * "do(I)I", "do()V", "do(Ljava/lang/String;Z)V"
	 * 
	 * @param m method.
	 * @return desc.
	 * @throws NotFoundException 
	 */
	public static String getDesc(CtMethod m) throws NotFoundException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(m.getName());
		sb.append('(');
		
		CtClass[] types = m.getParameterTypes();
		for (CtClass type : types)
		{
			sb.append(getDesc(type));
		}
		sb.append(')');
		sb.append(getDesc(m.getReturnType()));
		
		return sb.toString();
	}
	
	/**
	 * <p>get constructor desc.</p>
	 * "()V", "(Ljava/lang/String;I)V"
	 * 
	 * @param c constructor.
	 * @return desc
	 */
	public static String getDesc(CtConstructor c) throws NotFoundException
	{
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		
		CtClass[] parameterTypes = c.getParameterTypes();
		for (CtClass parameterType : parameterTypes)
		{
			sb.append(getDesc(parameterType));
		}
		sb.append(')');
		sb.append('V');
		
		return sb.toString();
	}
	
	/**
	 * <p>get method desc.</p>
	 * "(I)I", "()V", "(Ljava/lang/String;Z)V".
	 * 
	 * @param m method.
	 * @return desc.
	 */
	public static String getDescWithoutMethodName(final CtMethod m) throws NotFoundException
	{
		StringBuilder sb = new StringBuilder();
		sb.append('(');

		CtClass[] parameterTypes = m.getParameterTypes();
		for (CtClass parameterType : parameterTypes)
		{
			sb.append(getDesc(parameterType));
		}
		sb.append(')');
		sb.append(getDesc(m.getReturnType()));
		
		return sb.toString();
	}
	
	/**
	 * name to desc.
	 * java.util.Map[][] => "[[Ljava/util/Map;"
	 * 
	 * @param name name.
	 * @return desc.
	 */
	public static String name2desc(String name)
	{
		StringBuilder sb = new StringBuilder();
		int c = 0,index = name.indexOf('[');
		if( index > 0 )
		{
			c = ( name.length() - index ) / 2;
			name = name.substring(0,index);
		}
		
		for( ; c-- > 0; )
		{
			sb.append("[");
		}
		
		if( "void".equals(name) )
		{
			sb.append(JVM_VOID);
		}
		else if( "boolean".equals(name) )
		{
			sb.append(JVM_BOOLEAN);
		}
		else if( "byte".equals(name) )
		{
			sb.append(JVM_BYTE);
		}
		else if( "char".equals(name) )
		{
			sb.append(JVM_CHAR);
		}
		else if( "double".equals(name) )
		{
			sb.append(JVM_DOUBLE);
		}
		else if( "float".equals(name) )
		{
			sb.append(JVM_FLOAT);
		}
		else if( "int".equals(name) )
		{
			sb.append(JVM_INT);
		}
		else if( "long".equals(name) )
		{
			sb.append(JVM_LONG);
		}
		else if( "short".equals(name) )
		{
			sb.append(JVM_SHORT);
		}
		else
		{
			sb.append('L');
			sb.append(name.replace('.', '/'));
			sb.append(';');
		}
		
		return sb.toString();
	}

	/**
	 * desc to name.
	 * "[[I" => "int[][]"
	 * 
	 * @param desc desc.
	 * @return name.
	 */
	public static String desc2name(String desc)
	{
		StringBuilder sb = new StringBuilder();
		int c = desc.lastIndexOf('[') + 1;
		if( desc.length() == c+1 )
		{
			switch( desc.charAt(c) )
			{
				case JVM_VOID: 
				{ 
					sb.append("void"); break; 
				}
				case JVM_BOOLEAN: 
				{ 
					sb.append("boolean"); break; 
				}
				case JVM_BYTE: 
				{ 
					sb.append("byte"); break; 
				}
				case JVM_CHAR: 
				{ 
					sb.append("char"); break; 
				}
				case JVM_DOUBLE: 
				{ 
					sb.append("double"); break;
				}
				case JVM_FLOAT: 
				{ 
					sb.append("float"); break; 
				}
				case JVM_INT: 
				{
					sb.append("int"); break; 
				}
				case JVM_LONG: 
				{ 
					sb.append("long"); break;
				}
				case JVM_SHORT: 
				{ 
					sb.append("short"); break; 
				}
				default:
				{
					throw new RuntimeException();
				}
			}
		}
		else
		{
			sb.append(desc.substring(c+1, desc.length()-1).replace('/','.'));
		}
		for( ; c-- > 0; )
		{
			sb.append("[]");
		}
		
		return sb.toString();
	}
	
	public static Class<?> forName(String name)
	{
		try 
		{
			return name2Class(name);
		} 
		catch (ClassNotFoundException e) 
		{
			throw new IllegalStateException(
				String.format("Not found class %s, cause: %s", name, e.getMessage()), e);
		}
	}
	
	/**
	 * <p>name to class.</p>
	 * "boolean" => boolean.class
	 * "java.util.Map[][]" => java.util.Map[][].class
	 * 
	 * @param name name.
	 * @return Class instance.
	 */
	public static Class<?> name2Class(String name) throws ClassNotFoundException
	{
		return name2Class(ClassHelper.getClassLoader(), name);
	}
	
	public static Class<?> name2Class(ClassLoader cl, String name) throws ClassNotFoundException
	{
		int c = 0;
		int index = name.indexOf('[');
		if (index > 0)
		{
			c = (name.length() - index) >> 1;
			name = name.substring(0, index);
		}
		
		if (c > 0)
		{
			StringBuilder sb = new StringBuilder();
			for ( ; c-- > 0; ) 
			{
				sb.append('[');
			}
			
			if( "void".equals(name) )
			{
				sb.append(JVM_VOID);
			}
			else if( "boolean".equals(name) )
			{
				sb.append(JVM_BOOLEAN);
				
			}
			else if( "byte".equals(name) )
			{
				sb.append(JVM_BYTE);
			}
			else if( "char".equals(name) )
			{
				sb.append(JVM_CHAR);
			}
			else if( "double".equals(name) )
			{
				sb.append(JVM_DOUBLE);
			}
			else if( "float".equals(name) )
			{
				sb.append(JVM_FLOAT);
			}
			else if( "int".equals(name) )
			{
				sb.append(JVM_INT);
			}
			else if( "long".equals(name) )
			{
				sb.append(JVM_LONG);
			}
			else if( "short".equals(name) )
			{
				sb.append(JVM_SHORT);
			}
			else
			{	// "java.lang.Object" ==> "Ljava.lang.Object;"
				sb.append('L');
				sb.append(name);
				sb.append(';'); 
			}
			
			name = sb.toString();
		}
		else
		{
			if( "void".equals(name) )
			{
				return void.class;
			}
			else if( "boolean".equals(name) )
			{
				return boolean.class;
			}
			else if( "byte".equals(name) )
			{
				return byte.class;
			}
			else if( "char".equals(name) )
			{
				return char.class;
			}
			else if( "double".equals(name) )
			{
				return double.class;
			}
			else if( "float".equals(name) )
			{
				return float.class;
			}
			else if( "int".equals(name) )
			{
				return int.class;
			}
			else if( "long".equals(name) )
			{
				return long.class;
			}
			else if( "short".equals(name) )
			{
				return short.class;
			}
		}
		
		if (cl == null)
		{
			cl = ClassHelper.getClassLoader();
		}
		
		return Class.forName(name, true, cl);
	}
	
	/**
	 * desc to class.
	 * "[Z" => boolean[].class
	 * "[[Ljava/util/Map;" => java.util.Map[][].class
	 * 
	 * @param desc desc.
	 * @return Class instance.
	 * @throws ClassNotFoundException 
	 */
	public static Class<?> desc2class(String desc) throws ClassNotFoundException
	{
		return desc2class(ClassHelper.getClassLoader(), desc);
	}
	
	/**
	 * desc to class.
	 * "[Z" => boolean[].class
	 * "[[Ljava/util/Map;" => java.util.Map[][].class
	 * 
	 * @param cl ClassLoader instance.
	 * @param desc desc.
	 * @return Class instance.
	 * @throws ClassNotFoundException 
	 */
	public static Class<?> desc2class(ClassLoader cl, String desc) throws ClassNotFoundException
	{
		switch (desc.charAt(0))
		{
			case JVM_VOID:
			{
				return void.class;
			}
			case JVM_BOOLEAN:
			{
				return boolean.class;
			}
			case JVM_BYTE:
			{
				return byte.class;
			}
			case JVM_CHAR:
			{
				return char.class;
			}
			case JVM_DOUBLE:
			{return double.class;
			
			}
			case JVM_FLOAT:
			{
				return float.class;
			}
			case JVM_INT:
			{
				return int.class;
			}
			case JVM_LONG:
			{
				return long.class;
			}
			case JVM_SHORT:
			{
				return short.class;
			}
			case 'L':
			{	// "Ljava/lang/Object;" ==> "java.lang.Object"
				desc = desc.substring(1, desc.length()-1).replace('/', '.'); 
				break;
			}
			case '[':
			{	// "[[Ljava/lang/Object;" ==> "[[Ljava.lang.Object;"
				desc = desc.replace('/', '.');  
				break;
			}
			default:
			{
				throw new ClassNotFoundException("Class not found: " + desc);
			}
		}
		
		if (cl == null)
		{
			cl = ClassHelper.getClassLoader();
		}
		
		return Class.forName(desc, true, cl);
	}
	
	/**
	 * get class array instance.
	 * 
	 * @param desc desc.
	 * @return Class class array.
	 * @throws ClassNotFoundException 
	 */
	public static Class<?>[] desc2classArray(String desc) throws ClassNotFoundException
	{
		return desc2classArray(ClassHelper.getClassLoader(), desc);
	}
	
	/**
	 * get class array instance.
	 * 
	 * @param cl ClassLoader instance.
	 * @param desc desc.
	 * @return Class[] class array.
	 * @throws ClassNotFoundException 
	 */
	public static Class<?>[] desc2classArray(ClassLoader cl, String desc) throws ClassNotFoundException
	{
		if (desc == null || desc.length() == 0)
		{
			return EMPTY_CLASS_ARRAY;
		}
		
		List<Class<?>> csArray = new ArrayList<Class<?>>();
		Matcher matcher = DESC_PATTERN.matcher(desc);
		for ( ; matcher.find();)
		{
			csArray.add(name2Class(matcher.group()));
		}
		
		return csArray.toArray(new Class<?>[csArray.size()]);
	}
	
	public static Method findMethodByMethodName(Class<?> clazz, String methodName)
	throws NoSuchMethodException, ClassNotFoundException 
	{
    	return findMethodByMethodSignature(clazz, methodName, null);
    }

	/**
	 * 根据方法签名从类中找出方法。
	 * 
	 * @param clazz 查找的类。
	 * @param methodSignature 方法签名，形如method1(int, String)。也允许只给方法名不参数只有方法名，形如method2。
	 * @return 返回查找到的方法。
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException  
	 * @throws IllegalStateException 给定的方法签名找到多个方法（方法签名中没有指定参数，又有有重载的方法的情况）
	 */
	public static Method findMethodByMethodSignature(Class<?> c, String methodName, String[] paramTypes)
	throws NoSuchMethodException, ClassNotFoundException
	{
		if (paramTypes == null)
		{
			List<Method> finded = new ArrayList<>(1);
            for (Method m : c.getMethods()) 
            {
                if (m.getName().equals(methodName)) 
                {
                    finded.add(m);
                }
            }
            
            if (finded.isEmpty()) 
            {
                throw new NoSuchMethodException("No such method " + methodName + " in class " + c);
            }
            
            if(finded.size() > 1) 
            {
                throw new IllegalStateException(String.format(
	            		"Not unique method for method name(%s) in class(%s), find %d methods.",
	                    methodName, c.getName(), finded.size()));
            }
            
            return finded.get(0);
		}
		else
		{
			Class<?>[] params = new Class<?>[paramTypes.length];
			for (int i = 0; i < paramTypes.length; i++) 
			{
				params[i] = name2Class(paramTypes[i]);
			}
			
			return c.getMethod(methodName, params);
		}
	}
	
	/**
	 * <p>查找构造方法</p>
	 * 
	 * @param c 要查找的类
	 * @param paramType 构造方法参数
	 * @return 找到的构造方法
	 * @throws NoSuchMethodException
	 */
	public static Constructor<?> findConstructor(Class<?> c, Class<?> paramType) throws NoSuchMethodException
	{
		try
		{
			return c.getConstructor(new Class<?>[]{paramType});
		}
		catch (NoSuchMethodException e)
		{
			Constructor<?>[] constructors = c.getConstructors();
			for (Constructor<?> constructor : constructors)
			{
				if (Modifier.isPublic(constructor.getModifiers()) &&
					constructor.getParameterTypes().length == 1 &&
					constructor.getParameterTypes()[0].isAssignableFrom(paramType))
				{
					return constructor;
				}
			}
			
			throw e;
		}
		
	}
	
	public static void main(String[] args) 
	{
		logger.trace(getCodeBase(ReflectUtils.class));
		logger.trace(getName(ReflectUtils.class));
		ReflectUtils[][][] aa = new ReflectUtils[1][1][1];
		logger.trace(getName(aa.getClass()));
		logger.trace(getSignature("getName", new Class<?>[]{Method.class}));
	}
	
}
