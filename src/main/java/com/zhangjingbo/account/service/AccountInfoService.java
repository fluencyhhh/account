package com.zhangjingbo.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.Page;
import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.form.AccountInfoForm;

import java.math.BigDecimal;
import java.util.List;

public interface AccountInfoService extends IService<AccountInfo> {

    void saveAccountInfo(AccountInfo accountInfo);

    BigDecimal queryCurrentBalance();

    List<AccountInfo> queryAllAccountInfo();

    Page<AccountInfo> queryAccountInfoByParam(AccountInfoForm accountInfoForm, Integer pageNo);

    void exportAccountInfoByParam(AccountInfoForm accountInfoForm);

    int deleteAccountInfo(AccountInfo o);

    int editAccuntInfo(AccountInfo accountInfo);
}
