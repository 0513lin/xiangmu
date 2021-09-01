package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.licai.model.LoanInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface LoanInfoMapper {

    //计算历史年化收益率平均值
    BigDecimal selectHistoryAvgRate();

    //根据产品类型，分页查询产品，按照rate从高到底排序
    List<LoanInfo> selectLoanInfoByTypePage(@Param("productType") Integer productType,
                                            @Param("offSet") Integer offSet,
                                            @Param("rows") Integer rows);

    //统计产品类型的总记录数量
    int selectRecordsProductType(@Param("productType") Integer productType);

    //投资，更新产品剩余可投资金额
    int updateLeftMoneyInvest(@Param("loanId") Integer loanId, @Param("money") BigDecimal money);

    //获取满标的产品
    List<LoanInfo> queryLoanManBiao();

    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);


}