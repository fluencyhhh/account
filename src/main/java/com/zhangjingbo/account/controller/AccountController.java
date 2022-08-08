package com.zhangjingbo.account.controller;

import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.service.AccountInfoService;
import com.zhangjingbo.account.util.DateUtils;
import com.zhangjingbo.account.vo.AccountInfoVo;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

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
    private TextField accountType;
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

    @FXML
    private DatePicker selectAccountTime;
    @FXML
    private TextField selectAccountName;
    @FXML
    private TextField selectAccountItem;
    @FXML
    private TextField selectItemDetail;
    @FXML
    private TextField selectItemName;
    @FXML
    private TextField selectOperator;
    @FXML
    private TextField selectAccountType;
    @FXML
    private TextField selectAccountVoucher;
    @FXML
    private TextField selectAccountNumber;
    @FXML
    private TextField selectAccountDebit;
    @FXML
    private TextField selectAccountCredit;
    @FXML
    private TextField selectBalance;

    @FXML
    private TableView<AccountInfoVo> accountInfoTable;

    @FXML
    private TableColumn accountIdResult;
    @FXML
    private TableColumn accountTimeResult;
    @FXML
    private TableColumn accountNameResult;
    @FXML
    private TableColumn accountItemResult;
    @FXML
    private TableColumn itemDetailResult;
    @FXML
    private TableColumn itemNameResult;
    @FXML
    private TableColumn operatorResult;
    @FXML
    private TableColumn accountTypeResult;
    @FXML
    private TableColumn accountVoucherResult;
    @FXML
    private TableColumn accountNumberResult;
    @FXML
    private TableColumn accountDebitResult;
    @FXML
    private TableColumn accountCreditResult;
    @FXML
    private TableColumn balanceResult;


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
        if (!StringUtils.isEmpty(accountType.getText())){
            accountInfo.setAccountType(accountType.getText());
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

    /**
     * 查询全部信息
     */
    public void queryAllAccountInfo(){
        List<AccountInfo> accountInfoList = accountInfoService.queryAllAccountInfo();
        System.out.println(accountInfoList);
    }

    /**
     * 按条件查询信息
     */
    public void queryAccountInfoByParam(){
        AccountInfo accountInfo = new AccountInfo();
        if (!StringUtils.isEmpty(selectAccountTime.getValue())){
            accountInfo.setAccountTime(DateUtils.getDateBuLocalDate(selectAccountTime.getValue()));
        }
        if (!StringUtils.isEmpty(selectAccountName.getText())){
            accountInfo.setAccountName(selectAccountName.getText());
        }
        if (!StringUtils.isEmpty(selectAccountItem.getText())){
            accountInfo.setAccountItem(selectAccountItem.getText());
        }
        if (!StringUtils.isEmpty(selectItemDetail.getText())){
            accountInfo.setItemDetail(selectItemDetail.getText());
        }
        if (!StringUtils.isEmpty(selectItemName.getText())){
            accountInfo.setItemName(selectItemName.getText());
        }
        if (!StringUtils.isEmpty(selectOperator.getText())){
            accountInfo.setOperator(selectOperator.getText());
        }
        if (!StringUtils.isEmpty(selectAccountType.getText())){
            accountInfo.setAccountType(selectAccountType.getText());
        }
        if (!StringUtils.isEmpty(selectAccountVoucher.getText())){
            accountInfo.setAccountVoucher(selectAccountVoucher.getText());
        }
        if (!StringUtils.isEmpty(selectAccountNumber.getText())){
            accountInfo.setAccountNumber(selectAccountNumber.getText());
        }
        if (!StringUtils.isEmpty(selectAccountDebit.getText())){
            accountInfo.setAccountDebit(Integer.valueOf(selectAccountDebit.getText()));
        }
        if (!StringUtils.isEmpty(selectAccountCredit.getText())){
            accountInfo.setAccountCredit(Integer.valueOf(selectAccountCredit.getText()));
        }
        if (!StringUtils.isEmpty(selectBalance.getText())){
            accountInfo.setBalance(Integer.valueOf(selectBalance.getText()));
        }
        List<AccountInfo> accountInfoList = accountInfoService.queryAccountInfoByParam(accountInfo);
        System.out.println(accountInfoList);
        initializeAccountInfoTable(accountInfoList);
    }

    public List<AccountInfoVo> getAccountInfoVo(List<AccountInfo> accountInfoList){
        ObservableList<AccountInfoVo> accountInfoResult;
        List<AccountInfoVo> accountInfoVoList = new ArrayList<>();
        accountInfoList.forEach(o -> accountInfoVoList.add(new AccountInfoVo(o)));
        accountInfoResult = observableArrayList(accountInfoVoList);
        return accountInfoResult;
    }

    public void initializeAccountInfoTable(List<AccountInfo> accountInfoList){
        accountIdResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, String>("accountId"));
        accountTimeResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, String>("accountTime"));
        accountNameResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, String>("accountName"));
        accountItemResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, String>("accountItem"));
        itemDetailResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, String>("itemDetail"));
        itemNameResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, String>("itemName"));
        operatorResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, String>("operator"));
        accountTypeResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, String>("accountType"));
        accountVoucherResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, String>("accountVoucher"));
        accountNumberResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, String>("accountNumber"));
        accountDebitResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, Integer>("accountDebit"));
        accountCreditResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, Integer>("accountCredit"));
        balanceResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, Integer>("balance"));
        accountInfoTable.getItems().setAll(getAccountInfoVo(accountInfoList));
    }

}
