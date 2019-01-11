package backend.api;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface Banking extends Remote {
    public boolean login(String customerID, String password) throws RemoteException;

    public ArrayList<Account> showAccounts(String customerID) throws RemoteException;

    public Account showAccount(String accountID) throws RemoteException;

    public boolean transfer(String fromAccountID, String toAccountID, double amount, LocalDate date)
	    throws AccessException, RemoteException, NotBoundException;
}
