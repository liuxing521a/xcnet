/**
 * 
 */
package org.itas.xcnet.common;

import java.util.List;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日下午3:18:59
 */
@Extension("impl1")
public interface Ext4 
{
	 @Adaptive
	 String bark(String name, List<Object> list); // 没有URL参数的方法

}
