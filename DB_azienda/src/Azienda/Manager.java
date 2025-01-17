package Azienda;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Manager  {

	private static Connection conn = Connessione.getConnection("azienda");

	private static int idManager;
	private static double bonus;
	private static int teamGestito;
	private static int idDipendente;

	
	//metodo per stampare le operazioni CRUD
	public static void menuManager(Scanner scanner) {
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
				int insert = insertManager(scanner);
				if (insert>0) 
					System.out.println("Inserito manager con ID: "+insert);			
				break;
			case 2:
				readAllManager();
				deleteManager(scanner);
				break;
			case 3:
				readAllManager();
				updateManager(scanner);
				break;
			case 4:
				readAllManager();
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
	 * metodo per leggere la tabella manager
	 * 
	 * @param scanner -> per raccogliere i vari input dall'utente 
	 * 
	 * @return int contenente l'id del nuovo record; -1 in caso di errore*/
	public static void readAllManager() {
		String sql = "SELECT * FROM dipendenti\r\n"
				+ "RIGHT JOIN manager ON manager.id_dipendente = dipendenti.id_dipendente";

		System.out.println("Lista manager:");

		try (
				Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery(sql)) {

			while (rs.next()) {
				String nome = rs.getString("nome_dipendente");
				String cognome = rs.getString("cognome_dipendente");
				double stipendioBase = rs.getDouble("stipendio_base");
				idManager = rs.getInt("id_manager");
				bonus = rs.getDouble("bonus");
				teamGestito = rs.getInt("teamGestito");

				System.out.printf("ID Manager: %d | Nome: %s | Cognome: %s | Stipendio base: %.2f | Bonus: %.2f | ID Team Gestito: %d\n", 
						idManager, nome, cognome, stipendioBase, bonus, teamGestito);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * metodo per aggiungere nuovi manager
	 * 
	 * @param scanner ->	per raccogliere i vari input dall'utente (bonus, teamGestito, idDipendente)
	 * 
	 * @return int contenente l'id del nuovo record; -1 in caso di errore
	 */
	public static int insertManager(Scanner scanner) {
		String sql = "INSERT INTO manager(bonus, teamGestito, id_dipendente)\r\n"
				+ "VALUES (?, ?, ?)";
		try (
				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			System.out.println("Inserire l'ammontare del bonus del manager: ");
			bonus=scanner.nextDouble();
			pstmt.setDouble(1, bonus);
			Team.readAllTeam();
			System.out.println("Inserire l'ID del team gestito dal mananger: ");
			teamGestito=scanner.nextInt();
			pstmt.setInt(2, teamGestito);
			Employee.readAllDipendenti();
			System.out.println("Inserire l'ID dipendente per recuperare le informazioni base del manager:");
			idDipendente=scanner.nextInt();
			pstmt.setInt(3, idDipendente);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creazione manager fallita.");
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
	 * @param id -> id del record da eliminare
	 */
	public static void deleteManager(Scanner scanner) {
		String sql = "DELETE FROM manager WHERE id_manager=?";

		try (
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			System.out.println("Inserire l'ID del manager da cancellare: ");
			idManager=scanner.nextInt();
			pstmt.setInt(1, idManager);
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Manager con ID " + idManager + " cancellato correttamente.");
			} else {
				throw new SQLException("Nessun manager eliminato. Ricontrollare ID.");
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
	public static void updateManager(Scanner scanner) {

		// arrayList con i nomi dei campi della tabella dipendente
		List<String> colonneValide = Arrays.asList("bonus", "teamGestito", "id_dipendente");
		
		scanner.nextLine();

		// raccogliamo l'input del campo da modificare
		System.out.println("Quale campo vuoi aggiornare? 1) bonus 2) teamGestito 3) id_dipendente");
		String input = scanner.nextLine();
		

		// if-else per controllare se la stringa contenuta è nell'ArrayList
		if (colonneValide.contains(input)) {

			System.out.println("Inserisci il nuovo valore del manager da aggiornare: ");
			// dichiariamo un oggetto per memorizzare temporaneamente il valore immesso dall'utente
			Object nuovoValore = null;

			if (input.equals("teamGestito")||input.equals("id_dipendente")){
				nuovoValore = scanner.nextInt();
			} else if (input.equals("bonus")) {
				nuovoValore = scanner.nextDouble();
			}
				String sql = "UPDATE manager SET " + input + "= ? WHERE id_manager= ?";

				try (
						PreparedStatement pstmt = conn.prepareStatement(sql)) {

					System.out.println("Inserisci l'ID del manager che vuoi aggiornare: ");
					int ID = scanner.nextInt();
					scanner.nextLine();
					
					pstmt.setInt(2, ID);
					
					if (input.equals("teamGestito")||input.equals("id_dipendente")){
						pstmt.setInt(1, (Integer) nuovoValore);
					} else if (input.equals("bonus")) {
						pstmt.setDouble(1, (double) nuovoValore);
					}
					
					int affectedRows = pstmt.executeUpdate();
					if (affectedRows > 0) {
						System.out.println("Manager con ID " + ID + " aggiornato correttamente.");
					} else {
						System.out.println("Nessun manager aggiornato. Verificare l'ID.");
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			
		} else {
			System.out.println("Nome colonna non presente.");
		}
	}

}
