/*
 * 
 */
package org.itas.xcnet.common.serialize;


/**
 * Data input.<p>
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日下午5:02:03
 */
public interface DataInput {
	
	/**
	 * read boolean<p>
	 * 
	 * @return boolean value
	 * @throws IOException
	 */
	boolean readBool() throws java.io.IOException;
	
	/**
	 * read byte<p>
	 * 
	 * @return byte value
	 * @throws IOException
	 */
	byte readByte() throws java.io.IOException;
	
	/**
	 * read short<p>
	 * 
	 * @return short
	 * @throws IOException
	 */
	short readShort() throws java.io.IOException;
	
	/**
	 * read integer number<p>
	 * 
	 * @return integer
	 * @throws IOException
	 */
	int readInt() throws java.io.IOException;
	
	/**
	 * read long<p>
	 * 
	 * @return long
	 * @throws IOException
	 */
	long readLong() throws java.io.IOException;
	
	/**
	 * read float<p>
	 * 
	 * @return float
	 * @throws IOException
	 */
	float readFloat() throws java.io.IOException;
	
	/**
	 * read double<p>
	 * 
	 * @return double
	 * @throws IOException
	 */
	double readDouble() throws java.io.IOException;
	
	/**
	 * read String<p>
	 * 
	 * @return string chaEncoding UTF-8
	 * @throws IOException
	 */
	String readUTF8() throws java.io.IOException;
	
	/**
	 * read byte array<p>
	 * 
	 * @return byte array
	 * @throws IOException
	 */
	byte[] readBytes() throws java.io.IOException;
}
