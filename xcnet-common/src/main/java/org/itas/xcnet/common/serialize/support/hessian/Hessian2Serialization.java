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
import java.io.OutputStream;

import org.itas.xcnet.common.URL;
import org.itas.xcnet.common.serialize.ObjectInput;
import org.itas.xcnet.common.serialize.ObjectOutput;
import org.itas.xcnet.common.serialize.Serialization;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午4:11:33
 */
public class Hessian2Serialization implements Serialization {

	@Override
	public byte getContentTypeId() {
		return 2;
	}

	@Override
	public String getContentType() {
		return "x-application/hessian2";
	}

	@Override
	public ObjectOutput serialize(URL url, OutputStream out) throws IOException {
		return new Hessian2ObjectOutput(out);
	}

	@Override
	public ObjectInput deserialize(URL url, InputStream in) throws IOException {
		return new Hessian2ObjectInput(in);
	}

}
