package backend;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import backend.business.BankManager;

public class Main {
    public static void main(String[] args) throws AccessException, RemoteException, AlreadyBoundException {
	BankManager manager1 = new BankManager("UBS", "ubs");
	BankManager manager2 = new BankManager("Raiffeisen", "rai");
	BankManager stub1 = (BankManager) UnicastRemoteObject.exportObject(manager1, 0);
	BankManager stub2 = (BankManager) UnicastRemoteObject.exportObject(manager2, 0);

	LocateRegistry.createRegistry(123).bind("ubs", stub1);
	LocateRegistry.createRegistry(123).bind("rai", stub2);
    }
}
