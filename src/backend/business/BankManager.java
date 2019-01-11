package backend.business;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.ArrayList;

import backend.api.ATM;
import backend.api.AccountType;
import backend.api.Administration;
import backend.api.Bank;
import backend.api.Banking;
import backend.api.Transaction;
import backend.data.Account;
import backend.data.BankAccount;
import backend.data.Customer;
import backend.data.PrivateAccount;
import backend.data.SavingsAccount;
import backend.data.State;
import util.Utils;

public class BankManager implements ATM, Banking, Administration, Bank {
    private String bankName;
    private String bankNumber;
    private ArrayList<Account> accounts;
    private ArrayList<Customer> customers;
    private BankAccount bankAccount;
    Registry registry;

    public BankManager(String bankName, String bankNumber) throws RemoteException {
	super();
	this.bankName = bankName;
	this.bankNumber = bankNumber;
	this.accounts = new ArrayList<Account>();
	this.customers = new ArrayList<Customer>();
	this.bankAccount = new BankAccount(this.generateAccountID(), 1000000, 0, 0, 0, 0, 0, 123456789);

	registry = LocateRegistry.getRegistry("localhost", 2001);
    }

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
	    return false;
	}

	boolean isInternal = fromParts[0].equals(this.bankNumber) && toParts[0].equals(this.bankNumber);
	boolean isToExternal = fromParts[0].equals(this.bankNumber) && !toParts[0].equals(this.bankNumber);
	boolean isFromExternal = !fromParts[0].equals(this.bankNumber) && toParts[0].equals(this.bankNumber);

	if (isInternal) {
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
	} else if (isToExternal) {
	    Account fromAccount = findAccount(fromAccountID);

	    if (fromAccount == null) {
		return false;
	    }

	    if (!fromAccount.withdraw(amount)) {
		return false;
	    }
	    fromAccount.addTransaction(new Transaction(toAccountID, fromAccountID, -amount, date));

	    Bank externalBank = this.getRemoteBankManager(toParts[0]);
	    externalBank.transfer(fromAccountID, toAccountID, amount, date);
	} else if (isFromExternal) {
	    Account toAccount = findAccount(toAccountID);

	    if (toAccount == null) {
		return false;
	    }

	    toAccount.deposit(amount);
	    toAccount.addTransaction(new Transaction(toAccountID, fromAccountID, amount, date));
	} else {
	    return false;
	}

	return true;
    }

    @Override
    public String createCustomer(String firstName, String lastName, String password) {
	Customer customer = new Customer(Utils.generateGUID(), firstName, lastName, password);

	this.customers.add(customer);

	return customer.getCustomerID();
    }

    @Override
    public String createAccount(String customerID, AccountType type, double balance, double interest,
	    double overdraftInterest, double dailyLimit, double monthyLimit, double maxMinus, int pin) {
	Customer customer = this.findCustomer(customerID);

	if (customer == null) {
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
	    return null;
	}

	account.setCustomer(customer);
	customer.addAccount(account);
	this.accounts.add(account);

	return account.getAccountID();
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
    public boolean deposit(String accountID, double amount) throws AccessException, RemoteException, NotBoundException {
	Account toAccount = findAccount(accountID);

	if (toAccount == null) {
	    return false;
	}

	return this.transfer(this.bankAccount.getAccountID(), toAccount.getAccountID(), amount, LocalDate.now());
    }

    @Override
    public boolean withdraw(String accountID, double amount)
	    throws AccessException, RemoteException, NotBoundException {
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

    private Bank getRemoteBankManager(String bankNumber) throws AccessException, RemoteException, NotBoundException {
	return (Bank) registry.lookup(bankNumber);
    }

    private String generateAccountID() {
	return this.bankNumber + "_" + Utils.generateGUID();
    }

    @Override
    public boolean handleInterests(String accountID) {
	// TODO needs this to be withdrawn or deposited to the bank account?
	return false;
    }

    @Override
    public boolean handleOverdrawInterests(String accountID) {
	// TODO Auto-generated method stub
	return false;
    }
}
