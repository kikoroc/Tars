package com.qq.tars.db;

import com.qq.tars.entity.ApiMonitorData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17 0017.
 */
@Repository
public interface ApiMonitorDataMapper {

    List<ApiMonitorData> getApiMonitorData(@Param("tableName") String tableName);

}
