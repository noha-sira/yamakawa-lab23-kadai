package com.example.wsbp.page;

import com.example.wsbp.MySession;
import com.example.wsbp.page.signed.SignedPage;
import com.example.wsbp.service.IUserService;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.w3c.dom.ls.LSOutput;
import org.wicketstuff.annotation.mount.MountPath;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.example.wsbp.service.IUserService;

import java.util.Objects;

@AuthorizeInstantiation(Roles.USER)
@MountPath("Chat")
public class ChatPage extends WebPage {

    // ServiceをIoC/DIする
    @SpringBean
    private IUserService userService;


    public ChatPage(IModel<String> userNameModel) {
        var msgBodyModel = Model.of("");

//        var toSignedPageLink = new BookmarkablePageLink<>("toSinedPage", SignedPage.class);
//        add(toSignedPageLink);

//        Link<Void> signoutLink = new Link<Void>("signout") {
//
//            @Override
//            public void onClick() {
//                // セッションの破棄
//                MySession.get().invalidate();
//                // SignPageへ移動
//                setResponsePage(SignPage.class);
//            }
//        };
//        add(signoutLink);

        System.out.println("ok1");

        var chatInfoForm = new Form<Void>("chatInfo") {

            @Override
            protected void onSubmit() {
                var userName = userNameModel.getObject();

                var msgBody = msgBodyModel.getObject();
                System.out.println("ok2");
                var msg = "送信データ： "
                        + userName
                        + ", "
                        + msgBody;
                System.out.println(msg);

                // IoC/DI した userService のメソッドを呼び出す
                userService.registerChat(userName, msgBody);

            }
        };
        add(chatInfoForm);

        var userNameLabel = new Label("userName", userNameModel);
        chatInfoForm.add(userNameLabel);

        var msgBodyField = new TextField<>("msgBody", msgBodyModel) {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                // 文字列の長さを1〜140文字に制限するバリデータ
                add(StringValidator.lengthBetween(1, 140));
            }
        };
        chatInfoForm.add(msgBodyField);
    }
}