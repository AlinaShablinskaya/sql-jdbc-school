package ua.com.foxminded.school.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import ua.com.foxminded.school.validator.DataBaseRuntimeException;

public class ConnectionFactory {  
    private final HikariDataSource hikariDataSource;

    public ConnectionFactory(String fileConfig) {
        HikariConfig hikariConfig = new HikariConfig(fileConfig);
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public Connection connect() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            throw new DataBaseRuntimeException(e);
        }
    }
}
