package com.youkol.sms.jianzhou.service;

import com.google.common.base.Strings;
import com.jianzhou.sdk.BusinessService;
import com.youkol.sms.core.config.SmsConfig;
import com.youkol.sms.core.service.AbstractSmsService;

import lombok.Getter;

/**
 * @author
 */
public abstract class AbstractShJianZhouSmsService extends AbstractSmsService {

    public static final String PERSONAL_SEPARATOR = "||";
    public static final String BATCH_SEPARATOR = ";";
    public static final String TIME_PATTERN = "yyyyMMddHHmmss";

    @Getter
    private final BusinessService jianZhouService = new BusinessService();

    public AbstractShJianZhouSmsService(SmsConfig config) {
        super(config);

        if (!Strings.isNullOrEmpty(config.getServerHost())) {
            jianZhouService.setWebService(config.getServerHost());
        }
    }
}
