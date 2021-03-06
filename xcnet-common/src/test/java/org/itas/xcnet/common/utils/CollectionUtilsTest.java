/**
 * 
 */
package org.itas.xcnet.common.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月12日上午11:23:52
 */
public class CollectionUtilsTest 
{

	@Test
	public void sortTest()
	{
        List<Integer> list = new ArrayList<Integer>();
        list.add(100);
        list.add(10);
        list.add(20);

        assertEquals(Arrays.asList(10, 20, 100), CollectionUtils.sort(list));
	}
	
	@Test
    public void test_sort_null() throws Exception 
	{
		assertNull(CollectionUtils.sort(null));

        assertTrue(CollectionUtils.sort(new ArrayList<Integer>()).isEmpty());
    }
	
    @Test
    public void test_sortSimpleName() throws Exception 
    {
        List<String> list = new ArrayList<String>();
        list.add("aaa.z");
        list.add("b");
        list.add(null);
        list.add("zzz.a");
        list.add("c");
        list.add(null);

        List<String> sorted = CollectionUtils.sortSimpleName(list);
        assertNull(sorted.get(0));
        assertNull(sorted.get(1));
        assertEquals("zzz.a", list.get(2));
        assertEquals("aaa.z", list.get(3));
    }
    
    @Test
    public void test_sortSimpleName_null() throws Exception 
    {
        assertNull(CollectionUtils.sortSimpleName(null));

        assertTrue(CollectionUtils.sortSimpleName(new ArrayList<String>()).isEmpty());
    }
    
    @Test
    public void test_splitAll() throws Exception 
    {
        assertNull(CollectionUtils.splitAll(null, null));
        assertNull(CollectionUtils.splitAll(null, "-"));

        assertTrue(CollectionUtils.splitAll(new HashMap<String, List<String>>(), "-").isEmpty());

        Map<String, List<String>> input = new HashMap<String, List<String>>();
        input.put("key1", Arrays.asList("1:a", "2:b", "3:c"));
        input.put("key2", Arrays.asList("1:a", "2:b"));
        input.put("key3", null);
        input.put("key4", new ArrayList<String>());

        Map<String, Map<String, String>> expected = new HashMap<String, Map<String, String>>();
        expected.put("key1", CollectionUtils.toStringMap("1", "a", "2", "b", "3", "c"));
        expected.put("key2", CollectionUtils.toStringMap("1", "a", "2", "b"));
        expected.put("key3", null);
        expected.put("key4", new HashMap<String, String>());

        assertEquals(expected, CollectionUtils.splitAll(input, ":"));
    }
    
    @Test
    public void test_joinAll() throws Exception 
    {
        assertNull(CollectionUtils.joinAll(null, null));
        assertNull(CollectionUtils.joinAll(null, "-"));

        Map<String, List<String>> expected = new HashMap<String, List<String>>();
        expected.put("key1", Arrays.asList("1:a", "2:b", "3:c"));
        expected.put("key2", Arrays.asList("1:a", "2:b"));
        expected.put("key3", null);
        expected.put("key4", new ArrayList<String>());

        Map<String, Map<String, String>> input = new HashMap<String, Map<String, String>>();
        input.put("key1", CollectionUtils.toStringMap("1", "a", "2", "b", "3", "c"));
        input.put("key2", CollectionUtils.toStringMap("1", "a", "2", "b"));
        input.put("key3", null);
        input.put("key4", new HashMap<String, String>());

        Map<String, List<String>> output = CollectionUtils.joinAll(input, ":");

        assertEquals(expected, output);
    }
    
    @Test
    public void test_mapEquals() throws Exception 
    {
        assertTrue(CollectionUtils.mapEquals(null, null));
        assertFalse(CollectionUtils.mapEquals(null, new HashMap<String, String>()));
        assertFalse(CollectionUtils.mapEquals(new HashMap<String, String>(), null));
        
        assertTrue(CollectionUtils.mapEquals(CollectionUtils.toStringMap("1", "a", "2", "b"), CollectionUtils.toStringMap("1", "a", "2", "b")));
        assertFalse(CollectionUtils.mapEquals(CollectionUtils.toStringMap("1", "a"), CollectionUtils.toStringMap("1", "a", "2", "b")));
    }
    
    @Test
    public void test_toMap() throws Exception 
    {
        assertTrue(CollectionUtils.toMap().isEmpty());
        
        Map<String, Integer> expected = new HashMap<String, Integer>();
        expected.put("a", 1);
        expected.put("b", 2);
        expected.put("c", 3);
        
        assertEquals(expected, CollectionUtils.toMap("a", 1, "b", 2, "c", 3));
    }
    
    public static void main(String[] args) {
        
	}
}