/**
 * 
 */
package org.itas.xcnet.common;

/**
 * 节点<p>
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日下午4:30:45
 */
public interface Node 
{
	/**
	 * get node url.<p>
	 * 
	 * @return node self url
	 */
	public URL getURL();
	
	/**
	 * node is useAble<p>
	 * 
	 * @return can use or can't use
	 */
	public boolean isAvaliable();
	
	/**
	 * node destory
	 */
	public void destory();

}
