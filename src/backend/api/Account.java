package backend.api;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 2569733681448450445L;
    private double balance;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public Account(double balance, ArrayList<Transaction> transactions) {
	this.balance = balance;
	this.transactions = transactions;
    }

    public double getBalance() {
	return balance;
    }

    public ArrayList<Transaction> getTransactions() {
	return transactions;
    }
}
