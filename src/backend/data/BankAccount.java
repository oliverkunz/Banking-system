package backend.data;

import util.Utils;

public class BankAccount extends Account {
    public BankAccount(double balance, double interest, double overdraftInterest, double dailyLimit, double monthyLimit,
	    int pin) {
	super(Utils.generateGUID(), balance, interest, overdraftInterest, dailyLimit, monthyLimit, pin);
	// TODO Auto-generated constructor stub
    }

    @Override
    public boolean withdraw(double amount) {
	// TODO Auto-generated method stub
	return false;
    }

}
