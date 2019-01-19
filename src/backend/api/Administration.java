package backend.api;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * RMI Interface for the Admin client
 * 
 * @author fkg
 *
 */
public interface Administration extends Remote {

    /**
     * Creates a new customer, without any accounts
     * 
     * @param firstName
     * @param lastName
     * @param password
     * @return customerId
     * @throws RemoteException
     */
    public String createCustomer(String firstName, String lastName, String password) throws RemoteException;

    /**
     * Creates a new account for an existing customer
     * 
     * @param customerID
     * @param type
     * @param balance
     * @param interest
     * @param overdraftInterest
     * @param dailyLimit
     * @param monthyLimit
     * @param maxMinus
     * @param pin
     * @return accountId
     * @throws RemoteException
     */
    public String createAccount(String customerID, AccountType type, double balance, double interest,
	    double overdraftInterest, double dailyLimit, double monthyLimit, double maxMinus, int pin)
	    throws RemoteException;

    /**
     * Sets the state to closed for the account
     * 
     * @param accountID
     * @return success
     * @throws RemoteException
     */
    public boolean closeAccount(String accountID) throws RemoteException;

    /**
     * Shows the account (including transactions etc.)
     * 
     * @param accountID
     * @return account
     * @throws RemoteException
     */
    public Account showAccount(String accountID) throws RemoteException;

    /**
     * Show all accounts associated with the customer
     * 
     * @param customerID
     * @return listofAccounts
     * @throws RemoteException
     */
    public ArrayList<Account> showAccounts(String customerID) throws RemoteException;

    /**
     * Deposit money to the account
     * 
     * @param accountID
     * @param amount
     * @return success
     * @throws AccessException
     * @throws RemoteException
     * @throws NotBoundException
     */
    public boolean deposit(String accountID, double amount) throws AccessException, RemoteException, NotBoundException;

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

    /**
     * Handle interests
     * 
     * @param accountID
     * @return success
     * @throws RemoteException
     */
    public boolean handleInterests(String accountID) throws RemoteException;

    /**
     * Handle negative interests
     * 
     * @param accountID
     * @return success
     * @throws RemoteException
     */
    public boolean handleOverdrawInterests(String accountID) throws RemoteException;

    /**
     * @return bankName
     * @throws RemoteException
     */
    public String getBankname() throws RemoteException;
}
