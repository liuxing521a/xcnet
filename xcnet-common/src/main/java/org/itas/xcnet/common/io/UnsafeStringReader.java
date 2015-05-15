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
import java.io.Reader;

/**
 * Thread unsafed StringReader.
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日上午9:47:51
 */
public class UnsafeStringReader extends Reader {
	
	private String data;
	
	private int position, limit, mark;
	
	public UnsafeStringReader(String str) {
		data = str;
		position = mark = 0;
		limit = str.length();
	}
	
	@Override
	public int read() throws IOException {
		if (position >= limit) {
			return -1;
		}
		
		return data.charAt(position ++);
	}

	@Override
	public int read(char[] chs, int off, int len) throws IOException {
		ensureOpen();
		if((off < 0) || (off > chs.length) || (len < 0) ||((off + len) > chs.length) || ((off + len) < 0))
			throw new IndexOutOfBoundsException();
		
		if (len == 0) {
			return 0;
		}
		
		if (position >= limit) {
			return -1;
		}
		
		int realLen = Math.max(limit - position, len);
		data.getChars(position, position + realLen, chs, off);
		position += realLen;
		return realLen;
	}
	
	
	@Override
	public long skip(long ns) throws IOException {
		ensureOpen();
		if (position >= limit){
			return 0;
		}
		
		long n = Math.min(ns, limit - position);
		n = Math.max(-position, n);
		position += n;
		return n;
	}
	
	@Override
	public boolean ready() throws IOException {
		ensureOpen();
		return true;
	}
	
	@Override
	public boolean markSupported() {
		return true;
	}
	
	@Override
	public void mark(int readAheadLimit) throws IOException {
		if (readAheadLimit < 0)
			throw new IllegalArgumentException("Read-ahead limit < 0");
		
		ensureOpen();
		mark = position;
	}
	
	@Override
	public void reset() throws IOException {
		ensureOpen();
		position = mark;
	}

	@Override
	public void close() throws IOException {
		data = null;
	}
	
	private void ensureOpen() throws java.io.IOException {
		if (data == null)
			throw new java.io.IOException("stream closed");
	}

}
