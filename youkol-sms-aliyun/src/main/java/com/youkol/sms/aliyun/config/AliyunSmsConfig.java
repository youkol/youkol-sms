package com.youkol.sms.aliyun.config;

import com.youkol.sms.core.config.DefaultSmsConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AliyunSmsConfig extends DefaultSmsConfig {

    private String version = "2017-05-25";

}
