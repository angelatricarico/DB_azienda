package Azienda;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
				
		String[] Tabelle = { "Employee", "Developer", "Manager", "Linguaggi", "Linguaggi conosciuti", "Team", "Progetti assegnati"};
		
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
				Employee.menuDipendenti(scanner);
				break;
			case 2: 
				Developer.menuDeveloper(scanner);
				break;
			case 3:
				Manager.menuManager(scanner);
				break;
			case 4:
				Linguaggi.menuLinguaggi(scanner);
				break;
			case 5:
				LinguaggiConosciuti.menuLinguaggiConosciuti(scanner);
				break;
			case 6:
				Team.menuTeam(scanner);
				break;
			case 7:
				ProgettiDeveloper.menuProgettiDeveloper(scanner);
				break;
			case 0: 			
				System.out.print("Arrivederci.");
				break;
			default:
				System.out.print("Operazione non esistente.");
			}
		} while (inizioCiclo != 1);
		scanner.close();
		}


	}

