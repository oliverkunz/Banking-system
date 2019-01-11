package backend.api;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ATM extends Remote {
    public boolean login(String accountID, int pin) throws RemoteException;

    public Account showAccount(String accountID) throws RemoteException;

    public boolean withdraw(String accountID, double amount) throws AccessException, RemoteException, NotBoundException;
}
