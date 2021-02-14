package com.spatial.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

public class DBConnection {
    private Connection conn;
    private String jdbcUrl = null;
    private String userid = null;
    private String password = null;
    private boolean change;


    public DBConnection() {}
    public DBConnection(String jdbcUrl, String userid, String password){
        this.jdbcUrl = jdbcUrl;
        this.userid = userid;
        this.password = password;
        this.change = true;
    }

    public void connect() throws SQLException {
        if(change) {
            OracleDataSource ds;
            ds = new OracleDataSource();
            ds.setURL(jdbcUrl);
            conn = ds.getConnection(userid,password);
            change = false;
        }
    }

    public void close() throws SQLException {
        if(!conn.isClosed()) {
            conn.close();
            change = true;
        }
    }

    public Connection getConn() {
        return conn;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        change = true;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
        change = true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        change = true;
    }
}
