package com.bjpowernode.web.controller;

import com.bjpowernode.cotants.LiCaiRedisKey;
import com.bjpowernode.cotants.LicaiContants;
import com.bjpowernode.licai.model.FinanceAccount;
import com.bjpowernode.licai.model.LoanInfo;
import com.bjpowernode.licai.model.User;
import com.bjpowernode.licai.model.ext.LoanBidInfo;
import com.bjpowernode.util.CommonUtil;
import com.bjpowernode.vo.PageInfo;
import com.bjpowernode.vo.ResultObject;
import com.bjpowernode.vo.TopBean;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TopLevelAttribute;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class LoanController extends BaseController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //分页显示产品
    @GetMapping("/loan/loan")
    public String loan(@RequestParam("ptype") Integer productType,
                       @RequestParam(value = "pageNo",required = false,defaultValue = "1") Integer pageNo,
                       @RequestParam(value = "pageSize",required = false,defaultValue = "9") Integer pageSize,
                       Model model){

        List<LoanInfo> loanInfoList = new ArrayList<>();
        PageInfo pageInfo = new PageInfo();
        List<TopBean> topList = new ArrayList<>();
        //检查请求参数
        if(CommonUtil.checkProductType(productType)){
            //查询产品
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            //分页查询产品
            loanInfoList = loanInfoService.queryLoanInfoByTypePage(productType,pageNo,pageSize);

            //获取总记录数量
            int records = loanInfoService.staticRecordsProductType(productType);

            //创建PageInfo
            pageInfo = new PageInfo(pageNo,pageSize,records);

            //@TODO 后面做的 投资排行榜, 从Redis的ZSet类型中获取  数据 ， value 和 score

            Set<ZSetOperations.TypedTuple<String>> typedTuples =
                    stringRedisTemplate.opsForZSet().reverseRangeWithScores(LiCaiRedisKey.LICAI_INVEST_TOPLIST, 0, 4);


            typedTuples.forEach( t->{
                TopBean top  = new TopBean(t.getValue(),t.getScore());
                topList.add(top);

            });


        }
        model.addAttribute("topList", topList);
        model.addAttribute("ptype",productType);
        model.addAttribute("loanInfoList",loanInfoList);
        model.addAttribute("pageInfo",pageInfo);
        return "loan";
    }


    //某个产品的详情
    @GetMapping("/loan/loanInfo")
    public String loanInfo(@RequestParam("id") Integer loanId,
                           Model model,
                           HttpSession session){

        LoanInfo loanInfo = null;
        List<LoanBidInfo> loanBidInfoList  = new ArrayList<>();
        //需要做参数检查
        if( loanId != null &&  loanId > 0 ){
            //查询产品
            loanInfo = loanInfoService.queryById(loanId);
            //产品的投资记录
            loanBidInfoList = bidInfoService.queryRecentlyBidList(loanId);

            //查询资金余额
            User user  = (User) session.getAttribute(LicaiContants.LICAI_SESSION_USER);
            if( user != null){
                FinanceAccount account = financeAccountService.queryAccount(user.getId());
                if( account != null){
                    model.addAttribute("accountMoney", account.getAvailableMoney());
                }
            }


        }
        model.addAttribute("loanInfo",loanInfo);
        model.addAttribute("loanBidList",loanBidInfoList);
        return "loanInfo";
    }


    // 投资购买处产品
    @PostMapping("/loan/invest")
    @ResponseBody
    public ResultObject  invest(@RequestParam("money") BigDecimal money,
                                @RequestParam("loanId") Integer loanId,
                                HttpSession session){
        ResultObject ro  = ResultObject.fail("投资失败");
        //对money简单处理
        if( money == null && money.intValue() < 100 ){
            ro.setMsg("金额必须有效的数字");
        } else if( money.intValue() % 100 !=0 ){
            ro.setMsg("金额是100的整数倍");
        } else if( loanId == null || loanId < 1 ){
            ro.setMsg("购买产品不存在");
        } else {
            //调用dataservice中的方法，处理投资的所有业务逻辑
            User user = (User)session.getAttribute(LicaiContants.LICAI_SESSION_USER);
            if( bidInfoService.invest(user.getId(),loanId,money) ){
                ro = ResultObject.success("投资成功");

                //创建投资排行榜
                stringRedisTemplate.opsForZSet().incrementScore(
                        LiCaiRedisKey.LICAI_INVEST_TOPLIST,
                        user.getPhone(),money.doubleValue());

            }
        }
        return ro;
    }


}
