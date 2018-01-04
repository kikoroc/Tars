package com.qq.tars.web.udb;

import com.duowan.udb.util.codec.AESHelper;
import com.duowan.universal.login.BasicCredentials;
import com.duowan.universal.login.Credentials;
import com.duowan.universal.login.client.UniversalLoginClient;
import com.duowan.universal.login.client.UniversalLoginClient.CookieDomainEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;

@Controller
public class UdbWakeUpController {

    @Autowired
    @Qualifier("udbLoginConfigAdapterLocalData")
    private UdbLoginConfigAdapterLocalData udbLoginConfigAdapterLocalData;

    @RequestMapping(value = "/udb/wakeup.do")
    protected void wakeup(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // main code is copied from udb demo code. write by hiyoucai

        // response.setContentType("text/html; charset=utf-8");
        // //会导致url中某些浏览器执行js
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        StringBuilder json = null;
        String source = UdbLoginUtil.urlEncode(request.getParameter("source"));

        String domain = UdbLoginUtil.getDomain(request);
        String contextPath = request.getContextPath();

        try {
            // 提取必要信息，并假定其存在
            String callbackURL = domain + contextPath + "/pages/udb/callback.do?url=" + source;
            String denyCallbackURL = domain + contextPath + "/pages/udb/callback.do?cancel=1&url=" + source;
            ;
            // 构建client
            Credentials cc = new BasicCredentials(udbLoginConfigAdapterLocalData.appId,
                    udbLoginConfigAdapterLocalData.appKey); // 通过appid和appkey构造Credentials
            UniversalLoginClient duowan = new UniversalLoginClient(cc); // 构造Client实例

            // 把initialize()方法的参数设置为回调系统指定的URL
            duowan.initialize(callbackURL); // 初始化时要传入登陆成功后回调的URL
            // 获取请求token，并用cookie方式加密存储，存储由udb js sdk负责
            String tmpTokensecret = duowan.getTokenSecret();
            tmpTokensecret = AESHelper.encrypt(tmpTokensecret, udbLoginConfigAdapterLocalData.appKey);// cookiename
            // =
            // udboauthtmptokensec
            System.out.println("tmpTokensecret==" + tmpTokensecret);

            // 获取授权登录 URL
            // URL redirectURL = duowan.getAuthorizationURL();
            URL redirectURL = duowan.getAuthorizationURLForIPhone(CookieDomainEnum.DUOWAN_DOMAIN);

            // 追加关闭连接
            // 在授权登录URL后面显示地追加appid，避免后端服务质量导致定制效果的失真，参考下面代码：cssid=5142
            String url = redirectURL.toExternalForm().toString() + "&denyCallbackURL="
                    + URLEncoder.encode(denyCallbackURL) + "&cssid=5142" + "&customizeCssStyle=game1";

            json = new StringBuilder("{\"success\":\"1\",\"url\":").append("\"").append(url).append("\"")
                    .append(",\"ttokensec\":").append("\"").append(tmpTokensecret).append("\"").append("}");// 如果成功，要将tokenSecret追加到json里

        } catch (Exception e) {
            String msg = e.getMessage();
            int end = msg.indexOf("\n") - 1;
            if (end < 0)
                end = msg.length();
            json = new StringBuilder("{\"success\":\"0\",\"errMsg\":").append("\"").append(msg.substring(0, end))
                    .append("\"").append("}");
            e.printStackTrace();
        } finally {
            out.append(json);
            out.flush();
            out.close();
        }
    }

}
