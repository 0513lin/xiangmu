package com.bjpowernode.licai.service;

import com.bjpowernode.licai.model.RechargeRecord;

import java.util.List;

//充值的服务
public interface RechargeService {

    //分页查询用户的充值记录
    List<RechargeRecord> queryUserRechargePage(Integer uid,Integer pageNo,Integer pageSize);

    //创建充值记录
    int addRecharge(RechargeRecord rr);


    //支付宝notify
    int doWithAlipayNotify(String outTradeNo,String tradeStatus, String totalAmount);
}
