package com.bjpowernode.web.controller;

import com.bjpowernode.cotants.LicaiContants;
import com.bjpowernode.licai.model.FinanceAccount;
import com.bjpowernode.licai.model.User;
import com.bjpowernode.vo.ResultObject;
import com.bjpowernode.web.service.WebService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@Controller
public class HellpController extends BaseController {

    @Resource
    private WebService webService;

    //检查手机号是否注册过, 处理ajax请求
    @GetMapping("/loan/checkPhone")
    @ResponseBody
    public ResultObject checkPhone(String phone){
        ResultObject ro = ResultObject.fail("手机号已经注册，请更换手机号");
        if( phone != null && phone.length() == 11 ){
            //调用手机号的查询
            User user = userService.queryByPhone(phone);
            if( user == null ){
                ro = ResultObject.success("手机号可以注册");
            }
        }
        return ro;
    }

    //发送短信
    @PostMapping("/send/sms")
    @ResponseBody
    public ResultObject sendSms(String phone){
        ResultObject ro  = ResultObject.fail("短信发送失败，请稍后重试");
        if(phone != null && phone.length() == 11){
            //向手机号发送短信
            boolean isSend = webService.invokeJdwxSendSMSApi(phone);
            if( isSend ){
                ro = ResultObject.success("短信下发成功");
            }
        }
        return ro;
    }

    //查询用户的资金
    @GetMapping("/loan/account")
    @ResponseBody
    public  ResultObject queryAccountMoney(HttpSession session){
        BigDecimal money = new BigDecimal("0");
        User user = (User) session.getAttribute(LicaiContants.LICAI_SESSION_USER);
        FinanceAccount account = financeAccountService.queryAccount(user.getId());
        if( account != null){
            money = account.getAvailableMoney();
        }
        ResultObject ro = new  ResultObject(0,"查询成功",money);
        return ro;
    }
}
