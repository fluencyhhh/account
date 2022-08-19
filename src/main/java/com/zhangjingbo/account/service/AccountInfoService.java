package com.zhangjingbo.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjingbo.account.entity.AccountInfo;

import java.math.BigDecimal;
import java.util.List;

public interface AccountInfoService extends IService<AccountInfo> {

    void saveAccountInfo(AccountInfo accountInfo);

    BigDecimal queryCurrentBalance();

    List<AccountInfo> queryAllAccountInfo();

    List<AccountInfo> queryAccountInfoByParam(AccountInfo accountInfo);

    void exportAccountInfoByParam(AccountInfo accountInfo);

    int deleteAccountInfo(AccountInfo o);

    int editAccuntInfo(AccountInfo accountInfo);
}
