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
import java.util.Arrays;
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
    public TextField oldYear;

    @FXML
    public ChoiceBox oldName;

    @FXML
    public TextField oldVoucher;

    @FXML
    public HBox accountCreditBox;

    @FXML
    public HBox operatoerTypeHbox;

    @FXML
    public HBox oldHbox1;

    @FXML
    public HBox oldHbox2;

    @FXML
    public HBox oldHbox3;

    @FXML
    public ChoiceBox operatorType;

    @FXML
    public HBox accountDebitBox;



    @Autowired
    public UserUtil userUtil;

    @Autowired
    private AccountInfoService accountInfoService;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountTime.setValue(LocalDate.now());
        accountName.getItems().addAll("?????????", "???????????????", "??????", "??????", "??????", "??????");
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
            }
        });
        accountItem.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if("??????".equals((String)(accountName.getValue()))){
                    if(newValue.intValue()%2==0){
                        accountCreditBox.setVisible(false);
                        accountDebitBox.setVisible(true);
                    }else {
                        accountCreditBox.setVisible(true);
                        accountDebitBox.setVisible(false);
                    }
                    if(newValue.intValue()==1||newValue.intValue()==4||newValue.intValue()==6){
                        oldHbox1.setVisible(true);
                        oldHbox2.setVisible(true);
                        oldHbox3.setVisible(true);
                    }else {
                        oldHbox1.setVisible(false);
                        oldHbox2.setVisible(false);
                        oldHbox3.setVisible(false);
                    }
                }
                if("??????".equals((String)(accountName.getValue()))){
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
            accountType.getItems().addAll("??????","?????????","??????","????????????");
            operatoerTypeHbox.setVisible(true);
            operatorType.getItems().addAll("??????","??????");
        }else {
            accountType.getItems().addAll("????????????","??????");
            operatoerTypeHbox.setVisible(false);
        }


        operatorType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                accountType.getItems().clear();
                if(0==newValue.intValue()){
                    accountType.getItems().addAll("????????????","??????");
                }else if(1==newValue.intValue()){
                    accountType.getItems().addAll("??????","?????????","??????","????????????");
                }
            }
        });

        oldYear.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue!=null&&newValue.length()==4){
                    List<String> itemNameList=getItemName(newValue,(String)(accountItem.getValue()));
                    oldName.getItems().addAll(itemNameList);
                }
            }
        });

        oldName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                String[] tests=((String) newValue).split("[(]");
//                System.out.println(tests[1].substring(0,tests[1].length()-1));
                oldVoucher.setText(tests[1].substring(0,tests[1].length()-1));
            }
        });
    }

    private List<String> getItemName(String newValue, String value) {
        if("??????".equals(value)){
            value="??????";
        } else if ("???????????????".equals(value)) {
            value="?????????";
        }else if ("???????????????".equals(value)) {
            value="?????????";
        }
        if (accountInfoService == null) {
            accountInfoService=BeanUtils.getBean(AccountInfoService.class);
        }
        List<String> nameList=accountInfoService.getItemName(newValue,value);
        return nameList;
    }


    private void setAccountItem(int number) {
        accountItem.getItems().clear();
        switch (number) {
            case 0:
                accountItem.getItems().addAll("????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "???????????????", "????????????", "????????????", "????????????", "???????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "?????????", "?????????", "??????", "??????");
                break;
            case 1:
                accountItem.getItems().addAll("????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "???????????????", "????????????", "????????????", "????????????", "???????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "?????????", "?????????", "??????", "??????");
                break;
            case 2:
                accountItem.getItems().addAll("??????","??????","??????","??????");
                break;
            case 3:
                accountItem.getItems().addAll("?????????","??????","??????");
                break;
            case 4:
                accountItem.getItems().addAll("??????","??????","??????","?????????","???????????????","?????????","???????????????");
                break;
            case 5:
                accountItem.getItems().addAll("??????","??????");
                break;
            default:accountItem.getItems().addAll();
        }
    }

    /**
     * ??????????????????
     */
    public void saveAccountInfo() {
        AccountInfo accountInfo = new AccountInfo();
        if (!StringUtils.isEmpty(accountTime.getValue())) {
            accountInfo.setAccountTime(DateUtils.getDateBuLocalDate(accountTime.getValue()));
            if (!DateUtils.ifThisMonth(DateUtils.getDateBuLocalDate(accountTime.getValue()))) {
                noAccountVoucher.setVisible(false);
                saveSuccess.setVisible(false);
                noAccountTime.setText("??????????????????????????????");
                noAccountTime.setVisible(true);
                return;
            }
        } else {
            noAccountVoucher.setVisible(false);
            saveSuccess.setVisible(false);
            noAccountTime.setText("?????????????????????");
            noAccountTime.setVisible(true);
            return;
        }
        if (userUtil == null) {
            userUtil= BeanUtils.getBean(UserUtil.class);
        }
        if ("admin".equals(userUtil.getUserType())) {
            if(!StringUtils.isEmpty(operatorType.getValue())){
                accountInfo.setOperatorType((String) operatorType.getValue());
            }
        }else {
            accountInfo.setOperatorType("??????");
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
        if (!StringUtils.isEmpty(oldYear.getText())) {
            accountInfo.setOldYear(oldYear.getText());
        }
        if (!StringUtils.isEmpty(oldName.getValue())) {
            accountInfo.setOldName((String)(oldName.getValue()));
        }
        if (!StringUtils.isEmpty(oldVoucher.getText())) {
            accountInfo.setOldVoucher(oldVoucher.getText());
        }
        if (accountInfoService == null) {
            accountInfoService=BeanUtils.getBean(AccountInfoService.class);
        }
        AccountInfo result=accountInfoService.getAccount(accountInfo);
        if (result != null) {
            accountInfo.setOldVoucher(result.getAccountVoucher());
        }
        accountInfoService.editAccuntInfo(accountInfo);
        noAccountTime.setVisible(false);
        noAccountVoucher.setVisible(false);
        saveSuccess.setVisible(true);
    }

    /**
     * ??????????????????OLD_NAME
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
                accountInfoService.editAccuntInfo(accountInfo);
            }
            noAccountTime.setVisible(false);
            noAccountVoucher.setVisible(false);
            saveSuccess.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            noAccountTime.setVisible(false);
            noAccountVoucher.setVisible(false);
            saveSuccess.setText("??????????????????????????????!");
            saveSuccess.setVisible(true);
            System.out.println("????????????");
        }

    }
}
