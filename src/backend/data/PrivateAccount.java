package backend.data;

import java.io.Serializable;

public class PrivateAccount extends Account implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 68571892282171033L;

    public PrivateAccount(String accountID, double balance, double interest, double overdraftInterest,
	    double dailyLimit, double monthyLimit, double maxMinus, int pin) {
	super(accountID, balance, interest, overdraftInterest, dailyLimit, monthyLimit, maxMinus, pin);
    }

    /**
     * OverdraftInterests are handled different
     * 
     * @return
     */
    public boolean overdrawInterests() {
	return true;
    }
}
