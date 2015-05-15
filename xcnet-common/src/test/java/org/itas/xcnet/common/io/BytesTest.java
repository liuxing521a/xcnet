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

import org.junit.Assert;
import org.junit.Test;




/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日上午10:31:45
 */
public class BytesTest {
	
	@Test
	public void testMain() {
		short s = (short)0xabcd;
		Assert.assertEquals(s, Bytes.bytes2short(Bytes.short2bytes(s)));
		
		int i = 198284;
		Assert.assertEquals(i, Bytes.bytes2int(Bytes.int2bytes(i)) );

		long l = 13841747174l;
		Assert.assertEquals(l, Bytes.bytes2long(Bytes.long2bytes(l)) );

		float f = 1.3f;
		Assert.assertEquals(f, Bytes.bytes2float(Bytes.float2bytes(f)), 0.02F );

		double d = 11213.3;
		Assert.assertEquals(d, Bytes.bytes2double(Bytes.double2bytes(d)), 0.02D);
	}
	
	@Test
	public void testBase64() {
		byte[] mBs = "abcdasl;dfqopij;ajdfglas[dfp[alkjsdl;kaslkdfjlighoiqok".getBytes();
		String str = Bytes.bytes2base64(mBs);
		byte[] b2 = Bytes.base642bytes(str);
		assertSame(mBs, b2);
		
		Assert.assertEquals("abcdasl;dfqopij;ajdfglas[dfp[alkjsdl;kaslkdfjlighoiqok", new String(b2));
	}
	
	@Test
	public void testBase64_s2b2s_FailCaseLog() throws Exception {
		byte[] bytes1 = {3,12,14,41,12,2,3,12,4,67,23};
		byte[] bytes2 = {3,12,14,41,12,2,3,12,4,67};
		
		String sq = Bytes.bytes2base64(bytes1);
		byte[] out1 = Bytes.base642bytes(sq);
		Assert.assertArrayEquals(bytes1, out1);
		
		sq = Bytes.bytes2base64(bytes2);
		out1 = Bytes.base642bytes(sq);
		Assert.assertArrayEquals(bytes2, out1);
	}
	
	@Test
	public void testHex() throws Exception {
		byte[] b1 = "abcdasl;dfqopij;ajdfglas[dfp[alkjsdl;kaslkdfjlighoiqok".getBytes();
		
		String str = Bytes.bytes2hex(b1);
		assertSame(b1, Bytes.hex2bytes(str));
	}
	
	private static void assertSame(byte[] b1, byte[] b2) {
		Assert.assertEquals(b1.length, b2.length);
		for (int i = 0; i < b2.length; i++) {
			Assert.assertEquals(b1[i], b2[i]);
		}
	}

}
