package com.zhangjingbo.account.form;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AccountInfoForm {
    private String accountTime;
    private String accountTimeStart;
    private String accountTimeEnd;
    private String accountTimeBetween;

    private String accountVoucher;

    private Integer accountId;

    private String accountName;

    private String accountItem;

    private String itemDetail;

    private String itemName;

    private String operator;

    private String accountType;

    private String accountNumber;

    private String accountDebit;

    private String accountCredit;

    private String balance;

    private Date operatTime;

    private String operatorType;
}
