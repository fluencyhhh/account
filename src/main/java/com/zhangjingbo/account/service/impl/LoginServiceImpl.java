package com.zhangjingbo.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjingbo.account.entity.LoginLog;
import com.zhangjingbo.account.entity.User;
import com.zhangjingbo.account.mapper.LoginLogMapper;
import com.zhangjingbo.account.mapper.UserMapper;
import com.zhangjingbo.account.service.LoginService;
import com.zhangjingbo.account.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper,User> implements LoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public boolean login(String username, String password) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        List<User> userList=userMapper.selectList(queryWrapper);
        if (userList == null || userList.isEmpty()) {
            return false;
        }
        User user=userList.get(0);
        if(!password.equals(user.getPassword())){
            return false;
        }
        LoginLog loginLog=new LoginLog();
        loginLog.setId(UUIDUtil.get32UUID());
        loginLog.setUsername(username);
        loginLog.setUserright(user.getUserright());
        loginLog.setLoginTime(new Timestamp(new Date().getTime()));
        loginLog.setIslogined("1");
        loginLogMapper.insert(loginLog);
        return true;
    }

    @Override
    public void loginout() {
        QueryWrapper<LoginLog> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("islogined","1");
        List<LoginLog> loginLogList=loginLogMapper.selectList(queryWrapper);
        for (LoginLog loginLog:loginLogList){
            loginLog.setIslogined("0");
            loginLogMapper.updateById(loginLog);
        }
    }
}
