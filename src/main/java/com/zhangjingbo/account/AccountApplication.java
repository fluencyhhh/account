package com.zhangjingbo.account;

import com.zhangjingbo.account.view.MainAccountView;
import com.zhangjingbo.account.view.PrimaryStageView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(AccountApplication.class, MainAccountView.class,args);
    }

}
