package backend.business;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.persistence.ObjectStore;

import backend.api.ATM;
import backend.api.AccountType;
import backend.api.Administration;
import backend.api.Banking;
import backend.api.Transaction;
import backend.data.Account;
import backend.data.BankAccount;
import backend.data.Customer;
import backend.data.PrivateAccount;
import backend.data.SavingsAccount;
import backend.data.State;
import util.Utils;

// Bank interface is used for testing
/**
 * Handles all business logic for a bank
 * 
 * @author fkg
 *
 */
public class BankManager implements ATM, Banking, Administration, Bank {
    private String bankNumber;
    private String bankName;
    private ArrayList<Customer> customers;
    private BankAccount bankAccount;
    private ObjectStore<ArrayList<Customer>> store;

    Registry registry;

    private static final Logger LOGGER = Logger.getLogger(BankManager.class.getName());

    public BankManager(String bankNumber, String bankName) throws IOException {
	super();
	this.bankNumber = bankNumber;
	this.bankName = bankName;

	this.bankAccount = new BankAccount(this.generateAccountID(), 1000000, 0, 0, 0, 0, 0, 123456789);
	this.store = new ObjectStore<ArrayList<Customer>>();

	this.customers = this.store.load("customers");

	if (this.customers == null) {
	    LOGGER.log(Level.INFO, "[{0}] Restoring customer from file system", this.bankNumber);
	    this.customers = new ArrayList<Customer>();
	    this.save(this.customers);
	}

	registry = LocateRegistry.getRegistry("localhost", 2001);
    }

    /*
     * (non-Javadoc)
     * 
     * @see backend.api.Banking#login(java.lang.String, java.lang.String)
     */
    @Override
    public boolean login(String customerID, String password) {
	Customer customer = findCustomer(customerID);

	if (customer == null || !customer.getPassword().equals(password)) {
	    LOGGER.log(Level.INFO, "[{0}] Could not login customer {1}, wrong password",
		    new Object[] { this.bankNumber, customerID });
	    return false;
	}

	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see backend.api.ATM#login(java.lang.String, int)
     */
    @Override
    public boolean login(String accountID, int pin) {
	Account account = findAccount(accountID);

	if (account == null || account.getPin() != pin) {
	    LOGGER.log(Level.INFO, "[{0}] Could not login account {1}, wrong password",
		    new Object[] { this.bankNumber, accountID });
	    return false;
	}

	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see backend.api.Banking#showAccounts(java.lang.String)
     */
    @Override
    public ArrayList<backend.api.Account> showAccounts(String customerID) {
	ArrayList<backend.api.Account> simpleAccounts = new ArrayList<backend.api.Account>();

	Customer customer = findCustomer(customerID);

	if (customer == null) {
	    LOGGER.log(Level.INFO, "[{0}] Could not show accounts, customer {1} not found",
		    new Object[] { this.bankNumber, customerID });
	    return null;
	}

	ArrayList<Account> accounts = customer.getAccounts();

	for (Account account : accounts) {
	    simpleAccounts.add(
		    new backend.api.Account(account.getAccountID(), account.getBalance(), account.getTransactions()));
	}

	return simpleAccounts;
    }

    /*
     * (non-Javadoc)
     * 
     * @see backend.api.Banking#transfer(java.lang.String, java.lang.String, double,
     * java.time.LocalDate)
     */
    @Override
    public boolean transfer(String fromAccountID, String toAccountID, double amount, LocalDate date)
	    throws AccessException, RemoteException, NotBoundException {
	String[] toParts = toAccountID.split("_");
	String[] fromParts = fromAccountID.split("_");

	LOGGER.log(Level.INFO, "[{0}] Transfering money from {1} to {2}, amount: {3}, date: {4}",
		new Object[] { this.bankNumber, fromAccountID, toAccountID, amount, date });

	// transfer from internal account to internal account (fromParts[0] &&
	// toParts[0] == bankNumber)
	// transfer from internal account to external account (fromParts[0] ==
	// bankNumber, toParts[0] == external)
	// transfer from external account to internal account (called from the
	// external bank manager) (fromParts[0] == external, toParts[0] == bankNumber)
	// transfer from the bank account to internal account (deposit) as use case 1
	// transfer from internal account to bank account (withdraw) as use case 1

	if (toParts.length != 2 || fromParts.length != 2) {
	    LOGGER.log(Level.WARNING, "[{0}] Cannot transfer money, IBAN is wrong", this.bankNumber);

	    return false;
	}

	boolean isInternal = fromParts[0].equals(this.bankNumber) && toParts[0].equals(this.bankNumber);
	boolean isToExternal = fromParts[0].equals(this.bankNumber) && !toParts[0].equals(this.bankNumber);
	boolean isFromExternal = !fromParts[0].equals(this.bankNumber) && toParts[0].equals(this.bankNumber);

	if (isInternal) {
	    Account toAccount = findAccount(toAccountID);
	    Account fromAccount = findAccount(fromAccountID);

	    if (toAccount == null || fromAccount == null) {
		LOGGER.log(Level.WARNING, "[{0}] Cannot transfer money, account could not be found", this.bankNumber);

		return false;
	    }

	    if (!fromAccount.withdraw(amount)) {
		return false;
	    }
	    fromAccount.addTransaction(new Transaction(toAccountID, fromAccountID, -amount, date));

	    toAccount.deposit(amount);
	    toAccount.addTransaction(new Transaction(toAccountID, fromAccountID, amount, date));

	    LOGGER.log(Level.INFO, "[{0}] Transaction successfully done", this.bankNumber);
	} else if (isToExternal) {
	    Account fromAccount = findAccount(fromAccountID);

	    if (fromAccount == null) {
		LOGGER.log(Level.WARNING, "[{0}] Cannot transfer money, account could not be found", this.bankNumber);
		return false;
	    }

	    if (!fromAccount.withdraw(amount)) {
		LOGGER.log(Level.WARNING, "[{0}] Transaction failed, check monthly/daily reached or not enough money.",
			this.bankNumber);
		return false;
	    }

	    fromAccount.addTransaction(new Transaction(toAccountID, fromAccountID, -amount, date));

	    Bank externalBank = this.getRemoteBankManager(toParts[0]);
	    externalBank.transfer(fromAccountID, toAccountID, amount, date);

	    LOGGER.log(Level.INFO, "[{0}] Transaction succefully done.", this.bankNumber);
	} else if (isFromExternal)

	{
	    Account toAccount = findAccount(toAccountID);

	    if (toAccount == null) {
		LOGGER.log(Level.WARNING, "[{0}] Cannot transfer money, account could not be found.", this.bankNumber);
		return false;
	    }

	    toAccount.deposit(amount);
	    toAccount.addTransaction(new Transaction(toAccountID, fromAccountID, amount, date));

	    LOGGER.log(Level.INFO, "[{0}] Transaction succefully done.", this.bankNumber);
	} else {
	    LOGGER.log(Level.SEVERE, "[{0}] Cannot transfer money, transaction unknown.", this.bankNumber);
	    return false;
	}

	this.save(this.customers);

	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see backend.api.Administration#createCustomer(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public String createCustomer(String firstName, String lastName, String password) {
	LOGGER.log(Level.INFO, "[{0}] Creating new customer {1} {2}",
		new Object[] { this.bankNumber, firstName, lastName });

	Customer customer = new Customer(Utils.generateGUID(), firstName, lastName, password);

	this.customers.add(customer);

	this.save(this.customers);

	LOGGER.log(Level.INFO, "[{0}] Customer {1} successfully created",
		new Object[] { this.bankNumber, customer.getCustomerID() });

	return customer.getCustomerID();
    }

    /*
     * (non-Javadoc)
     * 
     * @see backend.api.Administration#createAccount(java.lang.String,
     * backend.api.AccountType, double, double, double, double, double, double, int)
     */
    @Override
    public String createAccount(String customerID, AccountType type, double balance, double interest,
	    double overdraftInterest, double dailyLimit, double monthyLimit, double maxMinus, int pin) {
	LOGGER.log(Level.INFO,
		"[{0}] Creating new account for customer {1} type: {2} balance {3}: interest {4}: overdraftInterest {5}, dailyLimit {6}, monthyLimit {7}, maxMinus {8}",
		new Object[] { this.bankNumber, customerID, type, balance, interest, overdraftInterest, dailyLimit,
			monthyLimit, maxMinus, pin });

	Customer customer = this.findCustomer(customerID);

	if (customer == null) {
	    LOGGER.log(Level.SEVERE, "[{0}] Could not create account for customer, customer couldn't be found",
		    new Object[] { this.bankNumber, customerID });
	    return null;
	}

	Account account = null;
	// really needed? Can't be done with same account?
	switch (type) {
	case PRIVATE:
	    account = new PrivateAccount(this.generateAccountID(), balance, interest, overdraftInterest, dailyLimit,
		    monthyLimit, maxMinus, pin);
	case SAVINGS:
	    account = new SavingsAccount(this.generateAccountID(), balance, interest, overdraftInterest, dailyLimit,
		    monthyLimit, maxMinus, pin);
	}

	if (account == null) {
	    LOGGER.log(Level.INFO, "[{0}] Could not create account for customer {1}, type is unkown",
		    new Object[] { this.bankNumber, customerID });
	    return null;
	}

	account.setCustomer(customer);
	customer.addAccount(account);

	this.save(this.customers);

	LOGGER.log(Level.INFO, "[{0}] Account {1} successfully created",
		new Object[] { this.bankNumber, account.getAccountID() });

	return account.getAccountID();
    }

    /*
     * (non-Javadoc)
     * 
     * @see backend.api.Administration#closeAccount(java.lang.String)
     */
    @Override
    public boolean closeAccount(String accountID) {
	Account account = findAccount(accountID);

	if (account == null) {
	    LOGGER.log(Level.WARNING, "[{0}] Could not close account, account is unkown.", this.bankNumber);
	    return false;
	}

	account.setState(State.CLOSED);

	this.save(this.customers);

	LOGGER.log(Level.INFO, "[{0}] Account {1} successfully closed", new Object[] { this.bankNumber, accountID });

	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see backend.api.ATM#showAccount(java.lang.String)
     */
    @Override
    public backend.api.Account showAccount(String accountID) {
	Account account = findAccount(accountID);

	if (account == null) {
	    LOGGER.log(Level.WARNING, "[{0}] Could not show account, account is unkown.", this.bankNumber);

	    return null;
	}

	return new backend.api.Account(account.getAccountID(), account.getBalance(), account.getTransactions());
    }

    /*
     * (non-Javadoc)
     * 
     * @see backend.api.Administration#deposit(java.lang.String, double)
     */
    @Override
    public boolean deposit(String accountID, double amount) throws AccessException, RemoteException, NotBoundException {
	Account toAccount = findAccount(accountID);

	if (toAccount == null) {
	    LOGGER.log(Level.WARNING, "[{0}] Could not deposit to account, account is unkown.", this.bankNumber);
	    return false;
	}

	return this.transfer(this.bankAccount.getAccountID(), toAccount.getAccountID(), amount, LocalDate.now());
    }

    /*
     * (non-Javadoc)
     * 
     * @see backend.api.ATM#withdraw(java.lang.String, double)
     */
    @Override
    public boolean withdraw(String accountID, double amount)
	    throws AccessException, RemoteException, NotBoundException {
	Account fromAccount = findAccount(accountID);

	if (fromAccount == null) {
	    LOGGER.log(Level.WARNING, "[{0}] Could not withdraw from account, account is unkown.", this.bankNumber);
	    return false;
	}

	return this.transfer(fromAccount.getAccountID(), this.bankAccount.getAccountID(), amount, LocalDate.now());
    }

    /**
     * finds an account based on its ID
     * 
     * @param accountID
     * @return account
     */
    private Account findAccount(String accountID) {
	if (this.bankAccount.getAccountID().equals(accountID)) {
	    return this.bankAccount;
	}

	for (Customer customer : this.customers) {
	    for (Account account : customer.getAccounts()) {
		if (account.getAccountID().equals(accountID)) {
		    return account;
		}
	    }
	}

	LOGGER.log(Level.WARNING, "[{0}] Could not find account {1}.", new Object[] { this.bankNumber, accountID });

	return null;
    }

    /**
     * finds an customer based on its ID
     * 
     * @param customerID
     * @return customer
     */
    private Customer findCustomer(String customerID) {
	for (Customer customer : this.customers) {
	    if (customer.getCustomerID().equals(customerID)) {
		return customer;
	    }
	}

	LOGGER.log(Level.WARNING, "[{0}] Could not find customer {1}.", new Object[] { this.bankNumber, customerID });

	return null;
    }

    /**
     * Gets the right bank, needed for transfer
     * 
     * @param bankNumber
     * @return bank
     * @throws AccessException
     * @throws RemoteException
     * @throws NotBoundException
     */
    private Bank getRemoteBankManager(String bankNumber) throws AccessException, RemoteException, NotBoundException {
	return (Bank) registry.lookup(bankNumber);
    }

    /**
     * Generates an accountID with bank prefix
     * 
     * @return accountID
     */
    private String generateAccountID() {
	return this.bankNumber + "_" + Utils.generateGUID();
    }

    /*
     * (non-Javadoc)
     * 
     * @see backend.api.Administration#handleInterests(java.lang.String)
     */
    @Override
    public boolean handleInterests(String accountID) {
	// TODO needs this to be withdrawn or deposited to the bank account?
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see backend.api.Administration#handleOverdrawInterests(java.lang.String)
     */
    @Override
    public boolean handleOverdrawInterests(String accountID) {
	// TODO Auto-generated method stub
	return false;
    }

    /**
     * Saves all customers to the file system
     * 
     * @param customers
     */
    public void save(ArrayList<Customer> customers) {
	try {
	    this.store.save("customers", customers);
	} catch (IOException e) {
	    LOGGER.log(Level.SEVERE, "[{0}] Could not save data to file system.", this.bankNumber);
	}
    }

}
