package com.gome.cloud.img.dao;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.gome.cloud.core.GFS;
import com.gome.cloud.core.GFSFactory;


/**
 * gomefs数据读写
 * */
@Repository
public class GomefsDAO {
	
	GFS gomefs;
	
	@Value("${zk.addr}") 
	private String zkAddr;
	
	public String write(byte[] data){
		return gomefs.writeBytes(data);
	}
	
	public byte[] read(String key){
		return gomefs.readBytes(key);
	}
	
	//初始化文件系统
	 @PostConstruct
	 private void initGomefs(){
		gomefs = GFSFactory.getGFS();
	 }
}
