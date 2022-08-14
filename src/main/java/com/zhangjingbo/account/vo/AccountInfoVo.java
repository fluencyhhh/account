package com.zhangjingbo.account.vo;

import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.util.DateUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
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

    private SimpleStringProperty accountDebit;

    private SimpleStringProperty accountCredit;

    private SimpleStringProperty balance;

    private SimpleObjectProperty<HBox> hBox;

    public AccountInfoVo(int accountId, Date accountTime, String accountName, String accountItem, String itemDetail, String itemName, String operator,
                         String AccountType, String accountVoucher, String accountNumber, BigDecimal accountDebit, BigDecimal accountCredit, BigDecimal balance) {
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
        this.accountDebit = new SimpleStringProperty(accountDebit.toString());
        this.accountCredit = new SimpleStringProperty(accountCredit.toString());
        this.balance = new SimpleStringProperty(balance.toString());
    }

    public AccountInfoVo(AccountInfo accountInfo , HBox hBox) {
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
        this.accountDebit = new SimpleStringProperty(accountInfo.getAccountDebit().toString());
        this.accountCredit = new SimpleStringProperty(accountInfo.getAccountCredit().toString());
        this.balance = new SimpleStringProperty(accountInfo.getBalance().toString());
        this.hBox = new SimpleObjectProperty<HBox>(hBox);
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

    public HBox gethBox() {
        return hBox.get();
    }

    public SimpleObjectProperty<HBox> hBoxProperty() {
        return hBox;
    }

    public void sethBox(HBox hBox) {
        this.hBox.set(hBox);
    }

    public String getAccountDebit() {
        return accountDebit.get();
    }

    public SimpleStringProperty accountDebitProperty() {
        return accountDebit;
    }

    public void setAccountDebit(String accountDebit) {
        this.accountDebit.set(accountDebit);
    }

    public String getAccountCredit() {
        return accountCredit.get();
    }

    public SimpleStringProperty accountCreditProperty() {
        return accountCredit;
    }

    public void setAccountCredit(String accountCredit) {
        this.accountCredit.set(accountCredit);
    }

    public String getBalance() {
        return balance.get();
    }

    public SimpleStringProperty balanceProperty() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance.set(balance);
    }

    //
//    public Button getButtonTable() {
//        return buttonTable.get();
//    }
//
//    public SimpleObjectProperty<Button> buttonTableProperty() {
//        return buttonTable;
//    }
//
//    public void setButtonTable(Button buttonTable) {
//        this.buttonTable.set(buttonTable);
//    }

    @Override
    public String toString() {
        return "AccountInfoVo{" + "accountId=" + accountId.get() + ", accountTime=" + accountTime.get() + ", accountName='" + accountName.get() + '\'' + ", accountItem='" + accountItem.get() + '\''
                + ", itemDetail='" + itemDetail.get() + '\'' + ", itemName='" + itemName.get() + '\'' + ", operator='" + operator.get() + '\'' + ", AccountType='" + accountType.get()
                + '\'' + ", accountVoucher='" + accountVoucher.get() + '\'' + ", accountNumber='" + accountNumber.get() + '\'' + ", accountDebit=" + accountDebit.get()
                + ", accountCredit=" + accountCredit.get() + ", balance=" + balance.get() + '}';
    }
}
