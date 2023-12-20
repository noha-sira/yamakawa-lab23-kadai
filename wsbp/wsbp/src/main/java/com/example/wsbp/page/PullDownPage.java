package com.example.wsbp.page;

import com.example.wsbp.data.AuthUser;
import com.example.wsbp.service.IUserService;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("PullDown")
public class PullDownPage extends WebPage {

    @SpringBean
    private IUserService userService;

    public PullDownPage() {

        // プルダウンメニューに表示するためのリストのModelを作成
        // ここでは、LoadableDetachableModelを利用している。
        // このModelは、Pageの表示準備の開始から終了の間までデータをキャッシュし、表示準備の終了時にデータを破棄する。
        // DBから取得するデータは、大量になる場合がある。
        // そのような場合、LoadableDetachableModelを利用することで、データの取得とキャッシュ（使い回し）、最新化、メモリ節約の管理を自動化できる。
        var selectionModel = LoadableDetachableModel.of(() -> userService.findAuthUsers());
        // プルダウンメニューから選択したものを入れるためのModelを作成
        // インスタンス名@ハッシュコード（データベースに保存されてるユーザ名がある場所）が帰ってくる
        var selectedModel = new Model<AuthUser>();

        var form = new Form<>("form"){
            @Override
            protected void onSubmit() {
                // 選択肢の値が送信されたときの処理
                System.out.println(selectedModel.getObject().toString());
            }
        };
        add(form);

        // 第1引数の文字列は、AuthUserのuserNameを選択肢の表示用として取り出すことを設定
        var renderer = new ChoiceRenderer<>("userName");

        // プルダウンメニューを作成するためのDropDownChoiceコンポーネント
        // 第1引数は wicket:id, 第2引数は選択したものを入れるためのModel, 第3引数は表示するためのリストのModel,
        // (WicketId, 選んだもの用Model, 選択肢のリスト用Model, 選択肢に表示する値の設定);
        // 選んだもの用Modelを省略する記述方法もある
        var userSelection = new DropDownChoice<>("userSelection", selectedModel, selectionModel, renderer){
            @Override
            protected void onInitialize() {
                // このDropDownChoiceの初期化用の処理
                super.onInitialize();
                // 必ず空欄の選択肢を用意するように設定
                setNullValid(true);
                setRequired(true);
                // エラーメッセージに表示する名前を設定
                setLabel(Model.of("学籍番号の選択肢"));
            }

            @Override
            protected String getNullValidDisplayValue() {
                // 何も選択されていない状態の表示用の文字列を設定
                // .propertiesファイルに nullValid の値を設定できるようになっているが、面倒なのでデフォルトをとってくる
                return getNullKeyDisplayValue();
            }
        };
        form.add(userSelection);

        var feedback = new FeedbackPanel("feedback");
        form.add(feedback);

    }
}