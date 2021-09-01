package com.bjpowernode.dataservice.service.imp;

import com.bjpowernode.cotants.LiCaiRedisKey;
import com.bjpowernode.dataservice.mapper.LoanInfoMapper;
import com.bjpowernode.licai.model.LoanInfo;
import com.bjpowernode.licai.service.LoanInfoService;
import com.bjpowernode.util.CommonUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@DubboService(interfaceClass = LoanInfoService.class,version = "1.0")
public class LoanInfoServiceImpl implements LoanInfoService {

    @Resource
    private LoanInfoMapper loanInfoMapper;

    @Resource
    private RedisTemplate redisTemplate;

    //获取历史年化收益率
    @Override
    public BigDecimal queryHistoryAvgRate() {

        BigDecimal avgRate = (BigDecimal) redisTemplate.opsForValue()
                        .get(LiCaiRedisKey.LICAI_HISTORY_AVGRATE);
        if(avgRate == null){
            synchronized (this){
                avgRate = (BigDecimal) redisTemplate.opsForValue()
                        .get(LiCaiRedisKey.LICAI_HISTORY_AVGRATE);
                if( avgRate == null){
                    avgRate =  loanInfoMapper.selectHistoryAvgRate();
                    redisTemplate.opsForValue().set(
                            LiCaiRedisKey.LICAI_HISTORY_AVGRATE,avgRate,20, TimeUnit.MINUTES);
                }
            }
        }
        return avgRate;
    }

    //根据产品类型，分页查询产品，按照rate从高到底排序
    @Override
    public List<LoanInfo> queryLoanInfoByTypePage(Integer productType,
                                                  Integer pageNo,
                                                  Integer pageSize) {

        List<LoanInfo> loanInfoList = new ArrayList<>();
        // dataservice开发人员，需要对参数进行检查
        if(CommonUtil.checkProductType(productType)){
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            // limit 的起始位置
            int offset = (pageNo - 1) * pageSize;
            loanInfoList = loanInfoMapper.selectLoanInfoByTypePage(
                    productType,offset,pageSize);
        }
        return loanInfoList;
    }

    //统计产品类型的总记录数量, 在分页查询中使用
    @Override
    public int staticRecordsProductType(Integer productType) {
        int rows = loanInfoMapper.selectRecordsProductType(productType);
        return rows;
    }

    //根据主键查询产品 查询到数据是非null， 产品不正确或者没有查询到是null
    @Override
    public LoanInfo queryById(Integer loanId) {
        LoanInfo loanInfo = null;
        if( loanId != null && loanId > 0 ){
            loanInfo = loanInfoMapper.selectByPrimaryKey(loanId);
        }
        return loanInfo;
    }
}
