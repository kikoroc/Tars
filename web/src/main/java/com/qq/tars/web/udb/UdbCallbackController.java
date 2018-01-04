package com.qq.tars.web.udb;

import com.duowan.udb.util.codec.AESHelper;
import com.duowan.udb.util.string.StringUtil;
import com.duowan.universal.login.BasicCredentials;
import com.duowan.universal.login.Credentials;
import com.duowan.universal.login.OAuthHeaderNames;
import com.duowan.universal.login.client.UniversalLoginClient;
import com.duowan.universal.login.client.UniversalLoginClient.CookieDomainEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

@Controller
public class UdbCallbackController {

	@Autowired
	@Qualifier("udbLoginConfigAdapterLocalData")
    private UdbLoginConfigAdapterLocalData udbLoginConfigAdapterLocalData;

	private static final Log logger = LogFactory.getLog(UdbCallbackController.class);

	@RequestMapping(value = "/udb/callback.do")
	public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// main code is copied from udb demo code. write by hiyoucai

		String url = request.getParameter("url");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.append("<html><head>");
		String cancel = request.getParameter("cancel");
		if ("1".equalsIgnoreCase(cancel)) {
			out.append("<script language=\"JavaScript\" type=\"text/javascript\">");
			out.append("self.parent.UDB.sdk.PCWeb.popupCloseLgn();");

			out.append("</script>");
			out.append("</head><body>");
			out.append("</body></html>");
			out.flush();
			return;
		}

		try {
			String oauth_token = request.getParameter(OAuthHeaderNames.TOKEN_KEY);
			String oauth_verifier = request.getParameter(OAuthHeaderNames.VERIFIER);
			String isRemMe = request.getParameter("isRemMe");

			logger.info("----------->isRemMe:" + isRemMe);

			String tokenSecret = null;
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("udboauthtmptokensec".equals(cookie.getName())) {
						tokenSecret = AESHelper.decrypt(cookie.getValue(), udbLoginConfigAdapterLocalData.appKey);
						break;
					}
				}
			}
			if (tokenSecret == null) {
				throw new RuntimeException("会话超时，请关闭登录窗重试！");
			}
			if (StringUtil.isEmpty(oauth_token)) {
				throw new RuntimeException("验证出错.tokenSecret为空");
			}
			if (StringUtil.isEmpty(oauth_verifier)) {
				throw new RuntimeException("验证出错.oauth_verifier为空");
			}

			Credentials cc = new BasicCredentials(udbLoginConfigAdapterLocalData.appId,
					udbLoginConfigAdapterLocalData.appKey);
			UniversalLoginClient duowan = new UniversalLoginClient(cc);
			logger.info("tokenSecret==" + tokenSecret);
			String[] accessTokenInfo = null;
			accessTokenInfo = duowan.getAccessToken(oauth_token, tokenSecret, oauth_verifier);

			String yyuid = duowan.getYyuid(accessTokenInfo[0]);

			List<String> reqDomainList = new LinkedList<String>();
			reqDomainList.add(CookieDomainEnum.YY_DOMAIN);
			reqDomainList.add(CookieDomainEnum.DUOWAN_DOMAIN);
			reqDomainList.add(CookieDomainEnum.KUAIKUAI_DOMAIN);

			reqDomainList.addAll(udbLoginConfigAdapterLocalData.cookieDomains);

			String writeCookieURL = duowan.getWriteCookieURL(accessTokenInfo[0], yyuid, reqDomainList);

			out.append(
					"<script language=\"JavaScript\" type=\"text/javascript\">function udb_callback(){self.parent.UDB.sdk.PCWeb.writeCrossmainCookieWithCallBack('"
							+ writeCookieURL + "',function(){self.parent.document.location.href='" + url
							+ "';});};udb_callback();</script>");
			out.append("</head><body>");

		} catch (Exception e) {
			e.printStackTrace();
			String msg = e.getMessage();
			int end = msg.indexOf("\n") - 1;
			if (end < 0)
				end = msg.length();
			out.append("<script language=\"JavaScript\" type=\"text/javascript\">function callback(){alert('ERROR:")
					.append(msg.subSequence(0, end))
					.append("');self.parent.document.location.reload(-1);};callback();</script>");
			out.append("</head><body>");
		}
		out.append("</body></html>");
		out.flush();
	}

}
