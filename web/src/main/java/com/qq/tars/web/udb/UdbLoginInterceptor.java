package com.qq.tars.web.udb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UdbLoginInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(UdbLoginInterceptor.class);

    @Autowired
    @Qualifier("udbLoginConfigAdapterLocalData")
    private UdbLoginConfigAdapterLocalData udbLoginConfigAdapterLocalData;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = UdbLoginUtil.getRequestUri(request);
        logger.info("===udb登录检查=== uri:"+uri+", filterUrlPattern:" + udbLoginConfigAdapterLocalData.filterUrlPattern.size());
        boolean matchFilter = UdbLoginUtil.matchFilterUrlPattern(uri, udbLoginConfigAdapterLocalData.filterUrlPattern);
        if (!matchFilter && !"/".equals(uri)) {
            return true;
        }

        boolean isExcluded = UdbLoginUtil.isExcludedUrl(uri, udbLoginConfigAdapterLocalData.excludedUrl);
        if (isExcluded) {
            return true;
        }

        if (UdbLoginUtil.isAvailableInnerSession(request)) {
            if (udbLoginConfigAdapterLocalData.businessSessionAdapter != null) {
                long sessionYyuid = UdbLoginUtil.getSessionYyuid(request);
                String SessionYyname = UdbLoginUtil.getSessionYyname(request);
                if (udbLoginConfigAdapterLocalData.businessSessionAdapter.isAvailableBusinessSession(request, response,
                        sessionYyuid, SessionYyname)) {
                    return true;
                }
            } else {
                return true;
            }
        }

        // must be last one filter
        if (UdbLoginUtil.validateOuterSession(request, response, this.udbLoginConfigAdapterLocalData.appId,
                this.udbLoginConfigAdapterLocalData.appKey)) {
            if (udbLoginConfigAdapterLocalData.businessSessionAdapter != null) {
                long sessionYyuid = UdbLoginUtil.getSessionYyuid(request);
                String SessionYyname = UdbLoginUtil.getSessionYyname(request);
                if (udbLoginConfigAdapterLocalData.businessSessionAdapter.callbackBusinessSession(request, response,
                        sessionYyuid, SessionYyname)) {
                    return true;
                }
            } else {
                return true;
            }
        }

        if (this.udbLoginConfigAdapterLocalData.loginStrategy == UdbLoginStrategy.forwardToLoginPage) {
            UdbWelcomeHtmlUtil.forwardLoginUrl(request, response);
            return false;
        }

        if (this.udbLoginConfigAdapterLocalData.loginStrategy == UdbLoginStrategy.forwardToUserLoginPage) {
            UdbWelcomeHtmlUtil.forwardToUserLoginUrl(request, response, this.udbLoginConfigAdapterLocalData.indexPage,
                    this.udbLoginConfigAdapterLocalData.loginPage);
            return false;
        }

        if (this.udbLoginConfigAdapterLocalData.loginStrategy == UdbLoginStrategy.error) {
            response.sendError(400, "please signup udb");
            return false;
        }

        if (this.udbLoginConfigAdapterLocalData.loginStrategy == UdbLoginStrategy.none) {
            return false;
        }

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
