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
package org.itas.xcnet.common.serialize.support.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.itas.xcnet.common.serialize.ObjectInput;


/**Java ObjectInput
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午3:37:59
 */
public class JavaObjectInput implements ObjectInput {

	public final static int MAX_BYTE_ARRAY_LENGTH = 8 * 1024 * 1024;
	
	private final ObjectInputStream input;
	
	public JavaObjectInput(InputStream is) throws IOException {
		input = new ObjectInputStream(is);
	}

	public JavaObjectInput(InputStream is, boolean compacted) throws IOException {
		input = compacted ? new CompactedObjectInputStream(is) : new ObjectInputStream(is);
	}
	
	@Override
	public boolean readBool() throws IOException {
		return input.readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return input.readByte();
	}

	@Override
	public short readShort() throws IOException {
		return input.readShort();
	}

	@Override
	public int readInt() throws IOException {
		return input.readInt();
	}

	@Override
	public long readLong() throws IOException {
		return input.readLong();
	}

	@Override
	public float readFloat() throws IOException {
		return input.readFloat();
	}

	@Override
	public double readDouble() throws IOException {
		return input.readDouble();
	}

	@Override
	public byte[] readBytes() throws IOException {
		int len = input.readInt();
		if (len < 0)
			return null;
		if (len == 0)
			return new byte[0];
		if (len > MAX_BYTE_ARRAY_LENGTH)
			throw new IOException("Byte array length too large. " + len);
		
		byte[] b = new byte[len];
		input.readFully(b);
		return b;
	}

	@Override
	public String readUTF8() throws IOException {
		if (input.readInt() < 0)
			return null;
		else
			return input.readUTF();
	}

	@Override
	public Object readObject() throws IOException, ClassNotFoundException {
		int is = input.readInt();
		if (is < 0)
			return null;
		else
			return input.readObject();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
		return (T)readObject();
	}


}
