package jedi;

import java.util.ArrayList;
import java.util.List;

public class JediKnight {

	public static List<JediKnight> knights;

	static {
		knights = new ArrayList<JediKnight>();
	}
	
	private String name;
	private String side;
	private String saber;
	private int power;
	
	public JediKnight(String name, String side, String saber, int power) {
		this.name = name;
		this.side = side;
		this.saber = saber;
		this.power = power;
		knights.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	public String toSQLvalues() {
		return String.format("('%s', '%s', '%s', %d);", name, side, saber, power);
	}

	@Override
	public String toString() {
		return String.format("%s [%s, %s, %d]", name, side, saber, power);
	}
	
}
