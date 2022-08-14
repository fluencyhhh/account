package com.zhangjingbo.account;

import com.zhangjingbo.account.controller.AccountDialogController;
import com.zhangjingbo.account.view.AccountDialogView;
import com.zhangjingbo.account.view.LoginView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        //TODO:判断是否已启动
        launch(AccountApplication.class, LoginView.class,args);
//        launch(AccountApplication.class, MainAccountView.class,args);
    }

}
