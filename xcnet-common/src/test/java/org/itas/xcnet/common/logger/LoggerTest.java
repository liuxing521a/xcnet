/**
 * 
 */
package org.itas.xcnet.common.logger;

import org.itas.xcnet.common.logger.support.JdkLoggerFactory;
import org.itas.xcnet.common.logger.support.Logger4jFactory;
import org.itas.xcnet.common.logger.support.Sf4jLoggerFactory;
import org.junit.Test;

/**
 * sf4j日志测试
 * @author liuzhen<liuxing521a@163.com>
 * @createTime 2015年5月8日上午10:25:23
 */
public class LoggerTest 
{
	
	@Test
	public void sf4jTest()
	{
		LoggerFactory.setLoggerFactory(new Sf4jLoggerFactory());
		Logger logger = LoggerFactory.getLogger(LoggerTest.class);
		LoggerFactory.setLevel(Level.ERROR);
		
		System.out.println("======================sf4j============================");
		logger.trace("====sf4j:{}====", "trace");
		logger.trace("====sf4j:{},{}====", "trace", "李文华");
		logger.trace("====sf4j:{},{},{}====", "trace", "李文航", "刘禹锡");
		
		logger.debug("====sf4j:{}====", "debug");
		logger.debug("====sf4j:{},{}====", "debug", "李文华");
		logger.debug("====sf4j:{},{},{}====", "debug", "李文航", "刘禹锡");
		
		logger.info("====sf4j:{}====", "info");
		logger.info("====sf4j:{},{}====", "info", "李文华");
		logger.info("====sf4j:{},{},{}====", "info", "李文航", "刘禹锡");
		
		logger.warn("====sf4j:{}====", "warn");
		logger.warn("====sf4j:{},{}====", "warn", "李文华");
		logger.warn("====sf4j:{},{},{}====", "warn", "李文航", "刘禹锡");
		
		logger.error("====sf4j:{}====", "error");
		logger.error("====sf4j:{},{}====", "error", "李文华");
		logger.error("====sf4j:{},{},{}====", "error", "李文航", "刘禹锡");
	}
	
	@Test
	public void log4jTest()
	{
		LoggerFactory.setLoggerFactory(new Logger4jFactory());
		Logger logger = LoggerFactory.getLogger(LoggerTest.class);
		LoggerFactory.setLevel(Level.ERROR);
		
		System.out.println("======================log4j============================");
		logger.trace("====log4j:{}====", "trace");
		logger.trace("====log4j:{},{}====", "trace", "李文华");
		logger.trace("====log4j:{},{},{}====", "trace", "李文航", "刘禹锡");
		
		logger.debug("====log4j:{}====", "debug");
		logger.debug("====log4j:{},{}====", "debug", "李文华");
		logger.debug("====log4j:{},{},{}====", "debug", "李文航", "刘禹锡");
		
		logger.info("====log4j:{}====", "info");
		logger.info("====log4j:{},{}====", "info", "李文华");
		logger.info("====log4j:{},{},{}====", "info", "李文航", "刘禹锡");
		
		logger.warn("====log4j:{}====", "warn");
		logger.warn("====log4j:{},{}====", "warn", "李文华");
		logger.warn("====log4j:{},{},{}====", "warn", "李文航", "刘禹锡");
		
		logger.error("====log4j:{}====", "error");
		logger.error("====log4j:{},{}====", "error", "李文华");
		logger.error("====log4j:{},{},{}====", "error", "李文航", "刘禹锡");
	}
	
	@Test
	public void jdkLogTest()
	{
		LoggerFactory.setLoggerFactory(new JdkLoggerFactory());
		Logger logger = LoggerFactory.getLogger(LoggerTest.class);
		//LoggerFactory.setLevel(Level.INFO);
		
		System.out.println("======================jdkLog============================");
		logger.trace("====JdkLog:{}====", "trace");
		logger.trace("====JdkLog:{},{}====", "trace", "李文华");
		logger.trace("====JdkLog:{},{},{}====", "trace", "李文航", "刘禹锡");
		
		logger.debug("====JdkLog:{}====", "debug");
		logger.debug("====JdkLog:{},{}====", "debug", "李文华");
		logger.debug("====JdkLog:{},{},{}====", "debug", "李文航", "刘禹锡");
		
		logger.info("====JdkLog:{}====", "info");
		logger.info("====JdkLog:{},{}====", "info", "李文华");
		logger.info("====JdkLog:{},{},{}====", "info", "李文航", "刘禹锡");
		
		logger.warn("====JdkLog:{}====", "warn");
		logger.warn("====JdkLog:{},{}====", "warn", "李文华");
		logger.warn("====JdkLog:{},{},{}====", "warn", "李文航", "刘禹锡");
		
		logger.error("====JdkLog:{}====", "error");
		logger.error("====JdkLog:{},{}====", "error", "李文华");
		logger.error("====JdkLog:{},{},{}====", "error", "李文航", "刘禹锡");
	}
}
