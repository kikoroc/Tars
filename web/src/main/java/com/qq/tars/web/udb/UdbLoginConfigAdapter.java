package com.qq.tars.web.udb;

import java.util.List;

public interface UdbLoginConfigAdapter {

    public List<String> filterUrlPattern();

    public List<String> excludedUrl();

    public List<String> cookieDomains();

    public UdbLoginStrategy loginStrategy();

    public boolean enabledAdmin();

    public UdbAdminAuthAdapter adminAuthAdapter();

    public String indexPage();

    public String loginPage();

    public String appId();

    public String appKey();

    public BusinessSessionAdapter businessSessionAdapter();

}
