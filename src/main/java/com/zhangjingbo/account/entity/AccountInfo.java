package com.zhangjingbo.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "MAIN_ACCOUNT")
public class AccountInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "ACCOUNT_ID", type = IdType.ASSIGN_UUID)
    private String accountId;

    /**
     * 账单时间
     */
    @TableField(value = "ACCOUNT_TIME")
    private Date accountTime;

    /**
     * 大类名称
     */
    @TableField(value = "ACCOUNT_NAME")
    private String accountName;

    /**
     * 小类名目
     */
    @TableField(value = "ACCOUNT_ITEM")
    private String accountItem;

    /**
     * 详细信息
     */
    @TableField(value = "ITEM_DETAIL")
    private String itemDetail;

    /**
     * 项目名称
     */
    @TableField(value = "ITEM_NAME")
    private String itemName;

    /**
     * 操作人
     */
    @TableField(value = "\"OPERATOR\"")
    private String operator;

    /**
     * 操作类型
     */
    @TableField(value = "ACCOUNT_TYPE")
    private String accountType;

    /**
     * 单务号码
     */
    @TableField(value = "ACCOUNT_VOUCHER")
    private String accountVoucher;

    /**
     * 银行凭证号码
     */
    @TableField(value = "ACCOUNT_NUMBER")
    private String accountNumber;

    /**
     * 收入
     */
    @TableField(value = "ACCOUNT_DEBIT")
    private BigDecimal accountDebit;

    /**
     * 支出
     */
    @TableField(value = "ACCOUNT_CREDIT")
    private BigDecimal accountCredit;

    /**
     * 余额
     */
    @TableField(value = "BALANCE")
    private BigDecimal balance;

    /**
     * 经转对应单务编号
     */
    @TableField(value = "OLD_VOUCHER")
    private String oldVoucher;

    /**
     * 经转对应年份
     */
    @TableField(value = "OLD_YEAR")
    private String oldYear;

    /**
     * 经转对应名称
     */
    @TableField(value = "OLD_NAME")
    private String oldName;

    /**
     * 操作人编号
     */
    @TableField(value = "OPERATOR_NUM")
    private String operatorNum;

    /**
     * 操作时间
     */
    @TableField(value = "OPERAT_TIME")
    private Date operatTime;

    /**
     * 操作时间
     */
    @TableField(value = "OPERATOR_TYPE")
    private String operatorType;
}