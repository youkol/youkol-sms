package com.youkol.sms.core.service;

import java.util.List;

import com.youkol.sms.core.exception.SmsException;
import com.youkol.sms.core.model.SmsBatchMessage;
import com.youkol.sms.core.model.SmsMessage;

/**
 * 短信发送接口
 * 
 * @author jackiea
 */
public interface SmsSender {

    /**
     * 发送短信（若有多个手机号，内容相同）
     * 
     * @param smsMessage 短信信息
     * @throws SmsException 异常信息
     */
    public void send(SmsMessage smsMessage) throws SmsException;

    /**
     * 发送短信（内容不相同）
     * 
     * @param smsMessages 短信信息
     * @throws SmsException 异常信息
     */
    public void send(List<SmsMessage> smsMessages) throws SmsException;

    /**
     * 发送短信（多个手机号，内容格式相同）
     * 
     * @param smsBatchMessage 批量短信信息
     * @throws SmsException 异常信息
     */
    public void send(SmsBatchMessage smsBatchMessage) throws SmsException;

    

}
