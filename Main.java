package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * The main class for the car sharing application.
 */
public class Main {

    public static void main(String[] args) {
        // Get database file name from command-line args or use default
        String databaseFileName = getDatabaseFileName(args);
        String databaseUrl = "jdbc:h2:./src/carsharing/db/" + databaseFileName;

        try (Connection connection = DriverManager.getConnection(databaseUrl)) {
            connection.setAutoCommit(true); // Enable auto-commit
            // Initialize database tables
            DatabaseInitializer.createCompanyTable(connection); // Create COMPANY table
            DatabaseInitializer.createCarTable(connection);     // Create CAR table
            DatabaseInitializer.createCustomerTable(connection); // Create CUSTOMER table

            // Create DAOs and menu
            CompanyDao companyDao = new CompanyDaoImpl(connection);
            CustomerDao customerDao = new CustomerDaoImpl(connection);
            Scanner scanner = new Scanner(System.in);
            Menu menu = new Menu(companyDao, customerDao, scanner);

            // Display main menu
            menu.displayMainMenu();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the database file name from command-line args, default is "default".
     *
     * @param args command-line arguments
     * @return the database file name
     */
    private static String getDatabaseFileName(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-databaseFileName".equals(args[i]) && i + 1 < args.length) {
                return args[i + 1];
            }
        }
        return "default";
    }
}
