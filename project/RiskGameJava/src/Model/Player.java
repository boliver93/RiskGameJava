package Model;


/**
 * A jatekos adatait adja meg.
 * @author Szabi
 * @version 1.0
 * @created 19-�pr.-2017 23:11:55
 */
public class Player {

	private Card cardsList;
	private Color color;
	private String name;
	/**
	 * Az el�z� k�rben bev�ltott k�rty�kb�l kapott b�nusz, k�zben tartott egys�gek
	 * sz�ma
	 */
	private byte reinforcementBonus;
	private int territoryCount;

	public Player(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Hozz�adja a j�t�kos k�vetkez� k�ri er�s�t�s�hez a bev�ltott k�rty�k �rt�k�t
	 * 
	 * @param troopCount
	 */
	public addReinforcements(byte troopCount){

	}

	public addTerritory(){

	}

	/**
	 * Ha a j�t�kosnak van 3 bev�lthat� k�rty�ja, ezeket elveszi t�le, majd a
	 * k�rty�khoz tartoz� er�s�t�si �rt�kkel t�r vissza. Ha a bev�lt�s nem sikeres, 0-
	 * val t�r vissza.
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