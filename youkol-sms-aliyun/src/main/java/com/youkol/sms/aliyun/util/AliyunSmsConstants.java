package com.youkol.sms.aliyun.util;

/**
 * @author jackiea
 */
public class AliyunSmsConstants {

    public static final String PHONE_SEPARATOR = ",";

    public enum SmsAction {
        /**
         * 短信发送接口
         * <p>支持在一次请求中向多个不同的手机号码发送同样内容的短信
         */
        SendSms,
        /**
         * 短信批量发送
         * <p>支持在一次请求中分别向多个不同的手机号码发送不同签名的短信
         */
        SendBatchSms,
        /**
         * 查看短信发送记录和发送状态
         */
        QuerySendDetails;
    }
}
