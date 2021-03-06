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
import java.io.ObjectStreamClass;
import java.io.OutputStream;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午3:16:14
 */
public class CompactedObjectOutputStream extends ObjectOutputStream {

	public CompactedObjectOutputStream(OutputStream os) throws IOException {
		super(os);
	}
	
	@Override
	protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
		Class<?> clazz = desc.forClass();
		if (clazz.isPrimitive() || clazz.isArray()) {
			write(0);
			super.writeClassDescriptor(desc);
		} else {
			write(0);
			writeUTF(desc.getName());
		}
	}
	
}
