package backend.data;

import java.time.LocalDate;

public class Transaction {
    private String receiverID;
    private String senderID;
    private double amount;
    private LocalDate dueDate;

    public Transaction(String receiverID, String senderID, double amount, LocalDate dueDate) {
	this.receiverID = receiverID;
	this.senderID = senderID;
	this.amount = amount;
	this.dueDate = dueDate;
    }

    public String getReceiver() {
	return receiverID;
    }

    public String getSender() {
	return senderID;
    }

    public double getAmount() {
	return amount;
    }

    public LocalDate getDueDate() {
	return dueDate;
    }

}
