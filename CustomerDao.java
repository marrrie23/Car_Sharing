package carsharing;

import java.util.List;

/**
 * Data Access Object interface for customer-related operations.
 */
public interface CustomerDao {
    void addCustomer(Customer customer);
    List<Customer> getAllCustomers();
    void updateCustomerRental(int customerId, Integer carId);
    Customer getCustomerByRentedCarId(int carId);
}
