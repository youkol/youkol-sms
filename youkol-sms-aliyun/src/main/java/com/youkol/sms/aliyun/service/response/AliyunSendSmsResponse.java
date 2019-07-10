package com.youkol.sms.aliyun.service.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jackiea
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AliyunSendSmsResponse extends AliyunBaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String BizId;
}
