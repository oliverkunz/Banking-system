package Calculator;

import java.rmi.RemoteException;

public class CalculatorImpl implements Calculator {

	public double add(double x, double y) throws RemoteException {
		System.out.println("add x: " + x + " add y: " + y);
		return x + y;
	}

}
