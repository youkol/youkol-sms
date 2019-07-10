package com.youkol.sms.aliyun.model;

import java.util.List;

import com.youkol.sms.core.model.SmsBatchMessage;

import lombok.Getter;
import lombok.Setter;

/**
 * @author jackiea
 */
@Getter
@Setter
public class AliyunSmsBatchMessage extends SmsBatchMessage {

    private static final long serialVersionUID = 1L;

    /**
     * 上行短信扩展码
     */
    private List<String> smsUpExtendCodes;
}
