package com.zc.erpext.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="constparam") //接收application.yml中的myProps下面的属性
public class ConstParam
{
    private String appid;
    private String secret;
    private String agentid;

    public String getAppId() {
        return appid;
    }
    public void setAppId(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }
    public void setSecret(String wxSecret) {
        this.secret = secret;
    }

    public String getAgentId() {
        return agentid;
    }
    public void setAgentId(String agentid) {
        this.agentid = agentid;
    }
}
