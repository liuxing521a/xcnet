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
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.itas.xcnet.common.serialize.ObjectOutput;

/**
 * Java Object output.
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午3:10:18
 */
public class JavaObjectOutput implements ObjectOutput {

	private final ObjectOutputStream out;
	
	public JavaObjectOutput(OutputStream os) throws IOException {
		out = new ObjectOutputStream(os);
	}
	
	public JavaObjectOutput(OutputStream os, boolean compact) throws IOException {
		out = compact ? new CompactedObjectOutputStream(os) : new ObjectOutputStream(os);
	}
	
	@Override
	public void writeBool(boolean v) throws IOException {
		out.writeBoolean(v);
	}

	@Override
	public void writeByte(byte v) throws IOException {
		out.writeByte(v);
	}

	@Override
	public void writeShort(short v) throws IOException {
		out.writeShort(v);
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
		out.writeFloat(v);
	}

	@Override
	public void writeDouble(double v) throws IOException {
		out.writeDouble(v);
	}

	@Override
	public void writeBytes(byte[] v) throws IOException {
		if (v == null)
			out.writeInt(-1);
		else
			out.write(v);
	}

	@Override
	public void writeBytes(byte[] v, int off, int len) throws IOException {
		out.writeInt(len);
		out.write(v, off, len);
	}

	@Override
	public void writeUTF8(String v) throws IOException {
		if (v == null) {
			out.writeInt(-1);
		} else {
			out.writeInt(v.length());
			out.writeUTF(v);
		}
	}
	
	@Override
	public void writeObject(Object o) throws IOException {
		if (o == null) {
			out.writeInt(-1);
		} else {
			out.writeInt(1);
			out.writeObject(o);
		}
	}
	
	@Override
	public void flushBuffer() throws IOException {
		out.flush();
	}

}
