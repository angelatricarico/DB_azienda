package Azienda;

import java.sql.*;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

public class Employee {

	private static Connection conn = Connessione.getConnection("azienda");

	private static int id;
	private static String nome;
	private static String cognome;
	private static double stipendioBase;
	private static int id_team;

		
	//metodo per stampare le operazioni CRUD
	public static void menuDipendenti(Scanner scanner) {
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
				int insert = insertDipendenti(scanner);
				if (insert>0) 
					System.out.println("Inserito dipendenti con ID: "+insert);
				break;
			case 2:
				readAllDipendenti();
				deleteDipendenti(scanner);
				break;
			case 3:
				scanner.nextLine();
				readAllDipendenti();
				updateDipendente(scanner);
				break;
			case 4:
				readAllDipendenti();
				break;
			case 0: 			
				System.out.println("Arrivederci.");
				break;
			default:
				System.out.print("Operazione non esistente.");
			}
		} while (sceltaCiclo != 0);
	}


	/*
	 * metodo per leggere la tabella dipendenti
	 * 
	 * @param scanner -> per raccogliere i vari input dall'utente 
	 * 
	 * @return int contenente l'id del nuovo record; -1 in caso di errore
	 */
	public static void readAllDipendenti() {
		String sql = "SELECT * FROM dipendenti";

		System.out.println("Lista dipendenti:");

		try (
				Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery(sql)) {

			while (rs.next()) {
				 id = rs.getInt("id_dipendente");
				 nome = rs.getString("nome_dipendente");
				 cognome = rs.getString("cognome_dipendente");
				 stipendioBase = rs.getDouble("stipendio_base");
				 id_team = rs.getInt("id_team");

				System.out.printf("ID: %d | Nome: %s | Cognome: %s | Stipendio: %.2f | ID team: %d\n", id, nome, cognome,
						stipendioBase, id_team);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * metodo per aggiungere nuovi dipendenti
	 * 
	 * @param scanner -> per raccogliere i vari input dall'utente 
	 * 
	 * @return int contenente l'id del nuovo record; -1 in caso di errore
	 */
	public static int insertDipendenti(Scanner scanner) {
		String sql = "INSERT INTO dipendenti(nome_dipendente, cognome_dipendente, stipendio_base, id_team)\r\n"
				+ "VALUES (?, ?, ?, ?)";
		try (
				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			System.out.println("Inserire il nome del dipendente:");
			nome=scanner.nextLine();
			pstmt.setString(1, nome);
			
			System.out.println("Inserire il cognome del dipendente:");
			cognome=scanner.nextLine();
			pstmt.setString(2, cognome);
			
			System.out.println("Inserire lo stipendio (base):");
			stipendioBase=scanner.nextDouble();
			pstmt.setDouble(3, stipendioBase);
			
			System.out.println("Inserire l'ID team del dipendente:");
			id_team=scanner.nextInt();
			pstmt.setInt(4, id_team);
		

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creazione dipendente fallita.");
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
	public static void deleteDipendenti(Scanner scanner) {
		String sql = "DELETE FROM dipendenti WHERE id_dipendente=?";

		try (
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			System.out.println("Inserire l'ID del dipendente da cancellare:");
			id=scanner.nextInt();
			pstmt.setInt(1, id);
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Dipendente con ID " + id + " cancellato correttamente.");
			} else {
				throw new SQLException("Nessun dipendente eliminato. Ricontrollare ID.");
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
	public static void updateDipendente(Scanner scanner) {

		// arrayList con i nomi dei campi della tabella dipendente
		List<String> colonneValide = Arrays.asList("nome_dipendente", "cognome_dipendente", "stipendio_base", "id_team");

		// raccogliamo l'input del campo da modificare
		System.out.println("Quale campo vuoi aggiornare? 1) nome_dipendente 2) cognome_dipendente 3) stipendio_base 4) id_team?");
		String input = scanner.nextLine();

		// if-else per controllare se la stringa contenuta è nell'ArrayList
		if (colonneValide.contains(input)) {

			System.out.println("Inserisci il nuovo valore del dipendente da aggiornare: ");
			// dichiariamo un oggetto per memorizzare temporaneamente il valore immesso dall'utente
			Object nuovoValore = null;

			if (input.equals("nome_dipendente") || input.equals("cognome_dipendente")) {
				nuovoValore = scanner.nextLine();
			} else if (input.equals("id_team")){
				nuovoValore = scanner.nextInt();
			} else if (input.equals("stipendio_base")) {
				nuovoValore = scanner.nextDouble();
			}
				String sql = "UPDATE dipendenti SET " + input + "= ? WHERE id_dipendente= ?";

				try (
						PreparedStatement pstmt = conn.prepareStatement(sql)) {

					System.out.println("Inserisci l'ID del dipendente che vuoi aggiornare: ");
					int ID = scanner.nextInt();
					scanner.nextLine();
					
					pstmt.setInt(2, ID);

					if (input.equals("nome_dipendente") || input.equals("cognome_dipendente")) {
						// il valore inserito dall'utente nell'oggetto assume un tipo di variabile
						pstmt.setString(1, (String) nuovoValore);
					}	else if (input.equals("id_team")){
						pstmt.setInt(1, (Integer) nuovoValore);
					} else if ( input.equals("stipendio_base")) {
						pstmt.setDouble(1, (double) nuovoValore);
					}
					int affectedRows = pstmt.executeUpdate();
					if (affectedRows > 0) {
						System.out.println("Dipendente con ID " + ID + " aggiornato correttamente.");
					} else {
						System.out.println("Nessun dipendente aggiornato. Verificare l'ID.");
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			
		} else {
			System.out.println("Nome colonna non presente.");
		}
	}

}