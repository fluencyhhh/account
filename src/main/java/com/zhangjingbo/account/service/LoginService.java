package com.zhangjingbo.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjingbo.account.entity.User;

public interface LoginService extends IService<User> {
    boolean login(String username, String password);

    void loginout();
}
