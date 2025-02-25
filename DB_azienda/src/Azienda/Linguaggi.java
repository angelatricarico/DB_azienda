package Azienda;

import java.sql.*;
import java.util.Scanner;

public class Linguaggi {

	private static Connection conn = Connessione.getConnection("azienda");

	// campi tabella
	private static int idLinguaggio;
	private static String nomeLinguaggio;

	//metodo per stampare le operazioni CRUD
	public static void menuLinguaggi(Scanner scanner) {
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
				int insert = insertLinguaggi(scanner);
				if (insert>0) 
					System.out.println("Inserito linguaggio con ID: "+insert);
				break;
			case 2:
				readAllLinguaggi();
				deleteLinguaggi(scanner);
				break;
			case 3:
				readAllLinguaggi();
				updateLinguaggi(scanner);
				break;
			case 4:
				readAllLinguaggi();
				break;
			case 0: 			
				System.out.println("Arrivederci.");
				break;
			default:
				System.out.println("Operazione non esistente.");
			}
		} while (sceltaCiclo != 0);
	}
	
	
	/*
	 * metodo per leggere la tabella linguaggi
	 * 
	 * @param scanner -> per raccogliere i vari input dall'utente 
	 * 
	 * @return int contenente l'id del nuovo record; -1 in caso di errore
	 */
	public static void readAllLinguaggi() {
		String sql = "SELECT * FROM linguaggi";

		System.out.println("Lista linguaggi:");

		try (
				Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery(sql)) {

			while (rs.next()) {
				idLinguaggio = rs.getInt("id_linguaggio");
				nomeLinguaggio = rs.getString("nome_linguaggio");

				System.out.printf("ID: %d | Nome linguaggio: %s\n", idLinguaggio, nomeLinguaggio);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	

	/*
	 * metodo per aggiungere nuovi linguaggio
	 * 
	 * @param scanner ->	per raccogliere dall'utente il valore da inserire nella tabella
	 * 
	 * @return int contenente l'id del nuovo record; -1 in caso di errore
	 */
	public static int insertLinguaggi(Scanner scanner) {
		String sql = "INSERT INTO linguaggi (nome_linguaggio) VALUES(?)";
		try (
				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			System.out.println("Inserisci il nome del linguaggio: ");
			nomeLinguaggio = scanner.nextLine();
			pstmt.setString(1, nomeLinguaggio);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creazione linguaggio fallita.");
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
	 * @param scanner	->	per lasciare all'utente la scelta dell'id da cancellare
	 */
	public static void deleteLinguaggi(Scanner scanner) {
		String sql = "DELETE FROM linguaggi WHERE id_linguaggio=?";

		try (
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			System.out.println("Inserisci l'ID del linguaggio che vuoi eliminare: ");
			idLinguaggio = scanner.nextInt();
			pstmt.setInt(1, idLinguaggio);
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Linguaggio con ID " + idLinguaggio + " cancellato correttamente.");
			} else {
				throw new SQLException("Nessun linguaggio eliminato. Ricontrollare ID.");
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
	public static void updateLinguaggi(Scanner scanner) {

		String sql = "UPDATE linguaggi SET nome_linguaggio= ? WHERE id_linguaggio= ?";

		try (
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			System.out.println("Inserisci l'ID del linguaggio che vuoi aggiornare: ");
			idLinguaggio = scanner.nextInt();
			pstmt.setInt(2, idLinguaggio);
			
			scanner.nextLine();
			
			System.out.println("Inserisci il nuovo valore: ");
			nomeLinguaggio = scanner.nextLine();
			pstmt.setString(1, nomeLinguaggio);

			
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Linguaggio con ID " + idLinguaggio + " aggiornato correttamente.");
			} else {
				System.out.println("Nessun linguaggio aggiornato. Verificare l'ID.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
