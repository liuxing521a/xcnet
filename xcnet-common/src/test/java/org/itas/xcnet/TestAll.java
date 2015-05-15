/**
 * 
 */
package org.itas.xcnet;

import org.itas.xcnet.common.ExtensionLoaderTest;
import org.itas.xcnet.common.bytecode.ProxyTest;
import org.itas.xcnet.common.bytecode.WrapperTest;
import org.itas.xcnet.common.io.BytesTest;
import org.itas.xcnet.common.io.StreamUtilsTest;
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
	//========org.itas.xcnet.common===========
	ExtensionLoaderTest.class,
	
	//========org.itas.xcnet.common.bytecode=========
	ProxyTest.class,
	WrapperTest.class,
	
	//========org.itas.xcnet.common.io==========
	BytesTest.class,
	StreamUtilsTest.class,

	//========utils==========
	CollectionUtilsTest.class,
})
public class TestAll 
{

}
