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
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import backend.api.Account;
import backend.api.AccountType;
import backend.business.Bank;

class BankManagerIT {

    static Bank stub1;
    static Bank stub2;

    static Map<String, List<String>> customers = new HashMap<String, List<String>>();
    static Map<String, List<String>> customers2 = new HashMap<String, List<String>>();

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
	stub2 = (Bank) registry.lookup("raif");
    }

    @Test
    void testRMI() throws RemoteException, NotBoundException {
	Registry registry = LocateRegistry.getRegistry("localhost", 2001);
	stub1 = (Bank) registry.lookup("ubs");
	stub2 = (Bank) registry.lookup("raif");

	String customerId = stub1.createCustomer("Fabian", "Küng", "1234");
	System.out.println(customerId);
	customers.put(customerId, new ArrayList<String>());

	assertNotNull(customerId);

	customerId = stub1.createCustomer("Oliver", "Kunz", "1234");
	customers.put(customerId, new ArrayList<String>());

	assertNotNull(customerId);

	String accountId = stub1.createAccount(customers.keySet().stream().findFirst().get(), AccountType.PRIVATE, 1000,
		0.25, 1.25, 600, 1000, 600, 1234);
	customers.values().stream().findFirst().get().add(accountId);
	assertNotNull(accountId);

	accountId = stub1.createAccount(customers.keySet().stream().skip(1).findFirst().get(), AccountType.SAVINGS,
		5000, 1.25, 0, 700, 1000, 0, 1234);
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

	// limit reached
	assertFalse(stub1.transfer(customers.values().stream().skip(1).findFirst().get().get(0),
		customers2.values().stream().skip(1).findFirst().get().get(0), 1400, LocalDate.now()));

    }

}
