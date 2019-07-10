package com.youkol.sms.aliyun.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.http.MethodType;
import com.google.gson.Gson;
import com.youkol.sms.aliyun.config.AliyunSmsConfig;
import com.youkol.sms.aliyun.model.AliyunSmsBatchMessage;
import com.youkol.sms.aliyun.model.AliyunSmsMessage;
import com.youkol.sms.aliyun.util.AliyunSmsConstants;
import com.youkol.sms.aliyun.util.AliyunSmsConstants.SmsAction;
import com.youkol.sms.core.model.SmsBatchMessage;
import com.youkol.sms.core.model.SmsMessage;
import com.youkol.sms.core.model.SmsTemplate;
import com.youkol.sms.core.service.SmsSender;
import com.youkol.sms.core.util.SmsUtil;

/**
 * 
 * @author jackiea
 */
public abstract class AbstractAliyunSmsSender implements SmsSender {

    protected CommonRequest creatRequest(SmsAction action) {
        AliyunSmsConfig config = (AliyunSmsConfig)this.getConfig();

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

    protected void fillParams(CommonRequest request, SmsMessage message) {
        AliyunSmsConfig config = (AliyunSmsConfig)this.getConfig();
        AliyunSmsMessage aliMessage = (AliyunSmsMessage)message;

        request.setSysAction(SmsAction.SendSms.name());

        request.putQueryParameter("RegionId", config.getServerName());
        request.putQueryParameter("SignName", config.getSignName());

        String phoneNumbers = SmsUtil.join(AliyunSmsConstants.PHONE_SEPARATOR, aliMessage.getPhones());
        request.putQueryParameter("PhoneNumbers", phoneNumbers);

        SmsTemplate template = aliMessage.getTemplate();
        request.putQueryParameter("TemplateCode", template.getExternalId());

        Map<String, String> templateParams = aliMessage.getTemplateParams();
        if (templateParams != null && !templateParams.isEmpty()) {
            Gson gson = new Gson();
            String params = gson.toJson(templateParams); 
            request.putQueryParameter("TemplateParam", params);
        }

        request.putQueryParameter("SmsUpExtendCode", aliMessage.getSmsUpExtendCode());
    }

    protected void fillBatchParams(CommonRequest request, SmsBatchMessage message) {
        AliyunSmsConfig config = (AliyunSmsConfig)this.getConfig();
        AliyunSmsBatchMessage aliMessage = (AliyunSmsBatchMessage)message;

        request.setSysAction(SmsAction.SendBatchSms.name());

        request.putQueryParameter("RegionId", config.getServerName());

        Gson gson = new Gson();

        List<String> phones = aliMessage.getPhones();
        String phoneNumberJson = gson.toJson(phones);
        request.putQueryParameter("PhoneNumberJson", phoneNumberJson);

        List<String> signNames = new ArrayList<>();
        phones.forEach(t -> signNames.add(config.getSignName()));
        String signNameJson = gson.toJson(signNames);
        request.putQueryParameter("SignNameJson", signNameJson);

        SmsTemplate template = aliMessage.getTemplate();
        request.putQueryParameter("TemplateCode", template.getExternalId());

        List<Map<String, String>> templateParams = aliMessage.getTemplateParams();
        if (templateParams != null && !templateParams.isEmpty()) {
            String params = gson.toJson(templateParams); 
            request.putQueryParameter("TemplateParamJson", params);
        }

        List<String> smsUpExtendCodes = aliMessage.getSmsUpExtendCodes();
        if (smsUpExtendCodes != null && !smsUpExtendCodes.isEmpty()) {
            String params = gson.toJson(smsUpExtendCodes); 
            request.putQueryParameter("SmsUpExtendCodeJson", params);
        }
    }
}
