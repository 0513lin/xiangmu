package com.bjpowernode.web.controller;

import com.bjpowernode.cotants.LicaiContants;
import com.bjpowernode.licai.model.User;
import com.bjpowernode.util.HttpClientUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RechargeController {

    //进入充值页面
    @GetMapping("/loan/page/recharge")
    public String toRechagePage(){

        return "toRecharge";
    }


    //接收用户要使用支付宝的充值请求
    @PostMapping("/loan/recharge/alipay")
    public void receiveAlipayRecharge(@RequestParam("rechargeMoney") BigDecimal rechargeMoney,
                                      HttpSession session,
                                      HttpServletResponse response){
        System.out.println("接收的金额："+rechargeMoney);

        User user = (User) session.getAttribute(LicaiContants.LICAI_SESSION_USER);
        //使用HttpClient访问 micr-pay服务的controller地址
        String url="http://localhost:8005/pay/alipay/pay";
        Map<String,Object> params = new HashMap<>();
        params.put("money",rechargeMoney);
        params.put("uid",user.getId());

        try {
            // form
            String result  = HttpClientUtils.doPost(url, params);
            System.out.println("pay服务返回的结果："+result);

            PrintWriter out = response.getWriter();
            out.println("<html>"+result+"</html>");
            out.flush();
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
