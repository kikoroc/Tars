package com.qq.tars.web.udb;

import com.huya.passport.auth.HuyaOAuthCookieClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HuyaUdbLoginInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(HuyaUdbLoginInterceptor.class);

    @Autowired
    @Qualifier("udbLoginConfigAdapterLocalData")
    private UdbLoginConfigAdapterLocalData udbLoginConfigAdapterLocalData;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = UdbLoginUtil.getRequestUri(request);
        logger.info("===huyaudb登录检查=== uri:"+uri+", filterUrlPattern:" + udbLoginConfigAdapterLocalData.filterUrlPattern.size());
        boolean matchFilter = UdbLoginUtil.matchFilterUrlPattern(uri, udbLoginConfigAdapterLocalData.filterUrlPattern);
        if (!matchFilter && !"/".equals(uri)) {
            return true;
        }

        boolean isExcluded = UdbLoginUtil.isExcludedUrl(uri, udbLoginConfigAdapterLocalData.excludedUrl);
        if (isExcluded) {
            return true;
        }

        HuyaOAuthCookieClient authClient = new HuyaOAuthCookieClient(
                udbLoginConfigAdapterLocalData.appId,
                udbLoginConfigAdapterLocalData.appKey, request);
        if(authClient.validate()){
            UdbLoginUtil.updateUdbSession(request, String.valueOf(authClient.getYYUID()), authClient.getUserName());
            return true;
        }

        UdbWelcomeHtmlUtil.forwardLoginUrl(request, response, true);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // TODO what do you want to do?
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO what do you want to do?
    }

}
