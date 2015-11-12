package com.gome.cloud.img.ds;

/**
 * 动态数据源key,支持多数据源操作，可以自行配置，做读写分离
 * */
public class DataSourceContextHolder {
	public static final String  masterDs = "masterDataSource";
	public static final String slaveDs = "slaveDataSource";
	public static final ThreadLocal<String>  dsConfig = new ThreadLocal<String>();
	
	//默认ds1
	public static String getDsConfig(){
		String ds =  dsConfig.get();
		if(ds == null || ds.equals("")) 
			ds = masterDs;
		return ds;
	}
	
	public static void setDsConfig(String ds){
		dsConfig.set(ds);
	}
	public static  void removeConfig(){
	   dsConfig.remove();
	} 
}
