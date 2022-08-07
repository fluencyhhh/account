package com.zhangjingbo.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjingbo.account.entity.AccountInfo;

import java.util.List;

public interface AccountInfoService extends IService {

    int saveAccountInfo(AccountInfo accountInfo);

    List<AccountInfo> queryAllAccountInfo();
}
