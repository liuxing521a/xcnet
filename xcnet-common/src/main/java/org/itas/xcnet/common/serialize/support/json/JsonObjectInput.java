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

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.itas.xcnet.common.serialize.ObjectInput;
import org.itas.xcnet.common.utils.Objects;


/**
 * Json Object Input
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月14日上午10:18:25
 */
public class JsonObjectInput implements ObjectInput {

	private final BufferedReader reader;
	
	public JsonObjectInput(InputStream in) {
		this(new InputStreamReader(in));
	}
	
	public JsonObjectInput(Reader reader) {
		this.reader = new BufferedReader(reader);
	}
	
	@Override
	public boolean readBool() throws IOException {
		return false;
	}

	@Override
	public byte readByte() throws IOException {
		return 0;
	}

	@Override
	public short readShort() throws IOException {
		return 0;
	}

	@Override
	public int readInt() throws IOException {
		return 0;
	}

	@Override
	public long readLong() throws IOException {
		return 0;
	}

	@Override
	public float readFloat() throws IOException {
		return 0;
	}

	@Override
	public double readDouble() throws IOException {
		return 0;
	}

	@Override
	public String readUTF8() throws IOException {
		return null;
	}

	@Override
	public byte[] readBytes() throws IOException {
		return null;
	}

	@Override
	public Object readObject() throws IOException {
		String json = readLine();
		if (json.startsWith("{")) {
			//return JSON.parse(json, Map.class);
		} else {
//			 json = "{\"value\":" + json + "}";
//			 Map<String, Object> map = JSON.parse(json, Map.class);
//			 return map.get("value");
		}
		return null;
	}

	@Override
	public <T> T readObject(Class<T> cls) throws IOException {
//		try {
//			return JSON.parseObject(readLine(), cls);
//		} catch (IOException e) {
//			throw new IOException(e);
//		}
		return null;
	}

	private String readLine() throws IOException, EOFException{
		String line = reader.readLine();
		if (Objects.isEmpty(line)) {
			throw new EOFException();
		}
		return line;
	}
}
