package com.gome.cloud.img.util;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.gome.cloud.img.bean.ImgObject;
import com.gome.cloud.img.exception.ImageServerException;



public class MetadataUtils {
	private final static long YEAR_30 = 946080000000L;
    private static FileTypeMap fileTypeMap;
    static {
        Resource res = new ClassPathResource("mime.types");
        try {
            fileTypeMap = new MimetypesFileTypeMap(res.getInputStream());
        } catch (IOException e) {
             throw new ImageServerException(e);
        }
    }
    //处理参数
    public static void process(ImgObject image) {
    	
        String key = image.getGomefsKey() == null ? image.getSourceKey() : image.getGomefsKey();
//        if (!(image.getIsPressed() == 0 && image.getIsThumbNail() == 0   && image.getWaterMark() == WaterMarkEnum.WATERMARK_NONE)) {
//            String zoomPara = image.toParaString();
//            image.getHeaders().add(HttpHeaders.ETAG, zoomPara);
//        }
        if (!image.isOriginal()) {
            String zoomPara = image.toParaString();
            image.getHeaders().add(HttpHeaders.ETAG, zoomPara);
        }else{
        	image.getHeaders().add(HttpHeaders.ETAG, image.toNullString());
        }
        image.getHeaders().add(HttpHeaders.CONTENT_LENGTH, String.valueOf(image.getZoomData().length));
        image.getHeaders().add(HttpHeaders.CONTENT_TYPE, fileTypeMap.getContentType(key.toLowerCase()));
        if(image.isHtml()){
        	//html 编码
        	MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        	image.getHeaders().setContentType(mediaType);
        }
//        long lastModified = image.gomefsKey == null ? 0 : KeyUtils.getCreateTime(image.gomefsKey);
//        image.headers.setLastModified(lastModified * 1000);
        image.getHeaders().setCacheControl("max-age=315360000");  //year 10
        long time = System.currentTimeMillis() + YEAR_30;
        image.getHeaders().setExpires(time);
    }
    
}
