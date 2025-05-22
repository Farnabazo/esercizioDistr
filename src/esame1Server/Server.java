package esame1Server;

import java.net.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import esame1Common.*;

import java.io.*;

public class Server {
	
	private LinkedList<Resource> listA;
	private LinkedList<Resource> listB;
	private final int PORT = 8000;
	private ServerSocket s;
	private Socket client;
	private static Server instance = new Server();
	
	private Server() {

		//inizializzo le liste
		
		listA = new LinkedList<Resource>();
		listB = new LinkedList<Resource>();
				
		//riempio le liste
				
		for(int i = 0; i < 2; i++) {
			listA.add(new Resource(ResourceType.A));
			listB.add(new Resource(ResourceType.B));
		}
		
	}
	
	public static Server getInstance() {
		return instance;
	}
	
	public void printout() {
		for(int i = 0; i < listA.size(); i++)
			System.out.println("" + listA.get(i).getType() + listA.get(i).getNum() + " ");
		System.out.println();
		for(int i = 0; i < listB.size(); i++)
			System.out.println("" + listB.get(i).getType() + listB.get(i).getNum() + " ");		
	}
	
	public void exec() {
		
		//stampo le liste per test
		
		printout();
		
		//il server resta in ascolto e avvia un thread per ogni socket creato
		
		try {
			s = new ServerSocket(PORT);
			while(true) {
				client = s.accept();
				new ServerSlave(client).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized Resource getA() throws InterruptedException {
		while(true) {
			try {
				Resource a = listA.remove();
				return a;
			}catch(NoSuchElementException e) {
				System.err.println("Non ci sono più risorse A!");
				wait();
			}
		}
	}
	
	public synchronized Resource getB() throws InterruptedException {
		while(true) {
			try {
				Resource b = listB.remove();
				return b;
			}catch(NoSuchElementException e) {
				System.err.println("Non ci sono più risorse B!");
				wait();
			}
		}
	}
	
	public synchronized void addA(Resource r) {
		listA.add(r);
	}
	
	public synchronized void addB(Resource r) {
		listB.add(r);
	}
	
}