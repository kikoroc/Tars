package com.qq.tars.web.udb;

import com.duowan.universal.login.client.UniversalLoginClient;
import com.qq.tars.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/1/3 0003.
 */
@Component("udbLoginConfigAdapter")
public class YaoGuoUdbLoginConfigAdapterImpl implements UdbLoginConfigAdapter {

    @Autowired
    private SystemConfigService systemConfigService;

    private static List<String> filterUrlPatternList = new ArrayList<>();
    private static List<String> excludedUrlList = new ArrayList<>();
    private static List<String> AdminList = new ArrayList<>();
    static {
        AdminList.add("dw_wangpeng1");
        AdminList.add("dw_zhangliang");
        AdminList.add("dw_shixiangwen1");
        AdminList.add("dw_huzhenjie");

        filterUrlPatternList.add("/pages/index");
        filterUrlPatternList.add("/pages/tree");
        filterUrlPatternList.add("/pages/server/");
    }

    @Override
    public List<String> filterUrlPattern() {
        return filterUrlPatternList;
    }

    @Override
    public List<String> excludedUrl() {
        return excludedUrlList;
    }

    @Override
    public List<String> cookieDomains() {
        return Arrays.asList(UniversalLoginClient.CookieDomainEnum.DOMAIN_HUYA);
    }

    @Override
    public UdbLoginStrategy loginStrategy() {
        return UdbLoginStrategy.forwardToLoginPage;
    }

    @Override
    public boolean enabledAdmin() {
        return true;
    }

    @Override
    public UdbAdminAuthAdapter adminAuthAdapter() {
        return new UdbAdminAuthAdapter() {
            @Override
            public boolean isAdmin(long yyuid, String passport) {
                return AdminList.contains(passport);
            }
        };
    }

    @Override
    public String indexPage() {
        return "/";
    }

    @Override
    public String loginPage() {
        return "/";
    }

    @Override
    public String appId() {
        return systemConfigService.getConf("udb.appId");
    }

    @Override
    public String appKey() {
        return systemConfigService.getConf("udb.appKey");
    }

    @Override
    public BusinessSessionAdapter businessSessionAdapter() {
        return null;
    }
}
