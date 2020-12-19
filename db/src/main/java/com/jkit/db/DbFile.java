package com.jkit.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class DbFile {

    String filePath;
    String jdbcUri;

    public static DbFile create(
        String filePath
    ) {
        return DbFile.of(
            filePath,
            "jdbc:h2:" + filePath + ";AUTO_SERVER=TRUE;"
        );
    }

    public Connection getSingleConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUri, "sa", "sa");
    }

    public HikariConfig getConfig() {
        val config = new HikariConfig();

        config.setJdbcUrl(this.getJdbcUri());
        config.setAutoCommit(true);
        config.setUsername("sa");
        config.setPassword("sa");
        config.setInitializationFailTimeout(0);
        config.setConnectionTimeout(3000);
        return config;
    }

    public HikariDataSource createHikariDataSource() {
        return new HikariDataSource(this.getConfig());
    }

    public JdbcDataSource createJdbcDataSource() {
        val ds = new JdbcDataSource();
        ds.setURL(this.jdbcUri);
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }

}
