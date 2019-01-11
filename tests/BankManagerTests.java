import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import backend.api.Account;
import backend.api.AccountType;
import backend.api.Bank;
import backend.api.Transaction;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BankManagerTests {
    static Bank stub1;
    static Bank stub2;

    static HashMap<String, ArrayList<String>> customers = new HashMap<String, ArrayList<String>>();
    static HashMap<String, ArrayList<String>> customers2 = new HashMap<String, ArrayList<String>>();

    @BeforeAll
    static void setUpBeforeClass() throws Exception {

	/*
	 * BankManager manager1 = new BankManager("UBS", "ubs"); BankManager manager2 =
	 * new BankManager("Raiffeisen", "rai"); stub1 = (Bank)
	 * UnicastRemoteObject.exportObject(manager1, 0); stub2 = (Bank)
	 * UnicastRemoteObject.exportObject(manager2, 0);
	 * 
	 * Registry registry = LocateRegistry.getRegistry("localhost", 2001);
	 * registry.bind("ubs", stub1); registry.bind("rai", stub2);
	 */

	// make sure backend.Main is running
	Registry registry = LocateRegistry.getRegistry("localhost", 2001);
	stub1 = (Bank) registry.lookup("ubs");
	stub2 = (Bank) registry.lookup("rai");
    }

    @Test
    void testOverdraftLimit() {
	// account with:
	// balance 100
	// 2,5% interest
	// 6% overdraft interest
	// 400 daily limit
	// 1000 monthly limit
	// 100 overdraft limit
	// pin 1234
	// should be false because balance would be -200 and overdraft limit is -100
	backend.data.Account account = new backend.data.PrivateAccount("1234", 100, 2.5, 6, 400, 1000, 100, 1234);
	assertFalse(account.withdraw(300));
    }

    @Test
    void testDailyLimit() {
	// account with:
	// balance 2500
	// 2,5% interest
	// 6% overdraft interest
	// 400 daily limit
	// 1000 monthly limit
	// 100 overdraft limit
	// pin 1234
	// should be false daily limit is 400
	backend.data.Account account = new backend.data.PrivateAccount("1234", 2500, 2.5, 6, 400, 1000, 100, 1234);
	assertFalse(account.withdraw(500));
    }

    @Test
    void testDailyLimit2() {
	// account with:
	// balance 2500
	// 2,5% interest
	// 6% overdraft interest
	// 400 daily limit
	// 1000 monthly limit
	// 100 overdraft limit
	// pin 1234

	// should be false because already 100 withdrawn today and now again 500 and
	// limit is 400
	backend.data.Account account = new backend.data.PrivateAccount("1234", 2500, 2.5, 6, 400, 1000, 100, 1234);

	account.withdraw(100);
	account.addTransaction(new Transaction("12345", "1234", 100, LocalDate.now()));

	assertFalse(account.withdraw(500));
    }

    @Test
    void testMonthlyLimit() {
	// account with:
	// balance 2500
	// 2,5% interest
	// 6% overdraft interest
	// 400 daily limit
	// 1000 monthly limit
	// 100 overdraft limit
	// pin 1234

	// should be false because already 800 withdrawn
	// limit is 1000
	backend.data.Account account = new backend.data.PrivateAccount("1234", 2500, 2.5, 6, 400, 1000, 100, 1234);

	account.withdraw(400);
	account.addTransaction(new Transaction("12345", "1234", -400, LocalDate.now().minusDays(4)));
	account.withdraw(400);
	account.addTransaction(new Transaction("12345", "1234", -400, LocalDate.now().minusDays(3)));

	assertFalse(account.withdraw(300));
    }

    @Test
    void testDeposit() {
	// account with:
	// balance 2500
	// 2,5% interest
	// 6% overdraft interest
	// 400 daily limit
	// 1000 monthly limit
	// 100 overdraft limit
	// pin 1234

	// should be true because 2500 + 300 = 2800
	backend.data.Account account = new backend.data.PrivateAccount("1234", 2500, 2.5, 6, 400, 1000, 100, 1234);
	account.deposit(300);
	assertEquals(2800, account.getBalance());
    }

    @Test
    void testWithdraw() {
	// account with:
	// balance 2500
	// 2,5% interest
	// 6% overdraft interest
	// 400 daily limit
	// 1000 monthly limit
	// 100 overdraft limit
	// pin 1234

	// should be true because 2500 - 300 = 2200
	backend.data.Account account = new backend.data.PrivateAccount("1234", 2500, 2.5, 6, 400, 1000, 100, 1234);
	account.withdraw(300);
	assertEquals(2200, account.getBalance());
    }

    @Test
    void testAllAccount() {
	// account with:
	// balance 2000
	// 2,5% interest
	// 6% overdraft interest
	// 400 daily limit
	// 1000 monthly limit
	// overdraft limit
	// pin 1234
	backend.data.Account account = new backend.data.PrivateAccount("1234", 2000, 2.5, 6, 400, 1000, 400, 1234);

	assertTrue(account.deposit(500));
	account.addTransaction(new Transaction("12345", "1234", 500, LocalDate.now()));

	// balance is now 2500
	assertTrue(account.getBalance() == 2500);

	// lets make a new transaction (1000.-) 40 days ago
	// monthly limit shouldnt be reached
	assertTrue(account.withdraw(400));
	assertTrue(account.withdraw(400));
	assertTrue(account.withdraw(200));
	account.addTransaction(new Transaction("1234", "12345", -400, LocalDate.now().minusDays(40)));
	account.addTransaction(new Transaction("1234", "12345", -400, LocalDate.now().minusDays(45)));
	account.addTransaction(new Transaction("1234", "12345", -200, LocalDate.now().minusDays(50)));

	// now a transaction 200.- 3 days ago
	// daily limit and monthly limit shouldnt be reached
	assertTrue(account.withdraw(200));
	account.addTransaction(new Transaction("1234", "12345", -200, LocalDate.now().minusDays(3)));

	// now a transaction 400.- 2 days ago
	// daily limit and monthly limit shouldnt be reached
	assertTrue(account.withdraw(400));
	account.addTransaction(new Transaction("1234", "12345", -400, LocalDate.now().minusDays(2)));

	// now make a transaction with exactly 400.-
	// daily limit should be reached
	// also monthly
	assertTrue(account.withdraw(400));
	account.addTransaction(new Transaction("1234", "12345bvv bb b bv bnbnbv v", -400, LocalDate.now()));

	// now another transaction, shoudln't be able because daily limit reached
	assertFalse(account.withdraw(5));

    }

    @Test
    void testRMI() throws RemoteException, NotBoundException {
	Registry registry = LocateRegistry.getRegistry("localhost", 2001);
	stub1 = (Bank) registry.lookup("ubs");
	stub2 = (Bank) registry.lookup("rai");

	String customerId = stub1.createCustomer("Fabian", "Küng", "1234");
	System.out.println(customerId);
	customers.put(customerId, new ArrayList<String>());

	assertNotNull(customerId);

	customerId = stub1.createCustomer("Oliver", "Kunz", "1234");
	customers.put(customerId, new ArrayList<String>());

	assertNotNull(customerId);

	String accountId = stub1.createAccount(customers.keySet().stream().findFirst().get(), AccountType.PRIVATE, 1000,
		0.25, 1.25, 200, 1000, 600, 1234);
	customers.values().stream().findFirst().get().add(accountId);
	assertNotNull(accountId);

	accountId = stub1.createAccount(customers.keySet().stream().skip(1).findFirst().get(), AccountType.SAVINGS,
		5000, 1.25, 0, 200, 1000, 0, 1234);
	customers.values().stream().skip(1).findFirst().get().add(accountId);
	assertNotNull(accountId);

	assertTrue(stub1.transfer(customers.values().stream().findFirst().get().get(0),
		customers.values().stream().skip(1).findFirst().get().get(0), 100, LocalDate.now()));

	Account account = stub1.showAccount(customers.values().stream().findFirst().get().get(0));
	assertTrue(account.getBalance() == 900);

	Account account2 = stub1.showAccount(customers.values().stream().skip(1).findFirst().get().get(0));
	assertTrue(account2.getBalance() == 5100);

	// tests for crossbank
	customerId = stub2.createCustomer("Fabian", "Küng external", "123456789");
	customers2.put(customerId, new ArrayList<String>());

	assertNotNull(customerId);

	customerId = stub2.createCustomer("Oliver", "Kunz external", "1234");
	customers2.put(customerId, new ArrayList<String>());

	assertNotNull(customerId);

	// create savings account with 6000 for fk
	accountId = stub2.createAccount(customers2.keySet().stream().findFirst().get(), AccountType.SAVINGS, 6000, 0.25,
		1.25, 200, 1000, 0, 1234);
	customers2.values().stream().findFirst().get().add(accountId);
	assertNotNull(accountId);

	// create private account with 400 for ok
	accountId = stub2.createAccount(customers2.keySet().stream().skip(1).findFirst().get(), AccountType.PRIVATE,
		400, 1.25, 0, 200, 500, 1000, 1234);
	customers2.values().stream().skip(1).findFirst().get().add(accountId);
	assertNotNull(accountId);

	// transfer from ubs fk private to raif fk saving
	// 900 - 500 && 6000 + 500
	assertTrue(stub1.transfer(customers.values().stream().findFirst().get().get(0),
		customers2.values().stream().findFirst().get().get(0), 500, LocalDate.now()));

	account = stub1.showAccount(customers.values().stream().findFirst().get().get(0));
	assertTrue(account.getBalance() == 400);

	account = stub2.showAccount(customers2.values().stream().findFirst().get().get(0));
	assertTrue(account.getBalance() == 6500);

	// transfer from ubs ok savings to raif ok private
	// 5000 - 400 && 400 + 400
	assertTrue(stub1.transfer(customers.values().stream().skip(1).findFirst().get().get(0),
		customers2.values().stream().skip(1).findFirst().get().get(0), 400, LocalDate.now()));

	account = stub1.showAccount(customers.values().stream().skip(1).findFirst().get().get(0));
	assertTrue(account.getBalance() == 4700);

	account = stub2.showAccount(customers2.values().stream().skip(1).findFirst().get().get(0));
	assertTrue(account.getBalance() == 800);
    }

}
