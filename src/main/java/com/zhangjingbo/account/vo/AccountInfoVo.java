package com.zhangjingbo.account.vo;

import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.util.DateUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.Date;

public class AccountInfoVo implements Serializable {
    private static final long serialVersionUID = 8659512506680957154L;

    private SimpleIntegerProperty accountId;

    private SimpleStringProperty accountTime;

    private SimpleStringProperty accountName;

    private SimpleStringProperty accountItem;

    private SimpleStringProperty itemDetail;

    private SimpleStringProperty itemName;

    private SimpleStringProperty operator;

    private SimpleStringProperty accountType;

    private SimpleStringProperty accountVoucher;

    private SimpleStringProperty accountNumber;

    private SimpleIntegerProperty accountDebit;

    private SimpleIntegerProperty accountCredit;

    private SimpleLongProperty balance;

    public AccountInfoVo(int accountId, Date accountTime, String accountName, String accountItem, String itemDetail, String itemName, String operator,
            String AccountType, String accountVoucher, String accountNumber, int accountDebit, int accountCredit, long balance) {
        this.accountId = new SimpleIntegerProperty(accountId);
        this.accountTime = new SimpleStringProperty(DateUtils.dateToString(accountTime));
        this.accountName = new SimpleStringProperty(accountName);
        this.accountItem = new SimpleStringProperty(accountItem);
        this.itemDetail = new SimpleStringProperty(itemDetail);
        this.itemName = new SimpleStringProperty(itemName);
        this.operator = new SimpleStringProperty(operator);
        this.accountType = new SimpleStringProperty(AccountType);
        this.accountVoucher = new SimpleStringProperty(accountVoucher);
        this.accountNumber = new SimpleStringProperty(accountNumber);
        this.accountDebit = new SimpleIntegerProperty(accountDebit);
        this.accountCredit = new SimpleIntegerProperty(accountCredit);
        this.balance = new SimpleLongProperty(balance);
    }

    public AccountInfoVo(AccountInfo accountInfo) {
        this.accountId = new SimpleIntegerProperty(accountInfo.getAccountId());
        this.accountTime = new SimpleStringProperty(DateUtils.dateToString(accountInfo.getAccountTime()));
        this.accountName = new SimpleStringProperty(accountInfo.getAccountName());
        this.accountItem = new SimpleStringProperty(accountInfo.getAccountItem());
        this.itemDetail = new SimpleStringProperty(accountInfo.getItemDetail());
        this.itemName = new SimpleStringProperty(accountInfo.getItemName());
        this.operator = new SimpleStringProperty(accountInfo.getOperator());
        this.accountType = new SimpleStringProperty(accountInfo.getAccountType());
        this.accountVoucher = new SimpleStringProperty(accountInfo.getAccountVoucher());
        this.accountNumber = new SimpleStringProperty(accountInfo.getAccountNumber());
        this.accountDebit = new SimpleIntegerProperty(accountInfo.getAccountDebit());
        this.accountCredit = new SimpleIntegerProperty(accountInfo.getAccountCredit());
        this.balance = new SimpleLongProperty(accountInfo.getBalance());
    }

    public int getAccountId() {
        return accountId.get();
    }

    public SimpleIntegerProperty accountIdProperty() {
        return accountId;
    }

    public void setAccountId(int accountID) {
        this.accountId.set(accountID);
    }

    public String getAccountTime() {
        return accountTime.get();
    }

    public SimpleStringProperty accountTimeProperty() {
        return accountTime;
    }

    public void setAccountTime(String accountTime) {
        this.accountTime.set(accountTime);
    }

    public String getAccountName() {
        return accountName.get();
    }

    public SimpleStringProperty accountNameProperty() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName.set(accountName);
    }

    public String getAccountItem() {
        return accountItem.get();
    }

    public SimpleStringProperty accountItemProperty() {
        return accountItem;
    }

    public void setAccountItem(String accountItem) {
        this.accountItem.set(accountItem);
    }

    public String getItemDetail() {
        return itemDetail.get();
    }

    public SimpleStringProperty itemDetailProperty() {
        return itemDetail;
    }

    public void setItemDetail(String itemDetail) {
        this.itemDetail.set(itemDetail);
    }

    public String getItemName() {
        return itemName.get();
    }

    public SimpleStringProperty itemNameProperty() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public String getOperator() {
        return operator.get();
    }

    public SimpleStringProperty operatorProperty() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator.set(operator);
    }

    public String getAccountType() {
        return accountType.get();
    }

    public SimpleStringProperty accountTypeProperty() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType.set(accountType);
    }

    public String getAccountVoucher() {
        return accountVoucher.get();
    }

    public SimpleStringProperty accountVoucherProperty() {
        return accountVoucher;
    }

    public void setAccountVoucher(String accountVoucher) {
        this.accountVoucher.set(accountVoucher);
    }

    public String getAccountNumber() {
        return accountNumber.get();
    }

    public SimpleStringProperty accountNumberProperty() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber.set(accountNumber);
    }

    public int getAccountDebit() {
        return accountDebit.get();
    }

    public SimpleIntegerProperty accountDebitProperty() {
        return accountDebit;
    }

    public void setAccountDebit(int accountDebit) {
        this.accountDebit.set(accountDebit);
    }

    public int getAccountCredit() {
        return accountCredit.get();
    }

    public SimpleIntegerProperty accountCreditProperty() {
        return accountCredit;
    }

    public void setAccountCredit(int accountCredit) {
        this.accountCredit.set(accountCredit);
    }

    public long getBalance() {
        return balance.get();
    }

    public SimpleLongProperty balanceProperty() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance.set(balance);
    }

    @Override
    public String toString() {
        return "AccountInfoVo{" + "accountId=" + accountId.get() + ", accountTime=" + accountTime.get() + ", accountName='" + accountName.get() + '\'' + ", accountItem='" + accountItem.get() + '\''
                + ", itemDetail='" + itemDetail.get() + '\'' + ", itemName='" + itemName.get() + '\'' + ", operator='" + operator.get() + '\'' + ", AccountType='" + accountType.get()
                + '\'' + ", accountVoucher='" + accountVoucher.get() + '\'' + ", accountNumber='" + accountNumber.get() + '\'' + ", accountDebit=" + accountDebit.get()
                + ", accountCredit=" + accountCredit.get() + ", balance=" + balance.get() + '}';
    }
}
