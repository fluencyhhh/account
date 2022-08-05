package com.zhangjingbo.account.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "MAIN_ACCOUNT")
public class AccountInfo {

    private int accountId;
    private Date accountTime;
    private String accountName;
    private String accountItem;
    private String itemDetail;
    private String itemName;
    private String operator;
    private String AccountType;
    private String accountVoucher;
    private String accountNumber;
    private int accountDebit;
    private int accountCredit;
    private long balance;
}
