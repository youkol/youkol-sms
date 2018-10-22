package com.youkol.sms.core.config;

/**
 * 短信配置接口
 * 
 * @author jackiea
 */
public interface SmsConfig {

    /**
     * 厂家服务名称
     * 
     * @return 厂家服务名称
     */
    String getServerName();

    void setServerName(String serverName);

    String getServerHost();

    void setServerHost(String serverHost);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    SignPosition getSignPosition();

    void setSignPosition(SignPosition signPosition);

    String getSignName();

    void setSignName(String signName);

    Integer getBatchMaxCount();

    void setBatchMaxCount(Integer batchMaxCount);

    /**
     * 短信签名位置
     */
    public enum SignPosition {
        /**
         * 无短信签名
         */
        NONE,
        /**
         * 签名位置：后缀
         */
        SUFFIX,
        /**
         * 签名位置：前缀
         */
        PREFIX;
    }
}