package Azienda;

import java.sql.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ProgettiDeveloper {
	
	private static Connection conn = Connessione.getConnection("azienda");

	private static int id_progetti_developer;
	private static int id_progetti;
	private static int id_developer;

	/*
	 * metodo per leggere la tabella progetti assegnati
	 * 
	 * @param scanner -> per raccogliere i vari input dall'utente 
	 * 
	 * @return int contenente l'id del nuovo record; -1 in caso di errore
	 */	
	public static void readAllProgettiDeveloper() {
		String sql = "SELECT progetti_developer.id_progetti_developer, dipendenti.nome_dipendente, dipendenti.cognome_dipendente, progetti.nome_progetto\r\n"
				+ "FROM dipendenti INNER JOIN developer ON dipendenti.id_dipendente = developer.id_dipendente\r\n"
				+ "INNER JOIN progetti_developer ON developer.id_developer = progetti_developer.id_developer\r\n"
				+ "INNER JOIN progetti ON progetti_developer.id_progetti=progetti.id_progetto;";

		System.out.println("Lista PROGETTI + DEVELOPER:");

		try (
				Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery(sql)) {

			while (rs.next()) {
				int id_progetti_developer = rs.getInt("id_progetti_developer");
				String nome = rs.getString("nome_dipendente");
				String cognome = rs.getString("cognome_dipendente");
				String nome_progetto = rs.getString("nome_progetto");
				

				System.out.printf("ID progetto developer: %d | Nome: %s | Cognome: %s | Linguaggio: %s\n", id_progetti_developer, nome, cognome, nome_progetto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * metodo per assegnare nuovi progetti ai developer
	 * 
	 * @param scanner -> per raccogliere i vari input dall'utente (id_progetti,
	 * id_developer)
	 * 
	 * @return int contenente l'id del nuovo record; -1 in caso di errore
	 */
	public static int insertProgettiDeveloper(Scanner scanner) {
		String sql = "INSERT INTO progetti_developer(id_progetti, id_developer)\r\n"
				+ "VALUES (?, ?)";
		try (
				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			System.out.println("Inserire l'ID del progetto: ");
			id_progetti = scanner.nextInt();
			pstmt.setInt(1, id_progetti);

			Developer.readAllDeveloper();
			System.out.println("Inserire l'ID del developer: ");
			id_developer = scanner.nextInt();
			pstmt.setInt(2, id_developer);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creazione campo progetti_developer fallita.");
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
	public static void deleteProgettiDeveloper(Scanner scanner) {
		String sql = "DELETE FROM progetti_developer\r\n"
				+ "WHERE id_progetti_developer=?";

		try (
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			System.out.println("Inserire l'ID del progetto assegnato da cancellare:");
			id_progetti_developer = scanner.nextInt();
			pstmt.setInt(1, id_progetti_developer);
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Progetto assegnato con ID " + id_progetti_developer + " cancellato correttamente.");
			} else {
				throw new SQLException("Nessun progetto assegnato eliminato. Ricontrollare ID.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * metodo dinamico per aggiornare una cella specifica della tabella
	 * 
	 * @param scanner -> scanner per gestire i vari input presenti nel metodo
	 */
	public static void updateProgettiDeveloper(Scanner scanner) {
		scanner.nextLine();
		
		// arrayList con i nomi dei campi della tabella dipendente
		List<String> colonneValide = Arrays.asList("id_progetti", "id_developer");

		// raccogliamo l'input del campo da modificare
		System.out.println("Quale campo vuoi aggiornare? 1) id_progetti 2) id_developer");
		String input = scanner.nextLine();

		// if-else per controllare se la stringa contenuta è nell'ArrayList
		if (colonneValide.contains(input)) {
			
			Progetti.readAllProgetto();

			System.out.println("Inserisci il nuovo valore da aggiornare: ");
			// dichiariamo un oggetto per memorizzare temporaneamente il valore immesso
			// dall'utente
			Object nuovoValore = null;

			if (input.equals("id_progetti") || input.equals("id_developer")) {
				nuovoValore = scanner.nextInt();
			}
			String sql = "UPDATE progetti_developer SET " + input + "= ? WHERE id_progetti_developer= ?";

			try (
					PreparedStatement pstmt = conn.prepareStatement(sql)) {

				System.out.println("Inserisci l'ID del progetto assegnato che vuoi aggiornare: ");
				int ID = scanner.nextInt();
				scanner.nextLine();

				pstmt.setInt(2, ID);

				if (input.equals("id_progetti") || input.equals("id_developer")) {
					pstmt.setInt(1, (Integer) nuovoValore);
				}

				int affectedRows = pstmt.executeUpdate();
				if (affectedRows > 0) {
					System.out.println("Progetto assegnato con ID " + ID + " aggiornato correttamente.");
				} else {
					System.out.println("Nessun progetto assegnato aggiornato. Verificare l'ID.");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("Nome colonna non presente.");
		}
	}
	
	//metodo per stampare le operazioni CRUD
	public static void menuProgettiDeveloper(Scanner scanner) {
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
				scanner.nextLine();
				Progetti.readAllProgetto();
				int insert = insertProgettiDeveloper(scanner);
				if (insert>0) 
					System.out.println("Inserito record \"progetti_developer\" con ID: "+insert);			
				break;
			case 2:
				readAllProgettiDeveloper();
				deleteProgettiDeveloper(scanner);
				break;
			case 3:
				readAllProgettiDeveloper();
				updateProgettiDeveloper(scanner);
				break;
			case 4:
				readAllProgettiDeveloper();
				break;
			case 0: 			
				System.out.println("Arrivederci.");
				break;
			default:
				System.out.print("Operazione non esistente.");
			}
		} while (sceltaCiclo != 0);
	}
	
}
