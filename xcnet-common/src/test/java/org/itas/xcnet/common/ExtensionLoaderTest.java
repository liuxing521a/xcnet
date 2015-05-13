/**
 * 
 */
package org.itas.xcnet.common;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日上午10:19:24
 */
public class ExtensionLoaderTest 
{
	@Test
	public void test_getExtension()
	{
		ExtensionLoader<Ext1> extLoader = ExtensionLoader.getExtensionLoader(Ext1.class);
		assertTrue(extLoader.getExtension("impl1") instanceof Ext1Impl1);
		assertTrue(extLoader.getExtension("impl2") instanceof Ext1Impl2);
		assertTrue(extLoader.getExtension("impl3") instanceof Ext1Impl3);
	}
	
	@Test
	public void test_getExtension_WithAutoProxy()
	{
		ExtensionLoader<Ext5> extLoader = ExtensionLoader.getExtensionLoader(Ext5.class);
		Ext5 impl1 = extLoader.getExtension("impl1");
		assertThat(impl1, anyOf(instanceOf(Ext5AutoProxy1.class), instanceOf(Ext5AutoProxy2.class)));
		
		Ext5 impl2 = extLoader.getExtension("impl2");
		assertThat(impl2, anyOf(instanceOf(Ext5AutoProxy1.class), instanceOf(Ext5AutoProxy2.class)));
		
		URL url  = new URL("p1", "1.2.3.4", 1001, "path1");
		int echoCount1 = Ext5AutoProxy1.echoCount.get();
        int echoCount2 = Ext5AutoProxy2.echoCount.get();
        int yellCount1 = Ext5AutoProxy1.yellCount.get();
        int yellCount2 = Ext5AutoProxy2.yellCount.get();
        
        assertEquals("Ext5.Impl1-echo", impl1.echo(url, "ha"));
        assertEquals(echoCount1 + 1, Ext5AutoProxy1.echoCount.get());
        assertEquals(echoCount2 + 1, Ext5AutoProxy2.echoCount.get());
        assertEquals(yellCount1, Ext5AutoProxy1.yellCount.get());
        assertEquals(yellCount2, Ext5AutoProxy2.yellCount.get());
        
        assertEquals("Ext5.Impl2-yell", impl2.yell(url, "ha"));
        assertEquals(echoCount1 + 1, Ext5AutoProxy1.echoCount.get());
        assertEquals(echoCount2 + 1, Ext5AutoProxy2.echoCount.get());
        assertEquals(yellCount1 + 1, Ext5AutoProxy1.yellCount.get());
        assertEquals(yellCount2 + 1, Ext5AutoProxy2.yellCount.get());
	}
	
	@Test
	public void test_getExtension_ExceptionNoExtension()
	{
		try 
		{
			ExtensionLoader.getExtensionLoader(Ext1.class).getExtension("XXX");
		} 
		catch (Exception e) 
		{
			  assertThat(e.getMessage(), containsString("No such extension org.itas.xcnet.common.Ext1 by name XXX"));
		}
	}
	
    @Test
    public void test_getExtension_ExceptionNoExtension_NameOnAutoProxyNoAffact() throws Exception 
    {
        try 
        {
            ExtensionLoader.getExtensionLoader(Ext5.class).getExtension("XXX");
        } 
        catch (IllegalStateException expected) 
        {
            assertThat(expected.getMessage(), containsString("No such extension org.itas.xcnet.common.Ext5 by name XXX"));
        }
    }
    
    @Test
    public void test_getExtension_ExceptionNullArg() throws Exception 
    {
        try 
        {
            ExtensionLoader.getExtensionLoader(Ext1.class).getExtension(null);
        } 
        catch (IllegalArgumentException expected) 
        {
            assertThat(expected.getMessage(), containsString("Extension name == null"));
        }
    }
    
    @Test
    public void test_hasExtension() throws Exception 
    {
        assertTrue(ExtensionLoader.getExtensionLoader(Ext1.class).hasExtension("impl1"));
        assertFalse(ExtensionLoader.getExtensionLoader(Ext1.class).hasExtension("impl1,impl2"));
        assertFalse(ExtensionLoader.getExtensionLoader(Ext1.class).hasExtension("xxx"));
        
        try 
        {
            ExtensionLoader.getExtensionLoader(Ext1.class).hasExtension(null);
            fail();
        } 
        catch (IllegalArgumentException expected) 
        {
            assertThat(expected.getMessage(), containsString("Extension name == null"));
        }
    }
    
    @Test
    public void test_getSupportedExtensions() throws Exception 
    {
        Set<String> exts = ExtensionLoader.getExtensionLoader(Ext1.class).getSupportedExtensions();
        
        Set<String> expected = new HashSet<String>();
        expected.add("impl1");
        expected.add("impl2");
        expected.add("impl3");
        
        assertEquals(expected, exts);
    }
    
    @Test
    public void test_getSupportedExtensions_NoExtension() throws Exception 
    {
        Set<String> exts = ExtensionLoader.getExtensionLoader(ExtensionLoaderTest.class).getSupportedExtensions();
        assertEquals(0, exts.size());
    }
    
    @Test
    public void test_getAdaptiveExtension_defaultExtension() throws Exception 
    {
    	ExtensionLoader<Ext1> loader = ExtensionLoader.getExtensionLoader(Ext1.class);
        Ext1 ext = loader.getAdaptiveExtension();

        Map<String, String> map = new HashMap<String, String>();
        URL url = new URL("p1", "1.2.3.4", 1010, "path1", map);

        String echo = ext.echo(url, "haha");
        assertEquals("Ext1.Impl1-echo", echo);

        echo = ext.yell(url, "haha");
        assertEquals("Ext1.Impl1-yell", echo);
    }
    
    @Test
    public void test_getAdaptiveExtension() throws Exception 
    {
        Ext1 ext = ExtensionLoader.getExtensionLoader(Ext1.class).getAdaptiveExtension();

        Map<String, String> map = new HashMap<String, String>();
        map.put("ext1", "impl2");
        URL url = new URL("p1", "1.2.3.4", 1010, "path1", map);

        String echo = ext.echo(url, "haha");
        assertEquals("Ext1.Impl2-echo", echo);

        url = url.addParameter("key1", "impl2");
        echo = ext.yell(url, "haha");
        assertEquals("Ext1.Impl2-yell", echo);
    }
    
    @Test
    public void test_getAdaptiveExtension_customizeKey() throws Exception 
    {
        Ext1 ext = ExtensionLoader.getExtensionLoader(Ext1.class).getAdaptiveExtension();

        Map<String, String> map = new HashMap<String, String>();
        map.put("key2", "impl2");
        URL url = new URL("p1", "1.2.3.4", 1010, "path1", map);

        String echo = ext.yell(url, "haha");
        assertEquals("Ext1.Impl2-yell", echo);

        url = url.addParameter("key1", "impl3"); // 注意： URL是值类型
        echo = ext.yell(url, "haha");
        assertEquals("Ext1.Impl3-yell", echo);
    }
    
    @Test
    public void test_getAdaptiveExtension_UrlNpe() throws Exception 
    {
        Ext1 ext = ExtensionLoader.getExtensionLoader(Ext1.class).getAdaptiveExtension();

        try 
        {
            ext.echo(null, "haha");
        } 
        catch (IllegalArgumentException e)
        {
            assertEquals("url == null", e.getMessage());
        }
    }
    
    @Test
    public void test_getAdaptiveExtension_ExceptionWhenNotAdativeMethod() throws Exception 
    {
        Ext1 ext = ExtensionLoader.getExtensionLoader(Ext1.class).getAdaptiveExtension();

        Map<String, String> map = new HashMap<String, String>();
        URL url = new URL("p1", "1.2.3.4", 1010, "path1", map);

        try 
        {
            ext.bang(url, 33);
            fail();
        } 
        catch (UnsupportedOperationException expected) 
        {
            assertThat(expected.getMessage(), containsString("method "));
            assertThat(
                    expected.getMessage(),
                    containsString("of interface org.itas.xcnet.common.Ext1 is not adaptive method!"));
        }
    }
    
    @Test
    public void test_getAdaptiveExtension_ExceptionWhenNoUrlAttrib() throws Exception 
    {
        try 
        {
            ExtensionLoader.getExtensionLoader(Ext4.class).getAdaptiveExtension();
            fail();
        } 
        catch (Exception expected) 
        {
            assertThat(expected.getMessage(), containsString("fail to create adative class for interface "));
            assertThat(expected.getMessage(), containsString(": not found url parameter or url attribute in parameters of method "));
        }
    }
    
    @Test
    public void test_getAdaptiveExtension_protocolKey() throws Exception 
    {
        Ext3 ext = ExtensionLoader.getExtensionLoader(Ext3.class).getAdaptiveExtension();
    
        Map<String, String> map = new HashMap<String, String>();
        URL url = new URL("impl3", "1.2.3.4", 1010, "path1", map);
        
        String echo = ext.echo(url, "s");
        assertEquals("Ext3.Impl3-echo", echo);
    
        url = url.addParameter("key1", "impl2");
        echo = ext.echo(url, "s");
        assertEquals("Ext3.Impl2-echo", echo);
        
        url = url.addParameter("protocol", "impl3");
        echo = ext.yell(url, "s");
        assertEquals("Ext3.Impl3-yell", echo);
        
        String yell = ext.yell(url, "d");
        assertEquals("Ext3.Impl3-yell", yell);
    }
    

    @Test
    public void test_getAdaptiveExtension_lastProtocolKey() throws Exception 
    {
        Ext2 ext = ExtensionLoader.getExtensionLoader(Ext2.class).getAdaptiveExtension();
        
        Map<String, String> map = new HashMap<String, String>();
        URL url = new URL("impl1", "1.2.3.4", 1010, "path1", map);
        String yell = ext.yell(url, "s");
        assertEquals("Ext2.Impl1-yell", yell);
        
        url = url.addParameter("key1", "impl2");
        yell = ext.yell(url, "s");
        assertEquals("Ext2.Impl2-yell", yell);
        
        URLHolder holder = new URLHolder();
        url = new URL("impl1", "1.2.3.4", 1010, "path1", map);
        url = url.addParameter("ext2", "impl1");
        holder.setUrl(url);
        yell = ext.echo(holder, "s");
        assertEquals("Ext2.Impl1-echo", yell);
    }
    
    @Test
    public void test_urlHolder_getAdaptiveExtension() throws Exception 
    {
        Ext2 ext = ExtensionLoader.getExtensionLoader(Ext2.class).getAdaptiveExtension();
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("ext2", "impl3");
        URL url = new URL("p1", "1.2.3.4", 1010, "path1", map);
        
        URLHolder holder = new URLHolder();
        holder.setUrl(url);
    
        String echo = ext.echo(holder, "haha");
        assertEquals("Ext2.Impl3-echo", echo);
    }
    
    @Test
    public void test_urlHolder_getAdaptiveExtension_noExtension() throws Exception 
    {
        Ext2 ext = ExtensionLoader.getExtensionLoader(Ext2.class).getAdaptiveExtension();

        URL url = new URL("p1", "1.2.3.4", 1010, "path1");
        URLHolder holder = new URLHolder();
        holder.setUrl(url);
        
        try 
        {
            ext.echo(holder, "haha");
            fail();
        } 
        catch (IllegalStateException expected) 
        {
            assertThat(expected.getMessage(), containsString("Fail to get extension("));
        }
        
        url = url.addParameter("ext2", "XXX");
        holder.setUrl(url);
        try 
        {
            ext.echo(holder, "haha");
            fail();
        } 
        catch (IllegalStateException expected) 
        {
            assertThat(expected.getMessage(), containsString("No such extension"));
        }
    }
    
    @Test
    public void test_urlHolder_getAdaptiveExtension_UrlNpe() throws Exception 
    {
        Ext2 ext = ExtensionLoader.getExtensionLoader(Ext2.class).getAdaptiveExtension();

        try 
        {
            ext.echo(null, "haha");
            fail();
        } 
        catch (IllegalArgumentException e) 
        {
            assertEquals("org.itas.xcnet.common.URLHolder argument == null", e.getMessage());
        }
        
        try 
        {
            ext.echo(new URLHolder(), "haha");
            fail();
        } 
        catch (IllegalArgumentException e) 
        {
            assertEquals("org.itas.xcnet.common.URLHolder argument getUrl() == null", e.getMessage());
        }
    }
    
    @Test
    public void test_urlHolder_getAdaptiveExtension_ExceptionWhenNotAdativeMethod() throws Exception 
    {
        Ext2 ext = ExtensionLoader.getExtensionLoader(Ext2.class).getAdaptiveExtension();

        Map<String, String> map = new HashMap<String, String>();
        URL url = new URL("p1", "1.2.3.4", 1010, "path1", map);

        Ext2 ext2 = ExtensionLoader.getExtensionLoader(Ext2.class).getExtension("impl2");
        assertEquals("Ext2.Impl2-bang", ext2.bang(null, 0));
        try 
        {
            ext.bang(url, 33);
            fail();
        } 
        catch (UnsupportedOperationException expected) 
        {
            assertThat(expected.getMessage(), containsString("method "));
            assertThat(
                    expected.getMessage(),
                    containsString("of interface org.itas.xcnet.common.Ext2 is not adaptive method!"));
        }
    }
    
    @Test
    public void test_urlHolder_getAdaptiveExtension_ExceptionWhenNameNotProvided() throws Exception 
    {
        Ext2 ext = ExtensionLoader.getExtensionLoader(Ext2.class).getAdaptiveExtension();

        URL url = new URL("p1", "1.2.3.4", 1010, "path1");
        //url = url.addParameter("ext2", "impl1");
        URLHolder holder = new URLHolder();
        holder.setUrl(url);
        
        try 
        {
            ext.echo(holder, "impl1");
            fail();
        } 
        catch (IllegalStateException expected) 
        {
            assertThat(expected.getMessage(), containsString("Fail to get extension("));
        }
        
        url = url.addParameter("key1", "impl1");
        holder.setUrl(url);
        try 
        {
            ext.echo(holder, "haha");
            fail();
        } 
        catch (IllegalStateException expected) 
        {
            assertThat(expected.getMessage(), containsString("Fail to get extension(org.itas.xcnet.common.Ext2) name from url"));
        }
    }
    
    @Test
    public void test_getAdaptiveExtension_inject() throws Exception 
    {
        Ext6 ext = ExtensionLoader.getExtensionLoader(Ext6.class).getAdaptiveExtension();

        URL url = new URL("p1", "1.2.3.4", 1010, "path1");
        url = url.addParameters("ext6", "impl1");
        
        assertEquals("Ext6.Impl1-echo-Ext1.Impl1-echo", ext.echo(url, "ha"));
        
        url = url.addParameters("ext1", "impl3");
        assertEquals("Ext6.Impl1-echo-Ext1.Impl3-echo", ext.echo(url, "ha"));
    }
    
    @Test
    public void test_getAdaptiveExtension_InjectNotExtFail() throws Exception 
    {
        Ext6 ext = ExtensionLoader.getExtensionLoader(Ext6.class).getExtension("impl2");
        
        Ext6Impl2 impl = (Ext6Impl2) ext;
        assertNull(impl.getList());
    }

}
