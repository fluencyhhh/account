package com.zhangjingbo.account.controller;

import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.service.AccountInfoService;
import com.zhangjingbo.account.util.DateUtils;
import com.zhangjingbo.account.util.ExcelUtils;
import com.zhangjingbo.account.util.UserUtil;
import com.zhangjingbo.account.vo.AccountInfoVo;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

@FXMLController
public class AccountController {



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

    @FXML
    private TableColumn hBox;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private UserUtil userUtil;

    /**
     * 查询全部信息
     */
    public void queryAllAccountInfo() {
        List<AccountInfo> accountInfoList = accountInfoService.queryAllAccountInfo();
        System.out.println(accountInfoList);
    }

    /**
     * 按条件查询信息
     */
    public void queryAccountInfoByParam() {
        AccountInfo accountInfo = new AccountInfo();
        if (!StringUtils.isEmpty(selectAccountTime.getValue())) {
            accountInfo.setAccountTime(DateUtils.getDateBuLocalDate(selectAccountTime.getValue()));
        }
        if (!StringUtils.isEmpty(selectAccountName.getText())) {
            accountInfo.setAccountName(selectAccountName.getText());
        }
        if (!StringUtils.isEmpty(selectAccountItem.getText())) {
            accountInfo.setAccountItem(selectAccountItem.getText());
        }
        if (!StringUtils.isEmpty(selectItemDetail.getText())) {
            accountInfo.setItemDetail(selectItemDetail.getText());
        }
        if (!StringUtils.isEmpty(selectItemName.getText())) {
            accountInfo.setItemName(selectItemName.getText());
        }
        if (!StringUtils.isEmpty(selectOperator.getText())) {
            accountInfo.setOperator(selectOperator.getText());
        }
        if (!StringUtils.isEmpty(selectAccountType.getText())) {
            accountInfo.setAccountType(selectAccountType.getText());
        }
        if (!StringUtils.isEmpty(selectAccountVoucher.getText())) {
            accountInfo.setAccountVoucher(selectAccountVoucher.getText());
        }
        if (!StringUtils.isEmpty(selectAccountNumber.getText())) {
            accountInfo.setAccountNumber(selectAccountNumber.getText());
        }
        if (!StringUtils.isEmpty(selectAccountDebit.getText())) {
            accountInfo.setAccountDebit(new BigDecimal(selectAccountDebit.getText()));
        }
        if (!StringUtils.isEmpty(selectAccountCredit.getText())) {
            accountInfo.setAccountCredit(new BigDecimal(selectAccountCredit.getText()));
        }
        if (!StringUtils.isEmpty(selectBalance.getText())) {
            accountInfo.setBalance(new BigDecimal(selectBalance.getText()));
        }
        List<AccountInfo> accountInfoList = accountInfoService.queryAccountInfoByParam(accountInfo);
        System.out.println(accountInfoList);
        initializeAccountInfoTable(accountInfoList);
    }

    public List<AccountInfoVo> getAccountInfoVo(List<AccountInfo> accountInfoList) {

        ObservableList<AccountInfoVo> accountInfoResult;
        List<AccountInfoVo> accountInfoVoList = new ArrayList<>();
        accountInfoList.forEach(o -> {
            Button deleteButton = new Button("删除");
            Button updateButton = new Button("修改");
            deleteButton.setOnMouseClicked(event -> {
                accountInfoService.deleteAccountInfo(o);
                queryAccountInfoByParam();
//                System.out.println("删除");
            });
            updateButton.setOnMouseClicked(event -> {
                openDialog(o);
                queryAccountInfoByParam();
            });
            HBox hBox = new HBox(10, updateButton, deleteButton);
            hBox.setAlignment(Pos.BASELINE_CENTER);
            accountInfoVoList.add(new AccountInfoVo(o, hBox));
        });
        accountInfoResult = observableArrayList(accountInfoVoList);
        return accountInfoResult;
    }

    private void openDialog(AccountInfo o) {
        try {
            System.out.println(userUtil.getUserType());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AccountDialogView.fxml"));
            Parent parent = null;
            parent = loader.load();
            AccountDialogController target = loader.getController();
            setValue(o, target);
            Scene scene = new Scene(parent);
            Stage newstage = new Stage();
            newstage.setScene(scene);
            newstage.setResizable(false);
            newstage.initModality(Modality.APPLICATION_MODAL);
            newstage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setValue(AccountInfo o, AccountDialogController target) {
        target.accountTime.setValue(DateUtils.dateToLocalDate(o.getAccountTime()));
        target.accountTime.setDisable(true);
        target.accountName.setValue(o.getAccountName());
        target.accountItem.setValue(o.getAccountItem());
        target.itemName.setText(o.getItemName());
        target.operator.setText(o.getOperator());
        target.accountType.setValue(o.getAccountType());
        target.accountNumber.setText(o.getAccountNumber());
        target.accountVoucher.setText(o.getAccountVoucher());
        target.accountVoucher.setDisable(true);
        target.itemDetail.setText(o.getItemDetail());
        target.accountCredit.setText(o.getAccountCredit().toString());
        target.accountDebit.setText(o.getAccountDebit().toString());
    }

    public void initializeAccountInfoTable(List<AccountInfo> accountInfoList) {
//        buttonTable.setCellFactory(param -> new TableCell<AccountInfo, AccountInfo>() {
//            private final Button deleteButton = new Button("删除");
//
//            @Override
//            protected void updateItem(AccountInfo accountInfo, boolean empty) {
//                super.updateItem(accountInfo, empty);
//
//                if (accountInfo == null) {
//                    setGraphic(null);
//                    return;
//                }
//
//                setGraphic(deleteButton);
//                deleteButton.setOnAction(
//                        event -> getTableView().getItems().remove(accountInfo)
//                );
//            }
//        });
//        accountIdResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, String>("accountId"));
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
        hBox.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, HBox>("hBox"));
        accountInfoTable.getItems().setAll(getAccountInfoVo(accountInfoList));
    }

    /**
     * 按条件导出信息
     */
    public void exportAccountInfoByParam() {
        AccountInfo accountInfo = new AccountInfo();
        if (!StringUtils.isEmpty(selectAccountTime.getValue())) {
            accountInfo.setAccountTime(DateUtils.getDateBuLocalDate(selectAccountTime.getValue()));
        }
        if (!StringUtils.isEmpty(selectAccountName.getText())) {
            accountInfo.setAccountName(selectAccountName.getText());
        }
        if (!StringUtils.isEmpty(selectAccountItem.getText())) {
            accountInfo.setAccountItem(selectAccountItem.getText());
        }
        if (!StringUtils.isEmpty(selectItemDetail.getText())) {
            accountInfo.setItemDetail(selectItemDetail.getText());
        }
        if (!StringUtils.isEmpty(selectItemName.getText())) {
            accountInfo.setItemName(selectItemName.getText());
        }
        if (!StringUtils.isEmpty(selectOperator.getText())) {
            accountInfo.setOperator(selectOperator.getText());
        }
        if (!StringUtils.isEmpty(selectAccountType.getText())) {
            accountInfo.setAccountType(selectAccountType.getText());
        }
        if (!StringUtils.isEmpty(selectAccountVoucher.getText())) {
            accountInfo.setAccountVoucher(selectAccountVoucher.getText());
        }
        if (!StringUtils.isEmpty(selectAccountNumber.getText())) {
            accountInfo.setAccountNumber(selectAccountNumber.getText());
        }
        if (!StringUtils.isEmpty(selectAccountDebit.getText())) {
            accountInfo.setAccountDebit(new BigDecimal(selectAccountDebit.getText()));
        }
        if (!StringUtils.isEmpty(selectAccountCredit.getText())) {
            accountInfo.setAccountCredit(new BigDecimal(selectAccountCredit.getText()));
        }
        if (!StringUtils.isEmpty(selectBalance.getText())) {
            accountInfo.setBalance(new BigDecimal(selectBalance.getText()));
        }
        accountInfoService.exportAccountInfoByParam(accountInfo);
    }

    public void openAccountAdd() {
        try {
            System.out.println(userUtil.getUserType());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AccountAddView.fxml"));
            Parent parent = null;
            parent = loader.load();
            Scene scene = new Scene(parent);
            Stage newstage = new Stage();
            newstage.setScene(scene);
            newstage.setResizable(false);
            newstage.initModality(Modality.APPLICATION_MODAL);
            newstage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
