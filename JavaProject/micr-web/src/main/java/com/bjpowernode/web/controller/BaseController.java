package com.bjpowernode.web.controller;

import com.bjpowernode.licai.service.*;
import org.apache.dubbo.config.annotation.DubboReference;

//把公用内容放到BaseController
public class BaseController {

    //引用远程对象
    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    protected UserService userService;

    //引用远程对象
    @DubboReference(interfaceClass = LoanInfoService.class,version = "1.0")
    protected LoanInfoService loanInfoService;


    //引用远程的 投资Service
    @DubboReference(interfaceClass = BidInfoService.class,version = "1.0")
    protected BidInfoService bidInfoService;

    //资金服务Service
    @DubboReference(interfaceClass = FinanceAccountService.class,version = "1.0")
    protected FinanceAccountService financeAccountService;

    //充值服务Service
    @DubboReference(interfaceClass = RechargeService.class,version = "1.0")
    protected RechargeService rechargeService;


    //收益服务
    @DubboReference(interfaceClass = IncomeService.class,version = "1.0")
    protected IncomeService incomeService;



}
