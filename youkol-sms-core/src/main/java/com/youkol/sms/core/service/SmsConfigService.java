package com.youkol.sms.core.service;

import com.youkol.sms.core.config.SmsConfig;

/**
 * 短信配置服务接口
 * 
 * @author jackiea
 */
public interface SmsConfigService {

    /**
     * 获取短信配置信息
     * 
     * @return 短信配置信息
     */
    public SmsConfig getConfig();
}
