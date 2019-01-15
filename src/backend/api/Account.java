package backend.api;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Account entity, transfered via rmi
 * 
 * @author fkg
 */
public class Account implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 2569733681448450445L;
    private String accountID;
    private double balance;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public Account(String accountID, double balance, ArrayList<Transaction> transactions) {
	this.accountID = accountID;
	this.balance = balance;
	this.transactions = transactions;
    }

    /**
     * get the current balance of the account
     * 
     * @return balance
     */
    public double getBalance() {
	return balance;
    }

    /**
     * get all the transaction associated with the account
     * 
     * @return transactions
     */
    public ArrayList<Transaction> getTransactions() {
	return transactions;
    }

    /**
     * @return accountID
     */
    public String getAccountID() {
	return accountID;
    }

}
