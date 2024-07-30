package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the CompanyDao interface for managing companies and cars in the database.
 */
public class CompanyDaoImpl implements CompanyDao {
    private final Connection connection;

    public CompanyDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addCompany(Company company) {
        String insertSQL = "INSERT INTO COMPANY (NAME) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, company.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        String selectSQL = "SELECT ID, NAME FROM COMPANY ORDER BY ID";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                companies.add(new Company(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public void addCar(Car car) {
        String insertSQL = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, car.getName());
            pstmt.setInt(2, car.getCompanyId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Car> getCarsByCompanyId(int companyId) {
        List<Car> cars = new ArrayList<>();
        String selectSQL = "SELECT ID, NAME FROM CAR WHERE COMPANY_ID = ? ORDER BY ID";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String name = rs.getString("NAME");
                    cars.add(new Car(id, name, companyId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public Car getCarById(int carId) {
        String selectSQL = "SELECT ID, NAME, COMPANY_ID FROM CAR WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, carId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("ID");
                    String name = rs.getString("NAME");
                    int companyId = rs.getInt("COMPANY_ID");
                    return new Car(id, name, companyId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Company getCompanyById(int companyId) {
        String selectSQL = "SELECT ID, NAME FROM COMPANY WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("ID");
                    String name = rs.getString("NAME");
                    return new Company(id, name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
