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
 * @author fkg Used to start the bank backend
 */
public class Main {
    private static final String CONFIG_FILE = "bank.properties";

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Creates the RMI server and binds each bank
     * 
     * @param args
     * @throws AlreadyBoundException
     * @throws IOException
     * 
     */
    public static void main(String[] args) throws AlreadyBoundException, IOException {
	Properties props = new Properties();
	props.load(new FileReader(CONFIG_FILE));
	props.list(System.out);

	Registry registry = LocateRegistry.createRegistry(2001);

	for (String key : props.stringPropertyNames()) {
	    if (key.startsWith("bank.") && key.endsWith(".id")) {
		String bankId = props.getProperty(key);
		String bankName = props.getProperty(key.replace(".id", ".name"));

		BankManager bank = new BankManager(bankId, bankName);
		Bank stub = (Bank) UnicastRemoteObject.exportObject(bank, 0);
		registry.bind(bankId, stub);

		LOGGER.log(Level.INFO, "Creating binding for bankId {0} and bankName {1}",
			new Object[] { bankId, bankName });
	    }
	}
    }
}
