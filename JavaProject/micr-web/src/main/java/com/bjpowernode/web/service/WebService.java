package com.bjpowernode.web.service;


import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.cotants.LiCaiRedisKey;
import com.bjpowernode.util.CommonUtil;
import com.bjpowernode.web.config.JdwxRealNameConfig;
import com.bjpowernode.web.config.JdwxSmsConfig;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class WebService {

    @Resource
    private JdwxSmsConfig smsConfig;

    @Resource
    private JdwxRealNameConfig realNameConfig;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //调用第三方接口，做实名认证
    public boolean invokeJdwxRealName(String name,String card){
        boolean result = false;

        //使用HttpClient访问 二要素的实名认证接口
        CloseableHttpClient client = HttpClients.createDefault();
        //https://way.jd.com/youhuoBeijing/test?cardNo=150429198407091210&realName=乐天磊&appkey=您申请的APPKEY
        String uri = realNameConfig.getUrl()+"?cardNo="+card+
                   "&realName="+name+"&appkey="+realNameConfig.getAppkey();

        HttpGet get = new HttpGet(uri);
        try {
            CloseableHttpResponse response = client.execute(get);
            if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                //String json  = EntityUtils.toString(response.getEntity());

                String json="{\n" +
                        "    \"code\": \"10000\",\n" +
                        "    \"charge\": false,\n" +
                        "    \"remain\": 1305,\n" +
                        "    \"msg\": \"查询成功\",\n" +
                        "    \"result\": {\n" +
                        "        \"error_code\": 0,\n" +
                        "        \"reason\": \"成功\",\n" +
                        "        \"result\": {\n" +
                        "            \"realname\": \"乐天磊\",\n" +
                        "            \"idcard\": \"350721197702134399\",\n" +
                        "            \"isok\": true" +
                        "        }\n" +
                        "    }\n" +
                        "}";

                if( json != null && !json.equals("")){
                    JSONObject obj = JSONObject.parseObject(json);
                    if("10000".equals(obj.getString("code"))){
                        JSONObject obj2 = obj.getJSONObject("result");
                        if(obj2 != null){
                            JSONObject obj3 = obj2.getJSONObject("result");
                            if( obj3 != null){
                                result = obj3.getBoolean("isok");
                            }
                        }
                    }
                }

            }
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }



    //调用第三方接口，发送短信
    public boolean invokeJdwxSendSMSApi(String phone){

        boolean isSend = false;
        //调用京东万象的短信发送接口
        CloseableHttpClient client = HttpClients.createDefault();

        String smsCode =CommonUtil.random(4);
        String content = String.format(smsConfig.getContent(),smsCode );
        String uri = smsConfig.getUrl()+"?mobile="+phone+"&content="+content+"&appkey="+smsConfig.getAppkey();
        HttpGet get  = new HttpGet(uri);

        System.out.println("========测试，获取redis中验证码的值："+smsCode+"=====================");
        try {
            CloseableHttpResponse response = client.execute(get);
            if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ){
                //String json = EntityUtils.toString(response.getEntity());
                String json="{\n" +
                        "    \"code\": \"10000\",\n" +
                        "    \"charge\": false,\n" +
                        "    \"remain\": 1305,\n" +
                        "    \"msg\": \"查询成功\",\n" +
                        "    \"result\": {\n" +
                        "        \"ReturnStatus\": \"Success\",\n" +
                        "        \"Message\": \"ok\",\n" +
                        "        \"RemainPoint\": 420842,\n" +
                        "        \"TaskID\": 18424321,\n" +
                        "        \"SuccessCounts\": 1\n" +
                        "    }\n" +
                        "}";//测试数据

                if( json != null && json.length() > 0 ){
                    //处理json字符串， 使用fastjson
                    JSONObject obj = JSONObject.parseObject(json);
                    if("10000".equals(obj.getString("code"))){
                        //调用下发短信接口成功
                        JSONObject resultObj = obj.getJSONObject("result");
                        if("Success".equalsIgnoreCase(resultObj.getString("ReturnStatus"))){
                            //ReturnStatus == Success 说明短信下发成功
                            //把短信验证存放redis
                            stringRedisTemplate.opsForValue().set(
                                    LiCaiRedisKey.LICAI_REGISTER_SMS_CODE+phone,
                                       smsCode,3, TimeUnit.MINUTES);

                            isSend = true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isSend;
    }


    /**
     * @param phone 手机号
     * @param code  请求中的短信验证码
     * @return
     */
    public boolean compCode(String phone, String code){
        boolean result = false;
        if( code != null){
            String key = LiCaiRedisKey.LICAI_REGISTER_SMS_CODE + phone;
            String dbCode = stringRedisTemplate.opsForValue().get(key);
            if( code.equals(dbCode)){
                result = true;
            }
        }

        return result;
    }

    //删除 短信验证的key
    public void removeSmsKey(String phone){
        String key = LiCaiRedisKey.LICAI_REGISTER_SMS_CODE + phone;
        stringRedisTemplate.delete(key);
    }


}
