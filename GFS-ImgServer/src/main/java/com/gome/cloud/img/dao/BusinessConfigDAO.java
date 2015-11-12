package com.gome.cloud.img.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Repository;

import com.gome.cloud.img.bean.BusinessConfig;
import com.gome.cloud.img.common.CommonConstants;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Repository
public class BusinessConfigDAO extends BaseDAO{
	
	//加载业务配置，并缓存
	private LoadingCache<String, Map<String, BusinessConfig>> businessConfigCache = CacheBuilder.newBuilder()
	            .expireAfterWrite(20, TimeUnit.MINUTES)
	            .build(new CacheLoader<String, Map<String, BusinessConfig>>() {
	                @Override
	                public Map<String, BusinessConfig> load(String key) throws Exception {
	                    return loadConfig();
	                }
	            });
	 
	public Map<String, BusinessConfig> loadConfig() {
		 Map<String, BusinessConfig> imgConfigMap = new HashMap<String, BusinessConfig>();
	     List<BusinessConfig> configList = sqlSessionTemplate.selectList("BusinessConfig.getBusinessConfig");
	     for (BusinessConfig imgConfig : configList) {
	    	 imgConfigMap.put(imgConfig.getBusinessName(), imgConfig);
	     }
	     return imgConfigMap;
	}

	/**
	 * 查看业务配置
	 * */
	public BusinessConfig getBusinessConfig(String businessName) {
		Map<String, BusinessConfig> map;
        try {
            map = businessConfigCache.get(CommonConstants.BUSINESS_CONFIG);
        } catch (ExecutionException e) {
            map = loadConfig();
        }
        return map.get(businessName);
	}
}
