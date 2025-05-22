package esame1Server;

import java.io.*;
import java.net.*;

import esame1Common.*;

public class ServerSlave extends Thread {
	
	private Socket client;				//connette il thread al client
	private String clientName;			//nome del client con cui siamo connessi
	private ObjectInput inObj;			//ObjectIO per serializzare le risorse
	private ObjectOutput outObj;
	private Server s;					//per gestire risorse in maniera sincronizzata
	
	//la connessione tra lo slave ed il client viene fornita direttamente dal server
	
	public ServerSlave(Socket client) throws IOException {
		this.client = client;
		s = Server.getInstance();
		inObj = new ObjectInputStream(this.client.getInputStream());
		outObj = new ObjectOutputStream(this.client.getOutputStream());
		outObj.flush();			//forziamo l'inizializzazione dello stream
	}
	
	
	//tolgo una risorsa dalla lista di A, la serializzo e la mando al client
	
	
	private void sendA() throws ClassNotFoundException, IOException{
		Resource a;
		try {
			
			//prendo la risorsa dal server
			
			a = s.getA();
			
			//la serializzo e la mando al client
			
			outObj.writeObject(a);
			outObj.flush();
			System.out.println("Risorsa " + a.getType() + a.getNum() + " ceduta a " + clientName);
			s.printout();
			
			//torniamo ad ascoltare il client
			
			listen();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//stessa cosa ma con le risorse B
	
	
	private void sendB() throws ClassNotFoundException, IOException{
		Resource b;
		try {
			b = s.getB();
			outObj.writeObject(b);
			outObj.flush();
			System.out.println("Risorsa " + b.getType() + b.getNum() + " ceduta a " + clientName);
			s.printout();
			listen();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//prendo una risorsa A usata ed inviata dal client e la rimetto a posto
	
	
	private void getResource() throws IOException, ClassNotFoundException{
		try {
			
			//aspettiamo un secondo per leggere correttamente la richiesta
			
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//leggiamo la risorsa rilasciata dal client
		
		Resource r = (Resource)inObj.readObject();
		ResourceType rt = r.getType();
		
		//se è di tipo A la mettiano nella lista A
		
		if(rt == ResourceType.A)
			s.addA(r);
		
		//altrimenti lista B
		
		else if(rt == ResourceType.B)
			s.addB(r);
		
		//se non è né A né B, allora c'è qualcosa che non va
		
		else
			throw new IOException();
		System.out.println("Il server riprende risorsa " + r.getType() + r.getNum() + " dal client " + clientName);
		s.printout();
		
		//torniamo ad ascoltare le richieste
		
		listen();
	}
	
	
	//ascoltiamo le richieste del client
	
	
	private void listen() throws ClassNotFoundException, IOException {
		try {
			
			//attendiamo per leggere correttamente 
			
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		char azione;
		while(true) {
			try {
				
				//in base a questo char, il thread eseguirà un metodo
				
				azione = (char)inObj.readObject();
				break;
			} catch(EOFException e) {
				System.out.println("Il server aspetta ordini");
			}
		}
		switch(azione ) {
		case 'A': sendA();
		case 'B': sendB();
		case 'R': getResource();
		default: throw new IOException();
		}
	}
	
	public void run() {
		try {
			
			//è il client stesso a fornire il suo nome
			
			clientName = (String)inObj.readObject();
			while(true) {
				listen();
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
}