package com.example.wsbp.page;

import com.example.wsbp.MySession;
import com.example.wsbp.data.Chat;
import com.example.wsbp.page.signed.SignedPage;
import com.example.wsbp.service.IUserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.wicketstuff.annotation.mount.MountPath;
import org.apache.wicket.markup.html.list.ListView;

@AuthorizeInstantiation(Roles.USER)
@MountPath("Chat")
public class ChatPage extends WebPage {

    // ServiceをIoC/DIする
    @SpringBean
    private IUserService userService;


    public ChatPage(IModel<String> userNameModel) {
        var userNameLabel = new Label("userName", userNameModel);
        add(userNameLabel);
        var msgBodyModel = Model.of("");

        var chatsModel = Model.ofList(userService.findChats());

//        var toSignedPageLink = new BookmarkablePageLink<>("toSignedPage", SignedPage.class);
//        add(toSignedPageLink);

        Link<Void> signoutLink = new Link<Void>("signout") {

            @Override
            public void onClick() {
                // セッションの破棄
                MySession.get().invalidate();
                // SignPageへ移動
                setResponsePage(SignPage.class);
            }
        };
        add(signoutLink);

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

                // チャットのリストを更新する
                chatsModel.getObject().clear();
                chatsModel.getObject().addAll(userService.findChats());

                // Ajaxでフォームを再描画する
                RequestCycle requestCycle = RequestCycle.get();
                AjaxRequestTarget target = requestCycle.find(AjaxRequestTarget.class).orElse(null);

                if (target != null) {

                    target.add(ChatPage.this);
                }

            }
        };
        add(chatInfoForm);

        var msgBodyField = new TextField<>("msgBody", msgBodyModel) {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                // 文字列の長さを1〜140文字に制限するバリデータ
                add(StringValidator.lengthBetween(1, 140));
            }
        };
        chatInfoForm.add(msgBodyField);

        // Service からデータベースのユーザ一覧をもらい、Modelにする
        // List型のモデルは Model.ofList(...) で作成する。
        // なお、DBや外部のWEB-APIなどのデータを取得する場合、通常はLoadableDetachableModelを利用する
        // 参考：https://ci.apache.org/projects/wicket/guide/9.x/single.html#_detachable_models


        // List型のモデルを表示する ListView
        var chatsLV = new ListView<>("chats", chatsModel) {
            @Override
            protected void populateItem(ListItem<Chat> listItem) {
                // List型のモデルから、 <li>...</li> ひとつ分に分けられたモデルを取り出す
                var itemModel = listItem.getModel();
                var chat = itemModel.getObject(); // 元々のListの n 番目の要素

                // インスタンスに入れ込まれたデータベースの検索結果を、列（＝フィールド変数）ごとにとりだして表示する
                // add する先が listItem になることに注意。
                var userNameModel = Model.of(chat.getUserName());
                var userNameLabel = new Label("userName", userNameModel);
                listItem.add(userNameLabel);

                var msgBodyModel = Model.of(chat.getMsgBody());
                var msgBodyLabel = new Label("msgBody", msgBodyModel);
                listItem.add(msgBodyLabel);
            }
        };
        add(chatsLV);


    }
}