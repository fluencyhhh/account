package com.zhangjingbo.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.mapper.AccountInfoMapper;
import com.zhangjingbo.account.service.AccountInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AccountInfoServiceImpl extends ServiceImpl implements AccountInfoService {
    @Resource
    private AccountInfoMapper accountInfoMapper;

    @Override
    public int saveAccountInfo(AccountInfo accountInfo) {
        System.out.println(accountInfo);
        return accountInfoMapper.saveAccountInfo(accountInfo);
    }

    @Override
    public List<AccountInfo> queryAllAccountInfo(){
        return accountInfoMapper.queryAllAccountInfo();
    }

    @Override
    public List<AccountInfo> queryAccountInfoByParam(AccountInfo accountInfo){
        System.out.println(accountInfo);
        return accountInfoMapper.queryAccountInfoByParam(accountInfo);
    }
}
