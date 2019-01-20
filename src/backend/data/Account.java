package backend.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import backend.api.Transaction;
import util.Utils;

/**
 * Internally used by the banks. Business logic for all accounts
 * 
 * @author fkg
 *
 */
public abstract class Account implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -5815010121334705462L;
    protected double balance;
    private double interest;
    private double overdraftInterest;
    private double dailyLimit;
    private double monthyLimit;
    private double maxMinus;
    private String accountID;
    private Customer customer;
    private int pin;
    private List<Transaction> transactions = new ArrayList<Transaction>();
    private State state;

    private static final Logger LOGGER = Logger.getLogger(Account.class.getName());

    public Account(String accountID, double balance, double interest, double overdraftInterest, double dailyLimit,
	    double monthyLimit, double maxMinus, int pin) {
	this.balance = balance;
	this.interest = interest;
	this.overdraftInterest = overdraftInterest;
	this.dailyLimit = dailyLimit;
	this.monthyLimit = monthyLimit;
	this.maxMinus = maxMinus;
	this.pin = pin;
	this.accountID = accountID;
    }

    /**
     * Sets the customer for the account
     * 
     * @param customer
     */
    public void setCustomer(Customer customer) {
	this.customer = customer;
    }

    /**
     * Deposit money to the account
     * 
     * @param amount
     * @return success
     */
    public boolean deposit(double amount) {
	this.balance += amount;

	return true;
    }

    /**
     * Withdraw money from the account
     * 
     * @param amount
     * @return success
     */
    public boolean withdraw(double amount) {
	if (isDailyLimitReached(amount) || isMontlyLimitReached(amount) || this.balance - amount <= -this.maxMinus) {
	    LOGGER.info("Could not withdraw money, limit is reached or negative limit reached");
	    return false;
	}

	this.balance -= amount;

	return true;
    }

    /**
     * Add new transaction to the account
     * 
     * @param transaction
     */
    public void addTransaction(Transaction transaction) {
	this.transactions.add(transaction);
    }

    /**
     * Login using pin
     * 
     * @param pin
     * @return success
     */
    public boolean login(int pin) {
	return this.pin == pin;
    }

    /**
     * Show accoutn details
     * 
     * @return account
     * 
     */
    public Account show() {
	return this;
    }

    /**
     * CHeck if monthly limit is reached
     * 
     * @param amount
     * @return reached
     */
    public boolean isMontlyLimitReached(double amount) {
	LocalDate date = LocalDate.now().minusMonths(1);

	return isLimitReached(this.getTransactions(), date, this.getMonthyLimit(), amount);
    }

    /**
     * CHeck if daily limit is reached
     * 
     * @param amount
     * @return reached
     */
    public boolean isDailyLimitReached(double amount) {
	LocalDate date = LocalDate.now();

	return isLimitReached(this.getTransactions(), date, this.getDailyLimit(), amount);
    }

    /**
     * CHeck if limit is reached
     * 
     * @param transactions
     * @param date
     * @param limit
     * @param amount
     * @return success
     */
    public boolean isLimitReached(List<Transaction> transactions, LocalDate date, double limit, double amount) {
	double currentTransfers = amount;

	if (limit == 0) {
	    return false;
	}

	for (Transaction transaction : this.getTransactions()) {
	    // transaction date between 1 month or 1 day and now
	    if (Utils.isBetween(date, LocalDate.now(), transaction.getDueDate())) {
		// transaction is withdraw
		if (transaction.getAmount() < 0) {
		    currentTransfers += -transaction.getAmount();
		}
	    }
	}

	return currentTransfers > limit;
    }

    /**
     * Sets state (closed, open)
     * 
     * @param state
     * @return success
     */
    public boolean setState(State state) {
	this.state = state;

	return true;
    }

    /**
     * @return balance
     */
    public double getBalance() {
	return balance;
    }

    /**
     * @return interest
     */
    public double getInterest() {
	return interest;
    }

    /**
     * @return overdraftInterest
     */
    public double getOverdraftInterest() {
	return overdraftInterest;
    }

    /**
     * @return dailyLimit
     */
    public double getDailyLimit() {
	return dailyLimit;
    }

    /**
     * @return monthlyLimit
     */
    public double getMonthyLimit() {
	return monthyLimit;
    }

    /**
     * @return accountID
     */
    public String getAccountID() {
	return accountID;
    }

    /**
     * @return customer
     */
    public Customer getCustomer() {
	return customer;
    }

    /**
     * @return pin
     */
    public int getPin() {
	return pin;
    }

    /**
     * @return transactions
     */
    public List<Transaction> getTransactions() {
	return transactions;
    }

    /**
     * @return state
     */
    public State getState() {
	return state;
    }

}
