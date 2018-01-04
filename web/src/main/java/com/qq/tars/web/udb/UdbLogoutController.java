package com.qq.tars.web.udb;

import com.duowan.universal.login.client.YYSecCenterOpenWSInvoker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class UdbLogoutController {

	@Autowired
	@Qualifier("udbLoginConfigAdapterLocalData")
    private UdbLoginConfigAdapterLocalData udbLoginConfigAdapterLocalData;

	private static final Log logger = LogFactory.getLog(UdbLogoutController.class);

	@RequestMapping(value = "/udb/logout.do")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// main code is copy from udb demo by hiyoucai
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		boolean logout = false;
		try {
			YYSecCenterOpenWSInvoker.getOAuthCookieDeleteURL(udbLoginConfigAdapterLocalData.appId,
					udbLoginConfigAdapterLocalData.appKey);
			// TODO:①清理cookie，含udb和业务系统自身；②失效业务系统session；
			{
				Cookie[] cookies = request.getCookies();
				for (Cookie cookie : cookies) {
					this.setCookie(request, response, cookie);
				}

				HttpSession session = request.getSession();
				if (request.isRequestedSessionIdValid() && session != null) {
					session.invalidate();
				}

				logout = true;

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.addHeader("P3P",
					"CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
			logger.info("logout=" + logout);
			out.append(
					"<html><head><meta http-equiv='Refresh' content='3; url=" + udbLoginConfigAdapterLocalData.indexPage
							+ "' /></head><body>退出" + (logout ? "成功" : "失败") + "，3秒后跳到首页...</body></html>");
			out.flush();
			out.close();
		}
	}

	private void setCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {
		cookie.setMaxAge(0);
		cookie.setValue(null);
		cookie.setDomain(UdbLoginUtil.getTopMain(request));
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
