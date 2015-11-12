package com.gome.cloud.img.common;


/**
 * 常量参数
 * */

public class CommonConstants {
	public final static String DEFAULT_ENCODING 		= "UTF-8";
	public final static String IMG_CONTEXT 				= "Img-Context";												 // image对象存储名称
	public final static String GOMEFS_REGULAR 			= "^(.*)gomefs(-(\\w+))?/t";
	public final static String IMG_SIZE 				= "^(.+)/gomefs(.+)N(-\\d{1,4})?(-\\d{1,4})?(_(\\d{1,4})?(x(\\d{1,4}))?)?\\.(.+)$";	 //标签和尺寸
	public final static String BUSINESS_CONFIG 			= "Bussess-Config";   											 // 业务配置 的  缓存名称

	public final static String ORIGINAL_BUSINESS		="n1";										 					 //原图业务
	public final static String THUMBNAIL_BUSINESS		="n2";															 //缩略图，默认标签位置8
//	public final static String THUMBNAIL_WATER_BUSINESS	="n3";															 //缩略图大水印 默认标签位置8
	public final static String HTML_BUSINESS			="html";														 //html业务

	public final static String SOURCE_IMG 				="^/?images?/(.+)$";	 										 // 原系统 image images 
	public final static String SOURCE_HTML				="^/?html/(.+)$";												 //原系统 html文件		
	
	
	public final static String PRO_DESC 				= "^(.+)/prodimg/productDesc/descImg/(.+)$";					 // 自营商品描述，不需要解析
	public final static String PRO_DESC_MERCHANT 		= "^(.+)/bbcimg/productDesc/descImg/(.+)$";  					 // 商家商品描述，不需要解析
	public final static String PROMOTION 				= "^(.+)/prodimg/promimg/(.+)$"; 								 // 促销商品 ，不需要解析
	public final static String PROMOTIONIMG 			= "^(.+)/prodimg/promotion_image/(.+)$"; 						 // 不需要解析

	public final static String PRO_IMG 					= "^(.+)/prodimg/production_image/(.+)$"; 						 // 自营商品图片，需要解析
	public final static String IMG 					    = "^(.+)/img/(.+)$"; 	 										 // 商品图片，需要解析
	public final static String THIRD_MERCHANT 			= "^(.+)/bbcimg/production_image/(.+)$"; 						 // 第三方商家商品图片
	public final static String SOURCE_KEY 				= "^([0-9a-zA-Z]*)(-30)?(-180)?(-bq)?(_\\d{0,3})?.[a-zA-Z]+$";
	public final static String IMG_CUT_RATE 			= "0.78@";
	public final static String WEBP_SUFFIX 				=".webp";
	public final static String WEBP_REGULAR 			="^(.+).webp";
}