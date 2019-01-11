package backend.data;

import java.io.Serializable;

public class PrivateAccount extends Account implements Serializable {
    public PrivateAccount(String accountID, double balance, double interest, double overdraftInterest,
	    double dailyLimit, double monthyLimit, double maxMinus, int pin) {
	super(accountID, balance, interest, overdraftInterest, dailyLimit, monthyLimit, maxMinus, pin);
    }

    public boolean overdrawInterests() {
	return true;
    }
}
