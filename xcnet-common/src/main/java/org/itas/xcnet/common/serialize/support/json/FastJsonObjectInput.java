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
import java.util.Map;

import org.itas.xcnet.common.serialize.ObjectInput;
import org.itas.xcnet.common.utils.Objects;

import com.alibaba.fastjson.JSON;

/**
 * json object input
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日下午5:38:38
 */
public class FastJsonObjectInput implements ObjectInput {

	private final BufferedReader reader;
	
	public FastJsonObjectInput(InputStream in) {
		this(new InputStreamReader(in));
	}
	
	public FastJsonObjectInput(Reader reader) {
		this.reader = new BufferedReader(reader);
	}
	
	@Override
	public boolean readBool() throws IOException {
		try {
			return readObject(boolean.class);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public byte readByte() throws IOException {
		try {
			return readObject(byte.class);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public short readShort() throws IOException {
		try {
			return readObject(short.class);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public int readInt() throws IOException {
		try {
			return readObject(int.class);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public long readLong() throws IOException {
		try {
			return readObject(long.class);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public float readFloat() throws IOException {
		try {
			return readObject(float.class);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public double readDouble() throws IOException {
		try {
			return readObject(double.class);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public String readUTF8() throws IOException {
		try {
			return readObject(String.class);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public byte[] readBytes() throws IOException {
		return readLine().getBytes();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object readObject() throws IOException {
		String json = readLine();
		
		if (json.startsWith("{")) {
			return JSON.parseObject(json, Map.class);
		} else {
			json = "{\"value\":" + json + "}";
			Map<String, Object> map = JSON.parseObject(json, Map.class);
			return map.get("value");
		}
	}

	@Override
	public <T> T readObject(Class<T> cls) throws IOException {
		return JSON.parseObject(readLine(), cls);
	}
	
	private String readLine() throws IOException, EOFException {
		String line = reader.readLine();
		if (Objects.isEmpty(line)) {
			throw new EOFException();
		}
		
		return line;
	}

}
