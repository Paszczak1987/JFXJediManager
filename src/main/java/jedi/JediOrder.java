package jedi;

import java.util.ArrayList;
import java.util.List;

public class JediOrder {
	
	public static List<JediOrder> orders;

	static {
		orders = new ArrayList<JediOrder>();
	}
	
	private String name;
	
	public JediOrder(String name) {
		this.name = name;
		orders.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	public String toSQLvalues() {
		return String.format("('%s');", name);
	}
	
	@Override
	public String toString() {
		return String.format("%s", name);
	}
	
}
