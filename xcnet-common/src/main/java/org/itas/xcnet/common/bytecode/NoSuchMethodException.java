/**
 * 
 */
package org.itas.xcnet.common.bytecode;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月11日上午10:08:42
 */
public class NoSuchMethodException extends RuntimeException 
{

	private static final long serialVersionUID = -5468822600194267554L;
	
	public NoSuchMethodException() 
	{
		super();
	}
	
	public NoSuchMethodException(String msg)
	{
		super(msg);
	}

}
