package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.licai.model.IncomeRecord;
import com.bjpowernode.licai.model.ext.IncomeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IncomeRecordMapper {

    //查询某个用户的投资记录
    List<IncomeInfo> selectUserIncomeRecord(@Param("uid") Integer uid,
                                            @Param("offset") Integer offset,
                                            @Param("rows") Integer rows);

    //获取到期的收益记录
    List<IncomeRecord> selectExpireList();


    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);
}