package backend.business;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private ArrayList<Account> accounts;
    private ArrayList<Customer> customers;
    private BankAccount bankAccount;
    private ObjectStore<ArrayList<Customer>> store;

    Registry registry;

    private static final Logger LOGGER = Logger.getLogger(BankManager.class.getName());

    public BankManager(String bankNumber) throws IOException {
	super();
	this.bankNumber = bankNumber;
	this.accounts = new ArrayList<Account>();
	this.bankAccount = new BankAccount(this.generateAccountID(), 1000000, 0, 0, 0, 0, 0, 123456789);
	this.store = new ObjectStore<ArrayList<Customer>>();

	this.customers = this.store.load("customers");

	if (this.customers == null) {
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
	    LOGGER.info("Could not login customer, wrong password");
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
	    LOGGER.info("Could not login account, wrong password");
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
	    LOGGER.info("Could not show accounts, customer not found");
	    return null;
	}

	ArrayList<Account> accounts = customer.getAccounts();

	for (Account account : accounts) {
	    simpleAccounts.add(new backend.api.Account(account.getBalance(), account.getTransactions()));
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

	// transfer from internal account to internal account (fromParts[0] &&
	// toParts[0] == bankNumber)
	// transfer from internal account to external account (fromParts[0] ==
	// bankNumber, toParts[0] == external)
	// transfer from external account to internal account (called from the
	// external bank manager) (fromParts[0] == external, toParts[0] == bankNumber)
	// transfer from the bank account to internal account (deposit) as use case 1
	// transfer from internal account to bank account (withdraw) as use case 1

	if (toParts.length != 2 || fromParts.length != 2) {
	    LOGGER.warning("Cannot transfer money, IBAN is wrong");

	    return false;
	}

	boolean isInternal = fromParts[0].equals(this.bankNumber) && toParts[0].equals(this.bankNumber);
	boolean isToExternal = fromParts[0].equals(this.bankNumber) && !toParts[0].equals(this.bankNumber);
	boolean isFromExternal = !fromParts[0].equals(this.bankNumber) && toParts[0].equals(this.bankNumber);

	if (isInternal) {
	    Account toAccount = findAccount(toAccountID);
	    Account fromAccount = findAccount(fromAccountID);

	    if (toAccount == null || fromAccount == null) {
		LOGGER.warning("Cannot transfer money, account could not be found");

		return false;
	    }

	    toAccount.deposit(amount);
	    toAccount.addTransaction(new Transaction(toAccountID, fromAccountID, amount, date));
	    fromAccount.withdraw(amount);
	    fromAccount.addTransaction(new Transaction(toAccountID, fromAccountID, -amount, date));

	    return true;
	} else if (isToExternal) {
	    Account fromAccount = findAccount(fromAccountID);

	    if (fromAccount == null) {
		LOGGER.warning("Cannot transfer money, account could not be found");
		return false;
	    }

	    if (!fromAccount.withdraw(amount)) {
		LOGGER.warning("Cannot transfer money, account could not be found");
		return false;
	    }
	    fromAccount.addTransaction(new Transaction(toAccountID, fromAccountID, -amount, date));

	    Bank externalBank = this.getRemoteBankManager(toParts[0]);
	    externalBank.transfer(fromAccountID, toAccountID, amount, date);
	} else if (isFromExternal) {
	    Account toAccount = findAccount(toAccountID);

	    if (toAccount == null) {
		LOGGER.warning("Cannot transfer money, account could not be found");
		return false;
	    }

	    toAccount.deposit(amount);
	    toAccount.addTransaction(new Transaction(toAccountID, fromAccountID, amount, date));
	} else {
	    LOGGER.severe("Cannot transfer money, transaction unknown");
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
	Customer customer = new Customer(Utils.generateGUID(), firstName, lastName, password);

	this.customers.add(customer);

	this.save(this.customers);

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
	Customer customer = this.findCustomer(customerID);

	if (customer == null) {
	    LOGGER.severe("Could not create account for customer, customer is null");
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
	    LOGGER.warning("Could not create account for customer, type is unkown");
	    return null;
	}

	account.setCustomer(customer);
	customer.addAccount(account);
	this.accounts.add(account);

	this.save(this.customers);

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
	    LOGGER.warning("Could not close account, account is unkown");
	    return false;
	}

	account.setState(State.CLOSED);

	this.save(this.customers);

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
	    LOGGER.warning("Could not show account, account is unkown");
	    return null;
	}

	return new backend.api.Account(account.getBalance(), account.getTransactions());
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
	    LOGGER.warning("Could not deposit to account, account is unkown");
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
	    LOGGER.warning("Could not withdraw from account, account is unkown");
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

	for (Account account : this.accounts) {
	    if (account.getAccountID().equals(accountID)) {
		return account;
	    }
	}

	LOGGER.warning("Could not find account");

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

	LOGGER.warning("Could not find customer");
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
	    // TODO Auto-generated catch block
	    LOGGER.severe("Could not save data to file system");
	}
    }
}
