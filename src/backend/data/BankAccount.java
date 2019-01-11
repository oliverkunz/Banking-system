package backend.data;

import java.io.Serializable;

public class BankAccount extends Account implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1266974104710152460L;

    public BankAccount(String accountID, double balance, double interest, double overdraftInterest, double dailyLimit,
	    double monthyLimit, double maxMinus, int pin) {
	super(accountID, balance, interest, overdraftInterest, dailyLimit, monthyLimit, maxMinus, pin);
    }
}
