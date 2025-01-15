package Azienda;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Manager extends Employee {

	// dati per login
	private static final String URL = "jdbc:mysql://localhost/azienda";
	private static final String USER = "root";
	private static final String PASSWORD = "sl._N2aScXw3i";

	private static int idManager;
	private static double bonus;
	private static int teamGestito;
	private static int idDipendente;

	public Manager(int id, String nome, String cognome, double stipendioBase, int id_team, int idManager, double bonus, int teamGestito, int id_dipendente) {
		super(id, nome, cognome, stipendioBase, id_team);
		this.idManager = idManager;
		this.bonus = bonus;
		this.teamGestito = teamGestito;
		this.idDipendente = idDipendente;
	}

	public double getBonus() {
		return bonus;
	}

	public int getTeamGestito() {
		return teamGestito;
	}

	public void setBonus(double bonus) {
		this.bonus = bonus;
	}

	public void setTeamGestito(int teamGestito) {
		this.teamGestito = teamGestito;
	}

	/*
	 * @Override public String toString () { super.toString(); return ", Bonus= " +
	 * bonus + ", Team gestito: " + teamGestito; }
	 */
	
	//metodo per stampare le operazioni
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
				insertManager(scanner);
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
				System.out.print("Arrivederci.");
				break;
			default:
				System.out.print("Operazione non esistente.");
			}
		} while (sceltaCiclo != 0);
	}

	// metodo per stampare la lista dei manager
	public static void readAllManager() {
		String sql = "SELECT * FROM manager";

		System.out.println("Lista manager:");

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery(sql)) {

			while (rs.next()) {
				idManager = rs.getInt("id_manager");
				bonus = rs.getDouble("bonus");
				teamGestito = rs.getInt("teamGestito");
				idDipendente = rs.getInt("id_dipendente");

				System.out.printf("ID Manager: %d | Bonus: %.2f | ID Team Gestito: %d | ID Dipendente: %d", idManager, bonus,
						teamGestito, idDipendente);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * metodo per aggiungere nuovi dipendenti
	 * 
	 * @param scanner ->	per raccogliere i vari input dall'utente (bonus, teamGestito, idDipendente)
	 * 
	 * @return int contenente l'id del nuovo record; -1 in caso di errore
	 */
	public static int insertManager(Scanner scanner) {
		String sql = "INSERT INTO manager VALUES(?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			System.out.println("Inserire l'ammontare del bonus del manager:");
			bonus=scanner.nextDouble();
			pstmt.setDouble(1, bonus);
			System.out.println("Inserire l'ID del team gestito dal mananger:");
			teamGestito=scanner.nextInt();
			pstmt.setInt(2, teamGestito);
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

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			System.out.println("Inserire l'ID del manager da cancellare:");
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

		// raccogliamo l'input del campo da modificare
		System.out.println("Quale campo vuoi aggiornare? 1) bonus 2) teamGestito 3) id_dipendente");
		String input = scanner.nextLine();

		// if-else per controllare se la stringa contenuta è nell'ArrayList
		if (colonneValide.contains(input)) {

			System.out.println("Inserisci il nuovo valore del dipendente da aggiornare: ");
			// dichiariamo un oggetto per memorizzare temporaneamente il valore immesso dall'utente
			Object nuovoValore = null;

			if (input.equals("teamGestito")||input.equals("id_dipendente")){
				nuovoValore = scanner.nextInt();
			} else if (input.equals("bonus")) {
				nuovoValore = scanner.nextDouble();
			}
				String sql = "UPDATE dipendenti SET " + input + "= ? WHERE id_dipendente= ?";

				try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
						PreparedStatement pstmt = conn.prepareStatement(sql)) {

					System.out.println("Inserisci l'ID del dipendente che vuoi aggiornare: ");
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
	
	
	public static void readNomeManager() {
		String sql = "SELECT nome_dipendente, stipendio_base, bonus FROM manager INNER JOIN dipendenti ON manager.id_dipendente=dipendenti.id_dipendente";

		System.out.println("Lista manager:");

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery(sql)) {

			while (rs.next()) {

				String nome = rs.getString("nome_dipendente");
				double stipendio = rs.getDouble("stipendio_base");
				bonus = rs.getDouble("bonus");
				

				System.out.printf("Nome: %s | Stipendio: %.2f | Bonus: %.2f", nome, stipendio,
						bonus);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
