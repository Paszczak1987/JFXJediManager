package jedi;

import java.util.ArrayList;
import java.util.List;

public class JediOrder {
	
	public static List<JediOrder> orders;
	private static int counter;
	
	static {
		orders = new ArrayList<JediOrder>();
		counter = 0;
	}
	
	private int id;
	private String name;
	
	{
		this.id = counter;
		counter++;
	}
	
	public JediOrder(String name) {
		this.name = name;
		orders.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public String toSQLvalues() {
		return String.format("('%s');", name);
	}
	
	@Override
	public String toString() {
		return String.format("%s", name);
	}
	
}
