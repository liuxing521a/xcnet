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
import java.io.OutputStream;

import org.itas.xcnet.common.serialize.ObjectOutput;

import com.caucho.hessian.io.Hessian2Output;

/**
 * Hessian2 Object Output
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午4:04:14
 */
public class Hessian2ObjectOutput implements ObjectOutput {
	
	private final Hessian2Output out;
	
	public Hessian2ObjectOutput(OutputStream os) {
		out = new Hessian2Output(os);
		out.setSerializerFactory(Hessian2SerializerFactory.SERIALIZER_FACTORY);
	}

	@Override
	public void writeBool(boolean v) throws IOException {
		out.writeBoolean(v);
	}

	@Override
	public void writeByte(byte v) throws IOException {
		out.writeInt(v);
	}

	@Override
	public void writeShort(short v) throws IOException {
		out.writeInt(v);
	}

	@Override
	public void writeInt(int v) throws IOException {
		out.writeInt(v);
	}

	@Override
	public void writeLong(long v) throws IOException {
		out.writeLong(v);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		out.writeDouble(v);
	}

	@Override
	public void writeDouble(double v) throws IOException {
		out.writeDouble(v);
	}

	@Override
	public void writeUTF8(String v) throws IOException {
		out.writeString(v);
	}

	@Override
	public void writeBytes(byte[] v) throws IOException {
		out.writeBytes(v);
	}

	@Override
	public void writeBytes(byte[] v, int off, int len) throws IOException {
		out.writeBytes(v, off, len);
	}

	@Override
	public void writeObject(Object o) throws IOException {
		out.writeObject(o);
	}
	
	@Override
	public void flushBuffer() throws IOException {
		out.flushBuffer();
	}

}
