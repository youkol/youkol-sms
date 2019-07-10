package com.youkol.sms.core.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 短信信息
 * <p>不同手机号内容相同
 * 
 * @author jackiea
 */
@Getter
@Setter
public class SmsMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> phones;

    private String content;

    // 秒值
    private Long sendTime;

    /**
     * 扩展内容，当现有字段不满足时，可使用该字段扩展
     */
    private Map<String, Object> additionalContent;

    /**
     * 短信使用的模板
     * <p>针对某些短信接口不支持直接发送内容，需要使用模板的情况
     * <p>该模板可通过内部的externalId与第三方的模板对应
     * 
     */
    private SmsTemplate template;

    /**
     * 短信模板变量对应的实际值
     */
    private Map<String, String> templateParams;

}
