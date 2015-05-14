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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Unsafe Byte Array OutputStrem.
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月14日下午4:50:11
 */
public class UnsafeByteArrayOutputStream extends OutputStream {

	private int position;

	private byte[] bytes;
	
	public UnsafeByteArrayOutputStream() {
		
	}
	
	public UnsafeByteArrayOutputStream(int size) {
		bytes = new byte[size <= 0 ? 32 : size];
	}
	
	@Override
	public void write(int b) throws IOException {
		int newPosition = position + 1;
		if (newPosition > bytes.length) {
			bytes = Bytes.copyOf(bytes, Math.max(bytes.length << 1, newPosition));
		}
		
		bytes[position] = (byte)b;
		position = newPosition;
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0 )) 
			throw new IndexOutOfBoundsException();
		
		if (len == 0) {
			return;
		}
		
		int newPosition = position + len;
		if (newPosition > bytes.length) {
			bytes = Bytes.copyOf(bytes, Math.max(bytes.length << 1, newPosition));
		}
		System.arraycopy(b, off, bytes, position, len);
		position = newPosition;
	}
	
	public int size() {
		return position;
	}
	
	public void reset() {
		position = 0;
	}
	
	public byte[] toByteArray() {
		return Bytes.copyOf(bytes, position);
	}
	
	public ByteBuffer toByteBuffer() {
		return ByteBuffer.wrap(bytes, 0, position);
	}
	
	public void writeTo(OutputStream os) throws IOException {
		os.write(bytes, 0, position);
	}
	
	@Override
	public String toString() {
		return new String(bytes, 0, position);
	}
	
	public String toString(String charset) throws UnsupportedEncodingException {
		return new String(bytes, 0, position, charset);
	}
	
	@Override
	public void close() throws IOException {
	}

}
