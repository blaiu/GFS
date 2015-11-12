package com.gome.cloud.img.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gome.cloud.img.common.CommonConstants;
import com.gome.cloud.img.dao.FileMapDAO;


@Service
public class FileMapService {
	
	@Autowired 
	FileMapDAO fileMapDAO;
	
	  public int addFileNameMap(String courseFileKey,String businessName ,String gomefsKey){
		  int returnValue ;
		  if(businessName != null ){
			  returnValue =  fileMapDAO.addFileNameMap(courseFileKey, businessName, gomefsKey);
		  }else{
			  returnValue =  fileMapDAO.addFileNameMap(courseFileKey, CommonConstants.ORIGINAL_BUSINESS, gomefsKey);
		  }
		  return returnValue;
	  }
  
	  public boolean checkFileAndBusiNameExist(String customerName,String businessName){
		  int returnValue =  fileMapDAO.checkFileAndBusiNameExist(customerName, businessName);
		  return returnValue>0;
	  }
	  
}
