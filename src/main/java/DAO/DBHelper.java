package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    public static final String URL = "jdbc:mysql://localhost:3306/medicine";
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";
    //定义获取数据库连接的静态方法
    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        Class.forName(DRIVER_CLASS);//加载数据库驱动类
        return DriverManager.getConnection(URL,USERNAME,PASSWORD);
    }
}
