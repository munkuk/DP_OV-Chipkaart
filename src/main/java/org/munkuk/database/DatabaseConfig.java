package org.munkuk.database;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Getter
public class DatabaseConfig {

    private final String dbURL;
    private final String dbUser;
    private final String dbPassword;

    public DatabaseConfig() {
        Properties properties = new Properties();
        try {
            FileInputStream file = new FileInputStream("src/main/resources/database.conf");
            properties.load(file);
            this.dbURL = properties.getProperty("host");
            this.dbUser = properties.getProperty("user");
            this.dbPassword = properties.getProperty("password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
