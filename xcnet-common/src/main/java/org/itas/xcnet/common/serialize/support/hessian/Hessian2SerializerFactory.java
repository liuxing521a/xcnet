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

import com.caucho.hessian.io.SerializerFactory;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午4:00:05
 */
public class Hessian2SerializerFactory extends SerializerFactory {
	
	public static final SerializerFactory SERIALIZER_FACTORY = new Hessian2SerializerFactory();
	
	private Hessian2SerializerFactory() {
	}
	
	@Override
	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

}
