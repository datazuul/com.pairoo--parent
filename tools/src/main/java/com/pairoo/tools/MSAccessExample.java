package com.pairoo.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;


/**
 * @author Tom
 *
 */
public class MSAccessExample {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

		Connection con = DriverManager
				.getConnection("jdbc:odbc:DRIVER={Microsoft Access Driver (*.mdb)};DBQ=/home/ralf/Desktop/TMP/2010-2\\ UNLOCODE\\ CodeList.mdb");

		Statement stmt = con.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT * FROM SubdivisionCodes");
		ResultSetMetaData rsmd = rs.getMetaData();
		int clmCnt = rsmd.getColumnCount();

		while (rs.next()) {
			for (int i = 1; i <= clmCnt; i++) {
				System.out.print(rs.getString(i));
				System.out.print(" ");
			}
			System.out.println();
		}
		rs.close();
		stmt.close();
		con.close();
	}
}