package backend.api;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Transactions for a account
 * 
 * @author fkg
 *
 */
public class Transaction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5300018486181450767L;
	private String receiverID;
	private String senderID;
	private double amount;
	private LocalDate dueDate;
	private TransactionType type;

	public Transaction(String receiverID, String senderID, double amount, LocalDate dueDate) {
		this(receiverID, senderID, amount, dueDate, TransactionType.NORMAL);
	}

	public Transaction(String receiverID, String senderID, double amount, LocalDate dueDate, TransactionType type) {
		this.receiverID = receiverID;
		this.senderID = senderID;
		this.amount = amount;
		this.dueDate = dueDate;
		this.type = type;
	}

	/**
	 * @return receiverID
	 */
	public String getReceiverID() {
		return receiverID;
	}

	/**
	 * @return senderID
	 */
	public String getSenderID() {
		return senderID;
	}

	/**
	 * @return amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @return dueDate
	 */
	public LocalDate getDueDate() {
		return dueDate;
	}

	/**
	 * @return type
	 */
	public TransactionType getType() {
		return type;
	}

}
