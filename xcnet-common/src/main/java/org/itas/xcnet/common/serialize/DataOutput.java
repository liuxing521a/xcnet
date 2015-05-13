/**
 * 
 */
package org.itas.xcnet.common.serialize;


/**
 * data output
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日下午5:09:14
 */
public interface DataOutput {

	/**
	 * write boolean<p>
	 * 
	 * @param v value
	 * @throws java.io.IOException
	 */
	void writeBool(boolean v) throws java.io.IOException;
	
	/**
	 * write byte<p>
	 * 
	 * @param v value
	 * @throws java.io.IOException
	 */
	void writeByte(byte v) throws java.io.IOException;
	
	/**
	 * write short
	 * 
	 * @param v value
	 * @throws java.io.IOException
	 */
	void writeShort(short v) throws java.io.IOException;
	
	/**
	 * write int<p>
	 * 
	 * @param v value
	 * @throws java.io.IOException
	 */
	void writeInt(int v) throws java.io.IOException;
	
	/**
	 * write long<p>
	 * 
	 * @param v value
	 * @throws java.io.IOException
	 */
	void writeLong(long v) throws java.io.IOException;
	
	/**
	 * write float<p>
	 * 
	 * @param v value
	 * @throws java.io.IOException
	 */
	void writeFloat(float v) throws java.io.IOException;
	
	/**
	 * write double<p>
	 * 
	 * @param v value
	 * @throws java.io.IOException
	 */
	void writeDouble(double v) throws java.io.IOException;
	
	/**
	 * write string<p>
	 * write String as charencoding UTF8
	 * 
	 * @param v value
	 * @throws java.io.IOException
	 */
	void writeUTF8(String v) throws java.io.IOException;
	
	/**
	 * write byte array<p>
	 * 
	 * @param v value
	 * @throws java.io.IOException
	 */
	void writeBytes(byte[] v) throws java.io.IOException;
	
	/**
	 * write byte array<p>
	 * 
	 * @param v value
	 * @param off offset
	 * @param len length
	 * @throws java.io.IOException
	 */
	void writeBytes(byte[] v, int off, int len) throws java.io.IOException;
	
	/**
	 * flush buffer<p>
	 * 
	 * @throws java.io.IOException
	 */
	void flushBuffer() throws java.io.IOException;
	
}
