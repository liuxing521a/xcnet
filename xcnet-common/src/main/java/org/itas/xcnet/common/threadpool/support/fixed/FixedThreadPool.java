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
package org.itas.xcnet.common.threadpool.support.fixed;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.itas.xcnet.common.Constants;
import org.itas.xcnet.common.URL;
import org.itas.xcnet.common.threadpool.ThreadPool;
import org.itas.xcnet.common.threadpool.support.AbortPolicyWithReport;
import org.itas.xcnet.common.utils.NamedThreadFactory;

/**
 * Fixed ThreadPool
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午2:01:17
 */
public class FixedThreadPool implements ThreadPool {

	@Override
	public Executor getExcutor(URL url) {
		String threadName = url.getParameter(Constants.THREAD_NAME_KEY, Constants.DEFAULT_THREAD_NAME);
		int threads = url.getIntParameter(Constants.THREADS_KEY, Constants.DEFAULT_THREADS);
        int queues = url.getIntParameter(Constants.QUEUES_KEY, Constants.DEFAULT_QUEUES);
		BlockingQueue<Runnable> queue = (queues <= 0) ?
				new SynchronousQueue<Runnable>() : new LinkedBlockingDeque<Runnable>();
        
        return new ThreadPoolExecutor(threads, 
	        		threads, 
	        		Constants.DEFAULT_THREAD_ALIVE, 
					TimeUnit.MILLISECONDS, queue, 
					new NamedThreadFactory(threadName, true), 
					new AbortPolicyWithReport(threadName, url));
	}

}
