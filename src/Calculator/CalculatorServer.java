package Calculator;

import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorServer {

	public static void main(String[] args) throws Exception {
		Calculator calculator = new CalculatorImpl();
		Calculator stub =  (Calculator) UnicastRemoteObject.exportObject(calculator, 0);
		LocateRegistry.createRegistry(123).bind("Calculator", stub);

	}

}
