package com.youkol.sms.aliyun.model;

import com.youkol.sms.core.model.SmsMessage;

import lombok.Getter;
import lombok.Setter;

/**
 * @author jackiea
 */
@Getter
@Setter
public class AliyunSmsMessage extends SmsMessage {

    private static final long serialVersionUID = 1L;

    /**
     * 上行短信扩展码
     */
    private String smsUpExtendCode;
}
