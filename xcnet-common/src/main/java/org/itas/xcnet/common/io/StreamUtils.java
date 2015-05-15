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
package org.itas.xcnet.common.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Stream utils.
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月15日上午10:10:40
 */
public class StreamUtils {
	
	private StreamUtils() {}
	
	public static InputStream limitedInputStream(InputStream is, int limit) throws IOException {
		return new InputStream() {
			
			private int iPosition, iMark, iLimit = Math.min(limit, is.available());
			
			@Override
			public int read() throws IOException {
				if (iPosition < limit) {
					iPosition ++;
					return is.read();
				}
				
				return -1;
			}
			
			@Override
			public int read(byte[] b, int off, int len) throws IOException {
				if (b == null)
					throw new NullPointerException();
				
				if (off < 0 || len < 0 || len > b.length)
					throw new IndexOutOfBoundsException();
				
				if (iPosition >= iLimit)
					return -1;
				
				len = Math.min(len, iLimit - iPosition);
				if (len < 0)
					return 0;
				
				is.read(b, off, len);
				iPosition += len;
				return len;
			}
			
			@Override
			public long skip(long n) throws IOException {
				long skip = Math.min(n, iLimit - iPosition);
				if (skip <= 0)
					return 0;
				
				is.skip(skip);
				iPosition += skip;
				return skip;
			}
			
			@Override
			public int available() throws IOException {
				return iLimit - iPosition;
			}
			
			@Override
			public boolean markSupported() {
				return is.markSupported();
			}
			
			@Override
			public synchronized void mark(int readlimit) {
				is.mark(readlimit);
				iMark = iPosition;
			}
			
			@Override
			public void reset() throws IOException {
				iPosition = iMark;
			}
			
			@Override
			public void close() throws IOException {
			}
		};
	}
	
	public static InputStream markSupportedInputStream(InputStream is, int markBufferSize) {
		if (is.markSupported())
			return is;
		
		return new InputStream() {
			
			byte[] bytes;
			boolean marked = false;
			boolean reset = false;
			boolean dry = false;
			int position = 0;
			int count = 0;
			
			@Override
			public int read() throws IOException {
				 if(!marked) {
	                    return is.read();
	                }
	                else {
	                    if(position < count) {
	                        byte b = bytes[position++];
	                        return b & 0xFF;
	                    }
	                    
	                    if(!reset) {
	                        if(dry) return -1;
	                        
	                        if(null == bytes) {
	                            bytes = new byte[markBufferSize];
	                        }
	                        if(position >= markBufferSize) {
	                            throw new IOException("Mark buffer is full!");
	                        }
	                        
	                        int read = is.read();
	                        if(-1 == read){
	                            dry = true;
	                            return -1;
	                        }
	                        
	                        bytes[position++] = (byte) read;
	                        count++;
	                        
	                        return read;
	                    }
	                    else {
	                        // mark buffer is used, exit mark status!
	                        marked = false;
	                        reset = false;
	                        position = 0;
	                        count = 0;
	                        
	                        return is.read();
	                    }
	                }
			}
			
			@Override
			public synchronized void mark(int readlimit) {
				marked = true;
				reset = false;
				
				// mark buffer is not empty
                int cut = count - position;
                if (cut > 0) {
                	 System.arraycopy(bytes, position, bytes, 0, cut);
                	 count = cut;
                     position = 0;
                }
			}
			
			@Override
			public synchronized void reset() throws IOException {
				if (!marked)
					throw new IOException("should mark befor reset!");
				
				reset = true;
				position = 0;
			}
			
			@Override
			public boolean markSupported() {
				return true;
			}
			
			@Override
			public int available() throws IOException {
				int available = is.available();
				if (marked && reset) {
					available += count - position;
				}
				
				return available;
			}
		};
	}
	
	public static InputStream markSupportedInputStream(final InputStream is) {
	    return markSupportedInputStream(is, 1024);
	}

}
