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
package org.itas.xcnet.common.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Internal ThreadFactory.
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午1:54:46
 */
public class NamedThreadFactory implements ThreadFactory {

	private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);
	
	private final AtomicInteger mThreadNum = new AtomicInteger(1);
	
	private final String mPrefix;
	
	private final boolean mDaemo;
	
	private final ThreadGroup mGroup;
	
	public NamedThreadFactory() {
		this("pool-" + POOL_SEQ.getAndIncrement(), false);
	}

	public NamedThreadFactory(String prefix) {
		this(prefix,false);
	}
	
	public NamedThreadFactory(String preffix, boolean daemo) {
		mPrefix = preffix + "-thread-";
		mDaemo = daemo;
		SecurityManager s = System.getSecurityManager();
		mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
	}
	
	@Override
	public Thread newThread(Runnable r) {
		String name = mPrefix + mThreadNum.getAndDecrement();
		Thread thread = new Thread(mGroup, name);
		thread.setDaemon(mDaemo);
		return thread;
	}

}
