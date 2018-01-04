package com.qq.tars.web.udb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huya.passport.auth.HuyaOAuthCookieClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class UdbLoginAdminInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(UdbLoginAdminInterceptor.class);

    @Autowired
    @Qualifier("udbLoginConfigAdapterLocalData")
    private UdbLoginConfigAdapterLocalData udbLoginConfigAdapterLocalData;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (udbLoginConfigAdapterLocalData.enabledAdmin) {
            String sessUsername = UdbLoginUtil.getSessionYyname(request);
            long sessYyuid = UdbLoginUtil.getSessionYyuid(request);

            if(StringUtils.isEmpty(sessUsername) || sessYyuid == 0){
                UdbWelcomeHtmlUtil.forwardLoginUrl(request, response);
                return false;
            }
            boolean isAdmin = udbLoginConfigAdapterLocalData.adminAuthAdapter.isAdmin(sessYyuid, sessUsername);
            if (!isAdmin) {
                UdbWelcomeHtmlUtil.isNotAdminPage(request, response);
                return false;
            }

            if (!StringUtils.isEmpty(sessUsername)) {
                request.setAttribute("sessUsername", sessUsername);
            }
            /*HuyaOAuthCookieClient authClient = new HuyaOAuthCookieClient(
                    udbLoginConfigAdapterLocalData.appId,
                    udbLoginConfigAdapterLocalData.appKey, request);
            String sessUsername = authClient.getUserName();
            long sessYyuid = authClient.getYYUID();

            logger.info("===huyaudb admin登录检查=== userName:"+sessUsername+",yyuid:"+sessYyuid);
            if(StringUtils.isEmpty(sessUsername) || sessYyuid == 0){
                UdbWelcomeHtmlUtil.forwardLoginUrl(request, response, true);
                return false;
            }
            boolean isAdmin = udbLoginConfigAdapterLocalData.adminAuthAdapter.isAdmin(sessYyuid, sessUsername);
            if (!isAdmin) {
                UdbWelcomeHtmlUtil.isNotAdminPage(request, response);
                return false;
            }

            if (!StringUtils.isEmpty(sessUsername)) {
                request.setAttribute("sessUsername", sessUsername);
            }*/
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}
