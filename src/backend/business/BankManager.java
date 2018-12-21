package backend.business;

import java.time.LocalDate;
import java.util.ArrayList;

import backend.api.ATM;
import backend.api.AccountType;
import backend.api.Administration;
import backend.api.Banking;
import backend.data.Account;
import backend.data.BankAccount;
import backend.data.Customer;
import backend.data.PrivateAccount;
import backend.data.SavingsAccount;
import backend.data.State;
import backend.data.Transaction;
import util.Utils;

public class BankManager implements Administration, ATM, Banking {
    private String bankName;
    private String bankNumber;
    private ArrayList<Account> accounts;
    private ArrayList<Customer> customers;
    private BankAccount bankAccount;

    public BankManager(String bankName, String bankNumber) {
	super();
	this.bankName = bankName;
	this.bankNumber = bankNumber;
	this.accounts = new ArrayList<Account>();
	this.customers = new ArrayList<Customer>();
	this.bankAccount = new BankAccount(1000000, 0, 0, 0, 0, 123456789);
    }

    /*
     * public static void main(String[] args) throws AccessException,
     * RemoteException, AlreadyBoundException { BankManager manager = new
     * BankManager("name1", "prefix1"); BankManager manager = new
     * BankManager("name2", "prefix2"); BankManager stub = (BankManager)
     * UnicastRemoteObject.exportObject(manager, 0); BankManager stub =
     * (BankManager) UnicastRemoteObject.exportObject(manager, 0);
     * 
     * LocateRegistry.createRegistry(123).bind("BankManager", stub); }
     */

    @Override
    public boolean login(String customerID, String password) {
	Customer customer = findCustomer(customerID);

	if (customer == null || !customer.getPassword().equals(password)) {
	    return false;
	}

	return true;
    }

    @Override
    public boolean login(String accountID, int pin) {
	Account account = findAccount(accountID);

	if (account == null || account.getPin() != pin) {
	    return false;
	}

	return true;
    }

    @Override
    public ArrayList<backend.api.Account> showAccounts(String customerID) {
	ArrayList<backend.api.Account> simpleAccounts = new ArrayList<backend.api.Account>();

	Customer customer = findCustomer(customerID);

	if (customer == null) {
	    return null;
	}

	ArrayList<Account> accounts = customer.getAccounts();

	for (Account account : accounts) {
	    simpleAccounts.add(new backend.api.Account(account.getBalance(), account.getTransactions()));
	}

	return simpleAccounts;
    }

    @Override
    public boolean transfer(String fromAccountID, String toAccountID, double amount, LocalDate date) {
	String[] parts = toAccountID.split("_");

	// transfer to our bank
	if (parts.length > 0 && parts[0].equals(this.bankNumber)) {
	    Account toAccount = findAccount(toAccountID);
	    Account fromAccount = findAccount(fromAccountID);

	    if (toAccount == null || fromAccount == null) {
		return false;
	    }

	    toAccount.deposit(amount);
	    toAccount.addTransaction(new Transaction(toAccountID, fromAccountID, amount, date));
	    fromAccount.withdraw(amount);
	    fromAccount.addTransaction(new Transaction(toAccountID, fromAccountID, -amount, date));

	    return true;
	} else {
	    // TODO transfer to another bank
	}

	return false;
    }

    @Override
    public boolean createCustomer(String firstName, String lastName, String password) {
	Customer customer = new Customer(Utils.generateGUID(), firstName, lastName, password);

	this.customers.add(customer);

	return true;
    }

    @Override
    public boolean createAccount(String customerID, AccountType type, double balance, double interest,
	    double overdraftInterest, double dailyLimit, double monthyLimit, int pin) {
	Customer customer = this.findCustomer(customerID);

	if (customer == null) {
	    return false;
	}

	Account account = null;
	// really needed? Can't be done with same account?
	switch (type) {
	case PRIVATE:
	    account = new PrivateAccount(this.generateAccountID(), balance, interest, overdraftInterest, dailyLimit,
		    monthyLimit, pin);
	case SAVINGS:
	    account = new SavingsAccount(this.generateAccountID(), balance, interest, overdraftInterest, dailyLimit,
		    monthyLimit, pin);
	}

	if (account == null) {
	    return false;
	}

	account.setCustomer(customer);
	customer.addAccount(account);
	this.accounts.add(account);

	return true;
    }

    @Override
    public boolean closeAccount(String accountID) {
	Account account = findAccount(accountID);

	if (account == null) {
	    return false;
	}

	account.setState(State.CLOSED);

	return true;
    }

    @Override
    public backend.api.Account showAccount(String accountID) {
	Account account = findAccount(accountID);

	if (account == null) {
	    return null;
	}

	return new backend.api.Account(account.getBalance(), account.getTransactions());
    }

    @Override
    public boolean deposit(String accountID, double amount) {
	Account toAccount = findAccount(accountID);

	if (toAccount == null) {
	    return false;
	}

	return this.transfer(this.bankAccount.getAccountID(), toAccount.getAccountID(), amount, LocalDate.now());
    }

    @Override
    public boolean withdraw(String accountID, double amount) {
	Account fromAccount = findAccount(accountID);

	if (fromAccount == null) {
	    return false;
	}

	return this.transfer(fromAccount.getAccountID(), this.bankAccount.getAccountID(), amount, LocalDate.now());
    }

    private Account findAccount(String accountID) {
	if (this.bankAccount.getAccountID().equals(accountID)) {
	    return this.bankAccount;
	}

	for (Account account : this.accounts) {
	    if (account.getAccountID().equals(accountID)) {
		return account;
	    }
	}

	return null;
    }

    private Customer findCustomer(String customerID) {
	for (Customer customer : this.customers) {
	    if (customer.getCustomerID().equals(customerID)) {
		return customer;
	    }
	}

	return null;
    }

    private String generateAccountID() {
	return this.bankNumber + "_" + Utils.generateGUID();
    }

}
