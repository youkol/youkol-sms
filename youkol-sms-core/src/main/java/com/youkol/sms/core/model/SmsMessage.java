package com.youkol.sms.core.model;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 单条短信
 * 
 * @author jackiea
 */
@Getter
@Setter
public class SmsMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String phone;

    private String content;

    // 秒值
    private Long sendTime;

    private Map<String, String> additionalContent;

}