package frontend.common;

import java.rmi.RemoteException;

public interface Step {
    public void onNavigate() throws RemoteException;
}
