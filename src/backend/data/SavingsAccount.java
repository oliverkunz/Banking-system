package backend.data;

public class SavingsAccount extends Account {
    public SavingsAccount(String accountID, double balance, double interest, double overdraftInterest,
	    double dailyLimit, double monthyLimit, int pin) {
	super(accountID, balance, interest, overdraftInterest, dailyLimit, monthyLimit, pin);
	// TODO Auto-generated constructor stub
    }

    public boolean withdraw(double amount) {
	return true;
    }
}
