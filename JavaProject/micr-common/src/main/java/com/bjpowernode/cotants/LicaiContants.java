package com.bjpowernode.cotants;

public class LicaiContants {

    //新手宝产品类型
    public static final int LOAN_PRODUCT_TYPE_XIN=0;
    //优选产品类型
    public static final int LOAN_PRODUCT_TYPE_YOU=1;
    //散标产品类型
    public static final int LOAN_PRODUCT_TYPE_SAN=2;

    //产品的状态 未满标
    public static final int LOAN_STATUS_NOT_MANBIAO = 0;
    //满标
    public static final int LOAN_STATUS_MANBIAO = 1;
    //满标生成收益计划
    public static final int LOAN_STATUS_MANBIAO_PLAN =2;


    //收益表状态 生成收益计划， 未返还
    public static final int INCOME_STATUS_NOT_BACK= 0;
    //已返还
    public static final int INCOME_STATUS_BACK= 1;

    //充值表的 recharge_status
    //充值中
    public static final int RECAHGE_STATUS_PROCESSING=0;
    //充值成功
    public static final int RECAHGE_STATUS_SUCCESS=1;
    //充值失败
    public static final int RECAHGE_STATUS_FAIL=2;

    //session中的user
    public static final String LICAI_SESSION_USER = "licaisessionuser";
}
