package Model;

/**
 * Teruletkartyak adatait adja meg.
 * 
 * @author Szabi
 * @version 1.0
 * @created 19-ápr.-2017 23:11:54
 */
public class Card {

	private int id;
	private Territory territory;
	private Unit type;

	public Card(int id, Unit type) {
		this.id = id;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public Territory getTerritory() {
		return territory;
	}

	public Unit getType() {
		return type;
	}

	/*
	 * Return the card value
	 * Converted from the unit enum
	 * 
	 * @return int
	 * @throws Exception
	 */
	public int getValue() throws Exception {
		switch (type) {
		case INFANTRY:
			return 1;
		case CAVALRY:
			return 5;
		case ARTILLERY:
			return 10;
		default:
			throw new Exception("Unknown unit type!");
		}
	}
}