package com.bjpowernode.dataservice.service.imp;

import com.bjpowernode.cotants.LicaiContants;
import com.bjpowernode.dataservice.mapper.BidInfoMapper;
import com.bjpowernode.dataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.dataservice.mapper.IncomeRecordMapper;
import com.bjpowernode.dataservice.mapper.LoanInfoMapper;
import com.bjpowernode.licai.model.BidInfo;
import com.bjpowernode.licai.model.FinanceAccount;
import com.bjpowernode.licai.model.IncomeRecord;
import com.bjpowernode.licai.model.LoanInfo;
import com.bjpowernode.licai.model.ext.IncomeInfo;
import com.bjpowernode.licai.service.IncomeService;
import com.bjpowernode.util.CommonUtil;
import com.bjpowernode.util.DecimalTools;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = IncomeService.class,version = "1.0")
public class IncomeServiceImpl implements IncomeService {

    @Resource
    private IncomeRecordMapper incomeRecordMapper;

    @Resource
    private LoanInfoMapper loanInfoMapper;

    @Resource
    private BidInfoMapper bidInfoMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public List<IncomeInfo> queryUserIncomeList(Integer uid, Integer pageNo, Integer pageSize) {
        List<IncomeInfo> incomeInfoList = new ArrayList<>();
        if( uid != null && uid > 0 ){
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offSet = (pageNo- 1) * pageSize;
            incomeInfoList = incomeRecordMapper.selectUserIncomeRecord(uid, offSet,pageSize);
        }
        return incomeInfoList;
    }

    //生成满标产品的收益计划。
    @Transactional
    @Override
    public synchronized void generateIncomePlan() {

        int rows = 0;
        Date incomeDate = null; //产品到期时间
        BigDecimal income = new BigDecimal("0");  //利息

        //1. 获取满标产品
        List<LoanInfo>  loanInfoList = loanInfoMapper.queryLoanManBiao();
        //2. 计算的收益。 获取产品的投资记录 b_bid_info
        for(LoanInfo loan : loanInfoList){

            //某个产品所有投资记录
            List<BidInfo> bidInfoList = bidInfoMapper.selectBidListLoanId(loan.getId());
            //每个投资记录有自己的收益
            for(BidInfo bid: bidInfoList){
                //计算收益 = 投资金额 * 周期 * 利率
                if( loan.getProductType() == LicaiContants.LOAN_PRODUCT_TYPE_XIN){
                    // 新手宝 周期是 天
                    BigDecimal  dayRate =  DecimalTools.div(DecimalTools.div(loan.getRate(), new BigDecimal("100")),
                                                            new BigDecimal("365"));
                    //利息
                    income= DecimalTools.income(bid.getBidMoney(),dayRate,loan.getCycle());

                    //到期时间 ，满标时间 + cycle 天数
                    incomeDate = DateUtils.addDays( loan.getProductFullTime(),loan.getCycle());
                } else {
                    //其他优选， 散标周期是 月
                    BigDecimal  dayRate =  DecimalTools.div(DecimalTools.div(loan.getRate(), new BigDecimal("100")),
                            new BigDecimal("365"));

                    income = DecimalTools.income(bid.getBidMoney(),dayRate, (loan.getCycle() * 30));

                    incomeDate = DateUtils.addMonths( loan.getProductFullTime(),loan.getCycle());
                }

                //创建收益表数据
                IncomeRecord record = new IncomeRecord();
                record.setBidId(bid.getId());
                record.setBidMoney(bid.getBidMoney());
                record.setIncomeDate(incomeDate);
                record.setIncomeMoney(income);
                record.setIncomeStatus(LicaiContants.INCOME_STATUS_NOT_BACK);
                record.setLoanId(loan.getId());
                record.setUid(bid.getUid());

                //3.生成收益记录
                rows = incomeRecordMapper.insertSelective(record);
                if(rows < 1){
                    throw new RuntimeException("生成收益计划，创建收益记录失败");

                }
            }

            //4. 产品的状态，更新为  2
            loan.setProductStatus(LicaiContants.LOAN_STATUS_MANBIAO_PLAN);
            rows = loanInfoMapper.updateByPrimaryKey(loan);
            if( rows < 1){
                throw new RuntimeException("生成收益计划，更新产品状态是2失败");
            }
        }
    }

    //收益返还
    @Transactional
    @Override
    public void generateIncomeBack() {

        int rows  =  0 ;
        //1.获取到期的收益记录
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectExpireList();
        //2.循环每个到期的收益记录
        for(IncomeRecord ir : incomeRecordList){
            //获取 incomeMoney(收益) 和 bidMoney(本金)
            // 对account 上锁
            FinanceAccount account = financeAccountMapper.selectByUidForUpdate(ir.getUid());
            if( account !=null){
                //增加金额
                rows  = financeAccountMapper.updateMoneyIncomeBack(
                        ir.getUid(),ir.getBidMoney(),ir.getIncomeMoney());
                if( rows < 1){
                    throw new RuntimeException("收益返还，更新资金账号失败");
                }

                //3.更新收益记录的状态为 1
                ir.setIncomeStatus(LicaiContants.INCOME_STATUS_BACK);
                rows  = incomeRecordMapper.updateByPrimaryKey(ir);
                if( rows < 1){
                    throw new RuntimeException("收益返还，更新收益记录状态为1失败");
                }
            }
        }
    }
}
