package com.example.wsbp.repository;

import com.example.wsbp.data.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatRepository implements IChatRepository{
    //chatテーブル用

    // SpringJDBCのデータベース制御用インスタンス
    private final JdbcTemplate jdbc;

    // jdbc の di/ioc 設定（Wicketとやり方が異なるので注意）
    @Autowired
    public ChatRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int insert(String userName, String msgBody) {
        var sql = "insert into chat values (?, ?)";
        var n = jdbc.update(sql, userName, msgBody);
        return n;
    }

    @Override
    public List<Chat> find() {
        // chat テーブルの user_name, msg_body を検索する
        String sql = "select user_name, msg_body from chat";

        // 検索用のSQLを実行する方法。
        // 取り出したいデータの型によって、第2引数の RowMapper を切り替える。
        // ? を使うSQLであれば、第3引数の Object型配列 の要素に順番に設定する。
        List<Chat> chats = jdbc.query(sql,
                DataClassRowMapper.newInstance(Chat.class));

        // 取り出したデータ（Listの要素）をそのまま返値とする。
        return chats;
    }

}
