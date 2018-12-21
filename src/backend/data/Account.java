package backend.data;

import java.util.ArrayList;

public abstract class Account {
    private double balance;
    private double interest;
    private double overdraftInterest;
    private double dailyLimit;
    private double monthyLimit;
    private String accountID;
    private Customer customer;
    private int pin;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private State state;

    public boolean deposit(double amount) {
	return true;
    }

    public boolean withdraw(double amount) {
	return true;
    }

    public boolean login(int pin) {
	return true;
    }

    public Account show() {
	return this;
    }

    public boolean setState(State state) {
	return true;
    }

}
