package carsharing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the creation of database tables.
 */
public class DatabaseInitializer {

    /**
     * Creates the COMPANY table if it does not exist.
     *
     * @param connection the database connection
     * @throws SQLException if a database access error occurs
     */
    public static void createCompanyTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) NOT NULL UNIQUE)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        }
    }

    /**
     * Creates the CAR table if it does not exist.
     *
     * @param connection the database connection
     * @throws SQLException if a database access error occurs
     */
    public static void createCarTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS CAR (" +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) NOT NULL UNIQUE, " +
                "COMPANY_ID INT NOT NULL, " +
                "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID) " +
                "ON DELETE SET NULL ON UPDATE CASCADE)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        }
    }

    /**
     * Creates the CUSTOMER table if it does not exist.
     *
     * @param connection the database connection
     * @throws SQLException if a database access error occurs
     */
    public static void createCustomerTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) NOT NULL UNIQUE, " +
                "RENTED_CAR_ID INT, " +
                "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID) " +
                "ON DELETE SET NULL ON UPDATE CASCADE)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        }
    }
}
