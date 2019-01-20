package backend;

import java.io.FileReader;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import backend.business.Bank;
import backend.business.BankManager;

/**
 * Used to start the bank backend
 * 
 * @author fkg
 */
public class Main {
    private static final String CONFIG_FILE = "bank.properties";

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Creates the RMI server and binds each bank
     * 
     * @param args
     *
     * @throws AlreadyBoundException
     * @throws IOException
     * 
     */
    public static void main(String[] args) throws AlreadyBoundException, IOException {
	// read the bank config file, used to configure all banks
	Properties props = new Properties();
	props.load(new FileReader(CONFIG_FILE));

	Registry registry = LocateRegistry.createRegistry(2001);

	// iterate to each entry, take each bank id
	for (String key : props.stringPropertyNames()) {
	    if (key.startsWith("bank.") && key.endsWith(".id")) {
		String bankId = props.getProperty(key);
		String bankName = props.getProperty(key.replace(".id", ".name"));

		// bind object for created bank
		BankManager bank = new BankManager(bankId, bankName);
		Bank stub = (Bank) UnicastRemoteObject.exportObject(bank, 0);
		registry.bind(bankId, stub);

		LOGGER.log(Level.INFO, "Creating binding for bankId {0} and bankName {1}",
			new Object[] { bankId, bankName });
	    }
	}
    }
}
