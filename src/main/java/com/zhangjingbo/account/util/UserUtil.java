package com.zhangjingbo.account.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhangjingbo.account.entity.LoginLog;
import com.zhangjingbo.account.mapper.LoginLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserUtil {
    @Autowired
    private LoginLogMapper loginLogMapper;

    public String getUserType(){
        QueryWrapper<LoginLog> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("islogined","1");
        queryWrapper.orderByAsc("login_time");
        List<LoginLog> loginLogList=loginLogMapper.selectList(queryWrapper);
        if (loginLogList == null || loginLogList.isEmpty()) {
            return "没有登录信息";
        }else {
            return loginLogList.get(0).getUserright();
        }
    }
}
