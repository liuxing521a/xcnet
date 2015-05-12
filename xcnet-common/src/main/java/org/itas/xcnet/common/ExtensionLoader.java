/**
 * 
 */
package org.itas.xcnet.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import org.itas.xcnet.common.bytecode.ClassGenerator;
import org.itas.xcnet.common.logger.Logger;
import org.itas.xcnet.common.logger.LoggerFactory;
import org.itas.xcnet.common.utils.ConcurrentHashSet;
import org.itas.xcnet.common.utils.Objects;
import org.itas.xcnet.common.utils.Reference;


/**
 * 使用的扩展点获取。<p>
 * <ul>
 * <li>自动注入关联扩展点。</li>
 * <li>自动Wrap上扩展点的Wrap类。</li>
 * <li>缺省获得的的扩展点是一个Adaptive Instance。
 * </ul>
 * @see <a href="http://java.sun.com/j2se/1.5.0/docs/guide/jar/jar.html#Service%20Provider">JDK5.0的自动发现机制实现</a>
 * @see Extension
 * @see Adaptive
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月12日下午2:22:21
 */
@SuppressWarnings("unchecked")
public class ExtensionLoader<T> 
{
	private static final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);
	
	/**
	 * 服务所在路径
	 */
	private static final String SERVICES_DIRECTORY;
	
	/**
	 * 扩展点加载列表
	 */
	private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS;
	
	public static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*");
	
	static
	{
		SERVICES_DIRECTORY = "META-INF/services/";
		EXTENSION_LOADERS = new ConcurrentHashMap<Class<?>, ExtensionLoader<?>>();
	}
	
	private final Class<?> type;
	
	private final Reference<Map<String, Class<?>>> cachedClasses;
	
	private final ConcurrentMap<String, Reference<Object>> cachedInstances;
	
	private volatile Class<?> cachedAdaptiveClass;
	
	private final Reference<Object> cachedAdaptiveInstance;
	
	private Set<Class<?>> cachedWrapperClasses;
	
	private String cachedDefaultName;
	
	private ExtensionLoader(Class<?> type)
	{
		this.type = type;
		this.cachedClasses = new Reference<>();
		this.cachedInstances = new ConcurrentHashMap<>();
		this.cachedAdaptiveClass = null;
		this.cachedAdaptiveInstance = new Reference<>();
		this.cachedWrapperClasses = null;
		this.cachedDefaultName = null;
	}
	
	
	public static <E> ExtensionLoader<E> getExtensionLoader(Class<E> type)
	{
		if (Objects.isNull(type))
		{
			throw new IllegalArgumentException("Extension type == null");
		}
		
		ExtensionLoader<E> loader = (ExtensionLoader<E>)EXTENSION_LOADERS.get(type);
		if (Objects.isNull(loader))
		{
			EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<E>(type));
			loader = (ExtensionLoader<E>)EXTENSION_LOADERS.get(type);
		}
		
		return loader;
	}
	
	public T getExtension(String name)
	{
		if (Objects.isEmpty(name))
		{
			throw new IllegalArgumentException("Extension name == null");
		}
		
		Reference<Object> ref = cachedInstances.get(name);
		if (Objects.isNull(ref))
		{
			cachedInstances.putIfAbsent(name, new Reference<>());
			ref = cachedInstances.get(name);;
		}
		
		if (!ref.isPresent())
		{
			synchronized (ref) 
			{
				if (!ref.isPresent())
				{
					ref.set(createExtension(name));
				}
			}
		}
		
		return (T)ref.get();
	}
	
	public boolean hasExtention(String name)
	{
		if (Objects.isEmpty(name))
		{
			throw new IllegalArgumentException("Extension name == null");
		}
		
		try
		{
			return getExtensionClass(name) != null;
		}
		catch (Throwable t)
		{
			return false;
		}
	}
	
	public Set<String> getSupportedExtensions()
	{
		 Map<String, Class<?>> clazzes = getExtensionClasses();
		 return Collections.unmodifiableSet(new TreeSet<>(clazzes.keySet()));
	}
	
	public T getAdaptiveExtension()
	{
		if (!cachedAdaptiveInstance.isPresent())
		{
			synchronized (cachedAdaptiveInstance) 
			{
				if (!cachedAdaptiveInstance.isPresent())
					cachedAdaptiveInstance.set(createAdaptiveExtension());
			}
		}
		
		return (T)cachedAdaptiveInstance.get();
	}
	
	private Class<?> getExtensionClass(String name)
	{
		if (type == null)
		{
			 throw new IllegalArgumentException("Extension type == null");
		}
		if (name == null)
		{
			throw new IllegalArgumentException("Extension name == null");
		}
		
		Class<?> clazz = getExtensionClasses().get(name);
		if (clazz == null)
		{
			throw new IllegalStateException("No such extension \"" + name + "\" for " + type.getName() + "!");
		}
		
		return clazz;
	}
	
	private Map<String, Class<?>> getExtensionClasses()
	{
		if (!cachedClasses.isPresent())
		{
			synchronized (cachedClasses) 
			{
				if (!cachedClasses.isPresent())
				{
					cachedClasses.set(loadExtensionClass());	
				}
			}
		}
		
		return cachedClasses.get();
	}

	private Class<?> getAdaptiveExtensionClass()
	{
		getExtensionClasses();
		if (Objects.isNull(cachedAdaptiveClass))
		{
			cachedAdaptiveClass = createAdaptiveExtensionClass();
		}
		
		return cachedAdaptiveClass;
	}
	
	private void injectExtension(Object instance)
	{
		try
		{
			Method[] methods = instance.getClass().getMethods();
			for (Method method : methods) 
			{
				if (method.getName().startsWith("set") &&
					method.getParameterTypes().length == 1 &&
					Modifier.isPublic(method.getModifiers()))
				{
					Class<?> pt = method.getParameterTypes()[0];
					if (pt.isInterface())
					{
						try
						{
							Object adaptive = getExtensionLoader(pt).getAdaptiveExtension();
							method.invoke(instance, adaptive);
						}
						catch (Exception e)
						{
							logger.error("fail to inject via method " + method.getName()
                            		+ " of interface " + type.getName() + ": " + e.getMessage(), e);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
	}
	
	private T createExtension(String name)
	{
		Class<?> clazz = getExtensionClasses().get(name);
		if (Objects.isNull(clazz))
		{
			throw new IllegalStateException("No such extension " + type.getName() + " by name " + name);
		}
		
		try
		{
			T instance = (T)clazz.newInstance();
			injectExtension(instance);
			
			Set<Class<?>> wrapperClasses = cachedWrapperClasses;
			if (Objects.nonEmpty(wrapperClasses))
			{
				for (Class<?> wrapperClass : wrapperClasses) 
				{
					instance = (T)wrapperClass.getConstructor(type).newInstance(instance);
					injectExtension(instance);
				}
			}
			
			return instance;
		}
		catch (Throwable t)
		{
			throw new IllegalStateException("Extension instance(name: " + name + ", class: " +
                    type + ")  could not be instantiated: " + t.getMessage(), t);
		}
	}
	
	private T createAdaptiveExtension()
	{
		try
		{
			return (T)getAdaptiveExtensionClass().newInstance();
		}
		catch (Exception e)
		{
			 throw new IllegalStateException("Can not create adaptive extenstion " + type + ", cause: " + e.getMessage(), e);
		}
	}
	
	private Class<?> createAdaptiveExtensionClass()
	{
		ClassLoader classLoader = findClassLoader();
		
		boolean hasAdaptiveAnnotion = false;
		// get all public method
		Method[] methods = type.getMethods();
		for (Method method : methods) 
		{
			if (method.isAnnotationPresent(Adaptive.class))
			{
				hasAdaptiveAnnotion = true;
				break;
			}
		}
		
		// 没有Adaptive方法，则不需要生成Adaptive类
		if (!hasAdaptiveAnnotion)
		{
			throw new IllegalStateException("No such adaptive class for extension " + type.getName());
		}
		
		ClassGenerator cg = ClassGenerator.newInstance(classLoader);
		cg.setClassName(type.getName() + "$Adpative");
		cg.addInterface(type);
		cg.addDefaultConstructor();
		
		for (Method method : methods) 
		{
			Class<?> rt = method.getReturnType();
			Class<?>[] pts = method.getParameterTypes();
			
			StringBuilder code = new StringBuilder(512);

			Adaptive adaptiveAnnotation = method.getAnnotation(Adaptive.class);
			if (Objects.isNull(adaptiveAnnotation))
			{
				code.append("throw new UnsupportedOperationException(\"method ");
				code.append(method.toString());
				code.append(" of interface ");
				code.append(type.getName());
				code.append(" is not adaptive method!\");");
			} 
			else
			{
				int urlTypeIndex = -1;
				for (int i = 0; i < pts.length; i++) 
				{
					if (pts[i].equals(URL.class))
					{
						urlTypeIndex = i;
						break;
					}
				}
				
				 // 有类型为URL的参数
				if (urlTypeIndex != -1)
				{
					code.append("if (arg").append(urlTypeIndex).append(" == null)");
					code.append("{ throw new IllegalArgumentException(\"url == null\"); }");
					
					code.append(URL.class.getName()).append(" url = arg").append(urlTypeIndex).append(';');
				}
				else
				{
					String attribMethod = null;
					LBL_PTS:
					for (int i = 0; i < pts.length; i++)
					{
						for (Method m : pts[i].getMethods()) 
						{
							String name = m.getName();
							if ((name.startsWith("get") || name.length() > 3) &&
									Modifier.isPublic(m.getModifiers()) &&
									!Modifier.isStatic(m.getModifiers()) &&
									m.getParameterTypes().length > 0 &&
									m.getReturnType() == URL.class)
							{
								urlTypeIndex = i;
								attribMethod = name;
								break LBL_PTS;
							}
						}
					}
					
					if (Objects.isNull(attribMethod))
					{
						 throw new IllegalStateException("fail to create adative class for interface " + type.getName()
	                        		+ ": not found url parameter or url attribute in parameters of method " + method.getName());
					}
					String s = String.format("if (arg%d == null)  { throw new IllegalArgumentException(\"%s argument == null\"); }", 
									urlTypeIndex, pts[urlTypeIndex].getName());
					code.append(s);
					
					s = String.format("if (arg%d.%s() == null)  { throw new IllegalArgumentException(\"%s argument %s() == null\"); }", 
							urlTypeIndex, attribMethod, pts[urlTypeIndex].getName(), attribMethod);
					code.append(s);
					
					s = String.format("%s url = arg%d.%s", URL.class.getName(), urlTypeIndex, attribMethod);
					code.append(s);
				}
				
				String[] value = adaptiveAnnotation.value();
				//没有设置Key，则使用“扩展点接口名的点分隔 作为Key
				if (value.length == 0)
				{
					char[] charArray = type.getSimpleName().toCharArray();
					StringBuilder sb = new StringBuilder(128);
					for (int i = 0; i < charArray.length; i++) 
					{
						if (Character.isUpperCase(charArray[i]))
						{
							if (i != 0)
							{
								sb.append('.');
							}
							sb.append(Character.toLowerCase(charArray[i]));
						}
						else
						{
							sb.append(charArray[i]);
						}
					}
					value = new String[]{sb.toString()};
				}
				
				String defaultExtName = cachedDefaultName;
				String getNameCode = null;
				for (int i = value.length - 1; i >= 0; --i) 
				{
					if (i == value.length - 1)
					{
						if (defaultExtName != null)
						{
							if (!"protocol".equals(value[i]))
							{
								getNameCode = String.format("url.getParameter(\"%s\", \"%s\")", value[i], defaultExtName);
							}
							else
							{
								getNameCode = String.format("url.getProtocol() == null ? \"%s\" : url.getProtocol()", defaultExtName);
							}
						}
						else
						{
							if (!"protocol".equals(value[i]))
							{
								getNameCode = String.format("url.getParameter(\"%s\")", value[i]);
							}
							else
							{
								getNameCode = "url.getProtocol()";
							}
						}
					}
					else
					{
						 if(!"protocol".equals(value[i]))
						 {
							 getNameCode = String.format("url.getParameter(\"%s\", %s)", value[i], getNameCode);
						 }
						 else
						 {
							 getNameCode = String.format("( url.getProtocol() == null ? (%s) : url.getProtocol() )", getNameCode);
						 }
					}
				}
				code.append("String extName = ").append(getNameCode).append(';');
				
				// check extName == null?
				String s = String.format("if (extName == null) { "
						+ "throw new IllegalStateException(\"Fail to get extension(%s) name from url(\" + url.toString() + \") use keys(%s)\"); }", 
						type.getName(), Arrays.toString(value));
				code.append("s");
				
				s = String.format("%s extension = (%s<s)%s.getExtensionLoader(%s.class).getExtension(extName);", 
						type.getName(), ExtensionLoader.class.getName(), type.getName());
				code.append("s");
				
				if (!rt.equals(void.class))
				{
					code.append("return ");
				}
				
				s = String.format("extension.%s(", method.getName());
				code.append(s);
				for (int j = 0; j < pts.length; j++) 
				{
					if (j != 0)
					{
						code.append(',');
					}
					code.append("arg").append(j);
				}
				code.append(");");
			}
			
			cg.addMethod(method.getName(), method.getModifiers(), rt, pts, method.getExceptionTypes(), code.toString());
		}
		
		return cg.toClass();
	}
	
	private Map<String, Class<?>> loadExtensionClass()
	{
		final Extension defaultAnnotation = type.getAnnotation(Extension.class);
		if (Objects.nonNull(defaultAnnotation))
		{
			String[] names = NAME_SEPARATOR.split(defaultAnnotation.value());
			if (names.length > 1)
			{
				throw new IllegalStateException("more than 1 default extension name on extension " + type.getName()
                        + ": " + Arrays.toString(names));
			}
			
			if (names.length == 1)
			{
				cachedDefaultName = names[0];
			}
		}
		
		ClassLoader classLoader = findClassLoader();
		Map<String, Class<?>> extensionClasses = new HashMap<>();
		String fileName = null;
		try
		{
			fileName = SERVICES_DIRECTORY + type.getName();
			Enumeration<java.net.URL> urls;
			if (classLoader != null)
			{
				urls = classLoader.getResources(fileName);
			}
			else
			{
				urls = ClassLoader.getSystemResources(fileName);
			}
			
			if (urls != null)
			{
				for ( ; urls.hasMoreElements();)
				{
					java.net.URL url = urls.nextElement();
					try (Reader inputReader = new InputStreamReader(url.openStream());
						 BufferedReader reader = new BufferedReader(inputReader))
					{
						String line = null;
						while ((line = reader.readLine()) != null)
						{
							line = line.trim();
							if (line.length() > 0)
							{
								try
								{
									Class<?> clazz = Class.forName(line, true, classLoader);
									if (!type.isAssignableFrom(clazz))
									{
										throw new IllegalStateException("Error when load extension class(interface: " +
												type + ", class line: " + clazz.getName() + "), class " 
												+ clazz.getName() + "is not subtype of interface.");
									}
									
									if (clazz.isAnnotationPresent(Adaptive.class))
									{
										if (cachedAdaptiveClass == null)
										{
											cachedAdaptiveClass = clazz;
										}
										else if (!cachedAdaptiveClass.equals(clazz))
										{
											 throw new IllegalStateException("More than 1 adaptive class found: "
                                                     + cachedAdaptiveClass.getClass().getName()
                                                     + ", " + clazz.getClass().getName());
										}
									}
									else
									{
										try
										{
											clazz.getConstructor(type);
											Set<Class<?>> autoproxies = cachedWrapperClasses;
											if (autoproxies == null)
											{
												cachedWrapperClasses = new ConcurrentHashSet<Class<?>>();
												autoproxies = cachedWrapperClasses;
											}
											autoproxies.add(clazz);
										}
										catch (NoSuchMethodException e)
										{
											clazz.getConstructor();
											Extension extension = clazz.getAnnotation(Extension.class);
											if (Objects.isNull(extension))
											{
												throw new IllegalStateException("No such @Extension annotation in class " + type.getName());
											}
											
											String name = extension.value();
											if (Objects.isEmpty(name))
											{
												throw new IllegalStateException("Illegal @Extension annotation in class " + type.getName());
											}
											
											String[] names = NAME_SEPARATOR.split(name);
											for (String n : names)
											{
												Class<?> c = extensionClasses.get(n);
												if (c == null)
												{
													extensionClasses.put(n, clazz);
												}
												else if (c != clazz)
												{
													throw new IllegalStateException("Duplicate extension " + type.getName() + " name " + n + " on " + c.getName() + " and " + clazz.getName());
												}
											}
										}
									}
									
								}
								catch (Throwable t)
								{
									logger.error("Exception when load extension class(interface: " +
                                            type + ", class line: " + line + ") in " + url, t);
								}
								
							}
						}
					}
					catch (Throwable t)
					{
						logger.error("Exception when load extension class(interface: " +
                                type + ", class file: " + url + ") in " + url, t);
					}
				}
			}
		} 
		catch (Throwable e)
		{
			 logger.error("Exception when load extension class(interface: " +
	                    type + ", description file: " + fileName + ").", e);
		}
		
		return extensionClasses;
	}
	
	private static ClassLoader findClassLoader()
	{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (Objects.isNull(loader))
		{
			loader = ExtensionLoader.class.getClassLoader(); 
		}
		
		return loader;
	}
}
