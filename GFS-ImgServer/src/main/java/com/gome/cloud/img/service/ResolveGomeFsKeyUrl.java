package com.gome.cloud.img.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gome.cloud.img.bean.ImgObject;
import com.gome.cloud.img.dao.GomefsDAO;
import com.gome.cloud.img.exception.ErrorEnum;
import com.gome.cloud.img.exception.ImageServerException;
import com.gome.cloud.img.util.ParamRegular;

@Service
public class ResolveGomeFsKeyUrl extends UrlResolveAbstract {
	@Autowired private GomefsDAO gomefsDao;
	@Autowired private CacheService cacheService;
	public final static String GomeFs_Key 	= "^(.+)/gomefs(.+)N(.+)$";	 //标签和尺寸
	private Pattern GOMEFS_KEY_PATTREN = Pattern.compile(GomeFs_Key);
	/**
	 * /域名/业务/gome_key.jpg!s120x30!q12!r20!w12!p5!b1!cffffff!l30-180!d5-8!@0.17
	 * s：尺寸 q：降质 r：圆形图片直径 w：水印图片名称 p：水印位置 b：边框 c：边框颜色 l：标签 d：标签位置
	 * 
	 * @：裁切率
	 * */
	@Override
	public void resolveLabelSize(ImgObject object) {
		//参数处理都在resolveQuality处理
		//之获取文件名称
		
		Matcher sourceKeyMatcher = GOMEFS_KEY_PATTREN.matcher(object.getNewUrl());
		int dotsite = object.getNewUrl().lastIndexOf(".");
		String  extension = "" ;
		if(dotsite>0){
			extension = object.getNewUrl().substring(dotsite);
		}
		if((sourceKeyMatcher.find())){
			object.setGomefsKey("gomefs"+sourceKeyMatcher.group(2)+"N"+extension);
		}
	}

	@Override
	public void resolveQuality(ImgObject object) {
		int beginIndex = 0;
		String[]  paramArray = object.getNewUrl().split("!");
		if(paramArray.length > 1){
			//去掉后缀名
			String lastStr = paramArray[paramArray.length-1];
			if(lastStr.lastIndexOf(".") > 0){
				paramArray[paramArray.length-1] = lastStr.substring(0, lastStr.lastIndexOf("."));
			}else{
				 throw new ImageServerException(ErrorEnum.BadRequest, " no extension ,Non recognition format ");
			}
		}
		object.setSourceKey(paramArray[0]);
		for(String urlParam : paramArray){
			if(beginIndex == 0 || urlParam.length() < 2){
				beginIndex++;
				continue;
			}
			
			String paramKey = urlParam.substring(0,1);
			String paramValue = urlParam.substring(1);
			if(paramKey.equals("@")){
				//裁切率
				try{
					if(ParamRegular.isFloatPointNumber(paramValue) && Float.parseFloat(paramValue)>0 && Float.parseFloat(paramValue) < 1){
						  object.setIsCut(1);	
						  object.setCutRate(Float.parseFloat(paramValue));
							continue;
					}else{
						 throw new ImageServerException(ErrorEnum.BadRequest, "cutRate must >0 and <1");
					}
				}catch(NumberFormatException e){
					 throw new ImageServerException(ErrorEnum.BadRequest, "cutRate must >0 and <1");
				}
			}
			if(paramKey.equals("q")){
				//降质
				try{
					if(ParamRegular.isIntegerNumber(paramValue) && Integer.parseInt(paramValue) > 0 && Integer.parseInt(paramValue) < 100){
							object.setQuality(Integer.parseInt(paramValue));
							continue;
					}else{
						 throw new ImageServerException(ErrorEnum.BadRequest, "quality  must >0 and <100");
					}
				}catch(NumberFormatException e){
					 throw new ImageServerException(ErrorEnum.BadRequest, "quality must >0 and <100");
				}
			}
			if(paramKey.equals("r")){
				//圆形图片
				try{
					if(ParamRegular.isIntegerNumber(paramValue) && Integer.parseInt(paramValue) > 0 ){
							object.setRadius(Integer.parseInt(paramValue));
							continue;
					}else{
						 throw new ImageServerException(ErrorEnum.BadRequest, " radius must >0 ");
					}
				}catch(NumberFormatException e){
					 throw new ImageServerException(ErrorEnum.BadRequest, " radius must >0 ");
				}
			}
			if(paramKey.equals("w")){
				//水印名称
				 if(ParamRegular.isStrOrNumber(paramValue)){
					 object.setWaterMark("1");
					 object.setWaterMarkPic(paramValue);
					 continue;
				 }else{
					 throw new ImageServerException(ErrorEnum.BadRequest, " waterMark only string+number");
				 }
			}
			if(paramKey.equals("p")){
				//水印位置
				try{
					if(ParamRegular.isIntegerNumber(paramValue) && Integer.parseInt(paramValue) > 0  && Integer.parseInt(paramValue) < 10 ){
							object.setWaterMarkPos(paramValue);
							continue;
					}else{
						 throw new ImageServerException(ErrorEnum.BadRequest, " waterMark position must  between 1-9  ");
					}
				}catch(NumberFormatException e){
					 throw new ImageServerException(ErrorEnum.BadRequest, " waterMark position must  between 1-9 ");
				}
			}
			if(paramKey.equals("b")){
				//边框及宽度
				try{
					if(ParamRegular.isIntegerNumber(paramValue) && Integer.parseInt(paramValue) > 0  ){
							object.setBorderSize(Integer.parseInt(paramValue));
							object.setIsBorder(1);
							continue;
					}else{
						 throw new ImageServerException(ErrorEnum.BadRequest, " border width must > 0  ");
					}
				}catch(NumberFormatException e){
					 throw new ImageServerException(ErrorEnum.BadRequest, " border width must > 0 ");
				}
			}
			if(paramKey.equals("c")){
				//边框颜色
					if(ParamRegular.isString(paramValue) ){
							object.setBorderColor(paramValue);
							continue;
					}else{
						 throw new ImageServerException(ErrorEnum.BadRequest, " border color format is not correct   ");
					}
			}
			if(paramKey.equals("l")){
				//标签
					if(ParamRegular.isStrOrNumAndline(paramValue) ){
							object.setLabelStr(paramValue);
							continue;
					}else{
						 throw new ImageServerException(ErrorEnum.BadRequest, " label format is not correct   ");
					}
			}
			if(paramKey.equals("d")){
				//标签位置
					if(ParamRegular.isNumAndline(paramValue) ){
							object.setLabelPosStr(paramValue);;
							continue;
					}else{
						 throw new ImageServerException(ErrorEnum.BadRequest, " label position  format is not correct   ");
					}
			}
			if(paramKey.equals("s")){
				//尺寸
				try{
					if(ParamRegular.isIntegerMultiInteger(paramValue) ){
						String[] widthAndHeight = paramValue.split("x");
						if(widthAndHeight.length==1){
							object.setHopeWidth(Integer.parseInt(widthAndHeight[0]));
							object.setHopeHeight(Integer.parseInt(widthAndHeight[0]));
						}else{
							object.setHopeWidth(Integer.parseInt(widthAndHeight[0]));
							object.setHopeHeight(Integer.parseInt(widthAndHeight[1]));
						}
							continue;
					}else{
						 throw new ImageServerException(ErrorEnum.BadRequest, " width and height format is not correct   ");
					}
				}catch(NumberFormatException e){
					 throw new ImageServerException(ErrorEnum.BadRequest, " width and height format is not correct   ");
				}
			}
		}
		
		//校验标签名和标签位置参数是否匹配
		if(object.getLabelStr() != null && object.getLabelPosStr() != null ){
			if(object.getLabelStr().split("-").length != object.getLabelPosStr().split("-").length){
				 throw new ImageServerException(ErrorEnum.BadRequest, " label and lablePos format is not correct   ");
			}
		}else{
			//如果不是都非空，则设置为空
			object.setLabelPosStr("");
			object.setLabelStr("");
		}
			object.setNewUrl(paramArray[0]);
		
		
	}
	@Override
	public void doTransferAndSetKey(ImgObject object){
		byte[] data = gomefsDao.read(object.getGomefsKey());
		if(data == null ){
			 throw new ImageServerException(ErrorEnum.InternalError, " GomeFS read error ,data is null ");
		}
		object.setZoomData(data); 
		
	}
}
