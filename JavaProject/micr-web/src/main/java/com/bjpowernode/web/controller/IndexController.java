package com.bjpowernode.web.controller;

import com.bjpowernode.cotants.LicaiContants;
import com.bjpowernode.licai.model.LoanInfo;
import com.bjpowernode.licai.service.LoanInfoService;
import com.bjpowernode.licai.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * 首页Controller
 */
@Controller
public class IndexController extends BaseController {

    //访问首页面 index.html
    @RequestMapping({"/index","/"})
    public String index(Model model){

        //调用远程对象的方法，获取数据
        int registerCountUser =  userService.registerUserCount();
        model.addAttribute("registerCountUser",registerCountUser);

        //调用远程对象， 获取 收益率平均值
        BigDecimal historyAvgRate = loanInfoService.queryHistoryAvgRate();
        model.addAttribute("historyAvgRate",historyAvgRate);

        //累计投资金额
        BigDecimal sumBidMoney = bidInfoService.querySumBidMoney();
        model.addAttribute("sumBidMoney",sumBidMoney);

        //获取新手宝产品 一个
        List<LoanInfo> xinLoanInfoList  = loanInfoService.queryLoanInfoByTypePage(
                                           LicaiContants.LOAN_PRODUCT_TYPE_XIN,1,1);
        model.addAttribute("xinInfoList",xinLoanInfoList);

        //优选的 4 个产品
        List<LoanInfo> youLoanInfoList = loanInfoService.queryLoanInfoByTypePage(
                                           LicaiContants.LOAN_PRODUCT_TYPE_YOU,1,4 );
        model.addAttribute("youInfoList",youLoanInfoList);
        //散标 8 个产品
        List<LoanInfo> sanLoanInfoList = loanInfoService.queryLoanInfoByTypePage(
                                          LicaiContants.LOAN_PRODUCT_TYPE_SAN,1,8 );
        model.addAttribute("sanInfoList",sanLoanInfoList);


        //逻辑视图
        return "index";
    }
}
