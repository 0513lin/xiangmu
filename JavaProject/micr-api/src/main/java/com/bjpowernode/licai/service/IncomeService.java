package com.bjpowernode.licai.service;

import com.bjpowernode.licai.model.ext.IncomeInfo;

import java.util.List;

//收益的服务
public interface IncomeService {

    //某个用户的收益记录
    List<IncomeInfo> queryUserIncomeList(Integer uid,Integer pageNo,Integer pageSize);


    //生成满标产品的收益计划。
    void generateIncomePlan();


    //收益返还， 把到期的收益金额 加到用户的账号里
    void generateIncomeBack();
}
