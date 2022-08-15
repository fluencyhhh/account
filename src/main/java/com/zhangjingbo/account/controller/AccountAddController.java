package com.zhangjingbo.account.controller;

import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.service.AccountInfoService;
import com.zhangjingbo.account.util.BeanUtils;
import com.zhangjingbo.account.util.DateUtils;
import com.zhangjingbo.account.util.ExcelUtils;
import com.zhangjingbo.account.util.UserUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class AccountAddController implements Initializable {

    @FXML
    private DatePicker accountTime;

    @FXML
    public ChoiceBox accountName;

    @FXML
    public ChoiceBox accountItem;

    @FXML
    private TextArea itemDetail;

    @FXML
    private TextField itemName;

    @FXML
    private TextField operator;

    @FXML
    private ChoiceBox accountType;

    @FXML
    private TextField accountVoucher;

    @FXML
    private TextField accountNumber;

    @FXML
    private TextField accountDebit;

    @FXML
    private TextField accountCredit;

    @FXML
    private Label noAccountTime;

    @FXML
    private Label noAccountVoucher;

    @FXML
    private Label saveSuccess;

    @FXML
    public HBox accountCreditBox;

    @FXML
    public HBox accountDebitBox;

    @Autowired
    public UserUtil userUtil;

    @Autowired
    private AccountInfoService accountInfoService;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountTime.setValue(LocalDate.now());
        accountName.getItems().addAll("管理费", "管理费返还", "人工", "收入", "经转", "其他");
        accountName.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setAccountItem(newValue.intValue());
                int accountNameNum=newValue.intValue();
                if(accountNameNum==0||accountNameNum==2){
                    accountCreditBox.setVisible(true);
                    accountDebitBox.setVisible(false);
                }else if(accountNameNum==1||accountNameNum==3){
                    accountCreditBox.setVisible(false);
                    accountDebitBox.setVisible(true);
                }
                System.out.println(oldValue + "    " + newValue);
            }
        });
        accountItem.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if("经转".equals((String)(accountName.getValue()))){
                    if(newValue.intValue()%2==0){
                        accountCreditBox.setVisible(false);
                        accountDebitBox.setVisible(true);
                    }else {
                        accountCreditBox.setVisible(true);
                        accountDebitBox.setVisible(false);
                    }
                }
                if("其他".equals((String)(accountName.getValue()))){
                    if(newValue.intValue()==0){
                        accountCreditBox.setVisible(false);
                        accountDebitBox.setVisible(true);
                    }else {
                        accountCreditBox.setVisible(true);
                        accountDebitBox.setVisible(false);
                    }
                }
            }
        });
        userUtil= BeanUtils.getBean(UserUtil.class);
        if("admin".equals(userUtil.getUserType())){
            accountType.getItems().addAll("现金","支付宝","微信","渤海");
        }else {
            accountType.getItems().addAll("北京银行","现金");
        }


    }

    private void setAccountItem(int number) {
        accountItem.getItems().clear();
        switch (number) {
            case 0:
                accountItem.getItems().addAll("办公耗材", "办公设备", "办公软任", "办公维修", "办公招聘", "办公培训", "公务招投标", "公务标书", "公务印装", "公务快递", "公务造价师", "外勤交通", "外勒差旅", "外勒驻场", "配置餐费", "配置福利", "配置礼品", "基础场地", "基础车辆", "咨询费", "财务费", "税金", "杂项");
                break;
            case 1:
                accountItem.getItems().addAll("办公耗材", "办公设备", "办公软件", "办公维修", "办公招聘", "办公培训", "公务招投标", "公务快递", "公务标书", "公务印装", "公务造价师", "外動交通", "外勒差旅", "外勒驻场", "配置餐费", "配置福利", "配置礼品", "基础场地", "基础车辆", "咨询费", "财务费", "税金", "杂项");
                break;
            case 2:
                accountItem.getItems().addAll("薪水","保险","奖金","福利");
                break;
            case 3:
                accountItem.getItems().addAll("咨询费","利息","杂项");
                break;
            case 4:
                accountItem.getItems().addAll("转入","转出","提现","投保金","投保金返还","汽油费","汽油费返还");
                break;
            case 5:
                accountItem.getItems().addAll("收入","支出");
                break;
            default:accountItem.getItems().addAll();
        }
    }

    /**
     * 填写保存信息
     */
    public void saveAccountInfo() {
        AccountInfo accountInfo = new AccountInfo();
        if (!StringUtils.isEmpty(accountTime.getValue())) {
            accountInfo.setAccountTime(DateUtils.getDateBuLocalDate(accountTime.getValue()));
            if (!DateUtils.ifThisMonth(DateUtils.getDateBuLocalDate(accountTime.getValue()))) {
                noAccountVoucher.setVisible(false);
                saveSuccess.setVisible(false);
                noAccountTime.setText("不可操作非本月信息！");
                noAccountTime.setVisible(true);
                return;
            }
        } else {
            noAccountVoucher.setVisible(false);
            saveSuccess.setVisible(false);
            noAccountTime.setText("日期不可为空！");
            noAccountTime.setVisible(true);
            return;
        }
        if (!StringUtils.isEmpty(accountName.getValue())) {
            accountInfo.setAccountName((String) accountName.getValue());
        }
        if (!StringUtils.isEmpty(accountItem.getValue())) {
            accountInfo.setAccountItem((String) accountItem.getValue());
        }
        if (!StringUtils.isEmpty(itemDetail.getText())) {
            accountInfo.setItemDetail(itemDetail.getText());
        }
        if (!StringUtils.isEmpty(itemName.getText())) {
            accountInfo.setItemName(itemName.getText());
        }
        if (!StringUtils.isEmpty(operator.getText())) {
            accountInfo.setOperator(operator.getText());
        }
        if (!StringUtils.isEmpty(accountType.getValue())) {
            accountInfo.setAccountType((String) accountType.getValue());
        }
        if (!StringUtils.isEmpty(accountVoucher.getText())) {
            accountInfo.setAccountVoucher(accountVoucher.getText());
        } else {
            noAccountTime.setVisible(false);
            saveSuccess.setVisible(false);
            noAccountVoucher.setVisible(true);
            return;
        }
        if (!StringUtils.isEmpty(accountNumber.getText())) {
            accountInfo.setAccountNumber(accountNumber.getText());
        }
        if (!StringUtils.isEmpty(accountDebit.getText())) {
            accountInfo.setAccountDebit(new BigDecimal(accountDebit.getText()));
        }
        if (!StringUtils.isEmpty(accountCredit.getText())) {
            accountInfo.setAccountCredit(new BigDecimal(accountCredit.getText()));
        }
        System.out.println(accountInfo);
        if (accountInfoService == null) {
            accountInfoService=BeanUtils.getBean(AccountInfoService.class);
        }
        accountInfoService.saveAccountInfo(accountInfo);
        noAccountTime.setVisible(false);
        noAccountVoucher.setVisible(false);
        saveSuccess.setVisible(true);
    }

    /**
     * 批量导入信息
     */
    @FXML
    public void saveBatchAccountInfo() {
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(new Stage());
            InputStream inputStream = new FileInputStream(file);
            List<AccountInfo> accountInfoList = ExcelUtils.importExcel(inputStream);
            System.out.println("accountInfoList:" + accountInfoList);
            if (accountInfoService == null) {
                accountInfoService=BeanUtils.getBean(AccountInfoService.class);
            }
            for (AccountInfo accountInfo : accountInfoList) {
                accountInfoService.saveAccountInfo(accountInfo);
            }
            noAccountTime.setVisible(false);
            noAccountVoucher.setVisible(false);
            saveSuccess.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            noAccountTime.setVisible(false);
            noAccountVoucher.setVisible(false);
            saveSuccess.setText("解析失败，请检查格式!");
            saveSuccess.setVisible(true);
            System.out.println("解析失败");
        }

    }
}
