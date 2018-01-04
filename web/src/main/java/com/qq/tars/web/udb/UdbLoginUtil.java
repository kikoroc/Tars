package com.qq.tars.web.udb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.duowan.udb.auth.UserinfoForOauth;
import com.duowan.udb.util.codec.AESHelper;

public abstract class UdbLoginUtil {

    private static final Log logger = LogFactory.getLog(UdbLoginUtil.class);

    public final static String UDB_SESSION = "udb_session";

    public final static long INNER_SESSION_EXPIRE_TIME = 1000 * 60 * 30;

    public static String getRequestUri(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestURI = "";
        if ("/".equals(contextPath)) {
            requestURI = request.getRequestURI();
        } else {
            String uri = request.getRequestURI();
            requestURI = uri.substring(contextPath.length());
        }
        if (requestURI.indexOf("//") != -1) {
            requestURI = requestURI.replaceAll("/+", "/");
        }
        return requestURI;
    }

    public static boolean matchFilterUrlPattern(String uri, List<String> filterUriList) {

        if (uri.startsWith("/udb")) {
            return false;
        }
        if (uri.startsWith("/error")) {
            return false;
        }

        if (filterUriList == null || filterUriList.size() < 1) {
            return true;
        }

        for (String filterUri : filterUriList) {
            if (filterUri.endsWith("/")) {
                if (uri.startsWith(filterUri)) {
                    return true;
                }
            } else {
                if (uri.equals(filterUri)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isExcludedUrl(String uri, List<String> excludesUrlList) {
        if (excludesUrlList == null || excludesUrlList.size() < 1) {
            return false;
        }
        for (String excludesUrl : excludesUrlList) {
            if (excludesUrl.endsWith("/")) {
                if (uri.startsWith(excludesUrl)) {
                    return true;
                }
            } else {
                if (uri.equals(excludesUrl)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isAvailableInnerSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            Object udbSession = session.getAttribute(UdbLoginUtil.UDB_SESSION);
            if (udbSession != null) {
                String[] uss = udbSession.toString().split("#");
                if (uss.length >= 3) {
                    if (evalLong(uss[0]) > 0) {
                        if (evalLong(uss[1]) - INNER_SESSION_EXPIRE_TIME <= System.currentTimeMillis()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static long getSessionYyuid(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            Object udbSession = session.getAttribute(UdbLoginUtil.UDB_SESSION);
            if (udbSession != null) {
                String[] uss = udbSession.toString().split("#");
                if (uss.length >= 3) {
                    if (evalLong(uss[0]) > 0) {
                        return evalLong(uss[0]);
                    }
                }
            }
        }
        return 0;
    }

    public static String getSessionYyname(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            Object udbSession = session.getAttribute(UdbLoginUtil.UDB_SESSION);
            if (udbSession != null) {
                String[] uss = udbSession.toString().split("#");
                if (uss.length >= 3) {
                    if (evalLong(uss[0]) > 0) {
                        return uss[2];
                    }
                }
            }
        }
        return null;
    }

    public static boolean validateOuterSession(HttpServletRequest request, HttpServletResponse response, String appId,
            String appKey) {

        String tokenSecret = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("udboauthtmptokensec".equals(cookie.getName())) {
                    tokenSecret = AESHelper.decrypt(cookie.getValue(), appKey);
                    break;
                }
            }
        }
        if (StringUtils.isEmpty(tokenSecret)) {
            return false;
        }

        UserinfoForOauth userinfo4oauth = new UserinfoForOauth(request, response, appId, appKey);
        boolean validFlag = userinfo4oauth.validate();

        if (validFlag) {
            updateUdbSession(request, userinfo4oauth.getYyuid(), userinfo4oauth.getUsername());
            return true;
        }

        return false;
    }

    public static boolean updateUdbSession(HttpServletRequest request, String yyuid, String username) {
        HttpSession session = request.getSession();
        String sessionStr = yyuid + "#" + (System.currentTimeMillis() + INNER_SESSION_EXPIRE_TIME) + "#" + username;
        session.setAttribute(UdbLoginUtil.UDB_SESSION, sessionStr);
        return true;
    }

    public static long evalLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return -1;
        }
    }

    public static List<String> toImmutableList(List<String> list) {
        if (list == null) {
            return new ArrayList<String>();
        }
        return Collections.unmodifiableList(list);
    }

    public static String urlEncode(String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String getDomain(HttpServletRequest request) {
        String serverName = getServerName(request);
        String scheme = request.getScheme();
        String domain = scheme + "://" + serverName;
        return domain;
    }

    public static String getServerName(HttpServletRequest request) {
        String serverName = request.getHeader("host");
        if (StringUtils.isEmpty(serverName)) {
            serverName = request.getServerName();
        }
        return serverName;
    }

    public static String getTopMain(HttpServletRequest request) {
        String serverName = getServerName(request);
        Pattern p = Pattern.compile("^.+\\.(\\w+\\.\\w+)$");
        Matcher matcher = p.matcher(serverName);
        if (matcher.matches()) {
            serverName = matcher.group(1);
        }
        return serverName;
    }

}
