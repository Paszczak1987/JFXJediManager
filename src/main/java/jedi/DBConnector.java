package jedi;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
	private MainWindowController parent;
	private Connection connection;
	private Statement statement;
	private final String JediKnights;
	private final String JediOrders;
	private final String knightsOrders;
	
	{//instancyjny blok inicjalizacyjny
		JediKnights = "CREATE TABLE JEDI_KNIGHTS ("
								+"ID_Knight serial,"
								+"Name varchar(30),"
								+"Side varchar(6),"
								+"Saber varchar(30),"
								+"Power int,"
								+"PRIMARY KEY (ID_Knight)"
								+");";
		
		JediOrders = "CREATE TABLE JEDI_ORDERS ("
								+"ID_Order serial,"
								+"Name varchar(30),"
								+"PRIMARY KEY(ID_Order)"
								+");";
		
		knightsOrders = "CREATE TABLE KNIGHTS_ORDERS ("
								+"ID_Knight_Order serial,"
								+"Knight_ID int,"
								+"Order_ID int,"
								+"PRIMARY KEY(ID_Knight_Order),"
								+"FOREIGN KEY(Knight_ID) REFERENCES JEDI_KNIGHTS(ID_Knight),"
								+"FOREIGN KEY(Order_ID) REFERENCES JEDI_ORDERS(ID_Order)"
								+");";
	}
	
	public DBConnector(MainWindowController parent, String localhostNr, String dbName) {
		this.parent = parent;
		String url = "jdbc:postgresql://localhost:" + localhostNr + "/" + dbName;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, "postgres", "Delfin8765");
			statement = connection.createStatement();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createTable(String table) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet result = metaData.getTables(null, null, table, null);	
		if(!result.next()) {
			if(table.equals("jedi_knights"))
				statement.executeUpdate(JediKnights);
			else if(table.equals("jedi_orders"))
				statement.executeUpdate(JediOrders);
			else if(table.equals("knights_orders"))
				statement.executeUpdate(knightsOrders);
			else
				return;
			System.out.println(table+" UTWORZONO");
		}else {
			System.out.println(table+" ISTNIEJE");
			getData(table, result);
		}
		
	}
	
	private void getData(String table, ResultSet result) throws SQLException {
		if(table.equals("jedi_knights")) {
			result = statement.executeQuery("SELECT * FROM jedi_knights");
			while(result.next()) {
				String name = result.getString("Name");
				String side = result.getString("Side");
				String saber = result.getString("Saber");
				int power = result.getInt("Power");
				System.out.printf("%s %s %s %d\n", name, side, saber, power);
				parent.addKnightOrOrder(new JediKnight(name, side, saber, power));
			}
				
		}else if(table.equals("jedi_orders")) {
			result = statement.executeQuery("SELECT * FROM jedi_orders");
			while(result.next()) {
				String name = result.getString("Name");
				System.out.printf("%s\n", name);
				parent.addKnightOrOrder(new JediOrder(name));
			}
			
		}else if(table.equals("knights_orders")){
			result = statement.executeQuery("SELECT * FROM knights_orders");
			while(result.next()) {
				/*
				 * dokoñczyæ kasowanie ryce¿y z joKnightsViw
				 */
			}
		}else
			return;
			
	}
	
	public void insertInto(Object object) {
		String sql = null;
		if(object instanceof JediKnight) {
			sql = "INSERT INTO jedi_knights(Name, Side, Saber, Power) VALUES "+((JediKnight) object).toSQLvalues();		
		}else if(object instanceof JediOrder) {
			sql = "INSERT INTO jedi_orders(Name) VALUES "+((JediOrder) object).toSQLvalues();
		}else
			return;
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertInto(String order, String knight) throws SQLException {
		int knightId = -1;
		int orderId = -1;
		ResultSet result = statement.executeQuery("SELECT * FROM jedi_knights");
		while(result.next()) {
			if(result.getString("Name").equals(knight))
				knightId = result.getInt("ID_Knight");
		}
		result = statement.executeQuery("SELECT * FROM jedi_orders");
		while(result.next()) {
			if(result.getString("Name").equals(order))
				orderId = result.getInt("ID_Order");
		}
		String sql = "INSERT INTO knights_orders(Knight_ID, Order_ID) VALUES ("+knightId+", "+orderId+");";
		statement.executeUpdate(sql); 
	}
	
}
