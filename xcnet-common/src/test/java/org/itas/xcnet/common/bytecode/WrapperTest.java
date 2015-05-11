/**
 * 
 */
package org.itas.xcnet.common.bytecode;

import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月11日下午5:34:05
 */
public class WrapperTest 
{
	@Test
	public void testMain() throws NoSuchMethodException, InvocationTargetException
	{
		Wrapper w = Wrapper.getWrapper(I1.class);
		String[] ns = w.getDeclaredMethodNames();
		Assert.assertEquals(5, ns.length);
		
		ns = w.getMethodNames();
		Assert.assertEquals(6, ns.length);
		
		Object obj = new Impl1();
		Assert.assertEquals("your name", w.getPropertyValue(obj, "name"));
		
		w.setPropertyValue(obj, "name", "航宇轩");
		Assert.assertEquals("your name", w.getPropertyValue(obj, "航宇轩"));
		
		w.invokeMethod(obj, "hello", new Class<?>[]{String.class}, new Object[]{"玟航"});
	}

	public static class Impl0
	{
		public float a,b,c;
	}
	
	public static interface I0
	{
		String getName();
	}
	
	public static interface I1 extends I0
	{
		void setName(String name);
		
		void hello(String name);
		
		int showInt(int value);
		
		void setFloat(float value);
		
		float getFloat();
	}
	
	public static class Impl1 implements I1
	{
		private String name = "your name";
		
		private float value = 0F;

		@Override
		public String getName() 
		{
			return name;
		}

		@Override
		public void setName(String name) 
		{
			this.name = name;
		}

		@Override
		public void hello(String name) 
		{
			System.out.println("hello " + name);
		}

		@Override
		public int showInt(int value) 
		{
			return value;
		}

		@Override
		public void setFloat(float value) 
		{
			this.value = value;
		}

		@Override
		public float getFloat() 
		{
			return value;
		}
		
	}
}
