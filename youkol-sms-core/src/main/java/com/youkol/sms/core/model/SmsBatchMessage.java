package com.youkol.sms.core.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 批量短信
 * 
 * @author jackiea
 */
@Getter
@Setter
public class SmsBatchMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> phone;

    private String content;

    // 秒值
    private Long sendTime;

    private Map<String, String> additionalContent;

}