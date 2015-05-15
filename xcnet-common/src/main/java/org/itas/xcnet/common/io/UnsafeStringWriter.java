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
import java.io.Writer;

/**
 * Thread unsafed StringWriter.
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日上午10:00:14
 */
public class UnsafeStringWriter extends Writer {

	private StringBuilder sb;
	
	public UnsafeStringWriter() {
		lock = sb = new StringBuilder();
	}
	
	public UnsafeStringWriter(int capacity) {
		lock = sb = new StringBuilder(capacity <= 0 ? 32 : capacity);
	}
	
	@Override
	public void write(int c) throws IOException {
		sb.append((char)c);
	}
	
	@Override
	public void write(char[] cbuf) throws IOException {
		sb.append(cbuf, 0, cbuf.length);
	}
	
	@Override
	public void write(char[] chs, int off, int len) throws IOException {
		if ((off < 0) || (off > chs.length) || (len < 0) || ((off + len) > chs.length) || ((off + len) < 0))
			throw new IndexOutOfBoundsException();
		
		if (len > 0)
			sb.append(chs, off, len);
	}
	
	@Override
	public void write(String str) throws IOException {
		sb.append(str);
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		sb.append(str.substring(off, (off + len)));
	}
	
	@Override
	public Writer append(char c) throws IOException {
		sb.append(c);
		return this;
	}
	
	@Override
	public Writer append(CharSequence csq) throws IOException {
		sb.append(csq == null ? "null" : csq.toString());
		return this;
	}
	
	@Override
	public Writer append(CharSequence csq, int start, int end) throws IOException {
		CharSequence cs = (csq == null ? "null" : csq);
		write(cs.subSequence(start, end).toString());
		return this;
	}
	
	@Override
	public void flush() throws IOException {

	}

	@Override
	public void close() throws IOException {

	}
	
	@Override
	public String toString() {
		return sb.toString();
	}

}
