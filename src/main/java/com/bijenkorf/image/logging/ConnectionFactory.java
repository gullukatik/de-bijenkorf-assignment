package com.bijenkorf.image.logging;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Value;

public class ConnectionFactory {

    @Value("${logdb.endpoint}")
    private String endpoint;

    @Value("${logdb.username}")
    private String username;

    @Value("${logdb.password}")
    private String password;

    private interface Singleton {
        ConnectionFactory INSTANCE = new ConnectionFactory();
    }

    private DataSource dataSource;

    private ConnectionFactory() {

        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);

        GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>();
        DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                endpoint, properties
        );

        new PoolableConnectionFactory(
                connectionFactory, pool, null, "SELECT 1", 3, false, false, Connection.TRANSACTION_READ_COMMITTED
        );

        this.dataSource = new PoolingDataSource(pool);
    }

    public static Connection getConnection() throws SQLException {
        return Singleton.INSTANCE.dataSource.getConnection();
    }
}