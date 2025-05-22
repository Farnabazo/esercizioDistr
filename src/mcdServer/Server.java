package mcdServer;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import mcdCommon.MCD;

public class Server extends UnicastRemoteObject implements MCD {
	
	public Server() throws RemoteException {
		super();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Registry reg = LocateRegistry.createRegistry(1099);
			MCD mcd = new Server();
			reg.bind("mcd", mcd);
			System.out.println("Server pronto");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public int mcd(int x, int y) throws RemoteException {
		int r;
		while(y != 0) {
			r = x % y;
			x = y; 
			y = r;
		}
		return x;
	}

}
