package com.gome.cloud.img.dao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.gome.cloud.img.log.RunLog;


@Repository
@SuppressWarnings({"unchecked", "rawtypes"})
public class CacheDAO {
	@Autowired
	private RedisTemplate redisTemplate;
	
	
	public void putCache(String key ,String value){
		try{
			redisTemplate.opsForValue().set(key, value);
		}catch(Exception e){
			com.gome.cloud.img.log.RunLog.LOG.error(" Redis put cache  [{}-{}] error ",key,value,e);
		}
	}
	 
	public String getCache(String key){
		try{
			return (String)redisTemplate.opsForValue().get(key);
		}catch(Exception e){
			RunLog.LOG.error(" Redis Get cache Error key : {}",key,e);
			return null;
		}
	}
	
	public void putCacheMap(String key,Map map){
		try{
			BoundHashOperations hashBound = redisTemplate.boundHashOps(key);
			hashBound.putAll(map);
		}catch(Exception e){
			RunLog.LOG.error(" Redis put cache  [{}-{}] error ",key,map,e);
		}
	}
	
	public Map getCacheMap(String key){
		try{
			BoundHashOperations hashBound = redisTemplate.boundHashOps(key);
			Map map =  hashBound.entries();
			if(map.isEmpty()){
				return null;
			}
			return map;
		}catch(Exception e){
			RunLog.LOG.error(" Redis Get cache Error key : {}",key,e);
			return null;
		}
	}

}
