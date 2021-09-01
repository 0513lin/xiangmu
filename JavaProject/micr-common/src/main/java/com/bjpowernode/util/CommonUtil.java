package com.bjpowernode.util;

import java.util.concurrent.ThreadLocalRandom;

public class CommonUtil {

    //检查产品类型
    public static  boolean checkProductType(Integer productType){
        boolean result = false;//不符合要求
        if( productType != null ){
            if( productType >=0 && productType<=2){
                result = true;
            }
        }
        return result;
    }

    //检查PageNO
    public static int defaultPageNo(Integer pageNo){
        int resultPageNo = pageNo;
        if( pageNo == null || resultPageNo < 1 ){
            resultPageNo = 1;
        }
        return resultPageNo;
    }

    //检查PageSize
    public static int defaultPageSize(Integer pageSize){
        int resultPageSize= pageSize;
        if( pageSize == null || pageSize < 1 || pageSize > 100){
            resultPageSize = 10;
        }
        return resultPageSize;
    }


    //生成随机数
    public static String random(int len){
        StringBuilder builder = new StringBuilder("");
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for( int i=0;i<len;i++){
            builder.append( random.nextInt(10));
        }
        return builder.toString();
    }
}
