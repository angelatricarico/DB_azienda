package Azienda;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Progetti {
	private static final String URL = "jdbc:mysql://localhost/azienda";
	private static final String USER = "root";
	private static final String PASSWORD = "Trust1981";

	private int idProgetto;
	private String nome;

	public Progetti(int idProgetto, String nome) {
		this.idProgetto = idProgetto;
		this.nome = nome;
	}

	public int getIdProgetto() {
		return idProgetto;
	}

	public void setIdProgetto(int idProgetto) {
		this.idProgetto = idProgetto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "id= " + idProgetto + ", nome= " + nome;
	}
	
	//metodo per stampare le operazioni
	public static void menuProgetti(Scanner scanner) {
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
				insertProgetto(scanner);
				break;
			case 2:
				readAllProgetto();
				deleteProgetto(scanner);
				break;
			case 3:
				readAllProgetto();
				updateProgetto(scanner);
				break;
			case 4:
				readAllProgetto();
				break;
			case 0: 			
				System.out.print("Arrivederci.");
				break;
			default:
				System.out.print("Operazione non esistente.");
			}
		} while (sceltaCiclo != 0);
	}
	
	public static int insertProgetto(Scanner scanner) {
		String sql = "INSERT INTO progetti (nome_progetto, id_team) VALUES (?,?);";
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			System.out.println("Inserire il nome del progetto da inserire");
			String nome = scanner.nextLine();
			
			System.out.println("Inserire l'id del team del progetto");
			int id_team = scanner.nextInt();
			
			pstmt.setString(1, nome);
			pstmt.setInt(2, id_team);
			
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creazione team fallita, nessuna riga aggiunta.");
			}

			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getInt(1);
				} else {
					throw new SQLException("Creazione team fallita, ID non recuperato.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static void deleteProgetto(Scanner scanner) {
        String sql = "DELETE FROM progetti WHERE id_progetto = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	
        	System.out.print("Inserire l'id del progetto da eliminare: ");
        	int id_progetto = scanner.nextInt();
        	
            pstmt.setInt(1, id_progetto);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Progetto con ID " + id_progetto + " eliminato correttamente.");
            } else {
                System.out.println("Nessun progetto eliminato. Verificare l'ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static void readAllProgetto() {
		String sql = "SELECT * FROM progetti;";
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id_progetto = rs.getInt("id_progetto");
				String nome = rs.getString("nome_progetto");
				int id_team = rs.getInt("id_team");
				
				System.out.printf(
						"ID Progetto: %d | Nome: %s | ID Team: %d%n",
						id_progetto, nome, id_team);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateProgetto(Scanner scanner) {
		scanner.nextLine(); 
		List<String> colonneValide = Arrays.asList("nome_progetto", "id_team");


        System.out.println("Vuoi aggiornare 1) nome_progetto 2) id_team? ");
        String input = scanner.nextLine();
           	
        if (colonneValide.contains(input)) {
	        
		    System.out.println("Inserisci il nuovo valore del progetto da aggiornare: ");
		    Object nuovoValore = null;
		            
		            
			if (input.equals("nome_progetto")) {
			    nuovoValore = scanner.nextLine();
			} else if (input.equals("id_team")) {
			    nuovoValore= scanner.nextInt();
			    scanner.nextLine(); 
			}
			             
		    String sql = "UPDATE progetti SET " + input + "= ? WHERE id_progetto = ?";
		 
		    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {
		        	
		        System.out.println("Inserisci l'ID del progetto che vuoi aggiornare: ");
		        int ID = scanner.nextInt();
		        scanner.nextLine();
		
		        pstmt.setInt(2, ID);
		            
			    if (input.equals("nome_progetto")) {
			    	pstmt.setString(1, (String) nuovoValore);
			    } else if (input.equals("id_team")) {
			        pstmt.setInt(1, (int) nuovoValore);
			    }                        
		           
		            
		        int affectedRows = pstmt.executeUpdate();
			    if (affectedRows > 0) {
			    	System.out.println("Progetto con ID " + ID + " aggiornato correttamente.");
			    } else {
			        System.out.println("Nessun progetto aggiornato. Verificare l'ID.");
			    }
		            
		    } catch (SQLException e) {
			   	e.printStackTrace();
			}
        } else {
        	System.out.println("Nome colonna non presente");
        }
	}
}
