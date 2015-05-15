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
package org.itas.xcnet.common.threadpool.support.cached;

import static org.itas.xcnet.common.Constants.DEFAULT_QUEUES;
import static org.itas.xcnet.common.Constants.DEFAULT_THREAD_ALIVE;
import static org.itas.xcnet.common.Constants.DEFAULT_THREAD_NAME;
import static org.itas.xcnet.common.Constants.QUEUES_KEY;
import static org.itas.xcnet.common.Constants.THREADS_KEY;
import static org.itas.xcnet.common.Constants.THREAD_ALIVE_KEY;
import static org.itas.xcnet.common.Constants.THREAD_NAME_KEY;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.itas.xcnet.common.URL;
import org.itas.xcnet.common.threadpool.ThreadPool;
import org.itas.xcnet.common.threadpool.support.AbortPolicyWithReport;
import org.itas.xcnet.common.utils.NamedThreadFactory;

/**
 * Cached ThreadPool
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午1:47:32
 */
public class CachedThreadPool implements ThreadPool {

	@Override
	public Executor getExcutor(URL url) {
		String threadName = url.getParameter(THREAD_NAME_KEY, DEFAULT_THREAD_NAME);
		int threads = url.getIntParameter(THREADS_KEY, Integer.MAX_VALUE);
		int queues = url.getIntParameter(QUEUES_KEY, DEFAULT_QUEUES);
		int keepalive = url.getIntParameter(THREAD_ALIVE_KEY, DEFAULT_THREAD_ALIVE);
		
		BlockingQueue<Runnable> queue = (queues <= 0) ? 
				new SynchronousQueue<Runnable>() : new LinkedBlockingQueue<Runnable>(queues);
				
		return new ThreadPoolExecutor(0, 
					threads, 
					keepalive, 
					TimeUnit.MILLISECONDS, 
					queue, 
					new NamedThreadFactory(threadName, true), 
					new AbortPolicyWithReport(threadName, url));
	}

}
