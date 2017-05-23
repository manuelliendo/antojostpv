package application;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB {

	static Connection conn = null;

	public static Connection Conectar() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager
					.getConnection("jdbc:sqlite:C:\\Users\\Manuel\\workspace\\PruebaDBFX\\prueba.sqlite");
			return conn;
		} catch (Exception e) {
			System.out.println(e);
			// TODO: handle exception
			return null;
		}
	}
}
