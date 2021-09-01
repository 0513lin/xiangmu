package com.bjpowernode.dataservice.service.imp;

import com.bjpowernode.cotants.LicaiContants;
import com.bjpowernode.dataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.dataservice.mapper.RechargeRecordMapper;
import com.bjpowernode.licai.model.FinanceAccount;
import com.bjpowernode.licai.model.RechargeRecord;
import com.bjpowernode.licai.service.RechargeService;
import com.bjpowernode.util.CommonUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = RechargeService.class,version = "1.0")
public class RechargeServiceImpl implements RechargeService {

    @Resource
    private RechargeRecordMapper rechargeRecordMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;
    //分页查询用户的充值记录
    @Override
    public List<RechargeRecord> queryUserRechargePage(Integer uid, Integer pageNo, Integer pageSize) {

        List<RechargeRecord> rechargeRecordList = new ArrayList<>();
        if( uid != null && uid > 0 ){
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo -1) * pageSize;
            rechargeRecordList = rechargeRecordMapper.selectUserRechargePage(uid,offset,pageSize);
        }
        return rechargeRecordList;
    }

    //创建充值记录
    @Override
    public int addRecharge(RechargeRecord rr) {
        int rows = rechargeRecordMapper.insert(rr);
        return rows;
    }

    //支付宝notify
    @Transactional
    @Override
    public int doWithAlipayNotify(String outTradeNo, String tradeStatus, String totalAmount) {
        int result = 0;
        //处理商家的业务逻辑
        //1. 查询订单是否是商家的。
        RechargeRecord rr = rechargeRecordMapper.selectByRechargeNo(outTradeNo);

        //2. 判断订单是否处理过，没有处理的才需要处理

        if( rr != null ){
            int rows = 0;
            int rechargeStatus = 0;
            if( rr.getRechargeStatus() == LicaiContants.RECAHGE_STATUS_PROCESSING ){
                //3. 判断金额和支付宝传递过来的金额是否一样
                if( rr.getRechargeMoney().compareTo(new BigDecimal(totalAmount)) == 0 ) {

                    if("TRADE_SUCCESS".equals(tradeStatus)){
                        rechargeStatus = LicaiContants.RECAHGE_STATUS_SUCCESS;
                        //4. 成功了， 更新用户资金账号的金额。 更新充值记录的状态是成功
                        FinanceAccount account = financeAccountMapper.selectByUidForUpdate(rr.getUid());
                        if(account != null){
                            rows = financeAccountMapper.updateMoneyAliapyRecharge(rr.getUid(),rr.getRechargeMoney());

                            if( rows < 1){
                                throw new RuntimeException("支付宝充值异步通知，更新资金账号失败");
                            }
                        } else {
                            result = 5;//资金账号不存在
                        }

                    } else {
                        //5. 充值失败 更新充值记录的状态是失败
                        rechargeStatus = LicaiContants.RECAHGE_STATUS_FAIL;
                    }

                    //更新充值记录的状态
                    rr.setRechargeStatus(rechargeStatus);
                    rows = rechargeRecordMapper.updateByPrimaryKey(rr);
                    if( rows < 1){
                        throw new RuntimeException("支付宝充值异步通知，更新充值记录"+rechargeStatus+"失败");
                    }
                    result = 1; //成功

                } else{
                    result = 4;//金额不一致
                }
            } else {
                result = 3;//此订单已经处理过了。
            }
        } else {
            result = 2;//商家没有此订单记录
        }

        return result;
    }
}
