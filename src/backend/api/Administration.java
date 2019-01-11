package backend.api;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Administration extends Remote {
    public String createCustomer(String firstName, String lastName, String password) throws RemoteException;

    public String createAccount(String customerID, AccountType type, double balance, double interest,
	    double overdraftInterest, double dailyLimit, double monthyLimit, double maxMinus, int pin)
	    throws RemoteException;

    public boolean closeAccount(String accountID) throws RemoteException;

    public Account showAccount(String accountID) throws RemoteException;

    public boolean deposit(String accountID, double amount) throws AccessException, RemoteException, NotBoundException;

    public boolean withdraw(String accountID, double amount) throws AccessException, RemoteException, NotBoundException;

    public boolean handleInterests(String accountID) throws RemoteException;

    public boolean handleOverdrawInterests(String accountID) throws RemoteException;
}
