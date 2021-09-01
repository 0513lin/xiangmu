package com.bjpowernode.licai.service;

import com.bjpowernode.licai.model.LoanInfo;

import java.math.BigDecimal;
import java.util.List;

public interface LoanInfoService {

    //获取历史年化收益率
    BigDecimal queryHistoryAvgRate();

    //根据产品类型，分页查询产品，按照rate从高到底排序
    List<LoanInfo> queryLoanInfoByTypePage(Integer productType,
                                           Integer pageNo,
                                           Integer pageSize);

    //统计产品类型的总记录数量
    int staticRecordsProductType(Integer productType);

    //根据主键查询产品
    LoanInfo queryById(Integer loanId);
}
