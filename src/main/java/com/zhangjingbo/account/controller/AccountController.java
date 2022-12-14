package com.zhangjingbo.account.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhangjingbo.account.entity.AccountInfo;
import com.zhangjingbo.account.form.AccountInfoForm;
import com.zhangjingbo.account.service.AccountInfoService;
import com.zhangjingbo.account.util.BeanUtils;
import com.zhangjingbo.account.util.DateUtils;
import com.zhangjingbo.account.util.UserUtil;
import com.zhangjingbo.account.vo.AccountInfoVo;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

@FXMLController
public class AccountController implements Initializable {

    @FXML
    private DatePicker selectAccountTimeStart;

    @FXML
    private DatePicker selectAccountTimeEnd;

    @FXML
    private ChoiceBox selectAccountName;

    @FXML
    private ChoiceBox selectAccountItem;

    @FXML
    private TextField selectItemDetail;

    @FXML
    private TextField selectItemName;

    @FXML
    private TextField selectOperator;

    @FXML
    private TextField selectAccountVoucher;

    @FXML
    private TextField selectAccountNumber;

    @FXML
    private TextField selectAccountDebit;

    @FXML
    private TextField selectAccountCredit;

    @FXML
    private ChoiceBox selectTimeBetween;

    @FXML
    private ChoiceBox selectAccountType;

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

    @FXML
    private Pagination pagination;

    @FXML
    private Button queryButton;

    @FXML
    private Button queryButton1;

    @FXML
    private HBox otherQueryButton;

    @FXML
    private HBox adminQueryButton;

    @FXML
    private HBox operatorType;

    @FXML
    private ChoiceBox selectOperatorType;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private UserUtil userUtil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        queryButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                queryAccountInfoByParam(1);
                pagination.setPageCount(1);
            }
        });
        queryButton1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                queryAccountInfoByParam(1);
                pagination.setPageCount(1);
            }
        });

        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                queryAccountInfoByParam(param+1);
                return accountInfoTable;
            }
        });
        pagination.setMaxPageIndicatorCount(5);
        selectAccountName.getItems().addAll("?????????", "???????????????", "??????", "??????", "??????", "??????");
        selectAccountName.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setAccountItem(newValue.intValue());
            }
        });
        if(userUtil==null){
            userUtil= BeanUtils.getBean(UserUtil.class);
        }
        if("admin".equals(userUtil.getUserType())){
            selectAccountType.getItems().addAll("??????","?????????","??????","????????????");
        }else {
            selectAccountType.getItems().addAll("????????????","??????");
        }
        selectTimeBetween.getItems().addAll("??????","?????????","?????????");
        if ("admin".equals(userUtil.getUserType())) {
            adminQueryButton.setVisible(true);
            otherQueryButton.setVisible(false);
            operatorType.setVisible(true);
            selectOperatorType.getItems().addAll("??????","??????");
        }else {
            adminQueryButton.setVisible(false);
            otherQueryButton.setVisible(true);
            operatorType.setVisible(false);
        }
        selectOperatorType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                selectAccountType.getItems().clear();
                if (newValue.intValue() == 0) {
                    selectAccountType.getItems().addAll("????????????","??????");
                }else if(newValue.intValue()==1){
                    selectAccountType.getItems().addAll("??????","?????????","??????","????????????");
                }
            }
        });
    }

    /**
     * ??????????????????
     */
    public void queryAllAccountInfo() {
        List<AccountInfo> accountInfoList = accountInfoService.queryAllAccountInfo();
        System.out.println(accountInfoList);
    }

    /**
     * ?????????????????????
     */
    public void queryAccountInfoByParam(Integer pageNo) {
        AccountInfoForm accountInfoForm = new AccountInfoForm();
        if (selectAccountTimeStart.getValue()!=null) {
            accountInfoForm.setAccountTimeStart(DateUtils.localDateToString(selectAccountTimeStart.getValue()));
        }
        if (selectAccountTimeEnd.getValue()!=null) {
            accountInfoForm.setAccountTimeEnd(DateUtils.localDateToString(selectAccountTimeEnd.getValue()));
        }
        if (StringUtils.isNotBlank((String)(selectTimeBetween.getValue()))) {
            accountInfoForm.setAccountTimeBetween((String)(selectTimeBetween.getValue()));
        }
        if(StringUtils.isNotBlank((String)(selectTimeBetween.getValue()))&&(null==userUtil.getUserType()||!"admin".equals(userUtil.getUserType()))){
            if(StringUtils.isNotBlank(accountInfoForm.getAccountTimeStart())&&DateUtils.getCurrYearFirst().compareTo(accountInfoForm.getAccountTimeStart())>0){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("??????");
                alert.setHeaderText(null);
                alert.setContentText("????????????????????????????????????");
                alert.showAndWait();
                return;
            }
            if(StringUtils.isNotBlank(accountInfoForm.getAccountTimeEnd())&&DateUtils.getCurrYearLast().compareTo(accountInfoForm.getAccountTimeEnd())>0){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("??????");
                alert.setHeaderText(null);
                alert.setContentText("????????????????????????????????????");
                alert.showAndWait();
                return;
            }
        }
        if (StringUtils.isNotBlank((String)(selectAccountName.getValue()))) {
            accountInfoForm.setAccountName((String)(selectAccountName.getValue()));
        }

        if (StringUtils.isNotBlank((String)(selectAccountItem.getValue()))) {
            accountInfoForm.setAccountItem((String)(selectAccountItem.getValue()));
        }
        if (StringUtils.isNotBlank(selectItemDetail.getText())) {
            accountInfoForm.setItemDetail(selectItemDetail.getText());
        }
        if (StringUtils.isNotBlank(selectItemName.getText())) {
            accountInfoForm.setItemName(selectItemName.getText());
        }
        if (StringUtils.isNotBlank(selectOperator.getText())) {
            accountInfoForm.setOperator(selectOperator.getText());
        }
        if (StringUtils.isNotBlank((String)(selectAccountType.getValue()))) {
            accountInfoForm.setAccountType((String)(selectAccountType.getValue()));
        }
        if (StringUtils.isNotBlank(selectAccountVoucher.getText())) {
            accountInfoForm.setAccountVoucher(selectAccountVoucher.getText());
        }
        if (StringUtils.isNotBlank(selectAccountNumber.getText())) {
            accountInfoForm.setAccountNumber(selectAccountNumber.getText());
        }
        if (StringUtils.isNotBlank(selectAccountDebit.getText())) {
            accountInfoForm.setAccountDebit(selectAccountDebit.getText());
        }
        if (StringUtils.isNotBlank(selectAccountCredit.getText())) {
            accountInfoForm.setAccountCredit(selectAccountCredit.getText());
        }
        if ("admin".equals(userUtil.getUserType())) {
            if (StringUtils.isNotBlank((String) (selectOperatorType.getValue()))) {
                accountInfoForm.setOperatorType((String) (selectOperatorType.getValue()));
            }
        }else {
            accountInfoForm.setOperatorType("??????");
        }
        Page<AccountInfo> page=accountInfoService.queryAccountInfoByParam(accountInfoForm,pageNo,20);
        List<AccountInfo> accountInfoList = page.getResult();
        initializeAccountInfoTable(accountInfoList);
        pagination.setPageCount(page.getPages());
    }

    public List<AccountInfoVo> getAccountInfoVo(List<AccountInfo> accountInfoList) {

        ObservableList<AccountInfoVo> accountInfoResult;
        List<AccountInfoVo> accountInfoVoList = new ArrayList<>();
        accountInfoList.forEach(o -> {
            Button deleteButton = new Button("??????");
            Button updateButton = new Button("??????");
            deleteButton.setOnMouseClicked(event -> {
                accountInfoService.deleteAccountInfo(o);
                queryAccountInfoByParam(pagination.getCurrentPageIndex()+1);
//                System.out.println("??????");
            });
            updateButton.setOnMouseClicked(event -> {
                openDialog(o);
                queryAccountInfoByParam(pagination.getCurrentPageIndex()+1);
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
        target.accountCredit.setText(o.getAccountCredit()!=null?o.getAccountCredit().toString():null);
        target.accountDebit.setText(o.getAccountDebit()!=null?o.getAccountDebit().toString():null);
        if("??????".equals(o.getOperatorType())){
            target.accountType.getItems().addAll("??????","?????????","??????","????????????");
        } else if ("??????".equals(o.getOperatorType())) {
            target.accountType.getItems().addAll("????????????","??????");
        }
        target.oldYear.setText(o.getOldYear());
        target.oldName.setValue(o.getOldName());
        target.oldVoucher.setText(o.getOldVoucher());
        if("??????".equals(o.getAccountItem())||"???????????????".equals(o.getAccountItem())||"???????????????".equals(o.getAccountItem())){
            target.oldHbox1.setVisible(true);
            target.oldHbox2.setVisible(true);
            target.oldHbox3.setVisible(true);
        }
    }

    public void initializeAccountInfoTable(List<AccountInfo> accountInfoList) {
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
        if ("admin".equals(userUtil.getUserType())) {
            balanceResult.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, Integer>("balance"));
        }
        hBox.setCellValueFactory(new PropertyValueFactory<AccountInfoVo, HBox>("hBox"));
        accountInfoTable.getItems().setAll(getAccountInfoVo(accountInfoList));

    }

    /**
     * ?????????????????????
     */
    public void exportAccountInfoByParam() {
        AccountInfoForm accountInfoForm = new AccountInfoForm();
        if (selectAccountTimeStart.getValue()!=null) {
            accountInfoForm.setAccountTimeStart(DateUtils.localDateToString(selectAccountTimeStart.getValue()));
        }
        if (selectAccountTimeEnd.getValue()!=null) {
            accountInfoForm.setAccountTimeEnd(DateUtils.localDateToString(selectAccountTimeEnd.getValue()));
        }
        if (StringUtils.isNotBlank((String)(selectTimeBetween.getValue()))) {
            accountInfoForm.setAccountTimeBetween((String)(selectTimeBetween.getValue()));
        }
        if(StringUtils.isNotBlank((String)(selectTimeBetween.getValue()))&&(null==userUtil.getUserType()||!"admin".equals(userUtil.getUserType()))){
            if(StringUtils.isNotBlank(accountInfoForm.getAccountTimeStart())&&DateUtils.getCurrYearFirst().compareTo(accountInfoForm.getAccountTimeStart())>0){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("??????");
                alert.setHeaderText(null);
                alert.setContentText("????????????????????????????????????");
                alert.showAndWait();
                return;
            }
            if(StringUtils.isNotBlank(accountInfoForm.getAccountTimeEnd())&&DateUtils.getCurrYearLast().compareTo(accountInfoForm.getAccountTimeEnd())>0){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("??????");
                alert.setHeaderText(null);
                alert.setContentText("????????????????????????????????????");
                alert.showAndWait();
                return;
            }
        }
        if (StringUtils.isNotBlank((String)(selectAccountName.getValue()))) {
            accountInfoForm.setAccountName((String)(selectAccountName.getValue()));
        }

        if (StringUtils.isNotBlank((String)(selectAccountItem.getValue()))) {
            accountInfoForm.setAccountItem((String)(selectAccountItem.getValue()));
        }
        if (StringUtils.isNotBlank(selectItemDetail.getText())) {
            accountInfoForm.setItemDetail(selectItemDetail.getText());
        }
        if (StringUtils.isNotBlank(selectItemName.getText())) {
            accountInfoForm.setItemName(selectItemName.getText());
        }
        if (StringUtils.isNotBlank(selectOperator.getText())) {
            accountInfoForm.setOperator(selectOperator.getText());
        }
        if (StringUtils.isNotBlank((String)(selectAccountType.getValue()))) {
            accountInfoForm.setAccountType((String)(selectAccountType.getValue()));
        }
        if (StringUtils.isNotBlank(selectAccountVoucher.getText())) {
            accountInfoForm.setAccountVoucher(selectAccountVoucher.getText());
        }
        if (StringUtils.isNotBlank(selectAccountNumber.getText())) {
            accountInfoForm.setAccountNumber(selectAccountNumber.getText());
        }
        if (StringUtils.isNotBlank(selectAccountDebit.getText())) {
            accountInfoForm.setAccountDebit(selectAccountDebit.getText());
        }
        if (StringUtils.isNotBlank(selectAccountCredit.getText())) {
            accountInfoForm.setAccountCredit(selectAccountCredit.getText());
        }
        if ("admin".equals(userUtil.getUserType())) {
            if (StringUtils.isNotBlank((String) (selectOperatorType.getValue()))) {
                accountInfoForm.setOperatorType((String) (selectOperatorType.getValue()));
            }
        }else {
            accountInfoForm.setOperatorType("??????");
        }

        accountInfoService.exportAccountInfoByParam(accountInfoForm);
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

    private void setAccountItem(int number) {
        selectAccountItem.getItems().clear();
        switch (number) {
            case 0:
                selectAccountItem.getItems().addAll("????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "???????????????", "????????????", "????????????", "????????????", "???????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "?????????", "?????????", "??????", "??????");
                break;
            case 1:
                selectAccountItem.getItems().addAll("????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "???????????????", "????????????", "????????????", "????????????", "???????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "????????????", "?????????", "?????????", "??????", "??????");
                break;
            case 2:
                selectAccountItem.getItems().addAll("??????","??????","??????","??????");
                break;
            case 3:
                selectAccountItem.getItems().addAll("?????????","??????","??????");
                break;
            case 4:
                selectAccountItem.getItems().addAll("??????","??????","??????","?????????","???????????????","?????????","???????????????");
                break;
            case 5:
                selectAccountItem.getItems().addAll("??????","??????");
                break;
            default:selectAccountItem.getItems().addAll();
        }
    }

    public void cleanQuery(ActionEvent actionEvent) {
        selectAccountTimeStart.setValue(null);
        selectAccountTimeEnd.setValue(null);
        selectAccountName.setValue(null);
        selectAccountItem.setValue(null);
        selectItemDetail.setText(null);
        selectItemName.setText(null);
        selectAccountItem.setValue(null);
        selectOperator.setText(null);
        selectAccountVoucher.setText(null);
        selectAccountNumber.setText(null);
        selectAccountCredit.setText(null);
        selectAccountDebit.setText(null);
        selectTimeBetween.setValue(null);
        selectOperatorType.setValue(null);
    }
}
