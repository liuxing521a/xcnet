/**
 * 
 */
package org.itas.xcnet.common;

import org.itas.xcnet.common.Adaptive;
import org.itas.xcnet.common.Extension;
import org.itas.xcnet.common.URL;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日上午10:21:46
 */
@Extension("impl3")
public interface Ext3 
{
	@Adaptive({"key1", "protocol"})
	String echo(URL url, String s);
	
	@Adaptive({"protocol", "key2"})
	String yell(URL url, String s);
	
	String bang(URL url, int i);
}
