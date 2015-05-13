/**
 * 
 */
package org.itas.xcnet.common;

import org.itas.xcnet.common.Adaptive;
import org.itas.xcnet.common.URL;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日上午10:26:45
 */
public interface Ext2 
{

	@Adaptive
	String echo(URLHolder holder, String s);
	
	@Adaptive({"key1", "protocol"})
	String yell(URL url, String s);
	
	String bang(URL url, int i);
}
