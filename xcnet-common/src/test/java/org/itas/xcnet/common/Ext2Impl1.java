/**
 * 
 */
package org.itas.xcnet.common;

import org.itas.xcnet.common.Extension;
import org.itas.xcnet.common.URL;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日上午10:23:53
 */
@Extension("impl1")
public class Ext2Impl1 implements Ext2
{

	@Override
	public String echo(URLHolder url, String s)
	{
		return "Ext2.Impl1-echo";
	}

	@Override
	public String yell(URL url, String s) 
	{
		return "Ext2.Impl1-yell";
	}

	@Override
	public String bang(URL url, int i) 
	{
		return "Ext2.Impl1-bang";
	}

}
