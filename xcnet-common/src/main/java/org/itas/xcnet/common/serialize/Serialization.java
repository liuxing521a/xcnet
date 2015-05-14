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
package org.itas.xcnet.common.serialize;

import java.io.InputStream;
import java.io.OutputStream;

import org.itas.xcnet.common.Adaptive;
import org.itas.xcnet.common.Extension;
import org.itas.xcnet.common.URL;

/**
 * Serialization (SPI, Singleton, ThreadSafe)
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日下午5:30:32
 */
@Extension("hessian2")
public interface Serialization {

	/**
	 * get content type id
	 * 
	 * @return content id
	 */
	byte getContentTypeId();
	
	/**
	 * get content type
	 * 
	 * @return content type
	 */
	String getContentType();
	
	/**
	 * serialize 
	 * 
	 * @param url 
	 * @param out 
	 * @return
	 * @throws java.io.IOException
	 */
	@Adaptive
	ObjectOutput serialize(URL url, OutputStream out) throws java.io.IOException;
	
	/**
	 * deserialize
	 * 
	 * @param url
	 * @param in
	 * @return
	 * @throws java.io.IOException
	 */
	@Adaptive
	ObjectInput deserialize(URL url, InputStream in) throws java.io.IOException;
}
