package com.gome.cloud.img.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gome.cloud.img.bean.ImgObject;
import com.gome.cloud.img.common.CommonConstants;
import com.gome.cloud.img.exception.ErrorEnum;
import com.gome.cloud.img.exception.ImageServerException;
import com.gome.cloud.img.log.RunLog;
import com.google.common.net.HttpHeaders;


//自定义拦截器
public class URLInterceptor extends HandlerInterceptorAdapter {
	
    private Pattern patternSource = Pattern.compile(CommonConstants.SOURCE_IMG);
    private Pattern patternHtml = Pattern.compile(CommonConstants.SOURCE_HTML);
    private Pattern patternGomefs = Pattern.compile(CommonConstants.GOMEFS_REGULAR);
    private Pattern patternWebp = Pattern.compile(CommonConstants.WEBP_REGULAR);
	/**
	 * 重写 preHandle()方法，在业务处理器处理请求之前对该请求进行拦截处理  
	 * 处理请求URI,将参数和key拆分
	 * */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {  
    	
    	//判断是否是get方法，如果不是直接跳过
    	if(!request.getMethod().toLowerCase().equals("get")){
    		return true;
    	}
    	
        if (request.getHeader(HttpHeaders.IF_MODIFIED_SINCE) != null) {
            response.setStatus(HttpStatus.NOT_MODIFIED.value());
            return false;
        }
        
        //校验处理URI
        checkLength(request.getRequestURI());
        ImgObject object = new ImgObject();
        object.setNewUrl(this.process(request.getRequestURI()));
        object.setReqUrl(object.getNewUrl());
        getObjectNewUrl(object,object.getReqUrl());
        
        RunLog.LOG.info("Accept {} {}", request.getMethod(), object.getReqUrl());
        request.setCharacterEncoding(CommonConstants.DEFAULT_ENCODING);
        request.setAttribute("startTime", System.currentTimeMillis());
        request.setAttribute(CommonConstants.IMG_CONTEXT, object);
        return true;
    }  
  
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView mav) throws Exception {  
    }  
  
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception excptn) throws Exception {  
        Long startTime = (Long) request.getAttribute("startTime");
        Long endTime = System.currentTimeMillis();
        RunLog.LOG.info("Complete request[{}] cost {}ms", request.getRequestURI(), endTime-startTime);
    }  
    
    /**
     * 校验URI长度
     * */
    
    private void checkLength(String URI) {
        if(!(URI.length() > 10 && URI.length() < 220)){
        	RunLog.LOG.error(" url too long, this URL is bad Request:{}",URI);
        	throw new ImageServerException(ErrorEnum.BadRequest," Bad Request Url:"+URI);
        }
    }
    
    /**
     * url处理，去掉多余的/
     * */
    
    public  String process(String url) throws UnsupportedEncodingException {
        url = URLDecoder.decode(url, "UTF-8");
        url = url.replaceAll("/{2,}", "/");
        return url.startsWith("/") ? url.substring(1) : url;
    }
    
    /**
     * 判断文件是否是文件系统
     * */
    public void getObjectNewUrl(ImgObject object, String url) throws Exception {
        Matcher matcherSource = patternSource.matcher(url);
        Matcher matcherHtml = patternHtml.matcher(url);
        Matcher matcherGomefs = patternGomefs.matcher(url);
        
        Matcher matcherWebp = patternWebp.matcher(url.toLowerCase());

        if (matcherWebp.find()) {
            object.setIsWebp(1);
        }
        
        if (matcherSource.find()) {
            object.setIsGomefsKey(false);
        } else if(matcherGomefs.find()){
            object.setIsGomefsKey(true);
        } else if(matcherHtml.find()){
        	object.setIsGomefsKey(false);
        	object.setHtml(true);
        } else {
        	//不识别的路径默认为源文件
        	 object.setIsGomefsKey(false);
//        	throw new ImageServerException(ErrorEnum.BadRequest," Bad Request Url:"+url);
        }
    }
}
