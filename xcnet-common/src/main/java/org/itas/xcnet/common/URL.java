/**
 * 
 */
package org.itas.xcnet.common;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.itas.xcnet.common.utils.CollectionUtils;
import org.itas.xcnet.common.utils.Objects;


/**
 * URL - Uniform Resource Locator (Immutable, ThreadSafe)
 * 
 * url example:
 * <ul>
 * <li>http://www.facebook.com/friends?param=value1
 * <li>http://username:password@10.20.130.230:8080/list?version=1.0.0
 * <li>registry://192.168.1.7:9090/com.alibaba.service1?param1=value1&param2=value2
 * <li>ftp://username:password@192.168.1.7:21/1/read.txt
 * <li>file:///home/user1/router.js?type=script<br>
 * for this case, url protocol = null, url host = null, url path = /home/user1/router.js
 * <li>file:///D:/1/router.js?type=script<br>
 * for this case, url protocol = null, url host = null, url path = /D:/1/router.js
 * </ul>
 * 
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月12日上午11:40:49
 */
public class URL implements Serializable
{
	private static final long serialVersionUID = -3779081100494675190L;

	private final String protocol;
	
	private final String username;
	
	private final String password;
	
	private final String host;
	
	private final int port;
	
	private final String path;
	
	private final Map<String, String> parameters;
	
	protected URL() 
	{
        this.protocol = null;
        this.username = null;
        this.password = null;
        this.host = null;
        this.port = 0;
        this.path = null;
        this.parameters = null;
    }
    
	public URL(String protocol, String host, int port) 
	{
	    this(protocol, null, null, host, port, null, (Map<String, String>) null);
	}
	
	public URL(String protocol, String host, int port, String... pairs) 
	{
        this(protocol, null, null, host, port, null, CollectionUtils.toStringMap(pairs));
    }
	
	public URL(String protocol, String host, int port, Map<String, String> parameters) {
        this(protocol, null, null, host, port, null, parameters);
    }
	
	public URL(String protocol, String host, int port, String path) 
	{
	    this(protocol, null, null, host, port, path, (Map<String, String>) null);
	}

	public URL(String protocol, String host, int port, String path, String... pairs) 
	{
        this(protocol, null, null, host, port, path, CollectionUtils.toStringMap(pairs));
    }
	
	public URL(String protocol, String host, int port, String path, Map<String, String> parameters) 
	{
		this(protocol, null, null, host, port, path, parameters);
	}
	
	public URL(String protocol, String username, String password, String host, int port, String path) 
	{
        this(protocol, username, password, host, port, path, (Map<String, String>) null);
    }
	
	public URL(String protocol, String username, String password, String host, int port, String path, String... pairs) 
	{
	    this(protocol, username, password, host, port, path, CollectionUtils.toStringMap(pairs));
	}
	
	public URL(String protocol, String username, String password, String host, int port, String path, Map<String, String> parameters)
	{
		if (Objects.isEmpty(username) && Objects.nonEmpty(password))
		{
			throw new IllegalArgumentException("Invalid url, password without username!");
		}
		
		this.protocol = protocol;
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.path = path;
		if (parameters == null)
		{
			this.parameters = Collections.emptyMap();
		}
		else
		{
			this.parameters = Collections.unmodifiableMap(parameters);
		}
	}

	public String getProtocol() 
	{
		return protocol;
	}

	public String getUsername() {
		
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public String getHost() 
	{
		return host;
	}

	public int getPort() 
	{
		return port;
	}
	
	public String getAddress()
	{
		return (port > 0) ? String.format("%s:%d", host, port) : host;
	}
	
	public InetSocketAddress getInetSocketAddress()
	{
		return new InetSocketAddress(host, port);
	}
	
	public String getPath()
	{
		return path;
	}
	
	public String getAbsolutePath()
	{
		if (Objects.nonNull(path) && !path.startsWith("/"))
		{
			return "/" + path;
		}
		else
		{
			return path;
		}
	}
	
   public Map<String, String> getParameters() 
   {
        return parameters;
   }
	
	public URL setProtocol(String protocol)
	{
	    return new URL(protocol, username, password, host, port, path, getParameters());
	}

    public URL setUsername(String username) 
    {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public URL setPassword(String password) 
    {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }
    
    public URL setAddress(String address) 
    {
        int i = address.lastIndexOf(':');
        String host;
        int port = this.port;
        if (i >= 0) 
        {
            host = address.substring(0, i);
            port = Integer.parseInt(address.substring(i + 1));
        } 
        else 
        {
            host = address;
        }
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public URL setHost(String host) 
    {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public URL setPort(int port) 
    {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public URL setPath(String path) 
    {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }
    
    public URL addParameterAndEncoded(String key, String value)
    {
    	try {
			value = URLEncoder.encode(value, "UTF-8");
		} 
    	catch (UnsupportedEncodingException e) 
    	{
    		throw new RuntimeException(e.getMessage(), e);
    	}
    	
    	return addParamter(key, value);
    }
    
    public URL addParameter(String key, boolean value) 
    {
        return addParameter(key, String.valueOf(value));
    }

    public URL addParameter(String key, char value) 
    {
        return addParameter(key, String.valueOf(value));
    }

    public URL addParameter(String key, byte value) 
    {
        return addParameter(key, String.valueOf(value));
    }
    
    public URL addParameter(String key, short value) 
    {
        return addParameter(key, String.valueOf(value));
    }
    
    public URL addParameter(String key, int value) 
    {
        return addParameter(key, String.valueOf(value));
    }
    
    public URL addParameter(String key, long value) 
    {
        return addParameter(key, String.valueOf(value));
    }

    public URL addParameter(String key, float value) 
    {
        return addParameter(key, String.valueOf(value));
    }
    
    public URL addParameter(String key, double value) 
    {
        return addParameter(key, String.valueOf(value));
    }
    
    public URL addParameter(String key, Enum<?> value) 
    {
        return addParameter(key, String.valueOf(value));
    }
    
    public URL addParameter(String key, Number value) 
    {
        return addParameter(key, String.valueOf(value));
    }

    public URL addParameter(String key, CharSequence value) 
    {
        return addParameter(key, String.valueOf(value));
    }
    
    public URL addParamter(String key, String value)
    {
    	if (Objects.isEmpty(key) || Objects.isEmpty(value))
    	{
    		return this;
    	}
    	
    	 Map<String, String> map = new HashMap<String, String>(getParameters());
         map.put(key, value);
         
         return new URL(protocol, username, password, host, port, path, map);
    }
}
