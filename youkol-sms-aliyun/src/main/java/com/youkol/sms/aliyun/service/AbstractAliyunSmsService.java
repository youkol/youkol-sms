package com.youkol.sms.aliyun.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.youkol.sms.aliyun.config.AliyunSmsConfig;
import com.youkol.sms.aliyun.model.AliyunSmsBatchMessage;
import com.youkol.sms.aliyun.model.AliyunSmsMessage;
import com.youkol.sms.aliyun.service.response.AliyunBaseResponse;
import com.youkol.sms.aliyun.util.AliyunSmsConstants;
import com.youkol.sms.aliyun.util.AliyunSmsConstants.SmsAction;
import com.youkol.sms.core.config.SmsConfig;
import com.youkol.sms.core.exception.SmsSendException;
import com.youkol.sms.core.model.SmsBatchMessage;
import com.youkol.sms.core.model.SmsMessage;
import com.youkol.sms.core.model.SmsTemplate;
import com.youkol.sms.core.service.AbstractSmsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jackiea
 */
@Slf4j
public class AbstractAliyunSmsService extends AbstractSmsService {

    private final IAcsClient client;

    private final Gson gson = new Gson();

    public AbstractAliyunSmsService(SmsConfig config) {
        super(config);

        String regionId = config.getServerName();
        String accessKeyId = config.getUsername();
        String accessSecret = config.getPassword();

        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
        this.client = new DefaultAcsClient(profile);
    }

    protected String toJson(Object src) {
        return gson.toJson(src);
    }

    protected CommonRequest creatRequest(SmsAction action) {
        AliyunSmsConfig config = (AliyunSmsConfig) this.getConfig();

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(config.getServerHost());
        request.setSysVersion(config.getVersion());
        if (action != null) {
            request.setSysAction(action.name());
        }

        return request;
    }

    protected CommonRequest creatRequest() {
        return creatRequest(null);
    }

    protected <T extends AliyunBaseResponse> T sendRequest(CommonRequest request, Class<T> resultType) throws SmsSendException {
        try {
            CommonResponse response = client.getCommonResponse(request);
            
            log.debug(response.getHttpResponse().toString());

            if (!response.getHttpResponse().isSuccess()) {
                throw new SmsSendException("发送短信发生异常, 错误状态码：" + response.getHttpStatus());
            }

            T result = gson.fromJson(response.getData(), resultType);
            return result;
        } catch (ClientException ex) {
            throw new SmsSendException("发送短信发生异常", ex);
        }
    }

    protected void fillSendParams(CommonRequest request, SmsMessage message) {
        AliyunSmsConfig config = (AliyunSmsConfig)this.getConfig();
        AliyunSmsMessage aliMessage = (AliyunSmsMessage)message;

        request.setSysAction(SmsAction.SendSms.name());

        request.putQueryParameter("RegionId", config.getServerName());
        request.putQueryParameter("SignName", config.getSignName());

        String phoneNumbers = this.join(AliyunSmsConstants.PHONE_SEPARATOR, aliMessage.getPhones());
        request.putQueryParameter("PhoneNumbers", phoneNumbers);

        SmsTemplate template = aliMessage.getTemplate();
        request.putQueryParameter("TemplateCode", template.getExternalId());

        Map<String, String> templateParams = aliMessage.getTemplateParams();
        if (templateParams != null && !templateParams.isEmpty()) {
            String params = this.toJson(templateParams); 
            request.putQueryParameter("TemplateParam", params);
        }

        request.putQueryParameter("SmsUpExtendCode", aliMessage.getSmsUpExtendCode());
    }

    protected void fillSendBatchParams(CommonRequest request, SmsBatchMessage message) {
        AliyunSmsConfig config = (AliyunSmsConfig)this.getConfig();
        AliyunSmsBatchMessage aliMessage = (AliyunSmsBatchMessage)message;

        request.setSysAction(SmsAction.SendBatchSms.name());

        request.putQueryParameter("RegionId", config.getServerName());

        List<String> phones = aliMessage.getPhones();
        String phoneNumberJson = this.toJson(phones);
        request.putQueryParameter("PhoneNumberJson", phoneNumberJson);

        List<String> signNames = new ArrayList<>();
        phones.forEach(t -> signNames.add(config.getSignName()));
        String signNameJson = this.toJson(signNames);
        request.putQueryParameter("SignNameJson", signNameJson);

        SmsTemplate template = aliMessage.getTemplate();
        request.putQueryParameter("TemplateCode", template.getExternalId());

        List<Map<String, String>> templateParams = aliMessage.getTemplateParams();
        if (templateParams != null && !templateParams.isEmpty()) {
            String params = this.toJson(templateParams); 
            request.putQueryParameter("TemplateParamJson", params);
        }

        List<String> smsUpExtendCodes = aliMessage.getSmsUpExtendCodes();
        if (smsUpExtendCodes != null && !smsUpExtendCodes.isEmpty()) {
            String params = this.toJson(smsUpExtendCodes); 
            request.putQueryParameter("SmsUpExtendCodeJson", params);
        }
    }
    
}
