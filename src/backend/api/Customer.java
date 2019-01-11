package backend.api;

public class Customer {
    private String firstName;
    private String lastName;
    private String customerID;

    public String getFirstName() {
	return firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public Customer(String firstName, String lastName, String customerID) {
	super();
	this.firstName = firstName;
	this.lastName = lastName;
	this.customerID = customerID;
    }

    public String getCustomerID() {
	return customerID;
    }
}
