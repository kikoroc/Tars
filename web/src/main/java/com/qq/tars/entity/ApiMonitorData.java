package com.qq.tars.entity;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/17 0017.
 */
public class ApiMonitorData {

    private String serverName;

    private String interfaceName;

    private Date startTime;

    private Date endTime;

    private int avgCost;

    private int maxCost;

    public ApiMonitorData() {
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(int avgCost) {
        this.avgCost = avgCost;
    }

    public int getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(int maxCost) {
        this.maxCost = maxCost;
    }
}
