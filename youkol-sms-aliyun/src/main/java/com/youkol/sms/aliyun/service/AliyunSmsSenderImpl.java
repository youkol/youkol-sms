package com.youkol.sms.aliyun.service;

import java.util.List;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.youkol.sms.aliyun.config.AliyunSmsConfig;
import com.youkol.sms.core.exception.SmsException;
import com.youkol.sms.core.exception.SmsMaxBatchSizeExceededException;
import com.youkol.sms.core.exception.SmsSendException;
import com.youkol.sms.core.model.SmsBatchMessage;
import com.youkol.sms.core.model.SmsMessage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云 短信服务
 * <p>
 * 第三方短信发送接口实现
 * <p>
 * 详见：https://help.aliyun.com/product/44282.html
 * 
 * @author jackiea
 */
@Slf4j
public class AliyunSmsSenderImpl extends AbstractAliyunSmsSender {

    private final IAcsClient client;

    @Getter
    private final AliyunSmsConfig config;

    public AliyunSmsSenderImpl(AliyunSmsConfig config) {
        this.config = config;

        String regionId = config.getServerName();
        String accessKeyId = config.getUsername();
        String accessSecret = config.getPassword();

        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
        this.client = new DefaultAcsClient(profile);
    }

    @Override
    public void send(SmsMessage smsMessage) throws SmsException {
        CommonRequest request = this.creatRequest();
        this.fillParams(request, smsMessage);

        CommonResponse response = null;
        try {
            response = client.getCommonResponse(request);

            log.debug(response.getHttpResponse().toString());
        } catch (ClientException ex) {
            throw new SmsSendException("发送短信发生异常", ex);
        }
    }

    @Override
    public void send(List<SmsMessage> smsMessages) throws SmsException {
        if (smsMessages.size() > config.getBatchMaxCount()) {
            throw new SmsMaxBatchSizeExceededException("超过最大发送条数限制");
        }

        for (SmsMessage smsMessage : smsMessages) {
            this.send(smsMessage);
        }
    }

    @Override
    public void send(SmsBatchMessage smsBatchMessage) throws SmsException {
        CommonRequest request = this.creatRequest();
        this.fillBatchParams(request, smsBatchMessage);

        CommonResponse response = null;
        try {
            response = client.getCommonResponse(request);

            log.debug(response.getHttpResponse().toString());
        } catch (ClientException ex) {
            throw new SmsSendException("发送短信发生异常", ex);
        }
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
