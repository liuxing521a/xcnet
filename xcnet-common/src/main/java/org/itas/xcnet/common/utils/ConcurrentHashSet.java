/**
 * 
 */
package org.itas.xcnet.common.utils;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月12日下午2:16:49
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E>, Serializable
{
	private static final long serialVersionUID = 150006415360035370L;

	private static final Object DATA = new Object();
	
	private final ConcurrentMap<E, Object> map;
	
	public ConcurrentHashSet()
	{
	    map = new ConcurrentHashMap<E, Object>();
	}

    public ConcurrentHashSet(int initialCapacity)
    {
        map = new ConcurrentHashMap<E, Object>(initialCapacity);
    }
	
	@Override
	public Iterator<E> iterator() 
	{
		return map.keySet().iterator();
	}

	@Override
	public int size() 
	{
		return map.size();
	}
	
	public boolean isEmpty() 
	{
		return map.isEmpty();
	}

	public boolean contains(Object o)
	{
		return map.containsKey(o);
	}

	public boolean add(E e) 
	{
		return map.put(e, DATA) == null;
	}

	public boolean remove(Object o) 
	{
		return map.remove(o) == DATA;
	}

	public void clear() 
	{
		map.clear();
	}

}
