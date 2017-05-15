package jdbcdemo;

import java.sql.*;
public class Driver {

	public static void main(String[] args) {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection myconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo","manuel","mysql1234");
			Statement mystmt = myconn.createStatement();
			ResultSet myRs = mystmt.executeQuery("select * from restaurante");
			while(myRs.next()){
				System.out.print(myRs.getString("nombre")+"\n");
			}
		}catch(Exception exc){
			exc.printStackTrace();
		};
		
	}
}