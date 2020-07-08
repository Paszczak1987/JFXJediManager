package jedi;

import java.util.ArrayList;
import java.util.List;

public class JediKnight {

	public static List<JediKnight> knights;
	private static int counter;

	static {
		knights = new ArrayList<JediKnight>();
		counter = 0;
	}
	
	private int id;
	private String name;
	private String side;
	private String saber;
	private int power;
	
	{
		this.id = counter;
		counter++;
	}
	
	public JediKnight(String name, String side, String lightSaber, int power) {
		this.name = name;
		this.side = side;
		this.saber = lightSaber;
		this.power = power;
		knights.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public String toSQLvalues() {
		return String.format("(%d,'%s', '%s', '%s', %d);", id, name, side, saber, power);
	}

	@Override
	public String toString() {
		return String.format("%s %s %s %d", name, side, saber, power);
	}
	
	
}
