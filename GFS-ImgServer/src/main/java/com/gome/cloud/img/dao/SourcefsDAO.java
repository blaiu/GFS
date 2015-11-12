package com.gome.cloud.img.dao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.gome.cloud.img.bean.ImgObject;
import com.gome.cloud.img.common.CommonConstants;
import com.gome.cloud.img.exception.ErrorEnum;
import com.gome.cloud.img.exception.ImageServerException;
import com.gome.cloud.img.log.RunLog;
import com.google.common.collect.Lists;

/**
 * 源文件系统访问，读写
 * */
@Repository
public class SourcefsDAO {
	@Value("${sourceUrl}") 
	private   String sourceUrl ;
	public List<String> sourceDomain  =Lists.newArrayList();
	

	public void setSourceDomain(String domains) {
		this.sourceDomain = Lists.newArrayList(domains);
	}

	public byte[] read(ImgObject object) {
		//文件名冲编码，防止中文
		String newUrl;
		byte[] data = null;
		try {
			newUrl = object.getReqUrl().substring(0,object.getReqUrl().lastIndexOf("/")+1)+ URLEncoder.encode(object.getSourceKey(), CommonConstants.DEFAULT_ENCODING);
			data = getImg(newUrl);
			if(data.length <= 0){
				data = getImg(object.getReqUrl());
			}
			
		} catch (UnsupportedEncodingException e) {
			RunLog.LOG.info(e.getMessage());
			throw new ImageServerException(ErrorEnum.BadRequest," request file contant unrecognized char : UnsupportedEncodingException" );
		} 
		return data;
	}
	
	private byte[] getImg(String imgUrl){
		ByteArrayOutputStream sourceStream = new ByteArrayOutputStream();
		for (String domain : sourceDomain) {
			try {
				String url = domain+ imgUrl ;  
//				代理，测试用
//				Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1", 8888));  
//				HttpURLConnection uc = (HttpURLConnection) serverUrl.openConnection(proxy);
				URL serverUrl = new URL(url);  
				HttpURLConnection    uc = (HttpURLConnection)serverUrl.openConnection();
//				uc.setFixedLengthStreamingMode(1024); 流式输出，占用内存固定，也可以用块输出 
				uc.setConnectTimeout(1*1000);
				uc.setReadTimeout(5*1000);
				if (uc.getResponseCode() == 200) {
					byte[] buff = new byte[1024];
					int rc = 0;
					InputStream is= uc.getInputStream();
					while ((rc = is.read(buff, 0, 1024)) > 0) {
						sourceStream.write(buff, 0, rc);  
					}
					is.close();
					break;	
					
					
				}else{
					 RunLog.LOG.error(" transfer file error ");
					 throw new ImageServerException(ErrorEnum.InternalError,"download file error:"+imgUrl);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return sourceStream.toByteArray();
	}
	// 初始化文件系统
	@PostConstruct
	private void initSourcefs() {
		setSourceDomain(sourceUrl);
	}
}
