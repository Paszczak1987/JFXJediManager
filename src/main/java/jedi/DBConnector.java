package jedi;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
	
	private Connection connection;
	private Statement statement;
	private final String createTableJediKnights;

	
	{//instancyjny blok inicjalizacyjny
		createTableJediKnights = "CREATE TABLE JEDI_KNIGHTS ("
								+"ID_Knight serial,"
								+"Name varchar(30),"
								+"Side varchar(6),"
								+"Saber varchar(30),"
								+"Power int,"
								+"PRIMARY KEY (ID_Knight)"
								+");";
	}
	
	public DBConnector(String localhostNr, String dbName) {
		String url = "jdbc:postgresql://localhost:" + localhostNr + "/" + dbName;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, "postgres", "Delfin8765");
			statement = connection.createStatement();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void createTableJediKnights() throws SQLException  {
		
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet result = metaData.getTables(null, null, "jedi_knights", null);
		
		if(!result.next()) {
			statement.executeUpdate(createTableJediKnights);
			System.out.println("utworzono");
		}else
			System.out.println("istanieje");

	}
	
	public void pushJedi(JediKnight jedi) {
		//String sql = "INSERT INTO jedi_knights(Name, Side, Saber, Power) VALUES "+jedi.toSQLvalues();
		String sql = "INSERT INTO jedi_knights VALUES "+jedi.toSQLvalues();
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
