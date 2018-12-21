package backend.api;

import java.rmi.Remote;

import backend.data.Account;
import backend.data.Customer;

public interface Administration extends Remote {
    public boolean createCustomer(Customer customer);

    public boolean createAccount(Account account, Customer customer);

    public boolean closeAccount(Account account);

    public Account showAccount(Account account);

    public boolean deposit(Account account, double amount);

    public boolean withdraw(Account account, double amount);
}
