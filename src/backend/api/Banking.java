package backend.api;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * RMI interface for the banking client
 * 
 * @author fkg
 *
 */
public interface Banking extends Remote {
    /**
     * Handles the login
     * 
     * @param customerID
     * @param password
     * @return success
     * @throws RemoteException
     */
    public boolean login(String customerID, String password) throws RemoteException;

    /**
     * Show all accounts associated with the customer
     * 
     * @param customerID
     * @return listofAccounts
     * @throws RemoteException
     */
    public List<Account> showAccounts(String customerID) throws RemoteException;

    /**
     * Show account details
     * 
     * @param accountID
     * @return account
     * @throws RemoteException
     */
    public Account showAccount(String accountID) throws RemoteException;

    /**
     * @param fromAccountID
     * @param toAccountID
     * @param amount
     * @param date
     * @return success
     * @throws AccessException
     * @throws RemoteException
     * @throws NotBoundException
     */
    public boolean transfer(String fromAccountID, String toAccountID, double amount, LocalDate date)
	    throws AccessException, RemoteException, NotBoundException;

    /**
     * @return bankName
     * @throws RemoteException
     */
    public String getBankname() throws RemoteException;
}
