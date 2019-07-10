package com.youkol.sms.core.config;

import lombok.Data;

/**
 * @author jackiea
 */
@Data
public class DefaultSmsConfig implements SmsConfig {

    private String serverName;
    private String serverHost;
    private String username;
    private String password;
    private String signName;
    private SignPosition signPosition;
    private Integer batchMaxCount;
    private Integer connectTimeout;
    private Integer readTimeout;
}
