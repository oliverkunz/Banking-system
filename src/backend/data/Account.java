package backend.data;

import java.time.LocalDate;
import java.util.ArrayList;

import util.Utils;

public abstract class Account {
    protected double balance;
    private double interest;
    private double overdraftInterest;
    private double dailyLimit;
    private double monthyLimit;
    private String accountID;
    private Customer customer;
    private int pin;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private State state;

    public Account(String accountID, double balance, double interest, double overdraftInterest, double dailyLimit,
	    double monthyLimit, int pin) {
	this.balance = balance;
	this.interest = interest;
	this.overdraftInterest = overdraftInterest;
	this.dailyLimit = dailyLimit;
	this.monthyLimit = monthyLimit;
	this.pin = pin;
	this.accountID = Utils.generateGUID();
    }

    public void setCustomer(Customer customer) {
	this.customer = customer;
    }

    public boolean deposit(double amount) {
	this.balance += amount;

	return true;
    }

    public boolean withdraw(double amount) {
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

    public boolean checkMontlyLimit() {
	LocalDate date = LocalDate.now().minusMonths(1);

	return checkLimit(this.getTransactions(), date, this.getMonthyLimit());
    }

    public boolean checkDailyLimit() {
	LocalDate date = LocalDate.now().minusDays(1);

	return checkLimit(this.getTransactions(), date, this.getDailyLimit());
    }

    public boolean checkLimit(ArrayList<Transaction> transactions, LocalDate date, double limit) {
	double currentTransfers = 0;

	for (Transaction transaction : this.getTransactions()) {
	    if (Utils.isBetween(date, LocalDate.now(), transaction.getDueDate())) {
		// transaction is withdraw
		if (transaction.getAmount() < 0) {
		    currentTransfers += -transaction.getAmount();
		}
	    }
	}

	return currentTransfers >= limit;
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
