package esame1RmiClient;

import esame1RmiCommon.*;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Main {
	
	static final int numUser=3;
	static final int numResources=1;
	
	public static void main(String[] args) {
		
		try {
			Registry stub = LocateRegistry.getRegistry(1099);
			ResourceManager rm = (ResourceManager)stub.lookup("RM");
			for(int i=0; i<numResources; i++) {  // Attenzione!
				// le risorse di ciascun tipo sono meno degli utilizzatori!
				Resource rA = new Resource(ResourceType.A);
				Resource rB = new Resource(ResourceType.B);
				rm.put(rA);
				rm.put(rB);
			}
			for(int i=0; i<numUser; i++) {
				new Client(rm).start();
			}
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}