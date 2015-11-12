package com.gome.cloud.img.ds;


public class TableUtil {
	
	public final static String TABLE_NAME_PREFIX  = "file_name_map_"; 	//表前缀
	
	public static String getTableName(String key){
		String tableName = "";
		if(key == null) return null;
		int hashCode = key.hashCode();
		if(hashCode>=0){
			tableName = TableUtil.TABLE_NAME_PREFIX + String.format("%02d", hashCode%100);
		}else{
			tableName = TableUtil.TABLE_NAME_PREFIX + String.format("%02d", hashCode%100*(-1));
		}
		return tableName;
	}
	
	public static void main(String[] args){
		System.out.println(getTableName("20150618首焦1修改修改.jpg"));
	}
}
