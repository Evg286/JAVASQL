package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends Database{

    private static MySQL instance;

    public static MySQL getInstance() throws SQLException {
        if(instance == null){
            instance = new MySQL();
        }
        return instance;
    }

    private MySQL() throws SQLException {
        setConnection();
    }

    private void setConnection() throws SQLException {
        connection = DriverManager.getConnection(
                DBConfig.getProperty("url"),
                DBConfig.getProperty("user"),
                DBConfig.getProperty("pass")
        );
    }

    public Connection getConnection() throws SQLException {

        if(!checkConnection())
            setConnection();

        return connection;
    }
}
