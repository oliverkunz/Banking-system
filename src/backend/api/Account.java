package backend.api;

import java.util.ArrayList;

import backend.data.Transaction;

public class Account {
    private double balance;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public Account(double balance, ArrayList<Transaction> transactions) {
	super();
	this.balance = balance;
	this.transactions = transactions;
    }
}
