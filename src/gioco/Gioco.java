package gioco;

public class Gioco {
	
	private boolean reset = false;
	private int numGiocatori = 0;
	FasiGioco faseCorrente;  // la fase corrente: gioco o lettura risultati
	boolean[] hannoFatto;    // tiene conto di quanti giocatori hanno giocato (se siamo nella fase di gioco)
	                         // o hanno letto il risultato (se siamo nella fase di lettura dei risultati)	
	private void reset() {
		for(int i = 0; i < numGiocatori; i++) {
			hannoFatto[i] = false; 
		}
	}
	
	private boolean hannoFattoTutti() {
		int count = 0;
		for(int i = 0; i < numGiocatori; i++) {
			if(hannoFatto[i]) {
				count++;
			}
		}
		return count == numGiocatori;
	}
	
	public Gioco(int ng) {
		numGiocatori = ng;
		hannoFatto = new boolean[ng];
		reset();
		faseCorrente = FasiGioco.Gioco;
	}
	
	public synchronized boolean possoGiocare(int mioId) {
		return faseCorrente == FasiGioco.Gioco;
	}
	
	public synchronized void giocata(int idGiocatore) {
		hannoFatto[idGiocatore] = true;
		if(hannoFattoTutti()) {
			notifyAll();
			reset();
			faseCorrente = FasiGioco.LetturaRisultati;
		}
	}
	
	public synchronized void attesa() {
		try {
			wait();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void sveglia() {
		notifyAll();
	}
	
	public synchronized boolean esitoDisponibile() {
		return faseCorrente == FasiGioco.LetturaRisultati;
	}
	
	public synchronized void letturaEsito(int idGiocatore) {
		hannoFatto[idGiocatore] = true;
		if(hannoFattoTutti()) {
			reset();
			faseCorrente = FasiGioco.Gioco;
		}
	}
	
}
