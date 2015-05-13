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
package org.itas.xcnet.common.serialize.support.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import org.itas.xcnet.common.serialize.ObjectOutput;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json object output
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日下午5:49:19
 */
public class FastJsonObjectOutput implements ObjectOutput {

	private final PrintWriter writer;
	
	public FastJsonObjectOutput(OutputStream out) {
		this(new OutputStreamWriter(out));
	}
	
	public FastJsonObjectOutput(Writer writer) {
		this.writer = new PrintWriter(writer);
	}
	
	@Override
	public void writeBool(boolean v) throws IOException {
		writeObject(v);
	}

	@Override
	public void writeByte(byte v) throws IOException {
		writeObject(v);
	}

	@Override
	public void writeShort(short v) throws IOException {
		writeObject(v);
	}

	@Override
	public void writeInt(int v) throws IOException {
		writeObject(v);
	}

	@Override
	public void writeLong(long v) throws IOException {
		writeObject(v);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		writeObject(v);
	}

	@Override
	public void writeDouble(double v) throws IOException {
		writeObject(v);
	}

	@Override
	public void writeUTF8(String v) throws IOException {
		writeObject(v);
	}

	@Override
	public void writeBytes(byte[] v) throws IOException {
		writer.println(new String(v));
	}

	@Override
	public void writeBytes(byte[] v, int off, int len) throws IOException {
		writer.println(new String(v, off, len));
	}

	@Override
	public void writeObject(Object o) throws IOException {
		SerializeWriter out = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(out);
		serializer.config(SerializerFeature.WriteEnumUsingToString, true);
		serializer.write(o);
		out.writeTo(writer);
		writer.println();
        writer.flush();
	}
	
	@Override
	public void flushBuffer() throws IOException {
		writer.flush();
	}

}