package mcdClient;

import mcdCommon.MCD;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Client {

	public static void main(String[] args) throws RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		Registry reg = LocateRegistry.getRegistry(1099);
		MCD mcd = (MCD) reg.lookup("mcd");
		System.out.println("Test con 12 e 18: l'MCD è: " + mcd.mcd(12, 18));

	}

}
