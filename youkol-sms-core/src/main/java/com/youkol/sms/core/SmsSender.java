package com.youkol.sms.core;

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
     * 发送单条短信
     * 
     * @param smsMessage 短信信息
     * @throws SmsException 异常信息
     */
    public void send(SmsMessage smsMessage) throws SmsException;

    /**
     * 发送多条短信（内容部分不相同）
     * 
     * @param smsMessages 短信信息
     * @throws SmsException 异常信息
     */
    public void send(List<SmsMessage> smsMessages) throws SmsException;

    /**
     * 发送多条短信（相同内容）
     * 
     * @param smsBatchMessage 批量短信信息
     * @throws SmsException 异常信息
     */
    public void send(SmsBatchMessage smsBatchMessage) throws SmsException;

    /**
     * 查询剩余短信数
     * 
     * @return 返回剩余短信数量
     * @throws SmsException 异常信息
     */
    public int getRemainedCount() throws SmsException;

    /**
     * 查询已发送短信数
     * 
     * @return 返回已发送短信数
     * @throws SmsException 异常信息
     */
    public int getSendedCount() throws SmsException;

    /**
     * 修改短信通道密码
     * 
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 如果修改成功返回true，否则返回false
     * @throws SmsException 异常信息
     */
    public boolean modifyPassword(String oldPassword, String newPassword) throws SmsException;

}
