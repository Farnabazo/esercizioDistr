package esame1Client;

import java.io.*;

public class Main {
	
	public static void main(String[] args) {
		
		try {
			
			for(int i = 0; i < 6; i++) {
				new Client().start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
