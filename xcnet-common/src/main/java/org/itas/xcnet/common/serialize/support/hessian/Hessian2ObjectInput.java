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
package org.itas.xcnet.common.serialize.support.hessian;

import java.io.IOException;
import java.io.InputStream;

import org.itas.xcnet.common.serialize.ObjectInput;

import com.caucho.hessian.io.Hessian2Input;

/**
 * Hessian2 Object Input
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午3:57:59
 */
public class Hessian2ObjectInput implements ObjectInput {

	private final Hessian2Input in;
	
	public Hessian2ObjectInput(InputStream is) {
		in = new Hessian2Input(is);
		in.setSerializerFactory(Hessian2SerializerFactory.SERIALIZER_FACTORY);
	}
	
	@Override
	public boolean readBool() throws IOException {
		return in.readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return (byte)in.readInt();
	}

	@Override
	public short readShort() throws IOException {
		return (short)in.readInt();
	}

	@Override
	public int readInt() throws IOException {
		return in.readInt();
	}

	@Override
	public long readLong() throws IOException {
		return in.readLong();
	}

	@Override
	public float readFloat() throws IOException {
		return (float)in.readDouble();
	}

	@Override
	public double readDouble() throws IOException {
		return in.readDouble();
	}

	@Override
	public byte[] readBytes() throws IOException {
		return in.readBytes();
	}

	@Override
	public String readUTF8() throws IOException {
		return in.readString();
	}

	@Override
	public Object readObject() throws IOException, ClassNotFoundException {
		return in.readObject();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
		return (T)in.readObject(cls);
	}

}
