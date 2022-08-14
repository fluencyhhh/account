package com.zhangjingbo.account.controller;

import com.zhangjingbo.account.AccountApplication;
import com.zhangjingbo.account.service.LoginService;
import com.zhangjingbo.account.util.UserUtil;
import com.zhangjingbo.account.view.MainAccountView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class LoginController implements Initializable {

    private ResourceBundle resourceBundle;
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label noUsername;

    @FXML
    private Label noPassword;



    @Autowired
    private LoginService loginService;
    @Autowired
    private UserUtil userUtil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage stage=AccountApplication.getStage();
        stage.setResizable(false);
        stage.setTitle("账务管理系统");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                loginService.loginout();
                System.out.println("关闭前执行");
                Platform.exit();
                System.exit(0);
            }
        });
    }

    private void jumpTest() {
        AccountApplication.showView(MainAccountView.class);
        Stage stage=AccountApplication.getStage();
        stage.setWidth(1720.0);
        stage.setHeight(1006);
        stage.setResizable(true);
        stage.centerOnScreen();
        System.out.println(userUtil.getUserType());
    }


    public void login(MouseEvent mouseEvent) {
        System.out.println("登录");
        boolean isCouldLogin=true;
        if(StringUtils.isBlank(username.getText())){
            noUsername.setVisible(true);
            isCouldLogin=false;
        }else {
            noUsername.setVisible(false);
            isCouldLogin=true;
        }
        if (StringUtils.isBlank(password.getText())) {
            noPassword.setVisible(true);
            isCouldLogin=false;
        }else {
            noPassword.setVisible(false);
            isCouldLogin=true;
        }
        if (isCouldLogin) {
            boolean islogin=loginService.login(username.getText(),password.getText());
            if (islogin) {
                jumpTest();
            }else {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setContentText("账号或密码错误");
                alert.showAndWait();
            }
        }
    }
}
