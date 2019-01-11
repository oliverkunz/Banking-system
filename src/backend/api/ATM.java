package backend.api;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for the ATM client
 * 
 * @author fkg
 *
 */
public interface ATM extends Remote {
    /**
     * Handles the login from the atm
     * 
     * @param accountID
     * @param pin
     * @return success
     * @throws RemoteException
     */
    public boolean login(String accountID, int pin) throws RemoteException;

    /**
     * Shows the accoutn details
     * 
     * @param accountID
     * @return account
     * @throws RemoteException
     */
    public Account showAccount(String accountID) throws RemoteException;

    /**
     * Withdraw money from the account
     * 
     * @param accountID
     * @param amount
     * @return success
     * @throws AccessException
     * @throws RemoteException
     * @throws NotBoundException
     */
    public boolean withdraw(String accountID, double amount) throws AccessException, RemoteException, NotBoundException;
}
