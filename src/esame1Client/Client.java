package esame1Client;

import java.io.*;
import java.net.*;
import java.util.Random;

import esame1Common.*;

public class Client extends Thread {
	
	private Random r;							//per scegliere cosa farà il client
	private Resource a;
	private Resource b;
	private static InetAddress addr;
	private static final int PORT = 8000;
	private Socket s;
	private char azione;						//per mandare richieste al server
	private boolean rilasciato = false;			//true se il client ha bisogno di due risorse ma
												//ne rilascia una per evitare deadlock
	private ObjectOutput outObj;
	private ObjectInput inObj;
	
	public Client() throws IOException {
		addr = InetAddress.getByName("localhost");
		s = new Socket(addr, PORT);
		r = new Random();
		outObj = new ObjectOutputStream(s.getOutputStream());
		outObj.flush();
		inObj = new ObjectInputStream(s.getInputStream());
	}
	
	public void acquisizioneA() throws ClassNotFoundException, IOException {
		while(true) {
			
			/*da eseguire qualora il client abbia precedentemente
			rilasciato una risorsa solo per evitare deadlock, ma
			che comunque gli serve*/
			
			if(rilasciato) {
				rilasciato = false;
				acquisizioneB();
			}
			azione = 'A';
			System.out.println(getName().concat(" sta richiedendo una risorsa di tipo A"));
			
			//mando la richiesta al server
			
			outObj.writeObject(azione);
			outObj.flush();
			
			//ricevo la risorsa (se c'è)
			
			a = (Resource)inObj.readObject();
			
			//se non c'è ci riprovo da capo
			
			if(a == null) {
				
				/*ma prima, se avevo un'altra risorsa la
				la rilascio per evitare deadlock*/
				
				if(b != null) {
					rilasciato = true;
					rilascio(b);
				}
			}else {
				System.out.println(getName() + " acquisice risorsa " + a.getType() + a.getNum());
				break;
			}
		}
	}
	
	public void acquisizioneB() throws ClassNotFoundException, IOException {
		while(true) {
			if(rilasciato) {
				rilasciato = false;
				acquisizioneA();
			}
			azione = 'B';
			System.out.println(getName().concat(" sta richiedendo una risorsa di tipo B"));
			outObj.writeObject(azione);
			outObj.flush();
			b = (Resource)inObj.readObject();
			if(b == null) {
				if(a != null) 
					rilasciato = true;
					rilascio(a);
			}else {
				System.out.println(getName() + " acquisisce risorsa " + b.getType() + b.getNum());
				break;
			}
		}
	}
	
	public void rilascio(Resource r) throws IOException {
		
		//mando la richiesta al server
		
		azione = 'R';
		outObj.writeObject(azione);
		outObj.flush();
		System.out.println(getName() + " rilascia risorsa " + r.getType() + r.getNum());
		
		//ora mando l'oggetto al server
		
		outObj.writeObject(r);
		outObj.flush();
		
		//e resetto la mia risorsa per evitare duplicati
		
		if(r.getType() == ResourceType.A) {
			a = null;
		}else {
			b = null;
		}
	}
	
	public void run() {
		try {
			
			//mando il mio nome al server
			
			outObj.writeObject(getName());
			outObj.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			if(r.nextBoolean()) {				//true = ho bisogno di una risorsa
				if(r.nextBoolean()) {			//true = risorsa di tipo A, altrimenti di tipo B
					try {
						acquisizioneA();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						acquisizioneB();
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				if(r.nextBoolean()) { 			//true = ho bisogno di un'altra risorsa (di tipo diverso)
					if(a == null) {
						try {
							acquisizioneA();
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							acquisizioneB();
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
					}
				}
				if(a != null) {
					try {
						rilascio(a);
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
				if(b != null) {
					try {
						rilascio(b);
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
