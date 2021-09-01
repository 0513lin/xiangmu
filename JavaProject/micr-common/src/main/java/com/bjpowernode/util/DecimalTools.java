package com.bjpowernode.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.zip.CheckedInputStream;

public class DecimalTools {

    //  判断 n1 >= n2 true ，其他false
    public static boolean ge(BigDecimal n1,BigDecimal n2){
        if( n1 == null || n2 == null){
            throw new RuntimeException("参数是null");
        }

        return  n1.compareTo(n2) >= 0;
    }


    //  判断 n1 <= n2 true ，其他false
    public static boolean le(BigDecimal n1,BigDecimal n2){
        if( n1 == null || n2 == null){
            throw new RuntimeException("参数是null");
        }

        return  n1.compareTo(n2) <= 0;
    }

    // 乘法
    public static BigDecimal mul(BigDecimal n1,BigDecimal n2){
        if( n1 == null || n2 == null){
            throw new RuntimeException("参数是null");
        }

        return n1.multiply(n2);
    }
    //除法
    public static BigDecimal div(BigDecimal n1,BigDecimal n2){
        if( n1 == null || n2 == null){
            throw new RuntimeException("参数是null");
        }
        //0.0003287671
        //0.0003287672
        return n1.divide(n2, 10,RoundingMode.CEILING);//取右边最近的数
    }
    //收益 rate: 日利率   cycle：天
    public static BigDecimal income(BigDecimal bidMoney,BigDecimal rate, Integer cycle){
        BigDecimal income =  mul(mul( bidMoney, rate), new BigDecimal(cycle));
        return income;
    }


    public static void main(String[] args) {
        BigDecimal r = div(new BigDecimal("12"), new BigDecimal("100"));
        r = div( r , new BigDecimal("365") );
        System.out.println("r="+r);
    }
}
