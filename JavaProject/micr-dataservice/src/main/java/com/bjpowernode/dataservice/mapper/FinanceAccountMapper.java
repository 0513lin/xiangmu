package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.licai.model.FinanceAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface FinanceAccountMapper {

    //根据userid查询account
    FinanceAccount selectByUid(@Param("uid") Integer uid);
    int deleteByPrimaryKey(Integer id);

    //扣除资金余额，在投资中
    int updateMoneyInvest(@Param("uid") Integer uid, @Param("bidMoney") BigDecimal bidMoney);


    //查询数据，并上锁
    FinanceAccount selectByUidForUpdate(@Param("uid") Integer uid);

    //收益返还，
    int updateMoneyIncomeBack(@Param("uid") Integer uid, @Param("bidMoney") BigDecimal bidMoney, @Param("incomeMoney") BigDecimal incomeMoney);

    //充值返回
    int updateMoneyAliapyRecharge(@Param("uid") Integer uid, @Param("rechargeMoney") BigDecimal rechargeMoney);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);
}