package backend.api;

import java.rmi.Remote;
import java.time.LocalDate;
import java.util.ArrayList;

public interface Banking extends Remote {
    public boolean login(String customerID, String password);

    public ArrayList<Account> showAccounts(String customerID);

    public Account showAccount(String accountID);

    public boolean transfer(String fromAccountID, String toAccountID, double amount, LocalDate date);
}
