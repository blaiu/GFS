package com.gome.cloud.img.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gome.cloud.img.bean.Authorization;
import com.gome.cloud.img.dao.AuthDAO;
import com.gome.cloud.img.dao.CacheDAO;
import com.gome.cloud.img.log.RunLog;


@Service
public class AuthService {

	@Autowired 	
	AuthDAO authdao;
	
	@Autowired 	
	CacheDAO cachedao;
	
	/**
	 * 查看授权，授权:true;未授权:false
	 * @param authCode
	 * @return boolean
	 * */
	public boolean authorization(String authCode){
		if (authdao.authorization(authCode.toString())) {
			RunLog.LOG.info(" authCode pass :" + authCode.toString());
			return true;
		} else {
			RunLog.LOG.warn(" authCode is not authorize :"
					+ authCode.toString());
			return false;
		}
	}
	/**
	 * 新增授权信息
	 * */
	public int addAuth(Authorization auth){
		return authdao.addAuthorization(auth);
	}
}
