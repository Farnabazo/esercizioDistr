package esame1;

import java.util.concurrent.*;

public class User extends Thread {
	
	ResourceManager repository;
	Resource rA = null, rB = null;
	String mioNome;
	boolean rilasciato = false;
	
	public User(ResourceManager r) {
		repository = r;
	}
	
	void acquisizioneA() {
		while(true) {
			if(rilasciato) {
				rilasciato = false;
				acquisizioneB();
			}
			rA = repository.getA();
			if(rA == null) {
				try {
					if(rB != null) {
						rilasciato = true;
						rilascio(rB);
					}
					Thread.sleep(ThreadLocalRandom.current().nextInt(20, 80));
				} catch(InterruptedException e) {
					System.err.println("Error" + e);
				}
			} else {
				break;
			}
		}
		System.out.println(mioNome + " acquisito risorsa " + rA.getType() + rA.getNum());
	}
	
	void acquisizioneB() {
		while(true) {
			if(rilasciato) {
				rilasciato = false;
				acquisizioneA();
			}
			rB = repository.getB();
			if(rB == null) {
				try {
					if(rA != null) {
						rilasciato = true;
						rilascio(rA);
					}
					Thread.sleep(ThreadLocalRandom.current().nextInt(20, 80));
				} catch(InterruptedException e) {
					System.err.println("Error" + e);
				}
			} else {
				break;
			}
		}
		System.out.println(mioNome + " acquisito risorsa " + rB.getType() + rB.getNum());
	}
	
	void rilascio(Resource r) {
		System.out.println(mioNome + " rilascio risorsa " + r.getType() + r.getNum());
		repository.put(r);
	}
	
	public void run() {
		mioNome = getName();
		try {
			for(;;){
				if(ThreadLocalRandom.current().nextBoolean()) {
					// ho bisogno di una risorsa
					if(ThreadLocalRandom.current().nextBoolean()) {
						System.out.println(mioNome + " acquisisco prima risorsa A ");
						acquisizioneA();
					} else {
						System.out.println(mioNome + " acquisisco prima risorsa B ");
						acquisizioneB();
					}
					Thread.sleep(ThreadLocalRandom.current().nextInt(100, 200));
					// vediamo se c'e` bisogno di una seconda risorsa
					if(ThreadLocalRandom.current().nextBoolean()) {
						// ho bisogno della seconda risorsa
						// che deve essere di tipo diverso dalla prima
						if(rA != null) {
							System.out.println(mioNome + " acquisisco risorsa B avendo A");
							acquisizioneB();
						} else {
							System.out.println(mioNome + " acquisisco risorsa A avendo B");
							acquisizioneA();
						}
					}
				}
				// elaborazione
				Thread.sleep(ThreadLocalRandom.current().nextInt(200, 400));
				//rilascio eventuali risorse
				if(rA != null) {
					rilascio(rA);
				}
				if(rB != null) {
					rilascio(rB);
				}
				rA=rB=null;
			}
		} catch(InterruptedException e) { 
			System.err.println("Error" + e);
		}
	}
}
