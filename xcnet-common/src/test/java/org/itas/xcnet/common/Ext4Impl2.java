/**
 * 
 */
package org.itas.xcnet.common;

import java.util.List;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日下午3:20:04
 */
@Extension("impl2")
public class Ext4Impl2 implements Ext4 
{

	public String echo(URL url, String s)
	{
        return "Ext4.Impl2-echo";
    }
    
    public String yell(URL url, String s) 
    {
        return "Ext4.Impl2-yell";
    }

    public String bang(URL url, int i) 
    {
        return "Ext4.Impl2-bang";
    }
    
    @Adaptive
    public String bark(String name, List<Object> list) 
    {
        return null;
    }

}
