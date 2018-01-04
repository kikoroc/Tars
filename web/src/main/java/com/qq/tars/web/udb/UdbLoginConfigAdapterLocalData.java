package com.qq.tars.web.udb;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class UdbLoginConfigAdapterLocalData {

    public final List<String> filterUrlPattern;

    public final List<String> excludedUrl;

    public final List<String> cookieDomains;

    public final UdbLoginStrategy loginStrategy;

    public final boolean enabledAdmin;

    public final UdbAdminAuthAdapter adminAuthAdapter;

    public final String appId;

    public final String appKey;

    public final String indexPage;

    public final String loginPage;

    public final BusinessSessionAdapter businessSessionAdapter;

    public UdbLoginConfigAdapterLocalData(UdbLoginConfigAdapter udbLoginConfigAdapter) {
        Assert.isTrue(StringUtils.isNotEmpty(udbLoginConfigAdapter.appId()));
        Assert.isTrue(StringUtils.isNotEmpty(udbLoginConfigAdapter.appKey()));
        this.filterUrlPattern = UdbLoginUtil.toImmutableList(udbLoginConfigAdapter.filterUrlPattern());
        this.excludedUrl = UdbLoginUtil.toImmutableList(udbLoginConfigAdapter.excludedUrl());
        this.cookieDomains = UdbLoginUtil.toImmutableList(udbLoginConfigAdapter.cookieDomains());
        this.loginStrategy = udbLoginConfigAdapter.loginStrategy();
        this.enabledAdmin = udbLoginConfigAdapter.enabledAdmin();
        this.adminAuthAdapter = udbLoginConfigAdapter.adminAuthAdapter();
        this.indexPage = udbLoginConfigAdapter.indexPage();
        this.loginPage = udbLoginConfigAdapter.loginPage();

        if (this.enabledAdmin) {
            Assert.isTrue(this.adminAuthAdapter != null, "请配置管理员认证接口！");
        }
        this.appId = udbLoginConfigAdapter.appId();
        this.appKey = udbLoginConfigAdapter.appKey();

        this.businessSessionAdapter = udbLoginConfigAdapter.businessSessionAdapter();
    }
}
