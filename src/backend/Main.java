package backend;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import backend.business.Bank;
import backend.business.BankManager;

/**
 * @author fkg Used to start the bank backend
 */
public class Main {
    /**
     * Creates the RMI server and binds each bank
     * 
     * @param args
     * @throws AlreadyBoundException
     * @throws IOException
     * 
     */
    public static void main(String[] args) throws AlreadyBoundException, IOException {
	BankManager manager1 = new BankManager("ubs");
	BankManager manager2 = new BankManager("raif");

	Bank stub1 = (Bank) UnicastRemoteObject.exportObject(manager1, 0);
	Bank stub2 = (Bank) UnicastRemoteObject.exportObject(manager2, 0);

	Registry registry = LocateRegistry.createRegistry(2001);
	registry.bind("ubs", stub1);
	registry.bind("raif", stub2);
    }
}
