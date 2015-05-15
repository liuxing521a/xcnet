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

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import org.itas.xcnet.common.status.Status;
import org.itas.xcnet.common.status.Status.Level;
import org.itas.xcnet.common.status.StatusChecker;

/**
 * Load Status
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午2:25:04
 */
public class LoadStatusChecker implements StatusChecker {

	@Override
	public Status check() {
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		double load = operatingSystemMXBean.getSystemLoadAverage();// 平均负载
		int cpu = operatingSystemMXBean.getAvailableProcessors();//cpu核心数
		
		Level level;
		StringBuilder sb = new StringBuilder(24);
		if (load < 0) {
			level = Status.Level.UNKNOWN;
		} else {
			sb.append("load:").append(load);
			level = (load < cpu) ? Level.OK : Level.WARN;
		}
		
		return new Status(level, sb.toString());
	}
}
