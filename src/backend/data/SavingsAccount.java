package backend.data;

public class SavingsAccount extends Account {
    public SavingsAccount(String accountID, double balance, double interest, double overdraftInterest,
	    double dailyLimit, double monthyLimit, double maxMinus, int pin) {
	super(accountID, balance, interest, overdraftInterest, dailyLimit, monthyLimit, maxMinus, pin);
	// TODO Auto-generated constructor stub
    }
}
