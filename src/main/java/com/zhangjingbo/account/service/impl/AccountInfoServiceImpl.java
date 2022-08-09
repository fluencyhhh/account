package com.zhangjingbo.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.mapper.AccountInfoMapper;
import com.zhangjingbo.account.service.AccountInfoService;
import com.zhangjingbo.account.util.DateUtils;
import com.zhangjingbo.account.util.ExcelUtils;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public List<AccountInfo> queryAllAccountInfo() {
        return accountInfoMapper.queryAllAccountInfo();
    }

    @Override
    public List<AccountInfo> queryAccountInfoByParam(AccountInfo accountInfo) {
        System.out.println(accountInfo);
        return accountInfoMapper.queryAccountInfoByParam(accountInfo);
    }

    @Override
    public void exportAccountInfoByParam(AccountInfo accountInfo) {
        System.out.println(accountInfo);
        //获取数据存入List<Map>
        //数据来源
        List<AccountInfo> accountInfoList = accountInfoMapper.queryAccountInfoByParam(accountInfo);
        //设置路径
        String filePath = "D:\\";
        //文件名
        String fileName = "报账信息"+ DateUtils.getCurrentTime() + ".xlsx";
        //设置表头
        String[] header = new String[] { "日期", "名称", "名目", "人物--明细--单位", "项目名称", "经办人", "类型", "凭证", "号码", "借金额", "贷金额", "余额" };
        List<String[]> excelData = new ArrayList<>();
        //遍历list获取每个map中的数据，根据key对应不同的列
        for (AccountInfo info : accountInfoList) {
            String[] data = new String[] {
                    DateUtils.dateToString(info.getAccountTime()) ,
                    info.getAccountName() ,
                    info.getAccountItem() ,
                    info.getItemDetail() ,
                    info.getItemName() ,
                    info.getOperator() ,
                    info.getAccountType() ,
                    info.getAccountVoucher() ,
                    info.getAccountNumber() ,
                    String.valueOf(info.getAccountDebit()) ,
                    String.valueOf(info.getAccountCredit()) ,
                    String.valueOf(info.getBalance()) };
            excelData.add(data);
        }
        ExcelUtils.toExport(filePath, fileName, header, excelData);
    }
}
