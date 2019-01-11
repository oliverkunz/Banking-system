package backend.api;

import java.rmi.Remote;

public interface ExternalBank extends Remote {
    public void addTransactionToAccount(String accountID, Transaction transaction);
}
