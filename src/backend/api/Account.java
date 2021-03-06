package backend.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	private List<Transaction> transactions = new ArrayList<Transaction>();

	public Account(String accountID, double balance, List<Transaction> transactions) {
		this.accountID = accountID;
		this.balance = balance;
		this.transactions = transactions;
	}

	public Account(String accountID, double balance) {
		this(accountID, balance, new ArrayList<Transaction>());
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
	public List<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @return accountID
	 */
	public String getAccountID() {
		return accountID;
	}

}
