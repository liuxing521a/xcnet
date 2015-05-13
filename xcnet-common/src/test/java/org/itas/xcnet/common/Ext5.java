/**
 * 
 */
package org.itas.xcnet.common;


/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日上午10:21:46
 */
@Extension("impl1")
public interface Ext5 
{
	String echo(URL url, String s);
	
	String yell(URL url, String s);
	
	String bang(URL url, int i);
}
