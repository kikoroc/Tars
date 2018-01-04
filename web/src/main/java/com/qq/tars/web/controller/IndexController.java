/*
 * Tencent is pleased to support the open source community by making Tars available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.qq.tars.web.controller;

import com.qq.common.WrappedController;
import com.qq.tars.service.SystemConfigService;
import com.qq.tars.web.udb.UdbLoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends WrappedController {

    @Autowired
    private SystemConfigService systemConfigService;

    @RequestMapping(
            value = "index",
            produces = {"text/html"}
    )
    @Transactional(rollbackFor = {Exception.class})
    public String index(HttpServletRequest request) throws Exception {
        request.setAttribute("userName", UdbLoginUtil.getSessionYyname(request));
        request.setAttribute("env", systemConfigService.getConf("env"));
        return "index";
    }
}