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

    String getServerHost();

    String getUsername();

    String getPassword();

    SignPosition getSignPosition();

    String getSignName();

    Integer getBatchMaxCount();

    Integer getConnectTimeout();

    Integer getReadTimeout();

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
