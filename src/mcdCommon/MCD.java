package mcdCommon;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MCD extends Remote {

	public int mcd(int x, int y) throws RemoteException;
	
}
