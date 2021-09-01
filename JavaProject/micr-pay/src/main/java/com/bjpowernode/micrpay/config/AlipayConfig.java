package com.bjpowernode.micrpay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Component
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {


    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public  String app_id = "2016110200785764";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public  String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCapWZ5mbYayPsHasgW4AzzBRBIZOAcol8JrrSvQU6BfPnia2sK4rU02VafGjI8Xbc/AXpdP9RXjX6wTW61M1AvUDRprmq3PHqFlv1rvORftbnMtbU6E3rIll8iIPbwi4fiuD22QFMOF7xU1t+NcqMBYMilDGJOc4YzXAr8RYWJ+ElaLvvTcapusFpU+mtLUumSp75psEMYw80H5bY8jR9sGP8lpSB7INHXTB7hABwRZ9WenBjYw+nOMUZE9CRdil/An6IoxT1BGF2QnQSBtoztieeqbOnGJX119XHGe8qZARY5ck/YcQLnJHW5PnzYuYsXmfired8RUgrOhdp1lIz1AgMBAAECggEBAJi1c/foPdc9WAX/AA72uFLSUT3rvxMHk+mvx2S4jJl5nBhmEpHxRRcm906tQ6YwtN9WykqC2WCLrOrTy9rLDQdroBYr0d2XrzVz4FQfdzS78vmfBZKP4dNqCg9dlfv7DPhpDyeFZX6pGaR9esvmw+h8diXlkL3/fioBXie3TyLppMsKrvnGfJBMF7V0UbRa0XXVsrTNZ0K6UW1oUu7hQ01JBI7MGp7wPTpqcIxJtTUgBE9SQt8nBkkzKzi2peaKhpMyPGkk14VGKgfHUvvPS09CZvYZZkPkyUq1PID+m7K20Q2OwfnUWrVeINAy8juilCC/VOsFIab6p0RpIXclIQECgYEA93wcPOEAsI0OaJqYDLPYV5e3U1b1N+x3sQ+vShMdgI6uLq+IyyiPP71Ywr94oPRIy0O/vZoW0pVqDGUGu9hZCDMSBRTKKGfL9Nl+lp+rFu5tvbhk9SV8rY9+y5sRhO+smNvKtzszB2zURCr1SrpQEr6+JIQZH0+yGn7cqj5c/oUCgYEAn/eM4CBCRfJ/TsbiYXgFSHra/eaJYAPtN93YNgzoCmdUPlIwnNytFhICQN99skb76GK28TWeLgWVyx1EA0/R9OfaCkZfPP/KA72+Sy/GKOWYurlFylPfzt+uZYYdr6eEKk89nZypzEluyV47g92tTd11GW7v6ae4qUD9zV5SN7ECgYEA1hR7DMKJ6S8rnprUGnDcHPE1eRcIqmaYJwbtV8NvSsyhqerYBv0/5SAyjsw4WerWqVYin62SPnlXMf+WMpn0ch5TYSfZs9gN2vDlCwB1bDG1pl7CnjjeP/iX8yJhpj/5aoT+N0AzZSHkAE+0vf6q03xCWK4YWTiEVV0WHwIxSU0CgYBk2gdhDjV6L+Z/Xfg/hxGdnocOaBfYBuG5xQ2ZFg388SQ1nk+ztZUSQwxUphJzITmUSxgXfrxZO4Ay7CKDDAHMq5fVvoQwyvsr3KZqdJY0FnxxzHjplHOX04H509JHeg9jNU4dXjXW670DC3vuKQYn/yTiggSThQBxBN5+aabgIQKBgCtu3zb8A5wot3F3/fzWz4zI4GZ6fhB0jiy0QSprDSsPdaAzKAfFZG32pLVepz6+ubWfFhgMPjo6l7W9b7qxkZMeL+nZr4VOFJTOQn6rbNVANG+Im6heNqyBP+QLi/nmLfduRWcbJK0gulvAMKDniX502pXQceEXYcgeDIgI/Ja4";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjjLJscD4uoxLv+lHdK8DsPj+LRB5zIXLqR7MC2VJ9A4Ok7GFpF0tgrc4G8758RxHSVNa7sDcXmGMDB4O20G9517weY55CpRFKbsCFMvKKV9joApGltJGuGv7BSNF3yEhxW70bw4mkO/1rYt+4INaG8ysaWw81HYVytsdAfZAesUanX39aTv/SuD+1N979ZUfCholdUooa/Jns4wctqFioth0GwCI7mp8rUoh8jaosUafCjK9q5qtHRAOyIWjpOxVkCgH35b3EODOf2HXrOfJrQlDgzi7FwSTq37UNdIKAdatbtHcWh6KMa/MZfz5NJ3T8WufuUjfBhyoahVcIo4wwwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public  String notify_url = "http://localhost:8080/demo/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public  String return_url = "http://localhost:8080/demo/return_url.jsp";

    // 签名方式
    public  String sign_type = "RSA2";

    // 字符编码格式
    public  String charset = "utf-8";

    // 支付宝网关
    public  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getMerchant_private_key() {
        return merchant_private_key;
    }

    public void setMerchant_private_key(String merchant_private_key) {
        this.merchant_private_key = merchant_private_key;
    }

    public String getAlipay_public_key() {
        return alipay_public_key;
    }

    public void setAlipay_public_key(String alipay_public_key) {
        this.alipay_public_key = alipay_public_key;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }
}

