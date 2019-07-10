package com.youkol.sms.core.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 短信内容模板
 * 
 * @author jackiea
 */
@Getter
@Setter
public class SmsTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板编号
     */
    private String id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板说明
     */
    private String templateDesc;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 外部编号，可与第三方短信模板关联
     */
    private String externalId;
}
