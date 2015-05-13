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
@Extension("impl3")
public class Ext1Impl3 implements Ext1
{

	@Override
	public String echo(URL url, String s)
	{
		return "Ext1.Impl3-echo";
	}

	@Override
	public String yell(URL url, String s) 
	{
		return "Ext1.Impl3-yell";
	}

	@Override
	public String bang(URL url, int i) 
	{
		return "Ext1.Impl3-bang";
	}

}
