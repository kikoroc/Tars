package com.qq.tars.service;

import com.qq.tars.db.ApiMonitorDataMapper;
import com.qq.tars.entity.ApiMonitorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Administrator on 2018/1/17 0017.
 */
@Service
public class ApiMonitorDataService {

    private final Logger log = LoggerFactory.getLogger(ApiMonitorDataService.class);

    @Autowired
    private ApiMonitorDataMapper apiMonitorDataMapper;

    public List<ApiMonitorData> getApiMonitorData(){
        //tars_stat_2018011705
        String tableName = "tars_stat.tars_stat_"+
                LocalDateTime.now().minusMinutes(2).format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        List<ApiMonitorData> ret = apiMonitorDataMapper.getApiMonitorData(tableName);
        log.info("==API性能监控== 表名:{}, 异常记录数: {}", tableName, ret.size());
        return ret;
    }
}
