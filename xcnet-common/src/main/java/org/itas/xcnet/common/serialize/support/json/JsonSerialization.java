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
import java.io.InputStream;
import java.io.OutputStream;

import org.itas.xcnet.common.URL;
import org.itas.xcnet.common.serialize.ObjectInput;
import org.itas.xcnet.common.serialize.ObjectOutput;
import org.itas.xcnet.common.serialize.Serialization;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午4:15:21
 */
public class JsonSerialization implements Serialization {

	@Override
	public byte getContentTypeId() {
		return 5;
	}

	@Override
	public String getContentType() {
		return "text/json";
	}

	@Override
	public ObjectOutput serialize(URL url, OutputStream out) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectInput deserialize(URL url, InputStream in) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
