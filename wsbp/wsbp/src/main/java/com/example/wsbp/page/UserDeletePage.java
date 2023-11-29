package com.example.wsbp.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.wicketstuff.annotation.mount.MountPath;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;

import org.apache.wicket.model.Model;

import java.sql.SQLOutput;
import java.util.logging.SocketHandler;

import org.apache.wicket.spring.injection.annot.SpringBean;
import com.example.wsbp.service.IUserService;

@MountPath("UserDelete")
public class UserDeletePage extends WebPage {

    //IUserService を IoC/DI する
    @SpringBean
    private IUserService userService;

    public UserDeletePage() {
        var userNameModel = Model.of("");


        var toHomeLink = new BookmarkablePageLink<>("toHome", HomePage.class);
        add(toHomeLink);

        //配置したFormコンポーネントを匿名クラス化して処理を上書きする
        var userInfoForm = new Form<Void>("userInfo"){
            @Override
            protected void onSubmit() {
                var userName = userNameModel.getObject();
                var msg = "送信データ： "
                        + userName;
                System.out.println(msg);

                // IoC/DI した userService のメソッドを呼び出す
                userService.removeUser(userName);
                //移動先のPageクラスのコンストラクタ引数を使って、モデルを渡している
                setResponsePage(new UserDeleteCompPage(userNameModel));
            }
        };
        add(userInfoForm);

        var userNameField = new TextField<>("userName", userNameModel);
        userInfoForm.add(userNameField);

        //ここでは、 userNameField, userPassField 用に用意したモデルの中身を標準出力に表示している。送信ボタンが押されると以下の様に処理が実行される。
        //ブラウザからフォームの入力値がサーバへ送信される
        //サーバの Wicket が情報をうけとり、 userNameField, userPassField のコンポーネントを通じて、それぞれのモデルにフォームの入力値が入る
        //userInfoForm の匿名クラスで onSubmit(){...} が実行されて、それぞれのモデルの中に入った値が標準出力に表示される




    }

}