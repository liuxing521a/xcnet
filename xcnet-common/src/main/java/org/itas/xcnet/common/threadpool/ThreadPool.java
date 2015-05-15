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
package org.itas.xcnet.common.threadpool;

import java.util.concurrent.Executor;

import org.itas.xcnet.common.Adaptive;
import org.itas.xcnet.common.Extension;
import org.itas.xcnet.common.URL;
import org.itas.xcnet.common.Constants;


/**
 * thread pool
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午1:41:09
 */
@Extension("fixed")
public interface ThreadPool {

	/**
	 * 线程池
	 * 
	 * @param url
	 * @return
	 */
	@Adaptive(Constants.THREADPOOL_KEY)
	Executor getExcutor(URL url);
	
}
