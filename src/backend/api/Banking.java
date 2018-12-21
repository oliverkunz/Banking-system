package backend.api;

import java.rmi.Remote;
import java.time.LocalDate;
import java.util.ArrayList;

import backend.data.Account;

public interface Banking extends Remote {
    public boolean login(String customerID, String password);

    public ArrayList<Account> showAccounts();

    public Account showAccount(Account account);

    public boolean transfer(Account toAccount, double amount, LocalDate date);
}
