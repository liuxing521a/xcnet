/**
 * 
 */
package org.itas.xcnet.common;

import java.util.List;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日下午4:14:05
 */
@Extension("impl2")
public class Ext6Impl2 implements Ext6 
{

    List<String> list;

    public List<String> getList() 
    {
        return list;
    }

    public void dsetList(List<String> list) 
    {
        this.list = list;
    }

    @Adaptive
    public String echo(URL url, String s) 
    {
        throw new UnsupportedOperationException();
    }

}
