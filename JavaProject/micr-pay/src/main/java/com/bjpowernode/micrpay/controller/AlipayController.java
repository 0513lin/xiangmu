package com.bjpowernode.micrpay.controller;

import com.bjpowernode.micrpay.service.AlipayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class AlipayController {


    @Resource
    private AlipayService alipayService;

    //接收micr-web服务传递过来的请求
    @RequestMapping("/alipay/pay")
    @ResponseBody
    public String receiveMicrWebAlipay(@RequestParam("money") BigDecimal money,
                                       @RequestParam("uid") Integer uid){
        String form ="";
        try{
            form = alipayService.alipayTradePagePay(uid ,money);
        }catch (Exception e){
            e.printStackTrace();
        }
        return form;
    }

    //接收支付宝的notifyUrl
    @PostMapping("/alipay/notify")
    public void receiveAlipayNotifyUrl(HttpServletRequest request, HttpServletResponse response){
        System.out.println("======receiveAlipayNotifyUrl======");
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();

        int result = 0;
        try{
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            }
            //params.put(name, valueStr); 就是请求的参数集合， 支付宝发送给商家的所有参数
            System.out.println("======receiveAlipayNotifyUrl  params======"+params);
            result = alipayService.alipayNotify(params);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //输出应答支付宝的success
            try {
                PrintWriter out = response.getWriter();
                out.println("success");
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    //提供给micr-task使用的 查询接口
    @GetMapping("/alipay/query")
    @ResponseBody
    public String alipayQuery(){
        alipayService.alipayQuery();
        return "调用查询接口成功";
    }
}
