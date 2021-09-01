package com.bjpowernode.dataservice.service.imp;

import com.bjpowernode.cotants.LiCaiRedisKey;
import com.bjpowernode.cotants.LicaiContants;
import com.bjpowernode.dataservice.mapper.BidInfoMapper;
import com.bjpowernode.dataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.dataservice.mapper.LoanInfoMapper;
import com.bjpowernode.licai.model.BidInfo;
import com.bjpowernode.licai.model.FinanceAccount;
import com.bjpowernode.licai.model.LoanInfo;
import com.bjpowernode.licai.model.ext.LoanBidInfo;
import com.bjpowernode.licai.service.BidInfoService;
import com.bjpowernode.util.CommonUtil;
import com.bjpowernode.util.DecimalTools;
import org.apache.dubbo.config.MetadataReportConfig;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


//投资服务
@DubboService(interfaceClass = BidInfoService.class,version = "1.0")
public class BidInfoServiceImpl implements BidInfoService {


    @Resource
    private BidInfoMapper bidInfoMapper;

    @Resource
    private FinanceAccountMapper accountMapper;

    @Resource
    private LoanInfoMapper loanInfoMapper;

    @Resource
    private RedisTemplate redisTemplate;


    //累计投资金额
    @Override
    public BigDecimal querySumBidMoney() {
        BigDecimal sumBidMoney = (BigDecimal) redisTemplate.opsForValue()
                .get(LiCaiRedisKey.LICAI_SUM_BIDMONEY);

        if( sumBidMoney == null){
            synchronized (this){
                sumBidMoney = (BigDecimal) redisTemplate.opsForValue()
                        .get(LiCaiRedisKey.LICAI_SUM_BIDMONEY);
                if( sumBidMoney == null){
                    sumBidMoney = bidInfoMapper.selectSumBidMoney();
                    redisTemplate.opsForValue()
                            .set(LiCaiRedisKey.LICAI_SUM_BIDMONEY,sumBidMoney,20, TimeUnit.MINUTES);
                }
            }
        }
        return sumBidMoney;
    }

    //某个产品的最近3条投资记录
    @Override
    public List<LoanBidInfo> queryRecentlyBidList(Integer loanId) {
        List<LoanBidInfo> loanBidInfoList = new ArrayList<>();
        if( loanId != null && loanId > 0 ){
          loanBidInfoList =   bidInfoMapper.selectRecentlyBidList(loanId);
        }
        return loanBidInfoList;
    }

    //某个用户的所有投资记录
    @Override
    public List<LoanBidInfo> queryUserBidList(Integer uid,
                                              Integer pageNo, Integer pageSize) {
        List<LoanBidInfo> loanBidInfoList = new ArrayList<>();
        if( uid != null && uid > 0 ){
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            loanBidInfoList = bidInfoMapper.selectUserBidList(uid, offset,pageSize);
        }
        return loanBidInfoList;
    }

    /**
     * 投资
     * @param uid  用户id
     * @param loanId 产品id
     * @param money  投资金额
     * @return
     */
    @Transactional
    @Override
    public boolean invest(Integer uid, Integer loanId, BigDecimal money) {

        int rows = 0;
        boolean result = false;
        //给资金账号数据上锁
        FinanceAccount account =  accountMapper.selectByUidForUpdate(uid);
        if( account != null){
            //判断用户资金是否充足
            if(DecimalTools.ge(account.getAvailableMoney(),money)){
                //判断产品的数据
                LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey(loanId);
                if( loanInfo != null){
                    //获取产品的 leftMoney, min ,max
                    BigDecimal min = loanInfo.getBidMinLimit();
                    BigDecimal max = loanInfo.getBidMaxLimit();
                    BigDecimal left = loanInfo.getLeftProductMoney();
                    //判断投资金额，在可用范围内
                    if( DecimalTools.ge(money,min) && DecimalTools.le( money,max) && DecimalTools.le( money,left) ){
                        //可以投资
                        //1.扣除资金余额
                        rows = accountMapper.updateMoneyInvest(uid,money);
                        if( rows < 1){
                            throw new RuntimeException("投资，更新资金余额失败");
                        }
                        //2.扣除产品的剩余可投资金额
                        rows = loanInfoMapper.updateLeftMoneyInvest(loanId,money);
                        if( rows < 1){
                            throw new RuntimeException("投资，更新产品剩余可投资金额失败");
                        }
                        //3.创建投资记录 b_bid_info

                        BidInfo bidInfo = new BidInfo();
                        bidInfo.setLoanId(loanId);
                        bidInfo.setBidMoney(money);
                        bidInfo.setBidStatus(1);
                        bidInfo.setBidTime(new Date());
                        bidInfo.setUid(uid);
                        rows = bidInfoMapper.insertSelective(bidInfo);
                        if( rows < 1){
                            throw new RuntimeException("投资，创建投资记录失败");
                        }
                        //4.判断产品是否满标
                        loanInfo = loanInfoMapper.selectByPrimaryKey(loanId);
                        if( loanInfo.getLeftProductMoney().intValue() == 0){
                            //满标,更新产品的状态是满标， 和满标时间
                            loanInfo.setProductStatus(LicaiContants.LOAN_STATUS_MANBIAO);
                            loanInfo.setProductFullTime( new Date());
                            rows = loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
                            if( rows < 1){
                                throw new RuntimeException("投资，更新产品的状态为满标失败");
                            }
                        }

                        //认为是成功
                        result = true;
                    }
                }
            }
        }
        return result;
    }
}
