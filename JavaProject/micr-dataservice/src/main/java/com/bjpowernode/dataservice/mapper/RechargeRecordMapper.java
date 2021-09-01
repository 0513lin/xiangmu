package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.licai.model.RechargeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeRecordMapper {


    //分页查询用户的充值记录
    List<RechargeRecord> selectUserRechargePage(@Param("uid") Integer uid,
                                                @Param("offset") Integer offset,
                                                @Param("rows") Integer rows);

    //根据商家订单号，查询订单记录
    RechargeRecord selectByRechargeNo(@Param("rechargeNo") String rechargeNo);

    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);
}