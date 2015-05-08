package org.itas.xcnet.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扩展点实现的信息。
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Extension 
{
	/**
     * 扩展点名称。<br>
     * 
     * 如果注解在扩展的接口上，则缺省的扩展点。<p>
     */
	String value() default "";
}
