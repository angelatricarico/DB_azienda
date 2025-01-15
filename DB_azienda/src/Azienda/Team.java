package Azienda;

import java.sql.*;
import java.util.Scanner;

public class Team {
	private static final String URL = "jdbc:mysql://localhost/azienda";
	private static final String USER = "root";
	private static final String PASSWORD = "Lore!SQL0111";

	private int idTeam;
	private String nome;

	public Team(int idTeam, String nome) {
		this.idTeam = idTeam;
		this.nome = nome;
	}

	public int getIdTeam() {
		return idTeam;
	}

	public void setIdTeam(int idTeam) {
		this.idTeam = idTeam;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "id= " + idTeam + ", nome= " + nome;
	}
	
	
	//metodo per stampare le operazioni
	public static void menuTeam(Scanner scanner) {
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
				insertTeam(scanner);
				break;
			case 2:
				readAllTeam();
				deleteTeam(scanner);
				break;
			case 3:
				readAllTeam();
				updateTeam(scanner);
				break;
			case 4:
				readAllTeam();
				break;
			case 0: 			
				System.out.print("Arrivederci.");
				break;
			default:
				System.out.print("Operazione non esistente.");
			}
		} while (sceltaCiclo != 0);
	}
	

	public static int insertTeam(Scanner scanner) {
		String sql = "INSERT INTO team (nome_team) VALUES (?);";
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			System.out.println("Inserire il nome del team da inserire");
			String nome = scanner.nextLine();
			
			pstmt.setString(1, nome);
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
	
	public static void deleteTeam(Scanner scanner) {
        String sql = "DELETE FROM team WHERE id_team = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	
        	System.out.print("Inserire l'id del team da eliminare: ");
        	int id_team = scanner.nextInt();
        	
            pstmt.setInt(1, id_team);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Team con ID " + id_team + " eliminato correttamente.");
            } else {
                System.out.println("Nessun team eliminato. Verificare l'ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	public static void readAllTeam() {
		String sql = "SELECT * FROM team;";
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id_team = rs.getInt("id_team");
				String nome = rs.getString("nome_team");
				
				System.out.printf(
						"ID Team: %d | Nome: %s%n",
						id_team, nome);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateTeam(Scanner scanner) {
		String sql = "UPDATE team SET nome_team = ? WHERE id_team = ?";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			System.out.println("Inserisci l'ID del team che vuoi aggiornare: ");
			int ID = scanner.nextInt();
			scanner.nextLine();
			pstmt.setInt(2, ID);
			
			System.out.println("Inserisci il nuovo nome del team: ");
			String nuovo_nome = scanner.nextLine();
			scanner.nextLine();
			pstmt.setString(1, nuovo_nome);
			
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Team con ID " + ID + " aggiornato correttamente.");
			} else {
				System.out.println("Nessun team aggiornato. Verificare l'ID.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
