package com.bjpowernode.web.controller;

import com.bjpowernode.cotants.LicaiContants;
import com.bjpowernode.licai.model.FinanceAccount;
import com.bjpowernode.licai.model.RechargeRecord;
import com.bjpowernode.licai.model.User;
import com.bjpowernode.licai.model.ext.IncomeInfo;
import com.bjpowernode.licai.model.ext.LoanBidInfo;
import com.bjpowernode.vo.ResultObject;
import com.bjpowernode.web.service.WebService;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import javassist.tools.web.Webserver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class UserController extends BaseController {

    @Resource
    private WebService webService;
    //注册页面
    @GetMapping("/loan/page/register")
    public String pageRegister(){
        return "register";
    }


    //实名认证的页面
    @GetMapping("/loan/page/realName")
    public String pageRealName(Model model,HttpSession session){
        //实名认证时，有session用户的
        User user  = (User) session.getAttribute(LicaiContants.LICAI_SESSION_USER);
        model.addAttribute("phone",user.getPhone());
        return "realName";
    }

    //用户中心
    @GetMapping("/loan/page/myCenter")
    public String pageMyCenter(Model model,HttpSession session){
        BigDecimal money = new BigDecimal("0");
        User user = (User) session.getAttribute(LicaiContants.LICAI_SESSION_USER);

        //查询金额
        FinanceAccount account = financeAccountService.queryAccount(user.getId());
        if( account != null){
            money = account.getAvailableMoney();
        }

        //获取用户的投资记录
        List<LoanBidInfo> loanBidInfoList = bidInfoService.queryUserBidList(user.getId(),0,5);
        model.addAttribute("bidInfoList",loanBidInfoList);

        //充值记录
        List<RechargeRecord> rechargeList = rechargeService.queryUserRechargePage(user.getId(),0,5);
        model.addAttribute("rechargeList",rechargeList);

        //收益
        List<IncomeInfo> incomeInfoList = incomeService.queryUserIncomeList(user.getId(),0,5);
        model.addAttribute("incomeList",incomeInfoList);


        model.addAttribute("accountMoney",money);
        return "myCenter";
    }

    //登录页面
    @GetMapping("/loan/page/login")
    public String pageLogin(@RequestParam(value = "returnUrl",
            required = false,
            defaultValue = "http://localhost:8001/licai/index") String returnUrl,
                            Model model){
        //注册用户数量
        int countUsers = userService.registerUserCount();
        //累计成交金额
        BigDecimal sumBidMoney = bidInfoService.querySumBidMoney();

        model.addAttribute("countUsers",countUsers);
        model.addAttribute("sumBidMoney",sumBidMoney);

        //返回的地址
        model.addAttribute("returnUrl",returnUrl);
        return "login";
    }


    //处理登录的业务逻辑
    @PostMapping("/loan/login")
    @ResponseBody
    public ResultObject login(@RequestParam("phone") String phone,
                              @RequestParam("pwd") String password,
                              HttpSession session){
        ResultObject ro  = ResultObject.fail("登录失败");
        if( phone == null || phone.length() != 11 ){
            ro.setMsg("手机号不正确");
        } else if( password == null || password.equals("") || password.length() !=32){
            ro.setMsg("请求的数据有误");
        } else {
            //登录处理
            User user = userService.login(phone,password);
            if(user != null){
                //登录成功，把user放到session
                session.setAttribute(LicaiContants.LICAI_SESSION_USER,user);
                //创建ro对象
                ro = ResultObject.success("登录成功");
            }
        }
        return ro;
    }

    //退出系统
    @GetMapping("/loan/logout")
    public String logout( HttpSession session){
        //1.session无效
        session.invalidate();
        //2.回到首页
        return "redirect:/index";

    }




    //处理注册逻辑
    @PostMapping("/loan/register")
    @ResponseBody
    public ResultObject register(@RequestParam("phone") String phone,
                                 @RequestParam("pwd") String pwd,
                                 @RequestParam("code") String code,
                                 HttpSession session){
        ResultObject ro  = ResultObject.fail("注册失败");
        if(phone == null || phone.length() !=11){
            ro.setMsg("手机号不正确");
        } else if( pwd == null || "".equals(pwd) || pwd.length() != 32 ){
            ro.setMsg("请求数据不正确");
        } else if( !webService.compCode(phone,code)){
            ro.setMsg("验证码不正确");
        } else {
            //注册用户
            User user = userService.register(phone,pwd);
            if( user != null){
                if( 1 == user.getCategory().intValue() ){
                    ro = ResultObject.success("注册成功");
                    //删除redis的中验证的key
                    webService.removeSmsKey(phone);
                    //把注册的用户放到session， 后面的实名认证需要使用session中的数据
                    session.setAttribute(LicaiContants.LICAI_SESSION_USER,user);
                }
            }
        }
        return ro;
    }


    //处理实名认证
    // @PostMapping("/loan/realName")
    @RequestMapping("/loan/realName")
    @ResponseBody
    public ResultObject realName(@RequestParam("name") String name,
                                 @RequestParam("card") String card,
                                 HttpSession session){
        ResultObject ro = ResultObject.fail("认证失败");

        if( name !=null && !name.equals("") &&
                card != null && !card.equals("")){
            //实名认证
            boolean result = webService.invokeJdwxRealName(name,card);
            //认证通过，修改用户的信息
            if(result ){
                User user = (User) session.getAttribute(LicaiContants.LICAI_SESSION_USER);
                int rows = userService.modifyUser(user.getId(),name,card);
                if(rows == 1){
                    ro = ResultObject.success("认证成功");
                }
            }
        }
        return ro;

    }
}
