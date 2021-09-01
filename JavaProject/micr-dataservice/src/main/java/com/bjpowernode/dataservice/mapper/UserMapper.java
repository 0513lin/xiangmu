package com.bjpowernode.dataservice.mapper;

import com.bjpowernode.licai.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    //统计注册用户总数
    int selectCountUser();

    //根据手机号，查询用户
    User  selectByPhone(@Param("phone") String phone);

    //insert用户，返回主键id
    int insertUserReturnId(User user);


    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}