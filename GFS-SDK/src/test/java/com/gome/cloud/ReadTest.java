package com.gome.cloud;

import junit.framework.TestCase;

import com.gome.cloud.core.GFSFactory;

public class ReadTest extends TestCase {

	public void testRead() {
		GFSFactory.getGFS().readBytes("gomefs/t25/1/620888/620888/595e4c28c140a6d8N");
	}
	
}
