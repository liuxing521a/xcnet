/**
 * 
 */
package org.itas.xcnet.common.bytecode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月11日下午1:58:07
 */
public class ProxyTest 
{
	
	@Test
	public void test()
	{
		Proxy proxy = Proxy.getProxy(ITest.class);
		ITest instance = (ITest)proxy.newInstance(new InvocationHandler() 
		{
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
			{
				if ("getName".equals(method.getName()))
				{
					Assert.assertEquals(0, args.length);
				}
				else if ("setName".equals(method.getName()))
				{
					Assert.assertEquals(2, args.length);
					Assert.assertEquals("李玟航", args[0]);
					Assert.assertEquals("刘雨轩", args[1]);
				}
				
				
				return null;
			}
		});
		
		Assert.assertNull(instance.getName());
		instance.setName("李玟航", "刘雨轩");
	}
	
	public static interface ITest
	{
		String getName();
		
		void setName(String arg1, String arg2);
	}

}
