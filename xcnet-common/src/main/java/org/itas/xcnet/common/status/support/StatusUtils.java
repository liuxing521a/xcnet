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

import java.util.Map;

import org.itas.xcnet.common.status.Status;
import org.itas.xcnet.common.status.Status.Level;
import org.itas.xcnet.common.utils.Reference;


/**
 * Status Manager
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日下午2:11:09
 */
public class StatusUtils {

	public static Status getSummaryStatus(Map<String, Status> statuses) {
		Reference<Level> ref = new Reference<Level>(Level.OK);
		
		StringBuilder msg = new StringBuilder();
		statuses.forEach((key, statu)->{
			if (statu.getLevel() == Level.ERROR) {
				ref.set(Level.ERROR);
				if (msg.length() > 0) 
                    msg.append(',');

				 msg.append(key);
			} else if (statu.getLevel() == Level.WARN) {
				if (ref.get() != Level.ERROR)
					ref.set(Level.WARN);
				
				if (msg.length() > 0) 
                    msg.append(',');
				
				 msg.append(key);
			}
		});
		
		return new Status(ref.get(), msg.toString());
	}
	
}
