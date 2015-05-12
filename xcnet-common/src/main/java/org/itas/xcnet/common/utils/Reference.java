/**
 * 
 */
package org.itas.xcnet.common.utils;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月12日下午2:13:32
 */
public class Reference<T> 
{
	private T value;
	
	public Reference()
	{
		this(null);
	}
	
	public Reference(T value)
	{
		this.value = value;
	}
	
	public T get()
	{
		return value;
	}
	
	public void set(T value)
	{
		this.value = value;
	}
	
	public boolean isPresent()
	{
		return Objects.nonNull(value);
	}
}
