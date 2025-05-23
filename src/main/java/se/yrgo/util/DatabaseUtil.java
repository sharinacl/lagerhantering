package se.yrgo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DatabaseUtil {

    private static final String DB_URL = "jdbc:derby:inventory-management-db;create=true";

    private static  final Logger logger = Logger.getLogger(DatabaseUtil.class.getName());

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void shutdown() {
        try{
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException se) {
            if ("XJ015".equals(se.getSQLState())) {
                logger.info("Derby shut down normally");
            } else {
                logger.log(Level.SEVERE, "Error while shutting down", se);
            }
        }
    }
}

