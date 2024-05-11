package com.principal.trabajofindegrado;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static final String URL = "jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11705797";
    private static final String USER = "sql11705797";
    private static final String PASSWORD = "AkZdklshbM";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Error al conectar a la base de datos.");
        }
    }
}

