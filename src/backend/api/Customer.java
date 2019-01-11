package backend.api;

/**
 * Customer used by the clients
 * 
 * @author fkg
 *
 */
public class Customer {
    private String firstName;
    private String lastName;
    private String customerID;

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

    public Customer(String firstName, String lastName, String customerID) {
	super();
	this.firstName = firstName;
	this.lastName = lastName;
	this.customerID = customerID;
    }

    /**
     * @return customerID
     */
    public String getCustomerID() {
	return customerID;
    }
}
