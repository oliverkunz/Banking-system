package backend.data;

public class PrivateAccount extends Account {
    public PrivateAccount(String accountID, double balance, double interest, double overdraftInterest,
	    double dailyLimit, double monthyLimit, int pin) {
	super(accountID, balance, interest, overdraftInterest, dailyLimit, monthyLimit, pin);
    }

    public boolean withdraw(double amount) {
	// TODO Maybe check if user has enough money?
	if (checkMontlyLimit() || checkDailyLimit()) {
	    return false;
	}

	this.balance -= amount;

	return true;
    }

    public boolean overdrawInterests() {
	return true;
    }
}
