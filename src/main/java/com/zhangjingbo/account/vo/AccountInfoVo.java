package com.zhangjingbo.account.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccountInfoVo implements Serializable {
    private static final long serialVersionUID = 8659512506680957154L;

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



    @Override
    public String toString() {
        return "AccountInfoVo{" + "accountTime=" + accountTime + ", accountName='" + accountName + '\'' + ", accountItem='" + accountItem + '\''
                + ", itemDetail='" + itemDetail + '\'' + ", itemName='" + itemName + '\'' + ", operator='" + operator + '\'' + ", AccountType='" + AccountType
                + '\'' + ", accountVoucher='" + accountVoucher + '\'' + ", accountNumber='" + accountNumber + '\'' + ", accountDebit=" + accountDebit
                + ", accountCredit=" + accountCredit + ", balance=" + balance + '}';
    }
}
