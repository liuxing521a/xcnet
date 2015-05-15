/**
 * 
 */
package org.itas.xcnet.common.serialize;


/**
 * object input
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日下午5:15:29
 */
public interface ObjectInput extends DataInput {

	/**
	 * read object
	 * 
	 * @return object
	 * @throws java.io.IOException
	 */
	Object readObject() throws java.io.IOException, ClassNotFoundException;

	/**
	 * read object
	 * 
	 * @param cls object class type
	 * @return object designated type
	 * @throws java.io.IOException
	 */
	<T> T readObject(Class<T> cls) throws java.io.IOException, ClassNotFoundException;
}
