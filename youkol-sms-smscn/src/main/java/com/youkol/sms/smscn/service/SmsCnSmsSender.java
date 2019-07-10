package com.youkol.sms.smscn.service;

import java.util.List;

import com.youkol.sms.core.config.SmsConfig;
import com.youkol.sms.core.exception.SmsException;
import com.youkol.sms.core.model.SmsBatchMessage;
import com.youkol.sms.core.model.SmsMessage;
import com.youkol.sms.core.service.AbstractSmsService;
import com.youkol.sms.core.service.SmsSender;

/**
 * 云短信
 * <p>第三方短信发送接口实现
 * <p>详见：http://www.sms.cn
 * 
 * @author jackiea
 */
public class SmsCnSmsSender extends AbstractSmsService implements SmsSender {

    public SmsCnSmsSender(SmsConfig config) {
        super(config);
    }

    @Override
    public void send(SmsMessage smsMessage) throws SmsException {
        // TODO 待实现
    }

    @Override
    public void send(List<SmsMessage> smsMessages) throws SmsException {
        // TODO 待实现
    }

    @Override
    public void send(SmsBatchMessage smsBatchMessage) throws SmsException {
        // TODO 待实现
    }

}
