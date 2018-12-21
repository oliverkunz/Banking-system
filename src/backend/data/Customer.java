package backend.data;

import java.util.ArrayList;

public class Customer {
    private String firstName;
    private String lastName;
    private String password;
    private String customerID;
    private ArrayList<Account> accounts = new ArrayList<Account>();

    private boolean login(String password) {
	return true;
    }
}
