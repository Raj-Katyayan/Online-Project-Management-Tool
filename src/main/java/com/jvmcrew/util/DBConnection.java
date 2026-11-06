package com.jvmcrew.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Central JDBC connection factory.
 *
 * Configuration priority:
 * 1. DB_URL, DB_USERNAME and DB_PASSWORD environment variables.
 * 2. A properties file selected through DB_CONFIG_FILE.
 * 3. A classpath db.properties file (useful for local development only).
 *
 * The Maven WAR build excludes WEB-INF/classes/db.properties so local database
 * credentials are not copied into the deployable WAR.
 */
public final class DBConnection {

    private DBConnection() {
        // Utility class; do not instantiate.
    }

    public static Connection getConnection() throws SQLException {

        Properties properties = loadProperties();

        String url = firstNonBlank(
                System.getenv("DB_URL"),
                properties.getProperty("db.url")
        );

        String username = firstNonBlank(
                System.getenv("DB_USERNAME"),
                properties.getProperty("db.username")
        );

        String password = firstNonBlank(
                System.getenv("DB_PASSWORD"),
                properties.getProperty("db.password")
        );

        if (isBlank(url) || isBlank(username) || isBlank(password)) {
            throw new SQLException(
                    "Database configuration is incomplete. Set DB_URL, "
                    + "DB_USERNAME and DB_PASSWORD, or configure DB_CONFIG_FILE."
            );
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }

        return DriverManager.getConnection(url, username, password);
    }

    private static Properties loadProperties() throws SQLException {

        Properties properties = new Properties();

        // An explicitly configured external file takes precedence over the
        // optional classpath file. It remains outside the WAR and Git repo.
        String configFile = firstNonBlank(
                System.getenv("DB_CONFIG_FILE"),
                System.getProperty("jvmcrew.db.config")
        );

        if (!isBlank(configFile)) {
            loadExternalProperties(properties, configFile);
            return properties;
        }

        // Backward-compatible fallback for local IDE/development execution.
        // This resource is excluded from the final WAR by the Maven plugin.
        try (InputStream input = DBConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input != null) {
                properties.load(input);
            }

        } catch (IOException e) {
            throw new SQLException(
                    "Unable to read classpath database configuration.", e
            );
        }

        return properties;
    }

    private static void loadExternalProperties(
            Properties properties,
            String configFile) throws SQLException {

        try {
            Path path = Paths.get(configFile).toAbsolutePath().normalize();

            if (!Files.isRegularFile(path)) {
                throw new SQLException(
                        "The DB_CONFIG_FILE path does not point to a file."
                );
            }

            try (InputStream input = Files.newInputStream(path)) {
                properties.load(input);
            }

        } catch (InvalidPathException | IOException e) {
            throw new SQLException(
                    "Unable to read the external database configuration file.", e
            );
        }
    }

    private static String firstNonBlank(String first, String second) {
        if (!isBlank(first)) {
            return first.trim();
        }
        if (!isBlank(second)) {
            return second.trim();
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
