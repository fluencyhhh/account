package com.zhangjingbo.account.controller;

import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.service.AccountInfoService;
import com.zhangjingbo.account.util.BeanUtils;
import com.zhangjingbo.account.util.DateUtils;
import com.zhangjingbo.account.util.UserUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class AccountDialogController implements Initializable {

    @FXML
    public DatePicker accountTime;

    @FXML
    public ChoiceBox accountName;

    @FXML
    public ChoiceBox accountItem;

    @FXML
    public TextArea itemDetail;

    @FXML
    public TextField itemName;

    @FXML
    public TextField operator;

    @FXML
    public ChoiceBox accountType;

    @FXML
    public TextField accountVoucher;

    @FXML
    public TextField accountNumber;

    @FXML
    public TextField accountDebit;

    @FXML
    public TextField accountCredit;

    @FXML
    public TextField oldYear;

    @FXML
    public ChoiceBox oldName;

    @FXML
    public TextField oldVoucher;

    @FXML
    public HBox accountCreditBox;

    @FXML
    public HBox accountDebitBox;

    @FXML
    public HBox oldHbox1;

    @FXML
    public HBox oldHbox2;

    @FXML
    public HBox oldHbox3;

    @Autowired
    public UserUtil userUtil;

    @Autowired
    public AccountInfoService accountInfoService;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        userUtil=BeanUtils.getBean(UserUtil.class);

        if("admin".equals(userUtil.getUserType())){
            accountType.getItems().addAll("现金","支付宝","微信","渤海银行");
        }else {
            accountType.getItems().addAll("北京银行","现金");
        }

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
        if("转出".equals(value)){
            value="转入";
        } else if ("投保金返还".equals(value)) {
            value="投保金";
        }else if ("汽油费返还".equals(value)) {
            value="汽油费";
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

    public void editAccount(ActionEvent actionEvent) {
        AccountInfo accountInfo = new AccountInfo();
        if (accountTime.getValue()!=null) {
            accountInfo.setAccountTime(DateUtils.getDateBuLocalDate(accountTime.getValue()));
        }
        if (StringUtils.isNoneBlank((String) (accountName.getValue()))) {
            accountInfo.setAccountName((String)(accountName.getValue()));
        }
        if (StringUtils.isNoneBlank((String)(accountItem.getValue()))) {
            accountInfo.setAccountItem((String)(accountItem.getValue()));
        }
        if (StringUtils.isNoneBlank(itemDetail.getText())) {
            accountInfo.setItemDetail(itemDetail.getText());
        }
        if (StringUtils.isNoneBlank(itemName.getText())) {
            accountInfo.setItemName(itemName.getText());
        }
        if (StringUtils.isNoneBlank(operator.getText())) {
            accountInfo.setOperator(operator.getText());
        }
        if (StringUtils.isNoneBlank((String)(accountType.getValue()))) {
            accountInfo.setAccountType((String)(accountType.getValue()));
        }
        if (StringUtils.isNoneBlank(accountVoucher.getText())) {
            accountInfo.setAccountVoucher(accountVoucher.getText());
        }
        if (StringUtils.isNoneBlank(accountNumber.getText())) {
            accountInfo.setAccountNumber(accountNumber.getText());
        }
        if (StringUtils.isNoneBlank(accountDebit.getText())) {
            accountInfo.setAccountDebit(new BigDecimal(accountDebit.getText()));
        }
        if (StringUtils.isNoneBlank(accountCredit.getText())) {
            accountInfo.setAccountCredit(new BigDecimal(accountCredit.getText()));
        }
        if (accountInfoService == null) {
            accountInfoService=BeanUtils.getBean(AccountInfoService.class);
        }
        accountInfoService.editAccuntInfo(accountInfo);
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("完成");
        alert.setHeaderText(null);
        alert.setContentText("修改成功");
        alert.showAndWait();
    }
}
