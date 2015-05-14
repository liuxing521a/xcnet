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

import java.io.IOException;
import java.io.InputStream;

/**
 * unsafe byte array InputStream
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月14日下午5:02:24
 */
public class UnsafeByteArrayInputStream extends InputStream {

	private byte[] bytes;
	
	private int positon, limit, mark;
	
	public UnsafeByteArrayInputStream(byte[] bs) {
		this(bs, 0);
	}
	
	public UnsafeByteArrayInputStream(byte[] bs, int off) {
		this(bs, off, bs.length - off);
	}
	
	public UnsafeByteArrayInputStream(byte[] bs, int off, int len) {
		bytes = bs;
		positon = mark = off;
		limit = Math.min(off + len, bs.length);
	}
	
	@Override
	public int read() throws IOException {
		return (positon < limit) ? bytes[positon ++] & 0xFF : -1;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if( b == null )
		    throw new NullPointerException();
		if( off < 0 || len < 0 || len > b.length - off )
		    throw new IndexOutOfBoundsException();
		
		if (positon >= limit) {
			return -1;
		}
		
		if (positon + len > limit) {
			len = limit - positon;
		}
		if (len < 0) {
			return 0;
		}
		
		System.arraycopy(bytes, positon, b, 0, len);
		positon += len;
		return len;
	}

	@Override
	public long skip(long len) throws IOException {
		if (positon + len >= limit) {
			len = limit - positon;
		}
		if (limit <= 0) {
			return 0;
		}
		
		positon += len;
		return len;
	}
	
	@Override
	public int available() throws IOException {
		return limit - positon;
	}
	
	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public synchronized void mark(int readlimit) {
		mark = positon;
	}
	
	@Override
	public void reset() throws IOException {
		positon = mark;
	}
	
	@Override
	public void close() throws IOException {
	}
	
	public int position() {
		return positon;
	}

	public void position(int positon) {
		this.positon = positon;
	}
	
	public int size() {
		return bytes == null ? 0 : bytes.length;
	}
}
