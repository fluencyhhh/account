package com.zhangjingbo.account.controller;

import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.service.AccountInfoService;
import com.zhangjingbo.account.util.DateUtils;
import com.zhangjingbo.account.vo.AccountInfoVo;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.xml.soap.Text;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

@FXMLController
public class AccountController {

    @FXML
    private DatePicker accountTime;
    @FXML
    private TextField accountName;
    @FXML
    private TextField accountItem;
    @FXML
    private TextField itemDetail;
    @FXML
    private TextField itemName;
    @FXML
    private TextField operator;
    @FXML
    private TextField AccountType;
    @FXML
    private TextField accountVoucher;
    @FXML
    private TextField accountNumber;
    @FXML
    private TextField accountDebit;
    @FXML
    private TextField accountCredit;
    @FXML
    private TextField balance;


    @Autowired
    private AccountInfoService accountInfoService;

    /**
     * 填写保存信息
     */
    public void saveAccountInfo() {
        AccountInfo accountInfo = new AccountInfo();
        if (!StringUtils.isEmpty(accountTime.getValue())){
            accountInfo.setAccountTime(DateUtils.getDateBuLocalDate(accountTime.getValue()));
        }
        if (!StringUtils.isEmpty(accountName.getText())){
            accountInfo.setAccountName(accountName.getText());
        }
        if (!StringUtils.isEmpty(accountItem.getText())){
            accountInfo.setAccountItem(accountItem.getText());
        }
        if (!StringUtils.isEmpty(itemDetail.getText())){
            accountInfo.setItemDetail(itemDetail.getText());
        }
        if (!StringUtils.isEmpty(itemName.getText())){
            accountInfo.setItemName(itemName.getText());
        }
        if (!StringUtils.isEmpty(operator.getText())){
            accountInfo.setOperator(operator.getText());
        }
        if (!StringUtils.isEmpty(AccountType.getText())){
            accountInfo.setAccountType(AccountType.getText());
        }
        if (!StringUtils.isEmpty(accountVoucher.getText())){
            accountInfo.setAccountVoucher(accountVoucher.getText());
        }
        if (!StringUtils.isEmpty(accountNumber.getText())){
            accountInfo.setAccountNumber(accountNumber.getText());
        }
        if (!StringUtils.isEmpty(accountDebit.getText())){
            accountInfo.setAccountDebit(Integer.valueOf(accountDebit.getText()));
        }
        if (!StringUtils.isEmpty(accountCredit.getText())){
            accountInfo.setAccountCredit(Integer.valueOf(accountCredit.getText()));
        }
        if (!StringUtils.isEmpty(balance.getText())){
            accountInfo.setBalance(Integer.valueOf(balance.getText()));
        }
        System.out.println(accountInfo);
        accountInfoService.saveAccountInfo(accountInfo);
    }

    public void queryAllAccountInfo(){
        List<AccountInfo> accountInfoList = accountInfoService.queryAllAccountInfo();
        System.out.println(accountInfoList);
    }

}
