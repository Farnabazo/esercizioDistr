package esame1RmiClient;

import java.rmi.RemoteException;
import java.util.concurrent.*;
import esame1RmiCommon.*;

public class Client extends Thread {
	
	ResourceManager repository;		//gestore risorse locale fornito dal server
	Resource rA, rB;				//risorse
	String mioNome;					//nome del thread
	boolean rilasciato = false;		/*true se il thread rilascia una
	 								risorsa per evitare deadlock (se
	 								il thread ha una risorsa A e ne 
	 								vuole una B ma non è disponibile,
	 								rilsaciato segnala che prima di 
	 								prendere quella nuova deve
	 								riprendersi quella rilasciata*/
	
	public Client(ResourceManager r) {
		repository = r;
	}
	
	void acquisizioneA() throws RemoteException {
		while(true) {
			if(rilasciato) {				/*se in precedenza
											il thread ha rilasciato
			 								una risorsa di tipo B
			 								dovrà riprendere prima
			 								quella*/
				rilasciato = false;
				acquisizioneB();
			}
			rA=repository.getA();
			if(rA==null) {
				if(rB!=null) {				/*per evitare deadlock
				 							se il thread aveva una
				 							risorsa di altro tipo
				 							la rilascia nell'attesa*/
					rilasciato = true;
					rilascio(rB);
					rB=null;
				}
				try {
					Thread.sleep(ThreadLocalRandom.current().nextInt(20, 80));
				} catch (InterruptedException e) {}
			} else {
				break;
			}
		}
		System.out.println(mioNome+" acquisito risorsa "+rA.getType()+rA.getNum());
	}
	
	void acquisizioneB() throws RemoteException {
		while(true) {
			if(rilasciato) {
				rilasciato = false;
				acquisizioneA();
			}
			rB=repository.getB();
			if(rB==null) {
				if(rA!=null) {
					rilasciato = true;
					rilascio(rA);
					rA=null;
				}
				try {
					Thread.sleep(ThreadLocalRandom.current().nextInt(20, 80));
				} catch (InterruptedException e) {}
			} else {
				break;
			}
		}
		System.out.println(mioNome+" acquisito risorsa "+rB.getType()+rB.getNum());
	}
	
	void rilascio(Resource r) throws RemoteException {
		System.out.println(mioNome+" rilascio risorsa "+r.getType()+r.getNum());
		repository.put(r);
	}
	
	public void run() {
		mioNome=getName();
		try {
			for(;;){
				if(ThreadLocalRandom.current().nextBoolean()) {
					// ho bisogno di una risorsa
					if(ThreadLocalRandom.current().nextBoolean()) {
						System.out.println(mioNome+" acquisisco prima risorsa A ");
						acquisizioneA();
					} else {
						System.out.println(mioNome+" acquisisco prima risorsa B ");
						acquisizioneB();
					}
					Thread.sleep(ThreadLocalRandom.current().nextInt(100, 200));
					// vediamo se c'e` bisogno di una seconda risorsa
					if(ThreadLocalRandom.current().nextBoolean()) {
						// ho bisogno della seconda risorsa
						// che deve essere di tipo diverso dalla prima
						if(rA!=null) {
							System.out.println(mioNome+" acquisisco risorsa B avendo A");
							acquisizioneB();
						} else {
							System.out.println(mioNome+" acquisisco risorsa A avendo B");
							acquisizioneA();
						}
					}
				}
				// elaborazione
				Thread.sleep(ThreadLocalRandom.current().nextInt(200, 400));
				//rilascio eventuali risorse
				if(rA!=null) {
					rilascio(rA);
				}
				if(rB!=null) {
					rilascio(rB);
				}
				rA=rB=null;
			}
		} catch (InterruptedException | RemoteException e) {
			e.printStackTrace();
		}
	}
}
