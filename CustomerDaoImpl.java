package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the CustomerDao interface for managing customers in the database.
 */
public class CustomerDaoImpl implements CustomerDao {
    private final Connection connection;

    public CustomerDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addCustomer(Customer customer) {
        String insertSQL = "INSERT INTO CUSTOMER (NAME) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, customer.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String selectSQL = "SELECT ID, NAME, RENTED_CAR_ID FROM CUSTOMER ORDER BY ID";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                Integer rentedCarId = (rs.getInt("RENTED_CAR_ID") == 0) ? null : rs.getInt("RENTED_CAR_ID");
                customers.add(new Customer(id, name, rentedCarId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public void updateCustomerRental(int customerId, Integer carId) {
        String updateSQL = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            if (carId == null) {
                pstmt.setNull(1, Types.INTEGER);
            } else {
                pstmt.setInt(1, carId);
            }
            pstmt.setInt(2, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Customer getCustomerByRentedCarId(int carId) {
        String selectSQL = "SELECT ID, NAME, RENTED_CAR_ID FROM CUSTOMER WHERE RENTED_CAR_ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, carId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("ID");
                    String name = rs.getString("NAME");
                    Integer rentedCarId = (rs.getInt("RENTED_CAR_ID") == 0) ? null : rs.getInt("RENTED_CAR_ID");
                    return new Customer(id, name, rentedCarId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
