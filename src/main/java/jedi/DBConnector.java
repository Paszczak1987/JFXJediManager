package jedi;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Platform;

public class DBConnector {
	private MainWindowController parent;
	private Connection connection;
	private Statement statement;
	private Statement statement02;
	private final String jediKnightsSQL;
	private final String jediOrdersSQL;
	private final String knightsOrdersSQL;
	private boolean isDBEmpty;
	
	{//instancyjny blok inicjalizacyjny
		jediKnightsSQL = "CREATE TABLE JEDI_KNIGHTS ("
								+"ID_Knight serial,"
								+"Name varchar(30),"
								+"Side varchar(6),"
								+"Saber varchar(30),"
								+"Power int,"
								+"PRIMARY KEY (ID_Knight)"
								+");";
		
		jediOrdersSQL = "CREATE TABLE JEDI_ORDERS ("
								+"ID_Order serial,"
								+"Name varchar(30),"
								+"PRIMARY KEY(ID_Order)"
								+");";
		
		knightsOrdersSQL = "CREATE TABLE KNIGHTS_ORDERS ("
								+"ID_Knight_Order serial,"
								+"Knight_ID int,"
								+"Order_ID int,"
								+"PRIMARY KEY(ID_Knight_Order),"
								+"FOREIGN KEY(Knight_ID) REFERENCES JEDI_KNIGHTS(ID_Knight),"
								+"FOREIGN KEY(Order_ID) REFERENCES JEDI_ORDERS(ID_Order)"
								+");";
	}
	
	public DBConnector(MainWindowController parent, String localhostNr, String dbName, String password) {
		this.parent = parent;
		String url = "jdbc:postgresql://localhost:" + localhostNr + "/" + dbName;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, "postgres", password);
			statement = connection.createStatement();
			statement02 = connection.createStatement();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createTable(String table) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet result = metaData.getTables(null, null, table, null);	
		if(!result.next()) {	//Jeœli baza jest pusta i tabele nie istniej¹
			if(!isDBEmpty)
				isDBEmpty = true;
			if(table.equals("jedi_knights"))
				statement.executeUpdate(jediKnightsSQL);
			else if(table.equals("jedi_orders"))
				statement.executeUpdate(jediOrdersSQL);
			else if(table.equals("knights_orders"))
				statement.executeUpdate(knightsOrdersSQL);
			else
				return;
		}else { 
			/*
			getData(table);
			*/
		}
		
	}
	
	public void getData(String table) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet result = metaData.getTables(null, null, table, null);	
		if(table.equals("jedi_knights")) {
			result = statement.executeQuery("SELECT * FROM jedi_knights");
			while(result.next()) {
				int id = result.getInt("id_knight");
				String name = result.getString("Name");
				String side = result.getString("Side");
				String saber = result.getString("Saber");
				int    power = result.getInt("Power");
				//blokada ¿eby nie powieliæ rycerzy przy imporcie
				boolean whetherThereIs = false;
				for(JediKnight jk: JediKnight.knights) {
					if(jk.getName().equals(name))
						whetherThereIs = true;
				}
				//sprawdzamy czy rycerz jest przypisany do jakiegoœ zakonu
				boolean isAssigned = false;
				ResultSet res = statement02.executeQuery("SELECT * FROM knights_orders");
				while(res.next()) {
					if(id == res.getInt("knight_id"))
						isAssigned = true;
				}
				// w zale¿noœci od tego czy jest przypisany czy nie 
				if(!whetherThereIs) {
					if(isAssigned) {
						JediKnight jk = new JediKnight(name, side, saber, power);
						jk.setAssign(isAssigned);
						parent.addKnightOrOrder(jk);
					}else if(!isAssigned) {
						parent.addKnightOrOrder(new JediKnight(name, side, saber, power));
					}
				}
			}				
		}else if(table.equals("jedi_orders")) {
			result = statement.executeQuery("SELECT * FROM jedi_orders");
			while(result.next()) {
				String name = result.getString("Name");
				boolean whetherThereIs = false;
				for(JediOrder jo: JediOrder.orders) {
					if(jo.getName().equals(name))
						whetherThereIs = true;
				}
				if(!whetherThereIs)
					parent.addKnightOrOrder(new JediOrder(name));
			}
		}else if(table.equals("knights_orders")){
			result = statement.executeQuery("SELECT * FROM knights_orders");			
			while(result.next()) {
				int jkIdFromDB = result.getInt("Knight_ID") - 1;
				int joIdFromDB = result.getInt("Order_ID") - 1;
				for(int jkId = 0; jkId < JediKnight.knights.size(); jkId++) {
					if(jkIdFromDB == jkId) {
						String knightName = JediKnight.knights.get(jkId).getName();
						parent.getJoKnights().remove(knightName);						
					}
				}
				for(int joId = 0; joId < JediOrder.orders.size(); joId++) {
					if(joIdFromDB == joId) {
						String order = parent.getJoOrders().get(joId);
						if(!order.contains(JediKnight.knights.get(jkIdFromDB).getName())) {
							if (!order.contains(" [ "))
								order = order + " [ ";
							order = order.replace(" ]", "; ");
							order = order + JediKnight.knights.get(jkIdFromDB).getName() + " ]";
							parent.getJoOrders().set(joId, order);
						}
					}	
				}
			}
		}
	}
	
	public void insertInto(Object object) {
		String sql = null;
		if(object instanceof JediKnight) {
			sql = "INSERT INTO jedi_knights(Name, Side, Saber, Power) VALUES "+((JediKnight) object).toSQLvalues();		
		}else if(object instanceof JediOrder) {
			sql = "INSERT INTO jedi_orders(Name) VALUES "+((JediOrder) object).toSQLvalues();
		}else
			return;
		
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(isDBEmpty)
			isDBEmpty = false;
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
		statement.executeUpdate("INSERT INTO knights_orders(Knight_ID, Order_ID) VALUES ("+knightId+", "+orderId+");"); 
	}
	
	public boolean isTableExist(String table) throws SQLException {
		ResultSet result = statement.executeQuery("SELECT * FROM "+table);
		if(result.next())
			return true;
		else
			return false;
	}
	
	public boolean isDBEmpty() {
		return isDBEmpty;
	}
	
	
	
}
