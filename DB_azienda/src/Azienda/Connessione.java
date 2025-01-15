package Azienda;

import java.sql.*;

public class Connessione {

		private static final String url = "jdbc:mysql://localhost/";
		private static final String USER = "root";
		private static final String PW = "Trust1981";
		private static Connection conn = null;

		/*
		 * metodo per stabilire la connessione
		 * 
		 * @param db_name -> nome del database da utilizzare
		 * 
		 * @return conn -> oggetto di tipo Connection (privato nel nostro caso)
		 */
		public static Connection getConnection(String db_name) {
			
			try {
			conn = DriverManager.getConnection(url + db_name, USER, PW);
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return conn;
		}

		public static void closeConnection() throws SQLException {
			conn.close();
		}

	}


