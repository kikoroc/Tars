package com.qq.tars.web.udb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.duowan.universal.login.client.YYSecCenterOpenWSInvoker;

public class UdbWelcomeHtmlUtil {

    private static final Log logger = LogFactory.getLog(UdbWelcomeHtmlUtil.class);

    public static String getFullUrl(String moreUri, String domain, String contextPath) {
        if (contextPath == null) {
            contextPath = "";
        } else if ("/".equals(contextPath)) {
            contextPath = "";
        }
        return domain + "" + contextPath + "" + moreUri;

    }

    public static String getLoginHtml(String moreUri, String domain, String contextPath) {
        if (contextPath == null) {
            contextPath = "";
        } else if ("/".equals(contextPath)) {
            contextPath = "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        sb.append("<title>YY统一验证中心</title>");
        sb.append("<script src=\"http://res.udb.duowan.com/js/jquery-1.4.2.min.js\" type=\"text/javascript\"></script>");
        sb.append("<script type=\"text/javascript\" src=\"http://res.udb.duowan.com/lgn/js/oauth/udbsdk/pcweb/udb.sdk.pcweb.popup.min.js\"></script>");
        sb.append("<script type=\"text/javascript\">");
        sb.append("function sdklogin(){ UDB.sdk.PCWeb.popupOpenLgn('" + domain + "" + contextPath
                + "/pages/udb/wakeup.do?source=" + moreUri + "','" + domain + "" + contextPath + "/pages/udb/callback.do?url="
                + domain + "" + contextPath + "" + moreUri + "','" + domain + "" + contextPath
                + "/pages/udb/callback.do?cancel=1');");
        sb.append("}");
        sb.append("</script>");
        sb.append("</head>");
        sb.append("<body onload=\"sdklogin();\">");
        sb.append("</body>");

        sb.append("</html>");
        return sb.toString();
    }

    public static String getHuyaLoginHtml(String returnUrl){
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        sb.append("<title>虎牙UDB登录 - 腰果tars后台</title>");
        sb.append("<script src=\"http://res.udb.duowan.com/js/jquery-1.4.2.min.js\" type=\"text/javascript\"></script>");
        sb.append("<script src='http://udbres.huya.com/js/HyUDBWebSDK-2.0.js'></script>");
        sb.append("<script src='http://udbres.huya.com/js/HyLoginSDK-2.0.js'></script>");
        sb.append("<script type=\"text/javascript\">");
        sb.append("var huyaLogin=new HyLogin({url:{common:'"+returnUrl+"'},global:{thirdWin:'2',redirectUrl:'self'}});");
        sb.append("</script>");
        sb.append("</head>");
        sb.append("<body onload=\"huyaLogin.login();\">");
        sb.append("</body>");

        sb.append("</html>");
        return sb.toString();
    }

    public static String getLogoutHtml(String referer, String appId, String appKey) {

        String deleteCookieURL = YYSecCenterOpenWSInvoker.getOAuthCookieDeleteURL(appId, appKey);
        logger.info("deleteCookieURL:" + deleteCookieURL);
        StringBuffer sb = new StringBuffer();

        String redirectUrl = StringUtils.isEmpty(referer) ? "http://udb.duowan.com/" : referer;
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        sb.append("<title>YY统一验证中心</title>");
        sb.append("<script src=\"http://res.udb.duowan.com/js/jquery-1.4.2.min.js\" type=\"text/javascript\"></script>");
        sb.append("<script src=\"http://res.udb.duowan.com/lgn/js/oauth/udbsdk/pcweb/udb.sdk.pcweb.popup.min.js\" type=\"text/javascript\"></script>");
        sb.append("</head>");
        sb.append("<script type=\"text/javascript\">");
        sb.append("function logout(){ UDB.sdk.PCWeb.deleteCrossmainCookieWithCallBack(\"" + deleteCookieURL + "\" ,");
        sb.append("                 function() { top.location.href = '").append(redirectUrl).append("' } ); }");
        sb.append("</script>");
        sb.append("<body onload=\"logout();\">");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    public static void forwardLoginUrl(HttpServletRequest request, HttpServletResponse response, boolean huya) {
        String domain = UdbLoginUtil.getDomain(request);
        String contextPath = request.getContextPath();
        String moreUri = request.getRequestURI();
        if (StringUtils.isNotEmpty(request.getQueryString())) {
            moreUri += "?" + UdbLoginUtil.urlEncode(request.getQueryString());
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            PrintWriter out = response.getWriter();
            if(huya){
                out.append(getHuyaLoginHtml("http://ygtars.huya.com/"));
            }else{
                out.append(getLoginHtml(moreUri, domain, contextPath));
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void forwardLoginUrl(HttpServletRequest request, HttpServletResponse response) {
        String domain = UdbLoginUtil.getDomain(request);
        String contextPath = request.getContextPath();
        String moreUri = request.getRequestURI();
        if (StringUtils.isNotEmpty(request.getQueryString())) {
            moreUri += "?" + UdbLoginUtil.urlEncode(request.getQueryString());
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            PrintWriter out = response.getWriter();
            out.append(getLoginHtml(moreUri, domain, contextPath));
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void forwardToUserLoginUrl(HttpServletRequest request, HttpServletResponse response,
            String indexPage, String loginPage) throws ServletException, IOException {
        String domain = UdbLoginUtil.getDomain(request);
        String contextPath = request.getContextPath();
        String shortUrl = request.getRequestURI();
        if (StringUtils.isNotEmpty(request.getQueryString())) {
            shortUrl += "?" + request.getQueryString();
        }

        if (StringUtils.isEmpty(shortUrl) || shortUrl.startsWith(loginPage)) {
            shortUrl = indexPage;
        }

        request.setAttribute("shortUrl", UdbLoginUtil.urlEncode(shortUrl));
        request.setAttribute("fullUrl", UdbLoginUtil.urlEncode(getFullUrl(shortUrl, domain, contextPath)));

        request.getRequestDispatcher(loginPage).forward(request, response);
    }

    public static void isNotAdminPage(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            PrintWriter out = response.getWriter();
            /*out.append("<script src=\"http://res.udb.duowan.com/js/jquery-1.4.2.min.js\" type=\"text/javascript\"></script>");
            out.append("<script src='http://udbres.huya.com/js/HyUDBWebSDK-2.0.js'></script>");
            out.append("<script src='http://udbres.huya.com/js/HyLoginSDK-2.0.js'></script>");
            out.append("<script>");
            out.append("var huyaLogin=new HyLogin({url:{common:'http://ygtars.huya.com/'},global:{thirdWin:'2',redirectUrl:'self'}});");
            out.append("</script>");*/
            out.append("你不是管理员<a href='/pages/udb/logout.do'>退出当前帐号</a>");
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
