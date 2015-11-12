package com.gome.cloud.img.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gome.cloud.img.bean.ImgObject;
import com.gome.cloud.img.common.CommonConstants;
import com.gome.cloud.img.dao.GomefsDAO;
import com.gome.cloud.img.exception.ErrorEnum;
import com.gome.cloud.img.exception.ImageServerException;

@Service
public class ResolveECGomeKeyUrl extends UrlResolveAbstract {
	
	private Pattern PATTERN_SIZE = Pattern.compile(CommonConstants.IMG_SIZE);
	
	@Autowired private GomefsDAO gomefsDao;
	@Autowired private CacheService cacheService;
	@Override
	public void resolveLabelSize(ImgObject object) {
		int dotsite = object.getNewUrl().lastIndexOf(".");
		String  extension = "" ;
		if(dotsite>0){
			extension = object.getNewUrl().substring(dotsite);
		}
		Matcher sourceKeyMatcher = PATTERN_SIZE.matcher(object.getNewUrl());
		if(sourceKeyMatcher.find()){
//			if(sourceKeyMatcher.group(3) != null){
//				object.setLabel_30(Integer.valueOf(sourceKeyMatcher.group(3).substring(1)));  
//			}
//			if(sourceKeyMatcher.group(4) != null){
//				object.setLabel_180(Integer.valueOf(sourceKeyMatcher.group(4).substring(1)));  
//			}
			if(sourceKeyMatcher.group(6) != null){
				object.setHopeWidth(Integer.valueOf(sourceKeyMatcher.group(6))); 
				if(sourceKeyMatcher.group(8) != null){
					object.setHopeHeight(Integer.valueOf(sourceKeyMatcher.group(8)));
				}else{
					object.setHopeHeight(Integer.valueOf(sourceKeyMatcher.group(6)));
				}
			}
			
			object.setGomefsKey("gomefs"+sourceKeyMatcher.group(2)+"N"+extension);
//			object.gomefsKey = object.gomefsKey+extension;
		}
		
	}

	@Override
	public void resolveQuality(ImgObject object) {
		/**
		 * 修改循环获取标签参数，支持降质，圆形图片 共同使用
		 * */
		for(int qualityPos = object.getNewUrl().lastIndexOf("!");qualityPos != -1;){
			String qualityStart = object.getNewUrl().substring(qualityPos);
			int qualityEnd = qualityStart.indexOf(".");
			if (qualityEnd == -1) {
				 throw new ImageServerException(ErrorEnum.BadRequest, 	"resolveQulity cannot find quality");
			}
			if (qualityStart.charAt(1) == 'q') {
				object.setQuality(Integer.valueOf(qualityStart.substring(2,	qualityEnd)));  
				if (object.getQuality() < 0 || object.getQuality() > 100) {
					 throw new ImageServerException(ErrorEnum.BadRequest, "quality must >0 and <100");
			} else if (qualityStart.charAt(1) == 'c') {
			}
				object.setRadius(Integer.valueOf(qualityStart.substring(2,qualityEnd))); 
				if (object.getRadius() <= 0 ) {
					 throw new ImageServerException(ErrorEnum.BadRequest, "radius must >0 ");
				}

			}
			object.setNewUrl(object.getNewUrl().substring(0, qualityPos)); 
			qualityPos = object.getNewUrl().lastIndexOf("!");
		}
//		int qualityPos = url.lastIndexOf("!");
//		if (qualityPos != -1) {
//			String qualityStart = url.substring(qualityPos);
//			int qualityEnd = qualityStart.indexOf(".");
//			if (qualityEnd == -1) {
//				 throw new ImageServerException(ErrorEnum.BadRequest, 	"resolveQulity cannot find quality");
//			}
//			if (qualityStart.charAt(1) == 'q') {
//				object.quality = Integer.valueOf(qualityStart.substring(2,
//						qualityEnd));
//				if (object.quality < 0 || object.quality > 100) {
//					 throw new ImageServerException(ErrorEnum.BadRequest, "quality must >0 and <100");
//				}
//			} else if (qualityStart.charAt(1) == 'c') {
//				object.radius = Integer.valueOf(qualityStart.substring(2,
//						qualityEnd));
//			}
//			object.newUrl = url.substring(0, qualityPos);
//		}
//		
	}
	@Override
	public void doTransferAndSetKey(ImgObject object){
		
		byte[] data =gomefsDao.read(object.getGomefsKey());
		if(data == null ){
			 throw new ImageServerException(ErrorEnum.InternalError, " GomeFS read error ,EC data is null ");
		}
		object.setZoomData(data); 
		
	}
	

}
