package com.example.wsbp.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("UserMaker")
public class UserMakerPage extends WebPage {

    public UserMakerPage() {

        var toHomePage = new BookmarkablePageLink<>("toHome", HomePage.class);
        add(toHomePage);

    }

}