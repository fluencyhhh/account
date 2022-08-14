package com.zhangjingbo.account.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class AccountDialogController implements Initializable {

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



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
