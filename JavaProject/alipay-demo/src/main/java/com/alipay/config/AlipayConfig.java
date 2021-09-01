package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016110200785764";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCapWZ5mbYayPsHasgW4AzzBRBIZOAcol8JrrSvQU6BfPnia2sK4rU02VafGjI8Xbc/AXpdP9RXjX6wTW61M1AvUDRprmq3PHqFlv1rvORftbnMtbU6E3rIll8iIPbwi4fiuD22QFMOF7xU1t+NcqMBYMilDGJOc4YzXAr8RYWJ+ElaLvvTcapusFpU+mtLUumSp75psEMYw80H5bY8jR9sGP8lpSB7INHXTB7hABwRZ9WenBjYw+nOMUZE9CRdil/An6IoxT1BGF2QnQSBtoztieeqbOnGJX119XHGe8qZARY5ck/YcQLnJHW5PnzYuYsXmfired8RUgrOhdp1lIz1AgMBAAECggEBAJi1c/foPdc9WAX/AA72uFLSUT3rvxMHk+mvx2S4jJl5nBhmEpHxRRcm906tQ6YwtN9WykqC2WCLrOrTy9rLDQdroBYr0d2XrzVz4FQfdzS78vmfBZKP4dNqCg9dlfv7DPhpDyeFZX6pGaR9esvmw+h8diXlkL3/fioBXie3TyLppMsKrvnGfJBMF7V0UbRa0XXVsrTNZ0K6UW1oUu7hQ01JBI7MGp7wPTpqcIxJtTUgBE9SQt8nBkkzKzi2peaKhpMyPGkk14VGKgfHUvvPS09CZvYZZkPkyUq1PID+m7K20Q2OwfnUWrVeINAy8juilCC/VOsFIab6p0RpIXclIQECgYEA93wcPOEAsI0OaJqYDLPYV5e3U1b1N+x3sQ+vShMdgI6uLq+IyyiPP71Ywr94oPRIy0O/vZoW0pVqDGUGu9hZCDMSBRTKKGfL9Nl+lp+rFu5tvbhk9SV8rY9+y5sRhO+smNvKtzszB2zURCr1SrpQEr6+JIQZH0+yGn7cqj5c/oUCgYEAn/eM4CBCRfJ/TsbiYXgFSHra/eaJYAPtN93YNgzoCmdUPlIwnNytFhICQN99skb76GK28TWeLgWVyx1EA0/R9OfaCkZfPP/KA72+Sy/GKOWYurlFylPfzt+uZYYdr6eEKk89nZypzEluyV47g92tTd11GW7v6ae4qUD9zV5SN7ECgYEA1hR7DMKJ6S8rnprUGnDcHPE1eRcIqmaYJwbtV8NvSsyhqerYBv0/5SAyjsw4WerWqVYin62SPnlXMf+WMpn0ch5TYSfZs9gN2vDlCwB1bDG1pl7CnjjeP/iX8yJhpj/5aoT+N0AzZSHkAE+0vf6q03xCWK4YWTiEVV0WHwIxSU0CgYBk2gdhDjV6L+Z/Xfg/hxGdnocOaBfYBuG5xQ2ZFg388SQ1nk+ztZUSQwxUphJzITmUSxgXfrxZO4Ay7CKDDAHMq5fVvoQwyvsr3KZqdJY0FnxxzHjplHOX04H509JHeg9jNU4dXjXW670DC3vuKQYn/yTiggSThQBxBN5+aabgIQKBgCtu3zb8A5wot3F3/fzWz4zI4GZ6fhB0jiy0QSprDSsPdaAzKAfFZG32pLVepz6+ubWfFhgMPjo6l7W9b7qxkZMeL+nZr4VOFJTOQn6rbNVANG+Im6heNqyBP+QLi/nmLfduRWcbJK0gulvAMKDniX502pXQceEXYcgeDIgI/Ja4";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjjLJscD4uoxLv+lHdK8DsPj+LRB5zIXLqR7MC2VJ9A4Ok7GFpF0tgrc4G8758RxHSVNa7sDcXmGMDB4O20G9517weY55CpRFKbsCFMvKKV9joApGltJGuGv7BSNF3yEhxW70bw4mkO/1rYt+4INaG8ysaWw81HYVytsdAfZAesUanX39aTv/SuD+1N979ZUfCholdUooa/Jns4wctqFioth0GwCI7mp8rUoh8jaosUafCjK9q5qtHRAOyIWjpOxVkCgH35b3EODOf2HXrOfJrQlDgzi7FwSTq37UNdIKAdatbtHcWh6KMa/MZfz5NJ3T8WufuUjfBhyoahVcIo4wwwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8080/demo/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8080/demo/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

