package com.qq.tars.web.udb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BusinessSessionAdapter {

    public boolean isAvailableBusinessSession(HttpServletRequest request, HttpServletResponse response,
                                              long sessionYyuid, String SessionYyname);

    public boolean callbackBusinessSession(HttpServletRequest request, HttpServletResponse response, long sessionYyuid,
                                           String SessionYyname);

}
