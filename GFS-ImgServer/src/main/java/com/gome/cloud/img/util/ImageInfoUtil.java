package com.gome.cloud.img.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gome.cloud.img.exception.ErrorEnum;
import com.gome.cloud.img.exception.ImageServerException;



public class ImageInfoUtil {
	//上传图片图片信息正则： 图片crc校验-图片大小.后缀名
    private final static Pattern PATTERN = Pattern.compile("^([a-zA-Z0-9]{0,10})-([\\d]{1,8}).([a-zA-Z]{2,4})$");
    
    /**
     * @param key
     * @return CRC
     * */
    public static Long getFileCRC(String key) {
        Matcher matcher = PATTERN.matcher(key);
        if (matcher.find()) {
            return Long.valueOf(matcher.group(1));
        }
        throw new ImageServerException(ErrorEnum.BadUpload,"Bad FileInfo , not find crc  : " + key);
    }
    /**
     * @param key
     * @return fileSize
     * */
    public static Long getFileSize(String key) {
        Matcher matcher = PATTERN.matcher(key);
        if (matcher.find()) {
            return Long.valueOf(matcher.group(2));
        }
        throw new ImageServerException(ErrorEnum.BadUpload,"Bad FileInfo , not file size :" + key);
    }
    /**
     * @param key
     * @return fileExtension
     * */
    
    public static String getFileExtension(String key) {
        Matcher matcher = PATTERN.matcher(key);
        if (matcher.find()) {
            return matcher.group(3);
        }
        throw new ImageServerException(ErrorEnum.BadUpload,"Bad FileInfo , not extension :" + key);
    }
}
