package backend.api;

import java.rmi.Remote;

import backend.data.Account;

public interface ATM extends Remote {
    public boolean login(String accountID, int pin);

    public Account showAccount(Account account);

    public boolean withdraw(Account account, double amount);
}
