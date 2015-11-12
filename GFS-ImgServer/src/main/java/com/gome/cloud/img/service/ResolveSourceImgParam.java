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
import com.gome.cloud.img.log.RunLog;


/**
 * 原系统中需要转换的图片
 * */
@Service
public class ResolveSourceImgParam extends UrlResolveAbstract {
	@Autowired private FsTransfer fsTransfer;
	@Autowired private FileMapService fileMapService;
	@Autowired private GomefsDAO gomefsDao;
	@Autowired private CacheService cacheService;
	private Pattern PATTERN_SOURCE_KEY 			= Pattern.compile(CommonConstants.SOURCE_KEY);
	private Pattern PATTERN_PRO_IMG 			= Pattern.compile(CommonConstants.PRO_IMG);
	private Pattern PATTERN_PRO_IMG_THIRD_MER 	= Pattern.compile(CommonConstants.THIRD_MERCHANT);
	private Pattern PATTERN_IMG   			    = Pattern.compile(CommonConstants.IMG);

	
	@Override
	public void resolveLabelSize(ImgObject object) {
		object.setSourceKey(object.getReqUrl().substring(object.getReqUrl().lastIndexOf("/")+1));
		Matcher sourceKeyMatcher 		= PATTERN_SOURCE_KEY.matcher(object.getSourceKey());
		Matcher proImgMatcher 			= PATTERN_PRO_IMG.matcher(object.getReqUrl());
		Matcher proImgThirdMerMatcher 	= PATTERN_PRO_IMG_THIRD_MER.matcher(object.getReqUrl());
		Matcher imgMatcher 		        = PATTERN_IMG.matcher(object.getReqUrl());

		if(sourceKeyMatcher.find(0)){
		//获取soucekey
		if(sourceKeyMatcher.group(4) != null){
			//标签
			if(sourceKeyMatcher.group(5) != null){
				object.setSourceKey(object.getSourceKey().replace(sourceKeyMatcher.group(5), "_800"));
			}else{
				object.setSourceKey(object.getSourceKey().substring(0,object.getSourceKey().indexOf("."))+"_800"+object.getSourceKey().substring(object.getSourceKey().indexOf(".")));
			}
		}else if(proImgThirdMerMatcher.find(0)){
			//第三方
			if(sourceKeyMatcher.group(5) != null){
				object.setSourceKey(object.getSourceKey().replace(sourceKeyMatcher.group(5), "_800"));
			}else{
				object.setSourceKey(object.getSourceKey().substring(0,object.getSourceKey().indexOf("."))+"_800"+object.getSourceKey().substring(object.getSourceKey().indexOf(".")));
			}
		}else if(proImgMatcher.find(0) || imgMatcher.find(0)){
			//自营 有30 180标签的 默认取800尺寸，没有标签的 默认取原图
			if(sourceKeyMatcher.group(2) != null || sourceKeyMatcher.group(3) != null){
				object.setSourceKey(object.getSourceKey().replace(sourceKeyMatcher.group(5), "_800"));
				
			}else{
				object.setSourceKey("y"+sourceKeyMatcher.group(1)+object.getSourceKey().substring(object.getSourceKey().indexOf(".")));
				//默认800尺寸图片 添加水印
				if(sourceKeyMatcher.group(5) != null && sourceKeyMatcher.group(5).equals("_800")){
					object.setWaterMark("1");
				}
			}
		}

		//设置参数,个性化标签
//		if(sourceKeyMatcher.group(2) != null){
//			object.setLabel_30( Integer.valueOf(sourceKeyMatcher.group(2).substring(1)));
//		}
//		if(sourceKeyMatcher.group(3) != null){
//			object.setLabel_180(Integer.valueOf(sourceKeyMatcher.group(3).substring(1)));
//		}
		if(sourceKeyMatcher.group(5) != null){
			object.setIsThumbNail(1);// 缩略图
			object.setHopeHeight(Integer.valueOf(sourceKeyMatcher.group(5).substring(1)));
			object.setHopeWidth(object.getHopeHeight());
		}
		}else{
			RunLog.LOG.error("can not resolve this url {}",object.getReqUrl());
			throw new ImageServerException(ErrorEnum.BadRequest);
		}
	}

	@Override
	public void resolveQuality(ImgObject object) {
		//原系统图片没有降质等其他参数，暂无逻辑
	}
	@Override
	public void doTransferAndSetKey(ImgObject object){
		if(object.getIsGomefsKey()){
			
			byte[] data = gomefsDao.read(object.getGomefsKey());
			if(data == null ){
				 throw new ImageServerException(ErrorEnum.InternalError, " GomeFS read error ,data is null ");
			}
			object.setZoomData(data); 
			
		}else{
			fsTransfer.transferFile(object);
			fileMapService.addFileNameMap(object.getSourceKey(),object.getBussinessName(), object.getGomefsKey());
		}
	
		
	}
}
