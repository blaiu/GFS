package com.gome.cloud.img.service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gome.cloud.img.bean.ImgObject;
import com.gome.cloud.img.common.CommonConstants;
import com.gome.cloud.img.dao.BusinessConfigDAO;
import com.gome.cloud.img.exception.ErrorEnum;
import com.gome.cloud.img.exception.ImageServerException;
import com.gome.cloud.img.log.RunLog;


@Service
public  class ResolveHolder {
    private Pattern patternSource			= Pattern.compile(CommonConstants.SOURCE_IMG);
    private Pattern patternHtml 			= Pattern.compile(CommonConstants.SOURCE_HTML);
    private Pattern patternGomefs 			= Pattern.compile(CommonConstants.GOMEFS_REGULAR);
	private Pattern patternProDesc 			= Pattern.compile(CommonConstants.PRO_DESC);
	private Pattern patternProDescMer		= Pattern.compile(CommonConstants.PRO_DESC_MERCHANT);
	private Pattern patternPromotion		= Pattern.compile(CommonConstants.PROMOTION);
	private Pattern patternPromotionImg		= Pattern.compile(CommonConstants.PROMOTIONIMG);
	private Pattern patternProImg 			= Pattern.compile(CommonConstants.PRO_IMG);
	private Pattern patternProImgThirdMer	= Pattern.compile(CommonConstants.THIRD_MERCHANT);
//	private Pattern patternSourceKey		= Pattern.compile(CommonConstants.SOURCE_KEY);
	private Pattern patternImg 			    = Pattern.compile(CommonConstants.IMG);
	
	@Autowired
	private BusinessConfigDAO businessConfigDAO;
	@Autowired
	private  Map<String,UrlResolveAbstract> resolveHolder  ;

	/**
	 * 解析业务
	 * */
	public void resolve(ImgObject object){
		
		/** 解析业务 */
		resolveBusiness(object);
		//调用解析方法
		if(object.getIsGomefsKey()){
			if(object.getBussinessName().equals(CommonConstants.ORIGINAL_BUSINESS) || object.getBussinessName().equals(CommonConstants.THUMBNAIL_BUSINESS)  || object.getBussinessName().equals(CommonConstants.HTML_BUSINESS)  ){
				if(object.getBussinessName().equals(CommonConstants.HTML_BUSINESS) ){
					object.setHtml(true);
				}
				resolveHolder.get("resolveECGomeKeyUrl").resolve(object);
			}else{
				resolveHolder.get("resolveGomeFsKeyUrl").resolve(object);
			}
		}else if(object.getBussinessName().equals(CommonConstants.HTML_BUSINESS)){
			resolveHolder.get("resolveSourceHtmlUrl").resolve(object);
		}else if(object.getBussinessName().equals(CommonConstants.ORIGINAL_BUSINESS)){
				resolveHolder.get("resolveSourceImg").resolve(object);
		}else if(object.getBussinessName().equals(CommonConstants.THUMBNAIL_BUSINESS) ){
				resolveHolder.get("resolveSourceImgParam").resolve(object);
		}else{
			 throw new ImageServerException(ErrorEnum.BadRequest, " this bussiness name not found  "+object.getBussinessName());
		}
		
	}
	/**
	 * 解析业务并赋值
	 * */
	private void resolveBusiness(ImgObject object){
        Matcher matcherSource 				= 	patternSource.matcher(object.getReqUrl());
        Matcher matcherHtml 				= 	patternHtml.matcher(object.getReqUrl());
        Matcher matcherGomefs 				= 	patternGomefs.matcher(object.getReqUrl());
        Matcher proDescMatcher 				= 	patternProDesc.matcher(object.getReqUrl());
 		Matcher proDescMerMatcher 			= 	patternProDescMer.matcher(object.getReqUrl());
 		Matcher proProMotionMatcher 		= 	patternPromotion.matcher(object.getReqUrl());
 		Matcher proImgMatcher 				= 	patternProImg.matcher(object.getReqUrl());
 		Matcher promotionImgMatcher 		= 	patternPromotionImg.matcher(object.getReqUrl());
 		Matcher proImgThirdMerMatcher 		= 	patternProImgThirdMer.matcher(object.getReqUrl());
 		Matcher imgMatcher 		            = 	patternImg.matcher(object.getReqUrl());

		
        if (matcherSource.find(0) && (proImgMatcher.find(0) || proImgThirdMerMatcher.find(0) || imgMatcher.find(0))) {
    		//商品图，
        	object.setBussinessName(CommonConstants.THUMBNAIL_BUSINESS);  
    	}else if (matcherSource.find(0) && (proDescMatcher.find(0) || proProMotionMatcher.find(0) || proDescMerMatcher.find(0) || promotionImgMatcher.find(0))){
    		//详细图，原图业务
    		object.setBussinessName(CommonConstants.ORIGINAL_BUSINESS) ;
        } else if(matcherGomefs.find(0)){
    		int bussinessPos = object.getNewUrl().indexOf("/");
    		if (bussinessPos <= 0) {
    			 throw new ImageServerException(ErrorEnum.BadRequest, " can't find the bussiness name from url ");
    		}
    		object.setBussinessName(object.getNewUrl().substring(0, bussinessPos));
    		//设置文件系统标识
    		object.setIsGomefsKey(true);
        } else if(matcherHtml.find(0)){
        	//html 
        	object.setBussinessName(CommonConstants.HTML_BUSINESS);
        }else{
        	/**
        	 * 查找业务，如果是EC固定业务，则按照EC业务处理，
        	 * 如果不是EC业务，则按照原图处理
        	 * */
        	
     		int bussinessPos = object.getNewUrl().indexOf("/");
    		if (bussinessPos <= 0) {
        		object.setBussinessName(CommonConstants.ORIGINAL_BUSINESS);  
    		}else{
    			String ecBusiness = object.getNewUrl().substring(0, bussinessPos);
    			if(ecBusiness.equals(CommonConstants.ORIGINAL_BUSINESS) || ecBusiness.equals(CommonConstants.THUMBNAIL_BUSINESS) || ecBusiness.equals(CommonConstants.HTML_BUSINESS)){
    				object.setBussinessName(ecBusiness);
    			}else{
    				object.setBussinessName(CommonConstants.ORIGINAL_BUSINESS);  
    			}
    		}
    		RunLog.LOG.error(" Don't know the URL format, request the original path {}",object.getReqUrl());
        }
	}
	
	
}
