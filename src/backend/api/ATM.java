package backend.api;

import java.rmi.Remote;

public interface ATM extends Remote {
    public boolean login(String accountID, int pin);

    public Account showAccount(String accountID);

    public boolean withdraw(String accountID, double amount);
}
