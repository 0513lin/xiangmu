package com.bjpowernode.licai.service;

import com.bjpowernode.licai.model.LoanInfo;
import com.bjpowernode.licai.model.ext.LoanBidInfo;

import java.math.BigDecimal;
import java.util.List;

//投资的Service
public interface BidInfoService {

    //累计投资总金额
    BigDecimal querySumBidMoney();

    //某个产品的最近3条投资记录
    List<LoanBidInfo>  queryRecentlyBidList(Integer loanId);

    //某个用户的所有投资记录
    List<LoanBidInfo> queryUserBidList(Integer uid,Integer pageNo,Integer pageSize);

    //投资
    boolean invest(Integer id, Integer loanId, BigDecimal money);
}
