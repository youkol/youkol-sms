package com.youkol.sms.aliyun.service;

import java.util.List;

import com.aliyuncs.CommonRequest;
import com.youkol.sms.aliyun.service.response.AliyunSendSmsResponse;
import com.youkol.sms.core.config.SmsConfig;
import com.youkol.sms.core.exception.SmsException;
import com.youkol.sms.core.exception.SmsMaxBatchSizeExceededException;
import com.youkol.sms.core.model.SmsBatchMessage;
import com.youkol.sms.core.model.SmsMessage;
import com.youkol.sms.core.service.SmsSender;

/**
 * 阿里云 短信服务
 * <p>
 * 第三方短信发送接口实现
 * <p>
 * 详见：https://help.aliyun.com/product/44282.html
 * 
 * @author jackiea
 */
public class AliyunSmsSender extends AbstractAliyunSmsService implements SmsSender {

    public AliyunSmsSender(SmsConfig config) {
        super(config);
    }

    @Override
    public void send(SmsMessage smsMessage) throws SmsException {
        CommonRequest request = this.creatRequest();
        this.fillSendParams(request, smsMessage);

        this.sendRequest(request, AliyunSendSmsResponse.class);
    }

    @Override
    public void send(List<SmsMessage> smsMessages) throws SmsException {
        if (smsMessages.size() > this.getConfig().getBatchMaxCount()) {
            throw new SmsMaxBatchSizeExceededException("超过最大发送条数限制");
        }

        for (SmsMessage smsMessage : smsMessages) {
            this.send(smsMessage);
        }
    }

    @Override
    public void send(SmsBatchMessage smsBatchMessage) throws SmsException {
        CommonRequest request = this.creatRequest();
        this.fillSendBatchParams(request, smsBatchMessage);

        this.sendRequest(request, AliyunSendSmsResponse.class);
    }
    
}
