package backend.api;

import java.rmi.Remote;

public interface Administration extends Remote {
    public boolean createCustomer(String firstName, String lastName, String password);

    public boolean createAccount(String customerID, AccountType type, double balance, double interest,
	    double overdraftInterest, double dailyLimit, double monthyLimit, int pin);

    public boolean closeAccount(String accountID);

    public Account showAccount(String accountID);

    public boolean deposit(String accountID, double amount);

    public boolean withdraw(String accountID, double amount);
}
