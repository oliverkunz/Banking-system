package backend.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Internally used by the bank, handles business logic
 * 
 * @author fkg
 *
 */
public class Customer implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -3444270841266657811L;
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

    /**
     * @param password
     * @return success
     */
    public boolean login(String password) {
	return true;
    }

    /**
     * @param account
     * @return success
     */
    public boolean addAccount(Account account) {
	this.accounts.add(account);
	return true;
    }

    /**
     * @return firstName
     */
    public String getFirstName() {
	return firstName;
    }

    /**
     * @return lastName
     */
    public String getLastName() {
	return lastName;
    }

    /**
     * @return password
     */
    public String getPassword() {
	return password;
    }

    /**
     * @return customerID
     */
    public String getCustomerID() {
	return customerID;
    }

    /**
     * @return accounts
     */
    public ArrayList<Account> getAccounts() {
	return accounts;
    }

}
