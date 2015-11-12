package com.gome.cloud.img.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;



public class BaseDAO {
	
	@Autowired
	public SqlSessionTemplate sqlSessionTemplate;

}
