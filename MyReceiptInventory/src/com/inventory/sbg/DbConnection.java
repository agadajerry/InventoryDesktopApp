package com.inventory.sbg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

	public static Connection getConnection()
	 {
		 Connection conn = null;
		 try
		 {

			 String dbURL = "jdbc:sqlite:C:\\BusinessTransaction\\BusinessInventoryAndReceipt.db";
			 
			
			
			 conn = DriverManager.getConnection(dbURL);
			
			 
		 }catch(SQLException ex)
		 {
			 System.out.println(ex.getMessage());
		 }
		 return conn;
	 }
	
}

