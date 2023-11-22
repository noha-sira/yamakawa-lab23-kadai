package com.example.wsbp.page;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import org.apache.wicket.markup.html.WebPage;
import org.wicketstuff.annotation.mount.MountPath;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.example.wsbp.service.ISampleService;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;

//Wicket-Spring-Bootが表示する最初のページに設定
@WicketHomePage
//HomePageクラスとそのHTMLを http://.../Home というURLでアクセスできるよう、URLのファイルパス部を設定する
@MountPath("Home")
//Wicketが提供する WebPage クラスを継承することで、同じ名前のHTMLファイルを書き換える能力をもつ
public class HomePage extends WebPage {

    // インターフェース型の変数をIoC/DI用に設定する
    // SpringはIoC/DI用に設定されたフィールドに、実装クラスを自動でインスタンス化する
    @SpringBean
    private ISampleService service;

    public HomePage() {
        //
        var youModel = Model.of("Wicket-Spring-Boot");
        var youLabel = new Label("you", youModel);
        add(youLabel);

        var gakusekiModel = Model.of("B2212670");
        var gakusekiLabel = new Label("gakuseki", gakusekiModel);
        add(gakusekiLabel);

        var nameModel = Model.of("ｘｘ");
        var nameLabel = new Label("name", nameModel);
        add(nameLabel);

        var timeModel = Model.of(service.makeCurrentHMS());
        var timeLabel = new Label("time", timeModel);
        add(timeLabel);

        var randModel = Model.of(service.makeRandInt());
        var randLabel = new Label("rand", randModel);
        add(randLabel);

        //toUserMaker のタグを UserMakerPage へのリンクに書き換えるようにする
        var toUserMakerLink = new BookmarkablePageLink<>("toUserMaker", UserMakerPage.class);
        add(toUserMakerLink);

        //toUserDelete のタグを UserDeletePage へのリンクに書き換えるようにする
        var toUserDeleteLink = new BookmarkablePageLink<>("toUserDelete", UserDeletePage.class);
        add(toUserDeleteLink);


    }

}