package com.gome.cloud.img.dao;

import org.springframework.stereotype.Repository;

import com.gome.cloud.img.bean.Authorization;

@Repository
public class AuthDAO extends BaseDAO {
	
	/**
	 * 根据授权码查询用户是否经过授权
	 * */
	public boolean authorization(String authCode) {
		Authorization auth = (Authorization)sqlSessionTemplate.selectOne("Authorization.selAuth",authCode);
		if(auth == null) return false ;
		return true;
	}
	
	/**
	 * 新增授权
	 * */
	public int addAuthorization(Authorization auth) {
		return sqlSessionTemplate.insert("Authorization.addAuth", auth);
	}
	
}
