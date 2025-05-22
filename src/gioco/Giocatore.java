package gioco;

import java.util.concurrent.*;

public class Giocatore extends Thread {
	
	Gioco ilGioco;
	int mioId;
	String mioNome;
	
	public Giocatore (int id, Gioco g) {
		ilGioco = g;
		mioId = id;
		mioNome = "giocatore_" + id;
	}

//	private void dormitina() {
//		try {
//			Thread.sleep(ThreadLocalRandom.current().nextInt(100, 200));
//		} catch (InterruptedException e) {	}
//	}

	public void run() {
		for(int i = 0; i < 10; i++){
			while(!ilGioco.possoGiocare(mioId)) {
				ilGioco.attesa();
			}
			ilGioco.sveglia();
			System.out.println(mioNome + ": gioco");
			ilGioco.giocata(mioId);
			while(!ilGioco.esitoDisponibile()) {
				ilGioco.attesa();
			}
			ilGioco.sveglia();
			System.out.println(mioNome+", turno[" + i + "]: leggo");
			ilGioco.letturaEsito(mioId);
		}
	}
	
}
