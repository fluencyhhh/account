package com.zhangjingbo.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.mapper.AccountInfoMapper;
import com.zhangjingbo.account.service.AccountInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AccountInfoServiceImpl extends ServiceImpl implements AccountInfoService {
    @Resource
    private AccountInfoMapper accountInfoMapper;

    public int saveAccountInfo(AccountInfo accountInfo) {
        System.out.println(accountInfo);
        return accountInfoMapper.saveAccountInfo(accountInfo);
    }
}
