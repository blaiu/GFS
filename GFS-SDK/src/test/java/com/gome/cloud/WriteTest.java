package com.gome.cloud;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

import com.gome.cloud.core.GFSFactory;
import com.google.common.io.ByteStreams;

public class WriteTest extends TestCase {

	public void testWrite() {
		try {
			FileInputStream f = new FileInputStream("D:\\Tulips.jpg");
			String key = GFSFactory.getGFS().writeBytes(ByteStreams.toByteArray(f));
			System.out.println(key);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
