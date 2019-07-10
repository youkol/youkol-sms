package com.youkol.sms.smscn.service;

import java.util.List;

import com.youkol.sms.core.config.SmsConfig;
import com.youkol.sms.core.exception.SmsException;
import com.youkol.sms.core.model.SmsBatchMessage;
import com.youkol.sms.core.model.SmsMessage;
import com.youkol.sms.core.service.AbstractSmsSender;

/**
 * 云短信
 * <p>第三方短信发送接口实现
 * <p>详见：http://www.sms.cn
 * 
 * // TODO: 暂未实现
 * 
 * @author jackiea
 */
public class SmsCnSmsSender extends AbstractSmsSender {

    public SmsCnSmsSender(SmsConfig config) {
        super(config);
    }

    @Override
    public void send(SmsMessage smsMessage) throws SmsException {

    }

    @Override
    public void send(List<SmsMessage> smsMessages) throws SmsException {

    }

    @Override
    public void send(SmsBatchMessage smsBatchMessage) throws SmsException {

    }

    @Override
    public int getRemainedCount() throws SmsException {
        return 0;
    }

    @Override
    public int getSendedCount() throws SmsException {
        return 0;
    }

    @Override
    public boolean modifyPassword(String oldPassword, String newPassword) throws SmsException {
        return false;
    }

}
