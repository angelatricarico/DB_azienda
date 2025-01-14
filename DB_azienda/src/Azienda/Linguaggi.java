package Azienda;

import java.sql.*;
import java.util.Scanner;

public class Linguaggi {

	// dati per login
	private static final String URL = "jdbc:mysql://localhost/azienda";
	private static final String USER = "root";
	private static final String PASSWORD = "sl._N2aScXw3i";

	// campi tabella
	private static int idLinguaggio;
	private static String nomeLinguaggio;

	// metodo per stampare la lista dei linguaggi
	public static void readAllLinguaggi() {
		String sql = "SELECT * FROM linguaggi";

		System.out.println("Lista linguaggi:");

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
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
		String sql = "INSERT INTO linguaggi VALUES(?)";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
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

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			System.out.println("Inserisci l'ID del linguaggio che vuoi eliminare: ");
			idLinguaggio = scanner.nextInt();
			pstmt.setInt(1, idLinguaggio);
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Dipendente con ID " + idLinguaggio + " cancellato correttamente.");
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
	 * @param scanner -> scanner per gestire i vari input presenti nel metodo
	 */
	public static void updateLinguaggi(Scanner scanner) {

		String sql = "UPDATE dipendenti SET nome_linguaggio= ? WHERE id_dipendente= ?";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			System.out.println("Inserisci l'ID del dipendente che vuoi aggiornare: ");
			idLinguaggio = scanner.nextInt();
			pstmt.setInt(2, idLinguaggio);
			
			scanner.nextLine();
			
			System.out.println("Inserisci il nuovo valore: ");
			nomeLinguaggio = scanner.nextLine();
			scanner.nextLine();
			pstmt.setString(1, nomeLinguaggio);

			
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Dipendente con ID " + idLinguaggio + " aggiornato correttamente.");
			} else {
				System.out.println("Nessun dipendente aggiornato. Verificare l'ID.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
