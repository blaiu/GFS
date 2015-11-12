/**
 * 
 */
package com.gome.cloud.monitor;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author blaiu
 *
 */
public class Value {
	
	/** 读文件 */
	AtomicLong fileRead = new AtomicLong();
	
	/** 读字节 */
	AtomicLong readByte = new AtomicLong();
	
	/** 读时间 */
	AtomicLong readTime = new AtomicLong();
	
	/** 读错误 */
	AtomicLong errorRead = new AtomicLong();
	
	/** 读错误 */
	AtomicLong finalReadError = new AtomicLong();
	
	/** 写文件 */
	AtomicLong fileWrite = new AtomicLong();
	
	/** 写字节 */
	AtomicLong writeByte = new AtomicLong();
	
	/** 写时间 */
	AtomicLong writeTime = new AtomicLong();
	
	/** 写错误 */
	AtomicLong errorWrite = new AtomicLong();
	
	/** 写错误 */
	AtomicLong finalWriteError = new AtomicLong();
	
	/** 连接数 */
	AtomicLong totalConnnections= new AtomicLong();
	
}
