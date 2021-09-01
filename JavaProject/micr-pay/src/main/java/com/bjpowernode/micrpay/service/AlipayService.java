package com.bjpowernode.micrpay.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.bjpowernode.cotants.LiCaiRedisKey;
import com.bjpowernode.cotants.LicaiContants;
import com.bjpowernode.licai.model.RechargeRecord;
import com.bjpowernode.licai.service.RechargeService;
import com.bjpowernode.micrpay.config.AlipayConfig;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service
public class AlipayService {


    @Resource
    private AlipayConfig alipayConfig;

    @DubboReference(interfaceClass = RechargeService.class,version = "1.0")
    private RechargeService rechargeService;
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    //向支付宝发起下单请求
    public String alipayTradePagePay(Integer uid,BigDecimal money) {
        //生成商户的唯一订单号
        String generateOutTradeNo = outTradeNo();
        //创建一个充值记录
        RechargeRecord rr = new RechargeRecord();
        rr.setChannel("alipay");
        rr.setRechargeDesc("支付宝充值");
        rr.setRechargeMoney(money);
        rr.setRechargeNo(generateOutTradeNo);
        rr.setRechargeStatus(LicaiContants.RECAHGE_STATUS_PROCESSING);
        rr.setRechargeTime(new Date());
        rr.setUid(uid);
        int rows = rechargeService.addRecharge(rr);

        //请求
        String result = "";
        if( rows > 0 ){
            //获得初始化的AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(
                    alipayConfig.getGatewayUrl(),
                    alipayConfig.getApp_id(),
                    alipayConfig.getMerchant_private_key(),
                    "json",
                    alipayConfig.getCharset(),
                    alipayConfig.getAlipay_public_key(),
                    alipayConfig.getSign_type());

            //设置请求参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(alipayConfig.getReturn_url());
            alipayRequest.setNotifyUrl(alipayConfig.getNotify_url());

            //商户订单号，商户网站订单系统中唯一订单号，必填
            String out_trade_no = generateOutTradeNo;
            //付款金额，必填
            String total_amount = money.toPlainString();
            //订单名称，必填
            String subject = "bjpowernode";
            //商品描述，可空
            String body = "licai";

            alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                    + "\"total_amount\":\"" + total_amount + "\","
                    + "\"subject\":\"" + subject + "\","
                    + "\"body\":\"" + body + "\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

            try {
                result = alipayClient.pageExecute(alipayRequest).getBody();

                //把订单号和时间存放到redis zset类型
                stringRedisTemplate.opsForZSet().add(LiCaiRedisKey.APLIAY_TRADENO_ZSET,generateOutTradeNo,rr.getRechargeTime().getTime() );


            } catch (AlipayApiException e) {
                result = "";
                e.printStackTrace();
            }
        }
        System.out.println("商家向支付宝发起下单请求， 支付宝返回的结果：" + result);
        return result;
    }




    //处理支付宝的notify
    public int alipayNotify(Map<String,String> params) throws AlipayApiException {

        int result  = 0;
        //验签
        boolean signVerified = AlipaySignature.rsaCheckV1(params,
                alipayConfig.getAlipay_public_key(),//获取公钥
                alipayConfig.getCharset(),//字符编码格式
                alipayConfig.getSign_type()); //调用SDK验证签名签名方式

        if(signVerified) {//验证成功
            //商户订单号
            String out_trade_no = params.get("out_trade_no");
            //支付宝交易号
            String trade_no = params.get("trade_no");
            //交易状态
            String trade_status = params.get("trade_status");
            //支付宝传递过来的金额
            String total_amount = params.get("total_amount");

            //获取app_id
            String app_id =params.get("app_id");
            //验证app_id
            if( alipayConfig.getApp_id().equals(app_id) ){
                //处理商家自己的订单
                result = rechargeService.doWithAlipayNotify(out_trade_no,trade_status,total_amount);
                if( result == 1){
                    //从redis删除已经处理订单
                    stringRedisTemplate.opsForZSet().remove(LiCaiRedisKey.APLIAY_TRADENO_ZSET,out_trade_no);
                }
            }
        }else {//验证失败
           result = 6; //验证失败
        }
        return result;
    }


    //处理查询订单
    public void alipayQuery(){
        //1. 获取要 查询的订单号， 从redis获取
        //rangeWithScores按照score的默认排序（小->大）,获取所有的value和score
        //value=订单号 score=订单的产生时间
        Set<ZSetOperations.TypedTuple<String>> sets = stringRedisTemplate.opsForZSet().rangeWithScores(LiCaiRedisKey.APLIAY_TRADENO_ZSET,0,-1);
        sets.forEach( s->{
            String tradeNo = s.getValue();
            long time =  new BigDecimal(s.getScore()).longValue();
            //和当前时间比，超过10分钟以上 的充值订单

            //获取 10分钟之前的时间， 和这个时间比较
            Date before10MinuteTime = DateUtils.addMinutes( new Date(), -10);
            if(  time <= before10MinuteTime.getTime() ){     //10分钟之前订单， 调用支付的接口
                //2. 每个订单调用 支付宝的查询接口
                invokeAlipayQueryApi(tradeNo);
            }

        });
    }


    //调用支付宝的查询接口
    public void invokeAlipayQueryApi(String tradeNo){
         //获得初始化的AlipayClient
         AlipayClient alipayClient = new DefaultAlipayClient(
                alipayConfig.getGatewayUrl(),
                alipayConfig.getApp_id(),
                alipayConfig.getMerchant_private_key(),
                "json",
                alipayConfig.getCharset(),
                alipayConfig.getAlipay_public_key(),
                alipayConfig.getSign_type());

        //设置请求参数
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = tradeNo;
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"}");

        String sss = "{\"out_trade_no\":\"" + out_trade_no + "\"}";
        System.out.println("sss=="+sss);
        //请求
        try {
            String result = "";
            AlipayTradeQueryResponse response = alipayClient.execute(alipayRequest);
            if( response.isSuccess()){
                result  = response.getBody();
                JSONObject obj = JSONObject.parseObject(result).getJSONObject("alipay_trade_query_response");
                if(obj !=null){
                    String param_out_trade_no = obj.getString("out_trade_no");
                    String total_amount = obj.getString("total_amount");
                    String trade_status = obj.getString("trade_status");
                    int doResult =  rechargeService.doWithAlipayNotify(param_out_trade_no,trade_status,total_amount);
                    if( doResult == 1 ){
                        stringRedisTemplate.opsForZSet().remove(LiCaiRedisKey.APLIAY_TRADENO_ZSET,out_trade_no);
                    }
                }
            } else {
                //把调用失败的订单号，记录到数据库或者日志文件。让客户人员处理。 发邮件给客户人员
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    //创建商户的订单号
    private String outTradeNo(){
        //时间序列
        String dateString = DateFormatUtils.format(new Date(),"yyyyMMddHHmmssSSS")
                + stringRedisTemplate.opsForValue().increment(LiCaiRedisKey.ALIPY_OUT_TRADE_SEQ);
        return dateString;

    }
}
