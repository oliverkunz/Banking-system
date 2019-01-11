package backend;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import backend.api.Bank;
import backend.business.BankManager;

public class Main {
    public static void main(String[] args) throws AccessException, RemoteException, AlreadyBoundException {
	BankManager manager1 = new BankManager("UBS", "ubs");
	BankManager manager2 = new BankManager("Raiffeisen", "rai");
	Bank stub1 = (Bank) UnicastRemoteObject.exportObject(manager1, 0);
	Bank stub2 = (Bank) UnicastRemoteObject.exportObject(manager2, 0);

	Registry registry = LocateRegistry.createRegistry(2001);
	registry.bind("ubs", stub1);
	registry.bind("rai", stub2);
    }
}
