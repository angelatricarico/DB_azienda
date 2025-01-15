package Azienda;

import java.sql.*;
import java.util.Scanner;

public class Developer  {
	
	private static Connection conn = Connessione.getConnection("azienda");

	
	//metodo per stampare le operazioni CRUD
	public static void menuDeveloper(Scanner scanner) {
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
				scanner.nextLine();
				scanner.nextLine();
				int insert = insertDeveloper(scanner);
				if (insert>0) 
					System.out.println("Inserito Developer con ID: "+insert);	
				break;
			case 2:
				readAllDeveloper();
				deleteDeveloper(scanner);
				break;
			case 3:
				scanner.nextLine();
				readAllDeveloper();
				updateDeveloper(scanner);
				break;
			case 4:
				readAllDeveloper();
				break;
			case 0: 			
				System.out.print("Arrivederci.");
				break;
			default:
				System.out.print("Operazione non esistente.");
			}
		} while (sceltaCiclo != 0);
	}
	
	/*
	 * metodo per aggiungere nuovi developer
	 * 
	 * @param scanner -> per raccogliere i vari input dall'utente 
	 * 
	 * @return int contenente l'id del nuovo record; -1 in caso di errore
	 */
	public static int insertDeveloper(Scanner scanner) {
		String sql = "INSERT INTO developer(id_dipendente)\r\n"
				+ "VALUES (?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			System.out.println("Inserire l'ID del dipendente: ");
			int id_dipendente = scanner.nextInt();
			
			pstmt.setInt(1, id_dipendente);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creazione developer fallita, nessuna riga aggiunta.");
			} 

			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getInt(1);
				} else {
					throw new SQLException("Creazione developer fallita, ID non recuperato.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/*
	 * metodo per leggere la tabella developer
	 * 
	 * @param scanner -> per raccogliere i vari input dall'utente 
	 * 
	 * @return int contenente l'id del nuovo record; -1 in caso di errore
	 */
	public static void readAllDeveloper() {
		String sql = "SELECT * FROM dipendenti\r\n"
				+ "RIGHT JOIN developer ON developer.id_dipendente = dipendenti.id_dipendente";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id_dipendente = rs.getInt("id_dipendente");
				String nome = rs.getString("nome_dipendente");
				String cognome = rs.getString("cognome_dipendente");
				double stipendioBase = rs.getDouble("stipendio_base");
				int id_developer = rs.getInt("id_developer");

				System.out.printf(
						"ID Dipendente: %d | Nome: %s | Cognome: %s | Stipendio base: %.2f | ID Developer: %d%n",
						id_dipendente, nome, cognome, stipendioBase, id_developer);
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
	public static void updateDeveloper(Scanner scanner) {
		String sql = "UPDATE developer\r\n"
				+ "SET id_dipendente=? WHERE id_developer=?";

		try (
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			System.out.println("Inserisci l'ID del developer che vuoi aggiornare: ");
			int ID = scanner.nextInt();
			scanner.nextLine();
			pstmt.setInt(2, ID);
			
			System.out.println("Inserisci il nuovo valore dipendente: ");
			int ID2 = scanner.nextInt();
			scanner.nextLine();
			pstmt.setInt(1, ID2);
			
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Developer con ID " + ID + " aggiornato correttamente.");
			} else {
				System.out.println("Nessun developer aggiornato. Verificare l'ID.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * metodo per cancellare un record dalla tabella
	 * 
	 * @param scanner -> per ricevere tramite input l'id dell'utente da eliminare
	 */
	public static void deleteDeveloper(Scanner scanner) {
        String sql = "DELETE FROM developer WHERE id_developer = ?";
        try (
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	
        	System.out.print("Inserire l'id del developer da eliminare: ");
        	int id_developer = scanner.nextInt();
        	
            pstmt.setInt(1, id_developer);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Developer con ID " + id_developer + " eliminato correttamente.");
            } else {
                System.out.println("Nessun developer eliminato. Verificare l'ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
