package Model;


/**
 * A jatekos adatait adja meg.
 * @author Szabi
 * @version 1.0
 * @created 19-ápr.-2017 23:11:55
 */
public class Player {

	private Card cardsList;
	private Color color;
	private String name;
	/**
	 * Az elõzõ körben beváltott kártyákból kapott bónusz, kézben tartott egységek
	 * száma
	 */
	private byte reinforcementBonus;
	private int territoryCount;

	public Player(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Hozzáadja a játékos következõ köri erõsítéséhez a beváltott kártyák értékét
	 * 
	 * @param troopCount
	 */
	public addReinforcements(byte troopCount){

	}

	public addTerritory(){

	}

	/**
	 * Ha a játékosnak van 3 beváltható kártyája, ezeket elveszi tõle, majd a
	 * kártyákhoz tartozó erõsítési értékkel tér vissza. Ha a beváltás nem sikeres, 0-
	 * val tér vissza.
	 */
	public byte exchangeCardsIfPossible(){
		return 0;
	}

	public Card getcards(){
		return cards;
	}

	/**
	 * 
	 * @param card
	 */
	public putcard(Card card){

	}

	public removeTerritory(){

	}

}