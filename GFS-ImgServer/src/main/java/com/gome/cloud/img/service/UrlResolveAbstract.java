package com.gome.cloud.img.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gome.cloud.img.bean.BusinessConfig;
import com.gome.cloud.img.bean.FileDelRecord;
import com.gome.cloud.img.bean.FileNameMap;
import com.gome.cloud.img.bean.ImgObject;
import com.gome.cloud.img.dao.BusinessConfigDAO;
import com.gome.cloud.img.dao.FileMapDAO;
import com.gome.cloud.img.exception.ErrorEnum;
import com.gome.cloud.img.exception.ImageServerException;


@Service
public abstract class UrlResolveAbstract {
//    private Pattern patternSource			= Pattern.compile(CommonConstants.SOURCE_IMG);
//    private Pattern patternHtml 			= Pattern.compile(CommonConstants.SOURCE_HTML);
//    private Pattern patternGomefs 			= Pattern.compile(CommonConstants.GOMEFS_REGULAR);
//	private Pattern patternProDesc 			= Pattern.compile(CommonConstants.PRO_DESC);
//	private Pattern patternProDescMer		= Pattern.compile(CommonConstants.PRO_DESC_MERCHANT);
//	private Pattern patternPromotion		= Pattern.compile(CommonConstants.PROMOTION);
//	private Pattern patternPromotionImg		= Pattern.compile(CommonConstants.PROMOTIONIMG);
//	private Pattern patternProImg 			= Pattern.compile(CommonConstants.PRO_IMG);
//	private Pattern patternProImgThirdMer	= Pattern.compile(CommonConstants.THIRD_MERCHANT);
//	private Pattern patternSourceKey		= Pattern.compile(CommonConstants.SOURCE_KEY);
//	private Pattern patternImg 			    = Pattern.compile(CommonConstants.IMG);
	
	@Autowired
	private BusinessConfigDAO businessConfigDAO;
	@Autowired
	private FileMapDAO fileMapDAO;
	@Autowired
	private CacheService cacheService;
	
	/**
	 * 解析业务
	 * */
	public void resolve(ImgObject object){
//		String[]  paramArray = object.getNewUrl().split("!");
//		for(String str : paramArray){
//			System.out.println("参数"+str);
//			System.out.println(str.substring(1));
//		}
		/**
		 * 根据业务初始化参数
		 * */
		initParamByBusiness(object);
		/**
		 * 解析降质圆角等参数
		 * */
		resolveQuality(object);
		/**
		 * 解析标签大小，源文件名称
		 * */
		resolveLabelSize(object);
		/**
		 * 校验是否有新路径替代,先取消
		 * */
		resolveNewUrl(object);
		/**
		 * 转换,获取数据
		 * */
		doTransferAndSetKey( object);
		
	}


 
	/**
	 * 根据业务读取配置
	 * */
	private void initParamByBusiness(ImgObject object){
		BusinessConfig businessConfig = businessConfigDAO
				.getBusinessConfig(object.getBussinessName());

		if (businessConfig == null) {
			 throw new ImageServerException(ErrorEnum.BadRequest, "No such bussiness name : "
					+ object.getBussinessName());
		}
		object.setIsThumbNail(businessConfig.getIsThumbNail());
		object.setIsPressed(businessConfig.getCompression());
		object.setIsBorder(businessConfig.getIsBorder());
		object.setBorderColor(businessConfig.getImgBorderColor());
		object.setBorderSize(businessConfig.getImgFramSize());
//		object.setLabel_180_pos(businessConfig.getLabel_180_pos());
//		object.setLabel_30_pos(businessConfig.getLabel_30_pos());
		
		String waterMark = businessConfig.getWaterMark();
		object.setWaterMark(waterMark);
		object.setWaterMarkPic(businessConfig.getWaterMarkPic());
//		 for(WaterMarkEnum e : WaterMarkEnum.values()){
//			 if(e.code == waterMark){
//				 object.setWaterMark(e);
//			 }
//		 }
		
		int waterMarkPos = Integer.valueOf(businessConfig
				.getWaterMarkPosition());
		if (!object.getWaterMark().equals("") && waterMarkPos >= 1 && waterMarkPos <= 9) {
			object.setWaterMarkPos(businessConfig.getWaterMarkPosition());
		}
		
	}
	/**
	 * 解析缩放标签
	 * */
	public abstract void  resolveLabelSize(ImgObject object);

	/**
	 * 解析其他参数，如：降质，圆图
	 * */
	public abstract void resolveQuality(ImgObject object);
	
	/**
	 * 判断是否替换新的url, 如果非gomefskey 查看是否存在映射
	 * object 获取源名称，
	 * 
	 * */
	private  void resolveNewUrl(ImgObject object){
		String sourceKey = object.getSourceKey();
//		String sourceUri = object.getNewUrl();
//		int pos = sourceUri.lastIndexOf(".");
//		if (pos < 0)   throw new ImageServerException(ErrorEnum.BadRequest, " Key format error, no suffix- key: "+sourceUri);
//		sourceUri = sourceUri.substring(0, pos);
		FileDelRecord fileDelRecord = cacheService.getGomeFsKeyFromDelRecord(object);
		if (fileDelRecord == null){
			if(!object.getIsGomefsKey()){
				FileNameMap gomeFileMap = cacheService.getGomefsKeyFromCache(object);
				if(gomeFileMap != null ){
					object.setNewUrl( "/"+gomeFileMap.getBusinessName()+"/"+gomeFileMap.getGomefsKey()); 
					object.setGomefsKey(gomeFileMap.getGomefsKey());
					object.setIsGomefsKey(true);  //有映射关系，更改为gomefskey
				}
			} else {
				object.setGomefsKey(sourceKey);
			}
			return ;
		}
		if (fileDelRecord.getFlag().equals("1")) {
			 throw new ImageServerException(ErrorEnum.BadRequest, "This key has been deleted: "	+ object.getNewUrl());
		} else if (fileDelRecord.getFlag().equals("2")) {
			if (fileDelRecord.getGomefsURI() != null && !fileDelRecord.getGomefsURI().equals("")) {
				object.setNewUrl( object.getNewUrl().replaceAll(sourceKey,fileDelRecord.getGomefsURI())); 
			} else {
				 throw new ImageServerException(ErrorEnum.BadRequest, 	"This key has been replace,but gomefsKey is null :" + object.getNewUrl());
			}
		}
	}
	
	/**
	 * 转换数据并设置key
	 * */
	protected void doTransferAndSetKey(ImgObject object){
		
	}
	
}
