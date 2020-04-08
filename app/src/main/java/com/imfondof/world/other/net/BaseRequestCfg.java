package com.imfondof.world.other.net;

/**
 * Imfondof on 2020/4/8
 */
public class BaseRequestCfg {
    public transient long startTime;//接口开始请求
    public transient long endTime;//接口结束
    public transient String targetUrl;//请求接口地址
    public transient int connectOutTime = 30;//请求超时时间设置
    public boolean needAES;
}
