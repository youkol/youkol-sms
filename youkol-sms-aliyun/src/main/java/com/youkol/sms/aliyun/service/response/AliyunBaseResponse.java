package com.youkol.sms.aliyun.service.response;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * AliyunBaseResponse
 */
@Getter
@Setter
@ToString
public class AliyunBaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("RequestId")
    private String requestId;

    @SerializedName("Code")
    private String code;

    @SerializedName("Message")
    private String message;
    
}
