package com.bjpowernode.dataservice.service.imp;

import com.bjpowernode.dataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.licai.model.FinanceAccount;
import com.bjpowernode.licai.service.FinanceAccountService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.yaml.snakeyaml.events.Event;

import javax.annotation.Resource;

@DubboService(interfaceClass = FinanceAccountService.class,version = "1.0")
public class FinanceAccountServiceImpl implements FinanceAccountService {

    @Resource
    private FinanceAccountMapper financeAccountMapper;

    //使用uid查询账号
    @Override
    public FinanceAccount queryAccount(Integer uid) {
        FinanceAccount account = null;
        if( uid != null && uid > 0 ){
            account = financeAccountMapper.selectByUid(uid);
        }
        return account;
    }
}
