/**
 * 
 */
package org.itas.xcnet.common;

import org.itas.xcnet.common.bytecode.ProxyTest;
import org.itas.xcnet.common.bytecode.WrapperTest;
import org.itas.xcnet.common.utils.CollectionUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月11日下午2:32:13
 */
@RunWith(Suite.class)
@SuiteClasses({
	//========bytecode=========
	ProxyTest.class,
	WrapperTest.class,
	
	//========utils==========
	CollectionUtilsTest.class,
})
public class TestAll 
{

}
