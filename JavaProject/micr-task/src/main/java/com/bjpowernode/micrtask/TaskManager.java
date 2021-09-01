package com.bjpowernode.micrtask;

import com.bjpowernode.licai.service.IncomeService;
import com.bjpowernode.util.HttpClientUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("taskManager")
public class TaskManager {

    @Value("${micrpay.alipay.queryUrl}")
    private String micrPayAlipayQueryUrl;

    @DubboReference(interfaceClass = IncomeService.class,version = "1.0")
    private IncomeService incomeService;

    //测试定时任务

    /**
     * @Scheduled：表示方法是定时任务的方法
     *
     *     属性： cron： 时间表达式
     *     位置:  在方法的上面
     * 定时任务的方法
     * 1. 是public 没有返回值的
     * 2. 方法没有参数的
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void testCron(){
        System.out.println("执行定时任务："+ new Date());
    }


    //作为dubbo的消费者，每20分钟访问一次提供者， 计算满标的收益计划
    @Scheduled(cron = "0 */20 * * * ?")
    public void generateIncomePlan(){

        //调用dubbo的提供者，
        incomeService.generateIncomePlan();
    }

    //收益返还
    //@Scheduled(cron = "0 */30 * * * ?")
    //零点1分1秒。
    @Scheduled(cron = "1 1 0 * * ?")
    public void generateIncomeBack(){
        incomeService.generateIncomeBack();
    }


    //调用支付服务
    @Scheduled(cron = "0 */10 * * * ?")
    public void invokeMicrPayAlipayQuery(){
        try {
            HttpClientUtils.doGet(micrPayAlipayQueryUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
