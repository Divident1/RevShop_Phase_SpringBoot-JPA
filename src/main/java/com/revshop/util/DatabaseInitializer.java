package com.revshop.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "revshop";
    private static final String FULL_URL = BASE_URL + DB_NAME;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void initialize() {
        if (databaseExists()) {
            System.out.println("Database '" + DB_NAME + "' already exists. Skipping initialization.");
            return;
        }

        System.out.println("Database '" + DB_NAME + "' not found. Initializing...");
        createDatabaseAndTables();
    }

    private static boolean databaseExists() {
        try (Connection conn = DriverManager.getConnection(FULL_URL, USERNAME, PASSWORD)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void createDatabaseAndTables() {
        try (Connection conn = DriverManager.getConnection(BASE_URL, USERNAME, PASSWORD);
                Statement stmt = conn.createStatement()) {

            InputStream is = DatabaseInitializer.class.getClassLoader().getResourceAsStream("schema.sql");
            if (is == null) {
                System.err.println("schema.sql not found!");
                return;
            }

            String script = new BufferedReader(new InputStreamReader(is))
                    .lines().collect(Collectors.joining("\n"));

            // Remove comments and split by ;
            // This is a simple parser, might be fragile for complex SQL but sufficient here
            String[] statements = script.split(";");

            for (String sql : statements) {
                if (sql.trim().isEmpty())
                    continue;
                stmt.execute(sql);
            }

            System.out.println("Database initialized successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
