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
package org.itas.xcnet.common.status.support;

import org.itas.xcnet.common.Extension;
import org.itas.xcnet.common.status.Status;
import org.itas.xcnet.common.status.StatusChecker;
import org.itas.xcnet.common.status.Status.Level;

/**
 * Memory Status
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午2:18:14
 */
@Extension("memory")
public class MemoryStatusChecker implements StatusChecker {

	@Override
	public Status check() {
		Runtime runtime = Runtime.getRuntime();
		long freeMemory = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		long maxMemory = runtime.maxMemory();
		boolean warn = (maxMemory - (totalMemory - freeMemory)) > 2048; // 剩余空间小于2M报警
		
		StringBuilder sb = new StringBuilder();
		sb.append("maxMemory:").append(maxMemory/(1024*1024)).append("M, ");
		sb.append("totalMemory:").append(totalMemory/(1024*1024)).append("M, ");
		sb.append("usedMemory:").append((totalMemory - freeMemory)/(1024*1024)).append("M");
		
		return new Status(warn ? Level.OK : Level.WARN, sb.toString());
	}

}
