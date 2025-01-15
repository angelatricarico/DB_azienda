package Azienda;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import java.sql.*;

public class LinguaggiConosciuti {
	

	// dati per login
	private static final String URL = "jdbc:mysql://localhost/azienda";
	private static final String USER = "root";
	private static final String PASSWORD = "Trust1981";
	
	private static int id_linguaggiConosciuti;
	private static int id_linguaggi;
	private static int id_developer;
	
	
	//metodo per stampare le operazioni
	public static void menuLinguaggiConosciuti(Scanner scanner) {
		String[] Operazioni = {"Insert", "Delete", "Update", "Read"};
		
		int sceltaCiclo = 0;
		do {
		System.out.print("Scegli l'operazione da eseguire:\n");
		for (int i = 0; i < Operazioni.length; i++) {
			System.out.println((i + 1) + ". " + Operazioni[i]);
	}
		System.out.println("0. ESCI");
		try {
			sceltaCiclo = scanner.nextInt();
		} catch (Exception e) {
			System.out.print("Carattere non valido. Riprovare.");
	    	   scanner.nextLine();
	    	   continue;
		}
			switch (sceltaCiclo) {
			case 1:
				insertLinguaggiConosciuti(scanner);
				break;
			case 2:
				readAllLinguaggiConosciuti();
				deleteLinguaggiConosciuti(scanner);
				break;
			case 3:
				readAllLinguaggiConosciuti();
				updateLinguaggiConosciuti(scanner);
				break;
			case 4:
				readAllLinguaggiConosciuti();
				break;
			case 0: 			
				System.out.print("Arrivederci.");
				break;
			default:
				System.out.print("Operazione non esistente.");
			}
		} while (sceltaCiclo != 0);
	}

	// metodo per stampare i dati dei developer e i linguaggi conosciuti
		public static void readAllLinguaggiConosciuti() {
			String sql = "SELECT nome_dipendente, cognome_dipendente, nome_linguaggio\r\n"
					+ "FROM dipendenti\r\n"
					+ "INNER JOIN developer ON dipendenti.id_dipendente = developer.id_dipendente\r\n"
					+ "INNER JOIN linguaggi_conosciuti ON developer.id_developer = linguaggi_conosciuti.id_developer\r\n"
					+ "INNER JOIN linguaggi ON linguaggi.id_linguaggio=linguaggi_conosciuti.id_linguaggi";

			System.out.println("Lista DEVELOPER+LINGUAGGI CONOSCIUTI:");

			try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
					Statement s = conn.createStatement();
					ResultSet rs = s.executeQuery(sql)) {

				while (rs.next()) {
					 String nome = rs.getString("nome_dipendente");
					 String cognome = rs.getString("cognome_dipendente");
					 String nome_linguaggio = rs.getString("nome_linguaggio");

					System.out.printf("Nome: %s | Cognome: %s | Linguaggio: %s\n", nome, cognome, nome_linguaggio);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		/*
		 * metodo per aggiungere nuovi dipendenti
		 * 
		 * @param scanner -> per raccogliere i vari input dall'utente (id_linguaggi, id_developer)
		 * 
		 * @return int contenente l'id del nuovo record; -1 in caso di errore
		 */
		public static int insertLinguaggiConosciuti(Scanner scanner) {
			String sql = "INSERT INTO linguaggi_conosciuti VALUES(?, ?)";
			try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

				System.out.println("Inserire l'ID del linguaggio: ");
				id_linguaggi=scanner.nextInt();
				pstmt.setInt(1, id_linguaggi);
				
				System.out.println("Inserire l'ID del developer: ");
				id_developer=scanner.nextInt();
				pstmt.setInt(2, id_developer);
				

				int affectedRows = pstmt.executeUpdate();
				if (affectedRows == 0) {
					throw new SQLException("Creazione campo linguaggi_conosciuti fallita.");
				}
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						return generatedKeys.getInt(1);
					} else {
						throw new SQLException("Nessun ID da ritornare.");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return -1;
		}

		/*
		 * metodo per cancellare un record dalla tabella
		 * 
		 * @param scanner -> per ricevere tramite input l'id dell'utente da eliminare
		 */
		public static void deleteLinguaggiConosciuti(Scanner scanner) {
			String sql = "DELETE FROM linguaggi_conosciuti WHERE id_linguaggiConosciuti=?";

			try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement pstmt = conn.prepareStatement(sql)) {
				
				System.out.println("Inserire l'ID del dipendente da cancellare: ");
				id_linguaggiConosciuti=scanner.nextInt();
				pstmt.setInt(1, id_linguaggiConosciuti);
				int affectedRows = pstmt.executeUpdate();

				if (affectedRows > 0) {
					System.out.println("Linguaggio Conosciuto con ID " + id_linguaggiConosciuti + " cancellato correttamente.");
				} else {
					throw new SQLException("Nessun linguaggio conosciuto eliminato. Ricontrollare ID.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		
		/*
		 * metodo dinamico per aggiornare una cella specifica della tabella
		 * 
		 * @param scanner	->	scanner per gestire i vari input presenti nel metodo
		 */
		public static void updateLinguaggiConosciuti(Scanner scanner) {

			// arrayList con i nomi dei campi della tabella dipendente
			List<String> colonneValide = Arrays.asList("id_linguaggi", "id_developer");

			// raccogliamo l'input del campo da modificare
			System.out.println("Quale campo vuoi aggiornare? 1) id_linguaggi 2) id_developer");
			String input = scanner.nextLine();

			// if-else per controllare se la stringa contenuta è nell'ArrayList
			if (colonneValide.contains(input)) {

				System.out.println("Inserisci il nuovo valore del linguaggio conosciuto da aggiornare: ");
				// dichiariamo un oggetto per memorizzare temporaneamente il valore immesso dall'utente
				Object nuovoValore = null;

				if (input.equals("id_linguaggi") || input.equals("id_developer")) {
					nuovoValore = scanner.nextInt();
				} 
					String sql = "UPDATE linguaggi_conosciuti SET " + input + "= ? WHERE id_linguaggiConosciuti= ?";

					try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
							PreparedStatement pstmt = conn.prepareStatement(sql)) {

						System.out.println("Inserisci l'ID del developer che vuoi aggiornare: ");
						int ID = scanner.nextInt();
						scanner.nextLine();
						
						pstmt.setInt(2, ID);
						
						if (input.equals("id_linguaggi") || input.equals("id_developer")) {
							pstmt.setInt(1, (Integer) nuovoValore);
						}
						
						int affectedRows = pstmt.executeUpdate();
						if (affectedRows > 0) {
							System.out.println("Linguaggio conosciuto con ID " + ID + " aggiornato correttamente.");
						} else {
							System.out.println("Nessun linguaggio conosciuto aggiornato. Verificare l'ID.");
						}

					} catch (SQLException e) {
						e.printStackTrace();
					}
				
			} else {
				System.out.println("Nome colonna non presente.");
			}
		}

}
