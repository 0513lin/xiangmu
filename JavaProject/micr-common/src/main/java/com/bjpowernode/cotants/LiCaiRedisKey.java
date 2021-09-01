package com.bjpowernode.cotants;

public class LiCaiRedisKey {

    //定义redis的key名称

    //平台注册用户数量
    public static final String LICAI_REGISTER_USERS="LICAI:REIGTER:USERS";
    //累计投资金额
    public static final String LICAI_SUM_BIDMONEY="LICAI:SUM:BIDMONEY";
    //历史年化收益率
    public static final String LICAI_HISTORY_AVGRATE="LICAI:HISTORY:AVGRATE";

    //注册使用短信验证码key   LICAI:REGISTER_SMS:1360000000
    public static final String LICAI_REGISTER_SMS_CODE="LICAI:REGISTER_SMS:";
    //订单号 生成的序列值
    public static final String ALIPY_OUT_TRADE_SEQ = "LICAI:ALIPAY:TRADE:SEQ";


    //充值的订单号ZSet
    public static final String APLIAY_TRADENO_ZSET="LICAI:ALIPAY:TRADE:LIST";

    //投资排行榜
    public static final String LICAI_INVEST_TOPLIST = "LICAI:INVEST:TOPLIST" ;
}
