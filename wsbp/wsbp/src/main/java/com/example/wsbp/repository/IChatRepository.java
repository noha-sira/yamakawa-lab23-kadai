package com.example.wsbp.repository;

import com.example.wsbp.data.Chat;

import java.util.List;

public interface IChatRepository {
    /**
     * ユーザー名とパスワードをChatテーブルに記録する
     *
     * @param userName ユーザー名
     * @param msgBody チャット内容
     * @return データベースの更新行数
     */
    public int insert(String userName, String msgBody);

    /**
     * Chatテーブルのすべての情報を検索する
     *
     * @return レコードの内容を {@link Chat} の {@link List} で返す
     */
    public List<Chat> find();

}
