/*
 * Copyright 2014-2015 itas group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.itas.xcnet.common.io;


import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日上午11:24:33
 */
public class StreamUtilsTest {

	@Test
	public void testMarkSupportedInputStream() throws IOException {
		InputStream is = new ByteArrayInputStream("0123456789".getBytes());
		Assert.assertEquals(10, is.available());
		
		is = new PushbackInputStream(is);
		Assert.assertEquals(10, is.available());
		Assert.assertFalse(is.markSupported());
		
		InputStream ins = StreamUtils.markSupportedInputStream(is);
		Assert.assertEquals(10, ins.available());
		
		ins.mark(0);
		assertEquals((int) '0', ins.read());
		assertEquals((int) '1', ins.read());
        
		ins.reset();
        assertEquals((int) '0', ins.read());
		assertEquals((int) '1', ins.read());
        assertEquals((int) '2', ins.read());
        
        ins.mark(0);
        assertEquals((int) '3', ins.read());
		assertEquals((int) '4', ins.read());
        assertEquals((int) '5', ins.read());
        
        ins.reset();
        assertEquals((int) '3', ins.read());
		assertEquals((int) '4', ins.read());
		
		ins.mark(0);
		assertEquals((int) '5', ins.read());
		assertEquals((int) '6', ins.read());
		
		ins.reset();
		assertEquals((int) '5', ins.read());
		assertEquals((int) '6', ins.read());
		assertEquals((int) '7', ins.read());
		assertEquals((int) '8', ins.read());
		assertEquals((int) '9', ins.read());
		
	    assertEquals(-1, ins.read());
        assertEquals(-1, is.read());
        
        ins.mark(0);
        assertEquals(-1, ins.read());
        assertEquals(-1, ins.read());
        
        ins.reset();
        assertEquals(-1, ins.read());
        assertEquals(-1, ins.read());
        
	}
}
