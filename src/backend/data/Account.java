package backend.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import backend.api.Transaction;
import util.Utils;

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
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private State state;

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

    public void setCustomer(Customer customer) {
	this.customer = customer;
    }

    public boolean deposit(double amount) {
	this.balance += amount;

	return true;
    }

    public boolean withdraw(double amount) {
	if (isDailyLimitReached(amount) || isMontlyLimitReached(amount) || this.balance - amount <= -this.maxMinus) {
	    return false;
	}

	this.balance -= amount;

	return true;
    }

    public void addTransaction(Transaction transaction) {
	this.transactions.add(transaction);
    }

    public boolean login(int pin) {
	return this.pin == pin;
    }

    public Account show() {
	return this;
    }

    public boolean isMontlyLimitReached(double amount) {
	LocalDate date = LocalDate.now().minusMonths(1);

	return isLimitReached(this.getTransactions(), date, this.getMonthyLimit(), amount);
    }

    public boolean isDailyLimitReached(double amount) {
	LocalDate date = LocalDate.now();

	return isLimitReached(this.getTransactions(), date, this.getDailyLimit(), amount);
    }

    public boolean isLimitReached(ArrayList<Transaction> transactions, LocalDate date, double limit, double amount) {
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

    public boolean setState(State state) {
	this.state = state;

	return true;
    }

    public double getBalance() {
	return balance;
    }

    public double getInterest() {
	return interest;
    }

    public double getOverdraftInterest() {
	return overdraftInterest;
    }

    public double getDailyLimit() {
	return dailyLimit;
    }

    public double getMonthyLimit() {
	return monthyLimit;
    }

    public String getAccountID() {
	return accountID;
    }

    public Customer getCustomer() {
	return customer;
    }

    public int getPin() {
	return pin;
    }

    public ArrayList<Transaction> getTransactions() {
	return transactions;
    }

    public State getState() {
	return state;
    }

}
