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
package org.itas.xcnet.common.status;

/**
 * Status
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午2:07:45
 */
public class Status {

	public static enum Level {
		OK, WARN, ERROR, UNKNOWN
	}
	
	private final Level level;
	
	private final String message;
	
	public Status(Level level) {
		this(level, null);
	}
	
	public Status(Level level, String message) {
		this.level = level;
		this.message = message;
	}

	public Level getLevel() {
		return level;
	}

	public String getMessage() {
		return message;
	}
	
}
