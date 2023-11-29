package com.example.wsbp.repository;

public interface IChatRepository {
    /**
     * ユーザー名とパスワードをChatテーブルに記録する
     *
     * @param userName ユーザー名
     * @param msgBody チャット内容
     * @return データベースの更新行数
     */
    public int insert(String userName, String msgBody);

}
