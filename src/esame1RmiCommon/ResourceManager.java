package esame1RmiCommon;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ResourceManager extends Remote{

	public void printout() throws RemoteException;
	
	public Resource getA() throws RemoteException;
	
	public Resource getB() throws RemoteException;
	
	public void put(Resource r) throws RemoteException;
	
}
