package com.qq.tars.web.udb;

public enum UdbLoginStrategy {

    forwardToLoginPage("跳转登录页", 1), none("提示空白内容", 2), error("提示异常操作", 3), forwardToUserLoginPage("跳转到用户自定义登录页", 4);

    private String name;
    private int index;

    UdbLoginStrategy(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

}
