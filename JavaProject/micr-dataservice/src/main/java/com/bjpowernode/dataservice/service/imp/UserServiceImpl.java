package com.bjpowernode.dataservice.service.imp;

import com.bjpowernode.cotants.LiCaiRedisKey;
import com.bjpowernode.dataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.dataservice.mapper.UserMapper;
import com.bjpowernode.licai.model.FinanceAccount;
import com.bjpowernode.licai.model.User;
import com.bjpowernode.licai.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//暴露服务
@DubboService(interfaceClass = UserService.class, version = "1.0")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;


    @Resource
    private RedisTemplate redisTemplate;


    /**
     * @return 平台总注册用户数量
     */
    @Override
    public int registerUserCount() {
        Integer countUsers = 0;
        //先从redis获取数据
        countUsers = (Integer) redisTemplate.opsForValue().get(LiCaiRedisKey.LICAI_REGISTER_USERS);
        if (countUsers == null) { //说明redis没有数据，从数据库获取数据

            synchronized (this) {
                //在查询一次redis
                countUsers = (Integer) redisTemplate.opsForValue().get(LiCaiRedisKey.LICAI_REGISTER_USERS);
                if (countUsers == null) {
                    countUsers = userMapper.selectCountUser();
                    //把数据存放redis，设置存活时间20分钟
                    redisTemplate.opsForValue().set(LiCaiRedisKey.LICAI_REGISTER_USERS,
                            countUsers, 20, TimeUnit.MINUTES);

                }
            }
        }
        //返回数据
        return countUsers;
    }

    //根据手机号，查询用户
    @Override
    public User queryByPhone(String phone) {
        User user = userMapper.selectByPhone(phone);
        return user;
    }
    //注册用户,需要事务

    //rollbackFor： 抛出异常就回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User register(String phone, String pwd) {
        //1.查询手机号是否注册过
        User user = userMapper.selectByPhone(phone);

        //2.没有注册过，注册用户
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setLoginPassword(pwd);
            user.setCategory(1);
            user.setAddTime(new Date());
            userMapper.insertUserReturnId(user);

            //3.创建 finance_account账号，初始金额888
            FinanceAccount account = new FinanceAccount();
            account.setUid(user.getId());
            account.setAvailableMoney(new BigDecimal("888"));
            financeAccountMapper.insertSelective(account);
        } else {
            // 1 :新注册用户； 2. 已存在用户
            user.setCategory(2); //已存在用户
        }
        return user;
    }
    //用户的登录
    @Override
    public User login(String phone, String password) {
        if( phone != null && phone.length() == 11
                && password != null && password.length() == 32){

            User dbUser = userMapper.selectByPhone(phone);
            if( dbUser != null){
                if( dbUser.getLoginPassword().equals(password)){
                    //登录是成功的,更新登录时间
                    dbUser.setLastLoginTime( new Date());
                    userMapper.updateByPrimaryKeySelective(dbUser);
                    return dbUser;
                }
            }
        }
        return null;

    }

    //更新用户的信息
    @Override
    public int modifyUser(Integer id, String name, String card) {

        int rows = 0;
        if (id != null && id > 0 && name != null && card != null) {
            User user = new User();
            user.setId(id);
            user.setIdCard(card);
            user.setName(name);
            rows = userMapper.updateByPrimaryKeySelective(user);
        }

        return rows;
    }

}
