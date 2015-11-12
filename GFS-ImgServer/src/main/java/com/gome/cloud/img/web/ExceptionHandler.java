package com.gome.cloud.img.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.gome.cloud.img.exception.ErrorEnum;
import com.gome.cloud.img.exception.ImageServerException;
import com.gome.cloud.img.log.RunLog;



public class ExceptionHandler extends DefaultHandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        ErrorEnum error;
        if (ex instanceof IllegalArgumentException) {
            error = ErrorEnum.InvalidArgument;
            RunLog.LOG.warn("Request [{}] reply a code [{}]", request.getRequestURI(), error.getErrorCode());
        } else if (ex instanceof ImageServerException) {
        	ImageServerException ire = (ImageServerException) ex;
            error = ire.getError();
            RunLog.LOG.warn("Request [{}] reply a code [{}] messages:[{}]", request.getRequestURI(), error.getErrorCode());
        } else {
            error = ErrorEnum.InternalError;
            RunLog.LOG.error("Request [{}]  has a InternalError.", request.getRequestURI(), ex);
        }

        response.setStatus(error.getHttpCode());
        try {
        	RunLog.LOG.error(ex.getMessage());
            response.getOutputStream().write(error.toJson().getBytes());
        } catch (IOException e) {
            // ignore this exception.
        }
        return new ModelAndView();
    }
}