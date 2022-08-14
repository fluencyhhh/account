package com.zhangjingbo.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "MAIN_ACCOUNT")
public class MainAccount implements Serializable {
    private static final long serialVersionUID=1L;
    @TableId(value = "ACCOUNT_ID", type = IdType.AUTO)
    private Integer accountId;

    @TableField(value = "ACCOUNT_TIME")
    private Date accountTime;

    @TableField(value = "ACCOUNT_NAME")
    private String accountName;

    @TableField(value = "ACCOUNT_ITEM")
    private String accountItem;

    @TableField(value = "ITEM_DETAIL")
    private String itemDetail;

    @TableField(value = "ITEM_NAME")
    private String itemName;

    @TableField(value = "\"OPERATOR\"")
    private String operator;

    @TableField(value = "ACCOUNT_VOUCHER")
    private String accountVoucher;

    @TableField(value = "ACCOUNT_NUMBER")
    private String accountNumber;

    @TableField(value = "ACCOUNT_DEBIT")
    private Integer accountDebit;

    @TableField(value = "ACCOUNT_CREDIT")
    private Integer accountCredit;

    @TableField(value = "BALANCE")
    private Object balance;

    @TableField(value = "ACCOUNT_TYPE")
    private String accountType;

    public static final String COL_ACCOUNT_ID = "ACCOUNT_ID";

    public static final String COL_ACCOUNT_TIME = "ACCOUNT_TIME";

    public static final String COL_ACCOUNT_NAME = "ACCOUNT_NAME";

    public static final String COL_ACCOUNT_ITEM = "ACCOUNT_ITEM";

    public static final String COL_ITEM_DETAIL = "ITEM_DETAIL";

    public static final String COL_ITEM_NAME = "ITEM_NAME";

    public static final String COL_OPERATOR = "OPERATOR";

    public static final String COL_ACCOUNT_VOUCHER = "ACCOUNT_VOUCHER";

    public static final String COL_ACCOUNT_NUMBER = "ACCOUNT_NUMBER";

    public static final String COL_ACCOUNT_DEBIT = "ACCOUNT_DEBIT";

    public static final String COL_ACCOUNT_CREDIT = "ACCOUNT_CREDIT";

    public static final String COL_BALANCE = "BALANCE";

    public static final String COL_ACCOUNT_TYPE = "ACCOUNT_TYPE";
}