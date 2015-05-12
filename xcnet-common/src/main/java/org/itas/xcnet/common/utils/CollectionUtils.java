/**
 * 
 */
package org.itas.xcnet.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月12日上午10:31:58
 */
public final class CollectionUtils 
{
	private static final Comparator<String> SIMPLE_NAME_COMPARTOR = new Comparator<String>()
	{
		@Override
		public int compare(String s1, String s2) 
		{
			if (s1 == null && s2 == null)
			{
				return 0;
			}
			
			if (s1 == null)
			{
				return -1;
			}
			
			if (s2 == null)
			{
				return 1;
			}
			
			int i1 = s1.lastIndexOf('.');
			if (i1 >= 0)
			{
				s1 = s1.substring(i1 + 1);
			}
			
			int i2 = s1.lastIndexOf('.');
			if (i1 >= 0)
			{
				s2 = s2.substring(i2 + 1);
			}
			
			return s1.compareToIgnoreCase(s2);
		}
	};
	
	public static <T extends Comparable<? super T>> List<T> sort(List<T> list)
	{
		if (list != null && list.size() > 0)
		{
			Collections.sort(list);
		}
	
		return list;
	}
	
	public static List<String> sortSimpleName(List<String> list)
	{
		if (list != null && list.size() > 0)
		{
			Collections.sort(list, SIMPLE_NAME_COMPARTOR);
		}
	
		return list;
	}
	
	public static Map<String, Map<String, String>> splitAll(Map<String, List<String>> list, String separator)
	{
		if (list == null)
		{
			return null;
		}
		
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		for (Map.Entry<String, List<String>> entry : list.entrySet())
		{
			result.put(entry.getKey(), split(entry.getValue(), separator));
		}
		return result;
	}
	
	public static  Map<String, List<String>> joinAll(Map<String, Map<String, String>> map, String separator)
	{
		if (map == null)
		{
			return null;
		}
		
		Map<String, List<String>> result = new HashMap<>(map.size());
		for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) 
		{
			result.put(entry.getKey(), join(entry.getValue(), separator));
		}
		return result;
	}
	
	public static Map<String, String> split(List<String> list, String separator)
	{
		if (list == null)
		{
			return null;
		}

		Map<String, String> map = new HashMap<String, String>(list.size());
		for (String item : list) 
		{
			int index = item.indexOf(separator);
			if (index == -1)
			{
				map.put(item, "");
			}
			else
			{
				map.put(item.substring(0, index), item.substring(index + 1));
			}
		}
		
		return map;
	}

	public static List<String> join(Map<String, String> map, String separator)
	{
		if (map == null)
		{
			return null;
		}
		
		List<String> list = new ArrayList<>(map.size());
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			StringBuilder sb = new StringBuilder(entry.getKey());
			
			sb.append(separator);
			if (entry.getValue() != null)
			{
				sb.append(entry.getValue());
			}
			list.add(sb.toString());
		}
		
		return list;
	}
	
	public static boolean mapEquals(Map<?, ?> map1, Map<?, ?> map2)
	{
		if (map1 == map2)
		{
			return true;
		}
		
		if (map1 == null || map2 == null)
		{
			return false;
		}
		
		if (map1.size() != map2.size())
		{
			return false;
		}
		
		for (Map.Entry<?, ?> entry : map1.entrySet()) 
		{
			Object value1 = entry.getValue();
			Object value2 = map2.get(entry.getKey());
			if (!objectEquals(value1, value2))
			{
				return false;
			}
		}
	
		return true;
	}
	
	public static Map<String, String> toStringMap(String...pairs)
	{
		int len = pairs.length;
		if (len == 0)
		{
			return Collections.emptyMap();
		}
		else if (len % 2 != 0)
		{
			throw new IllegalArgumentException("pairs must be even.");
		}
		else
		{
			Map<String, String> map = new HashMap<>(len >> 1);
			for (int i = 0; i < len; i += 2) 
			{
				map.put(pairs[i], pairs[i + 1]);
			}
			
			return map;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> toMap(Object...pairs)
	{
		int len = pairs.length;
		if (len == 0)
		{
			return Collections.emptyMap();
		}
		else if (len % 2 != 0)
		{
			throw new IllegalArgumentException("pairs must be even.");
		}
		else
		{
			Map<K, V> map = new HashMap<>(len >> 1);
			for (int i = 0; i < len; i += 2) 
			{
				map.put((K)pairs[i], (V)pairs[i + 1]);
			}
			
			return map;
		}
	}
	
	private static boolean objectEquals(Object o1, Object o2)
	{
		if (o1 == o2)
		{
			return true;
		}
		
		if (o1 == null || o2 == null)
		{
			return false;
		}
		
		return o1.equals(o2);
	}
	
	private CollectionUtils()
	{
	}
}
