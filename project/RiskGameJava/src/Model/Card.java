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
	private int value;

	public Card(Territory territory, Unit type, int value) {
		this.territory = territory;
		this.type = type;
		this.value = value;
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

	public int getValue() {
		return value;
	}
}