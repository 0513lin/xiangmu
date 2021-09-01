package com.bjpowernode.licai.service;

import com.bjpowernode.licai.model.User;

public interface UserService {

    //平台注册用户数量
    int registerUserCount();

    //根据手机号，查询用户
    User queryByPhone(String phone);

    //注册用户
    User register(String phone,String pwd);

    //用户的登录
    User login(String phone,String password);

    //更新用户的信息
    int modifyUser(Integer id,String name,String card);


}
