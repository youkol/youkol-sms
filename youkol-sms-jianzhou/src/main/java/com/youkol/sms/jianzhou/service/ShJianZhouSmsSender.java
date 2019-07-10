package com.youkol.sms.jianzhou.service;

import java.util.List;

import com.youkol.sms.core.config.SmsConfig;
import com.youkol.sms.core.exception.SmsAuthenticationException;
import com.youkol.sms.core.exception.SmsException;
import com.youkol.sms.core.exception.SmsInsufficientException;
import com.youkol.sms.core.exception.SmsMaxBatchSizeExceededException;
import com.youkol.sms.core.exception.SmsSendException;
import com.youkol.sms.core.model.SmsBatchMessage;
import com.youkol.sms.core.model.SmsMessage;
import com.youkol.sms.core.service.SmsSender;

import lombok.extern.slf4j.Slf4j;

/**
 * 上海建周
 * <p>第三方短信发送接口实现
 * <p>详见：http://www.shjianzhou.com
 * 
 * @author jackiea
 */
@Slf4j
public class ShJianZhouSmsSender extends AbstractShJianZhouSmsService implements SmsSender {

    public ShJianZhouSmsSender(SmsConfig config) {
        super(config);
    }

    /**
     * 发送单条短信
     *
     * @param smsMessage 短信信息
     * @throws SmsAuthenticationException 短信账户或密码错误异常
     * @throws SmsInsufficientException 短信账户余额不足异常
     * @throws SmsSendException 短信发送异常
     * @throws SmsException 短信通用异常
     */
    @Override
    public void send(SmsMessage smsMessage) throws SmsException {
        // 添加短信签名
        String content = this.addSignName(smsMessage.getContent());
        // 手机号
        String phone = this.join(BATCH_SEPARATOR, smsMessage.getPhones());

        // 发送时间
        Long sendTime = smsMessage.getSendTime();

        // 发送短信
        int result = 0;
        try {

            if (sendTime == null) {
                result = this.getJianZhouService().sendBatchMessage(
                    this.getConfig().getUsername(), 
                    this.getConfig().getPassword(),
                    phone, content);
            } else {
                String time = this.formatTime(sendTime, TIME_PATTERN);
                result = this.getJianZhouService().sendTimelyMessage(
                    this.getConfig().getUsername(), 
                    this.getConfig().getPassword(), 
                    time, phone, content);
            }

            log.debug("返回结果：" + result);
        } catch (Exception ex) {
            throw new SmsSendException("发送短信发生异常", ex);
        }

        // 结果处理
        if (result > 0) {
            return;
        }
        switch (result) {
            case 0:
                throw new SmsException("短信发送失败");
            case -1:
                throw new SmsInsufficientException("短信发送失败，账户余额不足");
            case -2:
                throw new SmsAuthenticationException("短信发送失败，账号或密码错误");
            default:
                throw new SmsException("短信发送失败，错误码：" + result);
        }
    }

    /**
     * 批量发送短信（内容部分不相同）
     *
     * @param smsMessages 短信信息集合
     * @throws SmsMaxBatchSizeExceededException 超过最大发送条数异常
     * @throws SmsAuthenticationException 短信账户或密码错误异常
     * @throws SmsInsufficientException 短信账户余额不足异常
     * @throws SmsSendException 短信发送异常
     * @throws SmsException 短信通用异常
     */
    @Override
    public void send(List<SmsMessage> smsMessages) throws SmsException {
        if (smsMessages.size() > this.getConfig().getBatchMaxCount()) {
            throw new SmsMaxBatchSizeExceededException("超过最大发送条数限制");
        }

        for (SmsMessage smsMessage : smsMessages) {
            this.send(smsMessage);
        }
    }

    /**
     * 发送多条短信（相同内容）
     * 如果内容带发送时间，则直接内部调用定时发送接口
     *
     * @param smsBatchMessage 批量短信信息
     * @throws SmsMaxBatchSizeExceededException 超过最大发送条数异常
     * @throws SmsAuthenticationException 短信账户或密码错误异常
     * @throws SmsInsufficientException 短信账户余额不足异常
     * @throws SmsSendException 短信发送异常
     * @throws SmsException 短信通用异常
     */
    @Override
    public void send(SmsBatchMessage smsBatchMessage) throws SmsException {
        if (smsBatchMessage.getPhones().size() > this.getConfig().getBatchMaxCount()) {
            throw new SmsMaxBatchSizeExceededException("超过最大发送条数限制");
        }

        String content = this.addSignNameJoin(PERSONAL_SEPARATOR, smsBatchMessage.getContents());
        String phone = this.join(PERSONAL_SEPARATOR, smsBatchMessage.getPhones());

        int result = 0;
        try {

            result = this.getJianZhouService().sendPersonalMessages(
                this.getConfig().getUsername(), 
                this.getConfig().getPassword(), 
                phone, content);

            log.debug("返回结果：" + result);
        } catch (Exception ex) {
            throw new SmsSendException("批量发送短信发生异常", ex);
        }

        // 结果处理
        if (result > 0) {
            return;
        }
        switch (result) {
            case 0:
                throw new SmsException("短信发送失败");
            case -1:
                throw new SmsInsufficientException("短信发送失败，账户余额不足");
            case -2:
                throw new SmsAuthenticationException("短信发送失败，账号或密码错误");
            default:
                throw new SmsException("短信发送失败，错误码：" + result);
        }
    }

}
