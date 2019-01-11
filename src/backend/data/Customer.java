package backend.data;

import java.util.ArrayList;

public class Customer {
    private String firstName;
    private String lastName;
    private String password;
    private String customerID;
    private ArrayList<Account> accounts = new ArrayList<Account>();

    public Customer(String customerID, String firstName, String lastName, String password) {
	this.firstName = firstName;
	this.lastName = lastName;
	this.password = password;
	this.customerID = customerID;
    }

    public boolean login(String password) {
	return true;
    }

    public boolean addAccount(Account account) {
	this.accounts.add(account);
	return true;
    }

    public String getFirstName() {
	return firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public String getPassword() {
	return password;
    }

    public String getCustomerID() {
	return customerID;
    }

    public ArrayList<Account> getAccounts() {
	return accounts;
    }

}
