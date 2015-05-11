/**
 * 
 */
package org.itas.xcnet.common.bytecode;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月11日上午10:08:42
 */
public class NoSuchPropertyException extends RuntimeException 
{

	private static final long serialVersionUID = -5468822600194267554L;
	
	public NoSuchPropertyException() 
	{
		super();
	}
	
	public NoSuchPropertyException(String msg)
	{
		super(msg);
	}

}
