package backend.business;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;

import backend.api.ATM;
import backend.api.Administration;
import backend.api.Banking;
import backend.data.Account;
import backend.data.Customer;

public class BankManager implements Administration, ATM, Banking {
    public static void main(String[] args) throws AccessException, RemoteException, AlreadyBoundException {
	BankManager manager = new BankManager();
	BankManager stub = (BankManager) UnicastRemoteObject.exportObject(manager, 0);
	LocateRegistry.createRegistry(123).bind("BankManager", stub);
    }

    @Override
    public boolean login(String customerID, String password) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public ArrayList<Account> showAccounts() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean transfer(Account toAccount, double amount, LocalDate date) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean login(String accountID, int pin) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean createCustomer(Customer customer) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean createAccount(Account account, Customer customer) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean closeAccount(Account account) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public Account showAccount(Account account) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean deposit(Account account, double amount) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean withdraw(Account account, double amount) {
	// TODO Auto-generated method stub
	return false;
    }

}
