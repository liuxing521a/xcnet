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
@Extension("impl2")
public class Ext5Impl2 implements Ext5
{

	@Override
	public String echo(URL url, String s)
	{
		return "Ext5.Impl2-echo";
	}

	@Override
	public String yell(URL url, String s) 
	{
		return "Ext5.Impl2-yell";
	}

	@Override
	public String bang(URL url, int i) 
	{
		return "Ext5.Impl2-bang";
	}

}
