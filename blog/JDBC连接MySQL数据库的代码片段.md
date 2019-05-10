---
title: JDBC连接MySQL数据库的代码片段
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---

```
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author kangyonggan
 * @since 4/25/17
 */
public class ExcelParse605 {

    private static String url = "jdbc:mysql://127.0.0.1:3306/dfjz?useUnicode=true&characterEncoding=UTF-8";
    private static String username = "root";
    private static String password = "123456";

    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            ps = conn.prepareStatement("SELECT bnk_resp_co FROM be_resp WHERE bnk_no = '605'");
            rs = ps.executeQuery();
            while (rs.next()) {
                String bnkRespCo = rs.getString("bnk_resp_co");
                System.out.println(bnkRespCo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
```