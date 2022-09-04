package com.zhangjingbo.account.service.impl;

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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountInfoServiceImpl extends ServiceImpl<AccountInfoMapper,AccountInfo> implements AccountInfoService {
    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private BalanceMapper balanceMapper;

//    @Override
//    public void saveAccountInfo(AccountInfo accountInfo) {
//        System.out.println("-------------"+accountInfo);
//        //操作余额
//        if (balanceMapper.selectCount(new QueryWrapper<>())==0){
//            Balance balance=new Balance();
//            balance.setId(UUIDUtil.get32UUID());
//            balance.setBalance(new BigDecimal(0));
//            balance.setOperateTime(new Date());
//            balanceMapper.insert(balance);
//        }
//        BigDecimal currentBalance = queryCurrentBalance();
//        System.out.println(currentBalance);
//        BigDecimal newBalance = currentBalance;
//        if (accountInfo.getAccountCredit()!=null){
//            newBalance = newBalance.subtract(accountInfo.getAccountCredit());
//        }else {
//            accountInfo.setAccountCredit(new BigDecimal(0));
//        }
//        if (accountInfo.getAccountDebit()!=null){
//            newBalance = newBalance.add(accountInfo.getAccountDebit());
//        }else {
//            accountInfo.setAccountDebit(new BigDecimal(0));
//        }
//        accountInfo.setBalance(newBalance);
//        Balance balance = new Balance();
//        balance.setOperateTime(new Date());
//        balance.setBalance(newBalance);
//        balance.setOperateAccountId(accountInfo.getAccountId());
//        insertAccountInfoAndBalance(accountInfo,balance);
//    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertAccountInfoAndBalance(AccountInfo accountInfo,Balance balance,BigDecimal diffBalance,String balanceType){
        balanceMapper.insert(balance);
        accountInfoMapper.insert(accountInfo);
        //查找此新增记录后时间的所有账单，进行余额变更
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String accountDate = sdf.format(accountInfo.getAccountTime());
        List<AccountInfo> accountInfoList = new ArrayList<>();
        if ("个人".equals(balanceType)){
            accountInfoList = accountInfoMapper.queryAccountInfoByDate(accountDate,"个人",null);
        }else {
            if ("单位现金".equals(balanceType)){
                accountInfoList = accountInfoMapper.queryAccountInfoByDate(accountDate,"单位","现金");
            }else {
                accountInfoList = accountInfoMapper.queryAccountInfoByDate(accountDate,"单位","北京银行");
            }
        }
        for (AccountInfo updateInfo:accountInfoList){
            QueryWrapper<AccountInfo> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("account_id",updateInfo.getAccountId());
            updateInfo.setBalance(updateInfo.getBalance().add(diffBalance));
            accountInfoMapper.update(updateInfo,queryWrapper);
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteAccountInfoAndBalance(AccountInfo accountInfo,Balance balance,BigDecimal diffBalance,String balanceType){
        balanceMapper.insert(balance);
        //删除对应记录
        QueryWrapper<AccountInfo> queryWrapper=new QueryWrapper();
        queryWrapper.eq("account_id",accountInfo.getAccountId());
        accountInfoMapper.delete(queryWrapper);
        //查找此删除记录后时间的所有账单，进行余额变更
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String accountDate = sdf.format(accountInfo.getAccountTime());
        String operTime = sdf2.format(accountInfo.getOperatTime());
        List<AccountInfo> accountInfoList = new ArrayList<>();
        if ("个人".equals(balanceType)){
            accountInfoList = accountInfoMapper.queryAccountInfoByDateTime(accountDate,operTime,"个人",null);
        }else {
            if ("单位现金".equals(balanceType)){
                accountInfoList = accountInfoMapper.queryAccountInfoByDateTime(accountDate,operTime,"单位","现金");
            }else {
                accountInfoList = accountInfoMapper.queryAccountInfoByDateTime(accountDate,operTime,"单位","北京银行");
            }
        }
        for (AccountInfo updateInfo:accountInfoList){
            queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("account_id",updateInfo.getAccountId());
            updateInfo.setBalance(updateInfo.getBalance().add(diffBalance));
            accountInfoMapper.update(updateInfo,queryWrapper);
        }
    }

    //新增账单信息与余额信息操作
    public void addAccountInfoAndBalance(AccountInfo accountInfo){
//        if (balanceMapper.selectCount(new QueryWrapper<>())==0){
//            Balance balance=new Balance();
//            balance.setId(UUIDUtil.get32UUID());
//            balance.setBalance(new BigDecimal(0));
//            balance.setOperateTime(new Date());
//            balanceMapper.insert(balance);
//        }
        String balanceType = "";
        if ("个人".equals(accountInfo.getOperatorType())){
            balanceType = "个人";
        }else {
            if ("现金".equals(accountInfo.getAccountType())){
                balanceType = "单位现金";
            }else {
                balanceType = "单位银行";
            }
        }
        BigDecimal currentBalance = queryCurrentBalance(balanceType);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String accountDate = sdf.format(accountInfo.getAccountTime());
        String nowTime = sdf2.format(new Date());
        BigDecimal lastBalance = new BigDecimal(0);
        if ("个人".equals(balanceType)){
            lastBalance = accountInfoMapper.queryBalanceByTime(accountDate,nowTime,"个人",null);
        }else {
            if ("单位现金".equals(balanceType)){
                lastBalance = accountInfoMapper.queryBalanceByTime(accountDate,nowTime,"单位","现金");
            }else {
                lastBalance = accountInfoMapper.queryBalanceByTime(accountDate,nowTime,"单位","北京银行");
            }
        }
        if (lastBalance==null){
            lastBalance = new BigDecimal(0);
        }
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
//        if ("insert".equals(operType)){
//
//        }else if ("delete".equals(operType)){
//            if (accountInfo.getAccountCredit()!=null){
//                newBalance = newBalance.add(accountInfo.getAccountCredit());
//            }else {
//                accountInfo.setAccountCredit(new BigDecimal(0));
//            }
//            if (accountInfo.getAccountDebit()!=null){
//                newBalance = newBalance.subtract(accountInfo.getAccountDebit());
//            }else {
//                accountInfo.setAccountDebit(new BigDecimal(0));
//            }
//        }
        BigDecimal diffBalance = newBalance.subtract(currentBalance);
        BigDecimal accountBalance = lastBalance.add(diffBalance);
        accountInfo.setBalance(accountBalance);
        Balance balance = new Balance();
        balance.setOperateTime(new Date());
        balance.setBalance(newBalance);
        balance.setOperateAccountId(accountInfo.getAccountId());
        balance.setBalanceType(balanceType);
        insertAccountInfoAndBalance(accountInfo,balance,diffBalance,balanceType);
    }

    //删除账单信息与余额信息操作
    public void delAccountInfoAndBalance(AccountInfo accountInfo){
        String balanceType = "";
        if ("个人".equals(accountInfo.getOperatorType())){
            balanceType = "个人";
        }else {
            if ("现金".equals(accountInfo.getAccountType())){
                balanceType = "单位现金";
            }else {
                balanceType = "单位银行";
            }
        }
        BigDecimal currentBalance = queryCurrentBalance(balanceType);
        System.out.println(currentBalance);
        BigDecimal newBalance = currentBalance;
        if (accountInfo.getAccountCredit()!=null){
            newBalance = newBalance.add(accountInfo.getAccountCredit());
        }
        if (accountInfo.getAccountDebit()!=null){
            newBalance = newBalance.subtract(accountInfo.getAccountDebit());
        }
        BigDecimal diffBalance = newBalance.subtract(currentBalance);
        Balance balance = new Balance();
        balance.setOperateTime(new Date());
        balance.setBalance(newBalance);
        balance.setOperateAccountId("已删除记录");
        balance.setBalanceType(balanceType);
        System.out.println("balance："+balance);
        deleteAccountInfoAndBalance(accountInfo,balance,diffBalance,balanceType);
    }

    //更新账单信息与余额信息操作
    public void changeAccountInfoAndBalance(AccountInfo oldInfo,AccountInfo newInfo){
        delAccountInfoAndBalance(oldInfo);
        addAccountInfoAndBalance(newInfo);
    }

    @Override
    public BigDecimal queryCurrentBalance(String balanceType) {
        return balanceMapper.queryCurrentBalance(balanceType);
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
        if (StringUtils.isNotBlank(accountInfoForm.getOperatorType())) {
            queryWrapper.eq("operator_type",accountInfoForm.getOperatorType());
        }
        queryWrapper.orderBy(true,false,"account_time","OPERAT_TIME");
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
//        QueryWrapper<AccountInfo> queryWrapper=new QueryWrapper();
//        queryWrapper.eq("account_id",o.getAccountId());
//        accountInfoMapper.delete(queryWrapper);
        delAccountInfoAndBalance(o);
        return 1;
    }

    @Override
    public int editAccuntInfo(AccountInfo accountInfo) {
        QueryWrapper<AccountInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("to_char(ACCOUNT_TIME,'yyyy-MM-dd')",DateUtils.dateToString(accountInfo.getAccountTime()));
        queryWrapper.eq("account_voucher",accountInfo.getAccountVoucher());
        List<AccountInfo> accountInfoList=accountInfoMapper.selectList(queryWrapper);
        if(accountInfoList==null || accountInfoList.isEmpty()){
            String voucher=getVoucher(accountInfo);
            accountInfo.setAccountVoucher(voucher);
            accountInfo.setOperatTime(new Date());
//            accountInfoMapper.insert(accountInfo);
            addAccountInfoAndBalance(accountInfo);
        }else {
            AccountInfo oldInfo = accountInfoList.get(0);
            changeAccountInfoAndBalance(oldInfo,accountInfo);
//            accountInfoMapper.update(accountInfo,queryWrapper);
        }
        return 1;
    }

    //年份+单位/个人+是否经转+类型+6位序列号 4+2+2+2+6
    private String getVoucher(AccountInfo accountInfo) {
        StringBuffer result=new StringBuffer(DateUtils.getDate().substring(0,4));
        if ("admin".equals(userUtil.getUserType())) {
            result.append("01");
        }else {
            result.append("02");
        }
        if ("经转".equals(accountInfo.getAccountItem())) {
            result.append("01");
        }else {
            result.append("00");
        }
        if ("现金".equals(accountInfo.getAccountType())) {
            result.append("01");
        } else if ("北京银行".equals(accountInfo.getAccountType())) {
            result.append("02");
        }else if ("支付宝".equals(accountInfo.getAccountType())) {
            result.append("03");
        }else if ("微信".equals(accountInfo.getAccountType())) {
            result.append("04");
        }else if ("渤海银行".equals(accountInfo.getAccountType())) {
            result.append("05");
        }
        return result.append(accountInfo.getAccountVoucher()).toString();
    }

    @Override
    public List<String> getItemName(String oldYear, String type) {
        QueryWrapper<AccountInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("to_char(account_time,'yyyy')",oldYear);
        queryWrapper.eq("account_item",type);
        queryWrapper.select("item_name","account_voucher");
        List<AccountInfo> accountInfoList=accountInfoMapper.selectList(queryWrapper);
        List<String> result=new ArrayList<>();
        for (AccountInfo item:accountInfoList) {
            result.add(item.getItemName()+"("+item.getAccountVoucher()+")");
        }
        return result;
    }

    @Override
    public AccountInfo getAccount(AccountInfo accountInfo) {
        QueryWrapper<AccountInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.likeLeft("account_voucher",accountInfo.getOldVoucher());
        List<AccountInfo> accountInfoList=accountInfoMapper.selectList(queryWrapper);
        if (accountInfoList == null || accountInfoList.isEmpty()) {
            return null;
        }
        return accountInfoList.get(0);
    }
}
