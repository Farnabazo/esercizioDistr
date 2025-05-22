package gioco;

public class Main {
	
	static final int numGiocatori = 10;
	
	public static void main(String[] args) {
		Gioco g = new Gioco(numGiocatori);
		for(int i = 0; i < numGiocatori; i++) {
			new Giocatore(i, g).start();
		}
	}
	
}
