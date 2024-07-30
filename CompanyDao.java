package carsharing;

import java.util.List;

/**
 * Data Access Object interface for company and car-related operations.
 */
public interface CompanyDao {
    void addCompany(Company company);
    List<Company> getAllCompanies();
    void addCar(Car car);
    List<Car> getCarsByCompanyId(int companyId);
    Car getCarById(int carId);
    Company getCompanyById(int companyId);
}
