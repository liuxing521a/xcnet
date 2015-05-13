/**
 * 
 */
package org.itas.xcnet.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日上午11:12:56
 */
public class Ext5AutoProxy2 implements Ext5 
{

	public static AtomicInteger echoCount = new AtomicInteger(0);
	public static AtomicInteger yellCount = new AtomicInteger(0);
	public static AtomicInteger bangCount = new AtomicInteger(0);
	
	private Ext5 instance;
	
	public Ext5AutoProxy2(Ext5 instance)
	{
		this.instance = instance;
	}
	
	@Override
	public String echo(URL url, String s) 
	{
		echoCount.incrementAndGet();
		return instance.echo(url, s);
	}

	@Override
	public String yell(URL url, String s)
	{
		yellCount.incrementAndGet();
		return instance.yell(url, s);
	}

	@Override
	public String bang(URL url, int i) 
	{
		bangCount.incrementAndGet();
		return instance.bang(url, i);
	}

}
