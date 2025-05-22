package esame1RmiServer;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import esame1RmiCommon.*;
import java.util.LinkedList;

public class Server extends UnicastRemoteObject implements ResourceManager{
	
	LinkedList<Resource> listA;  
	LinkedList<Resource> listB;
	
	public Server() throws RemoteException{
		super();
		listA = new LinkedList<>();  
		listB = new LinkedList<>(); 
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Registry reg = LocateRegistry.createRegistry(1099);
			ResourceManager obj = new Server();
			reg.bind("RM", obj);
			System.out.println("Server pronto");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void printout() {
		// TODO Auto-generated method stub
		Resource r;
		System.out.print("[A:");
		for(int i=0; i<listA.size(); i++) {
			r=listA.get(i);
			System.out.print(" "+r.getType()+r.getNum());
		}
		System.out.print("] [B:");
		for(int i=0; i<listB.size(); i++) {
			r=listB.get(i);
			System.out.print(" "+r.getType()+r.getNum());
		}
		System.out.println("]");
		
	}

	@Override
	public synchronized Resource getA() {
		Resource r;
		if(listA.isEmpty()) {
			return (Resource) null;
		} else {
			r=listA.remove(0);
			printout();
			return r;
		}
	}

	@Override
	public synchronized Resource getB() {
		Resource r;
		if(listB.isEmpty()) {
			return (Resource) null;
		} else {
			r=listB.remove(0);
			printout();
			return r;
		}
	}

	@Override
	public synchronized void put(Resource r) {
		if(r.getType()==ResourceType.A) {
			listA.add(r);
		}
		if(r.getType()==ResourceType.B) {
			listB.add(r);
		}
		printout();
	}

}
