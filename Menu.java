package carsharing;

import java.util.List;
import java.util.Scanner;

/**
 * Handles user interactions and menu displays.
 */
public class Menu {
    private final CompanyDao companyDao;
    private final CustomerDao customerDao;
    private final Scanner scanner;

    public Menu(CompanyDao companyDao, CustomerDao customerDao, Scanner scanner) {
        this.companyDao = companyDao;
        this.customerDao = customerDao;
        this.scanner = scanner;
    }

    /**
     * Displays the main menu.
     */
    public void displayMainMenu() {
        while (true) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 0) {
                break;
            } else if (choice == 1) {
                displayManagerMenu();
            } else if (choice == 2) {
                displayCustomerLoginMenu();
            } else if (choice == 3) {
                createCustomer();
            }
        }
    }

    /**
     * Displays the manager menu.
     */
    private void displayManagerMenu() {
        while (true) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");
            int managerChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (managerChoice == 0) {
                break;
            } else if (managerChoice == 1) {
                displayCompanyList();
            } else if (managerChoice == 2) {
                createCompany();
            }
        }
    }

    /**
     * Displays the customer login menu.
     */
    private void displayCustomerLoginMenu() {
        List<Customer> customers = customerDao.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            return;
        }

        System.out.println("Choose a customer:");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i + 1) + ". " + customers.get(i).getName());
        }
        System.out.println("0. Back");

        int customerChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (customerChoice > 0 && customerChoice <= customers.size()) {
            Customer selectedCustomer = customers.get(customerChoice - 1);
            displayCustomerMenu(selectedCustomer);
        }
    }

    /**
     * Displays the menu for a specific customer.
     *
     * @param customer the selected customer
     */
    private void displayCustomerMenu(Customer customer) {
        while (true) {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");
            int customerMenuChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (customerMenuChoice == 0) {
                break;
            } else if (customerMenuChoice == 1) {
                rentCar(customer);
            } else if (customerMenuChoice == 2) {
                returnRentedCar(customer);
            } else if (customerMenuChoice == 3) {
                displayRentedCar(customer);
            }
        }
    }

    /**
     * Allows the customer to rent a car.
     *
     * @param customer the customer renting the car
     */
    private void rentCar(Customer customer) {
        if (customer.getRentedCarId() != null) {
            System.out.println("You've already rented a car!");
            return;
        }

        List<Company> companies = companyDao.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }

        System.out.println("Choose a company:");
        for (int i = 0; i < companies.size(); i++) {
            System.out.println((i + 1) + ". " + companies.get(i).getName());
        }
        System.out.println("0. Back");

        int companyChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (companyChoice > 0 && companyChoice <= companies.size()) {
            Company selectedCompany = companies.get(companyChoice - 1);
            List<Car> cars = companyDao.getCarsByCompanyId(selectedCompany.getId());
            cars.removeIf(car -> customerDao.getCustomerByRentedCarId(car.getId()) != null);

            if (cars.isEmpty()) {
                System.out.println("No available cars in the '" + selectedCompany.getName() + "' company.");
                return;
            }

            System.out.println("Choose a car:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println((i + 1) + ". " + cars.get(i).getName());
            }
            System.out.println("0. Back");

            int carChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (carChoice > 0 && carChoice <= cars.size()) {
                Car selectedCar = cars.get(carChoice - 1);
                customerDao.updateCustomerRental(customer.getId(), selectedCar.getId());
                customer.setRentedCarId(selectedCar.getId());
                System.out.println("You rented '" + selectedCar.getName() + "'");
            }
        }
    }

    /**
     * Allows the customer to return a rented car.
     *
     * @param customer the customer returning the car
     */
    private void returnRentedCar(Customer customer) {
        if (customer.getRentedCarId() == null) {
            System.out.println("You didn't rent a car!");
            return;
        }

        customerDao.updateCustomerRental(customer.getId(), null);
        customer.setRentedCarId(null);
        System.out.println("You've returned a rented car!");
    }

    /**
     * Displays the car rented by the customer.
     *
     * @param customer the customer whose rented car is to be displayed
     */
    private void displayRentedCar(Customer customer) {
        if (customer.getRentedCarId() == null) {
            System.out.println("You didn't rent a car!");
            return;
        }

        Car rentedCar = companyDao.getCarById(customer.getRentedCarId());
        Company company = companyDao.getCompanyById(rentedCar.getCompanyId());
        System.out.println("Your rented car:");
        System.out.println(rentedCar.getName());
        System.out.println("Company:");
        System.out.println(company.getName());
    }

    /**
     * Displays the list of companies.
     */
    private void displayCompanyList() {
        List<Company> companies = companyDao.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Company list:");
            for (int i = 0; i < companies.size(); i++) {
                System.out.println((i + 1) + ". " + companies.get(i).getName());
            }
            System.out.println("0. Back");

            int companyChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (companyChoice > 0 && companyChoice <= companies.size()) {
                Company selectedCompany = companies.get(companyChoice - 1);
                displayCompanyMenu(selectedCompany);
            }
        }
    }

    /**
     * Displays the menu for a specific company.
     *
     * @param company the selected company
     */
    private void displayCompanyMenu(Company company) {
        while (true) {
            System.out.println("'" + company.getName() + "' company:");
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            int companyMenuChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (companyMenuChoice == 0) {
                break;
            } else if (companyMenuChoice == 1) {
                displayCarList(company);
            } else if (companyMenuChoice == 2) {
                createCar(company);
            }
        }
    }

    /**
     * Displays the list of cars for a specific company.
     *
     * @param company the selected company
     */
    private void displayCarList(Company company) {
        List<Car> cars = companyDao.getCarsByCompanyId(company.getId());
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("'" + company.getName() + "' cars:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println((i + 1) + ". " + cars.get(i).getName());
            }
        }
    }

    /**
     * Prompts the user to create a new car for a specific company.
     *
     * @param company the selected company
     */
    private void createCar(Company company) {
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        Car car = new Car(carName, company.getId());
        companyDao.addCar(car);
        System.out.println("The car was added!");
    }

    /**
     * Prompts the user to create a new company.
     */
    private void createCompany() {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        Company company = new Company(name);
        companyDao.addCompany(company);
        System.out.println("The company was created!");
    }

    /**
     * Prompts the user to create a new customer.
     */
    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        Customer customer = new Customer(name);
        customerDao.addCustomer(customer);
        System.out.println("The customer was added!");
    }
}
