package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.licai.model.BidInfo;
import com.bjpowernode.licai.model.ext.LoanBidInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidInfoMapper {
    //累计投资金额
    BigDecimal selectSumBidMoney();

    //某个产品的最近3条投资记录
    List<LoanBidInfo> selectRecentlyBidList(@Param("loanId") Integer loanId);

    //分页查询用户的投资记录，包含产品名称信息
    List<LoanBidInfo> selectUserBidList(@Param("uid") Integer uid, @Param("offSet") Integer offSet, @Param("rows") Integer rows);


    //某个产品的所有投资记录
    List<BidInfo> selectBidListLoanId(@Param("loanId") Integer loanId);

    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);
}