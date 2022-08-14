package com.zhangjingbo.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.mapper.AccountInfoMapper;
import com.zhangjingbo.account.service.AccountInfoService;
import com.zhangjingbo.account.util.DateUtils;
import com.zhangjingbo.account.util.ExcelUtils;
import com.zhangjingbo.account.util.UserUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AccountInfoServiceImpl extends ServiceImpl<AccountInfoMapper,AccountInfo> implements AccountInfoService {
    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private UserUtil userUtil;

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
        String userType = userUtil.getUserType();
        if (userType!=null&&"admin".equals(userType)){
            return accountInfoMapper.queryAccountInfoByParam(accountInfo,"","");
        }else {
            String startTime = DateUtils.getCurrYearFirst();
            String endTime = DateUtils.getCurrYearLast();
            return accountInfoMapper.queryAccountInfoByParam(accountInfo,startTime,endTime);
        }

    }

    @Override
    public void exportAccountInfoByParam(AccountInfo accountInfo) {
        System.out.println(accountInfo);
        //获取数据存入List<Map>
        //数据来源
        List<AccountInfo> accountInfoList = queryAccountInfoByParam(accountInfo);
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

    @Override
    public int deleteAccountInfo(AccountInfo o) {
        if(o.getAccountId()==0){
            return 0;
        }
        QueryWrapper<AccountInfo> queryWrapper=new QueryWrapper();
        queryWrapper.eq("account_id",o.getAccountId());
        accountInfoMapper.delete(queryWrapper);
        return 1;
    }

    @Override
    public int editAccuntInfo(AccountInfo accountInfo) {
        QueryWrapper<AccountInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("to_char(ACCOUNT_TIME,'yyyy-MM-dd')",DateUtils.dateToString(accountInfo.getAccountTime()));
        queryWrapper.eq("account_voucher",accountInfo.getAccountVoucher());
        List<AccountInfo> accountInfoList=accountInfoMapper.selectList(queryWrapper);
        if(accountInfoList==null || accountInfoList.isEmpty()){
            accountInfoMapper.insert(accountInfo);
        }else {
            accountInfoMapper.update(accountInfo,queryWrapper);
        }
        return 1;
    }
}
