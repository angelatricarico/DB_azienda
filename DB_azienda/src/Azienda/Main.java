package Azienda;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		menuGenerale(scanner);
		
		scanner.close();
		
		System.exit(0);
		}
	
	public static void menuGenerale (Scanner scanner) {
		String[] Tabelle = { "Developer", "Employee", "Linguaggi", "Linguaggi conosciuti", "Manager", "Progetti assegnati", "Progetti", "Team", };
		
		int inizioCiclo = 1;
		do {
		System.out.println("\tBENVENUTO in Verdivento, l'azienda ecosostenibile che pensa in verde! 🌱" + "\n" + 
				"Scegli di eseguire un'operazione su una delle seguenti categorie: ");
				for (int i = 0; i < Tabelle.length; i++) {
					System.out.println((i + 1) + ". " + Tabelle[i]);
				}
				System.out.println("0. ESCI");
		try {
			inizioCiclo = scanner.nextInt();
		} catch (Exception e) {
			System.out.println("Carattere non valido. Riprovare.");
	    	   scanner.nextLine();
	    	   continue;
		}
			switch (inizioCiclo) {
			case 1:
				Developer.menuDeveloper(scanner);
				break;
			case 2: 				
				Employee.menuDipendenti(scanner);
				break;
			case 3:
				Linguaggi.menuLinguaggi(scanner);
				break;
			case 4:
				LinguaggiConosciuti.menuLinguaggiConosciuti(scanner);
				break;
			case 5:
				Manager.menuManager(scanner);
				break;		
			case 6:				
				Progetti.menuProgetti(scanner);
				break;
			case 7:
				ProgettiDeveloper.menuProgettiDeveloper(scanner);
				break;
			case 8:
				Team.menuTeam(scanner);
				break;
			case 0: 			
				System.out.println("Arrivederci.");
				break;
			default:
				System.out.println("Operazione non esistente.");
			}
		} while (inizioCiclo != 0);


	}
}

