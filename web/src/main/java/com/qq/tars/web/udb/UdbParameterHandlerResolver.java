package com.qq.tars.web.udb;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class UdbParameterHandlerResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        String parameterName = parameter.getParameterName();
        if ("sessYyuid".equals(parameterName) || "sessUsername".equals(parameterName)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Object nativeReq = webRequest.getNativeRequest();

        if (nativeReq instanceof HttpServletRequest) {
            HttpServletRequest nativeReqTemp = (HttpServletRequest) nativeReq;
            String parameterName = parameter.getParameterName();
            if ("sessYyuid".equals(parameterName)) {
                return UdbLoginUtil.getSessionYyuid(nativeReqTemp);
            } else {
                return UdbLoginUtil.getSessionYyname(nativeReqTemp);
            }
        }

        throw new RuntimeException("not support resolveArgument");
    }

}
