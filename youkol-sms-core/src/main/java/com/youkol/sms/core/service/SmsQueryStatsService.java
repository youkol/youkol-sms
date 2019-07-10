package com.youkol.sms.core.service;

import com.youkol.sms.core.exception.SmsException;

/**
 * 短信查询统计服务接口
 * 
 * @author jackiea
 */
public interface SmsQueryStatsService {

    /**
     * 查询剩余短信数
     * 
     * @return 返回剩余短信数量
     * @throws SmsException 异常信息
     */
    public int queryRemainedCount() throws SmsException;

    /**
     * 查询已发送短信数
     * 
     * @return 返回已发送短信数
     * @throws SmsException 异常信息
     */
    public int querySendedCount() throws SmsException;
    
}
