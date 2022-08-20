package com.zhangjingbo.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.entity.Balance;
import com.zhangjingbo.account.form.AccountInfoForm;
import com.zhangjingbo.account.mapper.AccountInfoMapper;
import com.zhangjingbo.account.mapper.BalanceMapper;
import com.zhangjingbo.account.service.AccountInfoService;
import com.zhangjingbo.account.util.DateUtils;
import com.zhangjingbo.account.util.ExcelUtils;
import com.zhangjingbo.account.util.UUIDUtil;
import com.zhangjingbo.account.util.UserUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Autowired
    private BalanceMapper balanceMapper;

    @Override
    public void saveAccountInfo(AccountInfo accountInfo) {
        System.out.println("-------------"+accountInfo);
        //操作余额
        if (balanceMapper.selectCount(new QueryWrapper<>())==0){
            Balance balance=new Balance();
            balance.setId(UUIDUtil.get32UUID());
            balance.setBalance(new BigDecimal(0));
            balance.setOperateTime(new Date());
            balanceMapper.insert(balance);
        }
        BigDecimal currentBalance = queryCurrentBalance();
        System.out.println(currentBalance);
        BigDecimal newBalance = currentBalance;
        if (accountInfo.getAccountCredit()!=null){
            newBalance = newBalance.subtract(accountInfo.getAccountCredit());
        }else {
            accountInfo.setAccountCredit(new BigDecimal(0));
        }
        if (accountInfo.getAccountDebit()!=null){
            newBalance = newBalance.add(accountInfo.getAccountDebit());
        }else {
            accountInfo.setAccountDebit(new BigDecimal(0));
        }
        accountInfo.setBalance(newBalance);
        Balance balance = new Balance();
        balance.setOperateTime(new Date());
        balance.setBalance(newBalance);
        balance.setOperateAccountId(accountInfo.getAccountId());
        insertAccountInfoAndBalance(accountInfo,balance);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertAccountInfoAndBalance(AccountInfo accountInfo,Balance balance){
        balanceMapper.insert(balance);

        accountInfoMapper.saveAccountInfo(accountInfo);
    }

    @Override
    public BigDecimal queryCurrentBalance() {
        return balanceMapper.queryCurrentBalance();
    }

    @Override
    public List<AccountInfo> queryAllAccountInfo() {
        return accountInfoMapper.queryAllAccountInfo();
    }

    @Override
    public Page<AccountInfo> queryAccountInfoByParam(AccountInfoForm accountInfoForm, Integer pageNo) {
        String userType = userUtil.getUserType();
        QueryWrapper<AccountInfo> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(accountInfoForm.getAccountTimeStart())) {
            queryWrapper.ge("to_char(account_time,'yyyy-MM-dd')", accountInfoForm.getAccountTimeStart());
        } else {
            if ((userType == null || !"admin".equals(userType))) {
                queryWrapper.ge("to_char(account_time,'yyyy-MM-dd')", DateUtils.getCurrYearFirst());
            }
        }
        if (StringUtils.isNotBlank(accountInfoForm.getAccountTimeEnd())) {
            queryWrapper.le("to_char(account_time,'yyyy-MM-dd')", accountInfoForm.getAccountTimeEnd());
        } else {
            if (userType == null || !"admin".equals(userType)) {
                queryWrapper.le("to_char(account_time,'yyyy-MM-dd')", DateUtils.getCurrYearLast());
            }
        }
        if (StringUtils.isNotBlank(accountInfoForm.getAccountTimeBetween())) {
            if ("本月".equals(accountInfoForm.getAccountTimeBetween())) {
                queryWrapper.eq("to_char(account_time,'yyyy-MM')",DateUtils.getDate().substring(0,7));
            } else if ("本季度".equals(accountInfoForm.getAccountTimeBetween())) {
                String month=DateUtils.getCurrentTime().substring(4,6);
                if("01".equals(month)||"02".equals(month)||"03".equals(month)){
                    queryWrapper.ge("to_char(account_time,'yyyy-MM')", DateUtils.getDate().substring(0,5)+"01");
                    queryWrapper.le("to_char(account_time,'yyyy-MM')", DateUtils.getDate().substring(0,5)+"03");
                }else if("04".equals(month)||"05".equals(month)||"06".equals(month)){
                    queryWrapper.ge("to_char(account_time,'yyyy-MM')", DateUtils.getDate().substring(0,5)+"04");
                    queryWrapper.le("to_char(account_time,'yyyy-MM')", DateUtils.getDate().substring(0,5)+"06");
                }else if("07".equals(month)||"08".equals(month)||"09".equals(month)){
                    queryWrapper.ge("to_char(account_time,'yyyy-MM')", DateUtils.getDate().substring(0,5)+"07");
                    queryWrapper.le("to_char(account_time,'yyyy-MM')", DateUtils.getDate().substring(0,5)+"09");
                }else if("07".equals(month)||"10".equals(month)||"12".equals(month)){
                    queryWrapper.ge("to_char(account_time,'yyyy-MM')", DateUtils.getDate().substring(0,5)+"10");
                    queryWrapper.le("to_char(account_time,'yyyy-MM')", DateUtils.getDate().substring(0,5)+"12");
                }
            } else if ("本年".equals(accountInfoForm.getAccountTimeBetween())) {
                queryWrapper.eq("to_char(account_time,'yyyy')",DateUtils.getDate().substring(0,4));
            }
        }
        if (StringUtils.isNotBlank(accountInfoForm.getAccountItem())) {
            queryWrapper.eq("account_item", accountInfoForm.getAccountItem());
        }
        if (StringUtils.isNotBlank(accountInfoForm.getAccountName())) {
            queryWrapper.eq("account_name", accountInfoForm.getAccountName());
        }
        if (StringUtils.isNotBlank(accountInfoForm.getItemDetail())) {
            queryWrapper.like("item_detail", accountInfoForm.getItemDetail());
        }
        if (StringUtils.isNotBlank(accountInfoForm.getItemName())) {
            queryWrapper.like("item_name", accountInfoForm.getItemName());
        }
        if (StringUtils.isNotBlank(accountInfoForm.getOperator())) {
            queryWrapper.like("operator", accountInfoForm.getOperator());
        }
        if (StringUtils.isNotBlank(accountInfoForm.getAccountVoucher())) {
            queryWrapper.like("account_voucher", accountInfoForm.getAccountVoucher());
        }
        if (StringUtils.isNotBlank(accountInfoForm.getAccountNumber())) {
            queryWrapper.like("account_number", accountInfoForm.getAccountNumber());
        }
        if (StringUtils.isNotBlank(accountInfoForm.getAccountDebit())) {
            queryWrapper.eq("account_debit", accountInfoForm.getAccountDebit());
        }
        if (StringUtils.isNotBlank(accountInfoForm.getAccountCredit())) {
            queryWrapper.eq("account_credit", accountInfoForm.getAccountCredit());
        }
        PageHelper.startPage(pageNo, 20);
        List<AccountInfo> accountInfoList = accountInfoMapper.selectList(queryWrapper);
        Page<AccountInfo> page = (Page<AccountInfo>) accountInfoList;
        return page;
    }

    @Override
    public void exportAccountInfoByParam(AccountInfoForm accountInfoForm) {
        //获取数据存入List<Map>
        //数据来源
        List<AccountInfo> accountInfoList = queryAccountInfoByParam(accountInfoForm, 0).getResult();
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
        if (StringUtils.isBlank(o.getAccountId())) {
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
