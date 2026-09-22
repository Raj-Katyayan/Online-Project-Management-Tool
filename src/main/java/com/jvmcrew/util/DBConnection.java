package com.jvmcrew.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {

    private static final Properties PROPERTIES = loadProperties();

    private DBConnection() {
    }

    private static Properties loadProperties() {

        Properties properties = new Properties();

        try (InputStream input =
                     DBConnection.class
                             .getClassLoader()
                             .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "db.properties was not found in the classpath."
                );
            }

            properties.load(input);

            return properties;

        } catch (IOException e) {

            throw new ExceptionInInitializerError(
                    "Unable to load db.properties: "
                            + e.getMessage()
            );
        }
    }

    public static Connection getConnection()
            throws SQLException {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {

            throw new SQLException(
                    "MySQL JDBC Driver not found.",
                    e
            );
        }

        return DriverManager.getConnection(
                PROPERTIES.getProperty("db.url"),
                PROPERTIES.getProperty("db.username"),
                PROPERTIES.getProperty("db.password")
        );
    }
}