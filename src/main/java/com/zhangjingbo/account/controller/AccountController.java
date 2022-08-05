package com.zhangjingbo.account.controller;

import com.zhangjingbo.account.vo.AccountInfoVo;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.springframework.util.StringUtils;

import java.util.Date;

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

    /**
     * 填写保存信息
     */
    public void saveAccountInfo() {
        System.out.println(accountTime.getEditor());
        System.out.println(accountName.getText());
        AccountInfoVo accountInfoVo = new AccountInfoVo();
        if (StringUtils.isEmpty(accountTime.getEditor())){
            System.out.println(accountTime.getEditor());
        }
        if (StringUtils.isEmpty(accountName.getText())){
            System.out.println(accountName.getText());
        }
    }

}
