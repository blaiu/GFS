package com.gome.cloud.img.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gome.cloud.img.bean.FileDelRecord;
import com.gome.cloud.img.bean.FileNameMap;
import com.gome.cloud.img.ds.TableUtil;


@Repository
public class FileMapDAO extends BaseDAO {
	/**
	 * 根据用户文件查询删除记录 limit 1
	 * */
	public FileDelRecord getDelRecord(String sourceUri) {
		return sqlSessionTemplate.selectOne("FileDelRecord.getDelRecord",sourceUri);
	}

	/**
	 * 根据源文件查询文件系统key
	 */
	public String getGomefsKey(String courseFileKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableName", TableUtil.getTableName(courseFileKey));
		map.put("courseFileKey", courseFileKey);
		return sqlSessionTemplate.selectOne("FileNameMap.getGomefsKey", map);
	}
 
	/**
	 * 根据源文件查询文件系统映射
	 */
	public FileNameMap getGomeFileMap(String courseFileKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableName", TableUtil.getTableName(courseFileKey));
		map.put("courseFileKey", courseFileKey);
		List<FileNameMap> list = sqlSessionTemplate.selectList("FileNameMap.getGomefsFileMap", map);
		return list.size()>0?list.get(0):null;
	}	
	
	/**
	 * 新增文件映射信息
	 * */
	public int addFileNameMap(String courseFileKey,String businessName ,String gomefsKey){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("courseFileKey", courseFileKey);
		map.put("businessName", businessName);
		map.put("gomefsKey", gomefsKey);
		map.put("tableName", TableUtil.getTableName(courseFileKey));
		return sqlSessionTemplate.insert("FileNameMap.addFileNameMap", map);
	}
	
	/**
	 * 验证该业务下图片名称是否存在
	 * */
	public int checkFileAndBusiNameExist(String customerName,String businessName){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("courseFileKey", customerName);
		map.put("businessName", businessName);
		map.put("tableName", TableUtil.getTableName(customerName));
		return (Integer)sqlSessionTemplate.selectOne("FileNameMap.checkFileNameExist", map) ;
	}
}
