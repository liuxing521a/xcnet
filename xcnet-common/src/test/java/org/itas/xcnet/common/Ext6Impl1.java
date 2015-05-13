/**
 * 
 */
package org.itas.xcnet.common;

/**
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月13日下午4:13:17
 */
@Extension("impl1")
public class Ext6Impl1 implements Ext6 {

	Ext1 ext1;
    
    public void setExt1(Ext1 ext1) 
    {
        this.ext1 = ext1;
    }

    public String echo(URL url, String s) 
    {
        return "Ext6.Impl1-echo-" + ext1.echo(url, s);
    }

}
