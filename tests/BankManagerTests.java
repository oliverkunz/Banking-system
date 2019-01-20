import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import backend.api.Transaction;

class BankManagerTests {
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

}
