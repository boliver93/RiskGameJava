package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A jatekos adatait adja meg.
 * 
 * @author Szabi
 * @version 1.0
 * @created 19-�pr.-2017 23:11:55
 */
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Card> cardsList;
	private Color color;
	private String name;
	private Deck deck;
	/**
	 * Az el�z� k�rben bev�ltott k�rty�kb�l kapott b�nusz, k�zben tartott
	 * egys�gek sz�ma
	 */
	private int reinforcementBonus;
	private int territoryCount;
	
	private int unitsLeftToReinforce;

	public Player(Color color, String name, Deck deck) {
		super();
		this.cardsList = new ArrayList<Card>();
		this.color = color;
		this.name = name;
		this.reinforcementBonus = 0;
		this.territoryCount = 0;
		this.deck = deck;
	}

	/**
	 * Hozz�adja a j�t�kos k�vetkez� k�ri er�s�t�s�hez a bev�ltott k�rty�k
	 * �rt�k�t
	 * 
	 * @param troopCount
	 */
	public void addReinforcements(int troopCount) {
		reinforcementBonus += troopCount;
	}

	public int getReinforcementBonus() {
		return reinforcementBonus;
	}

	public void addTerritory() {
		territoryCount++;
	}
	
	public int getTerritoryCount() {
		return territoryCount;
	}

	public Color getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Ha a j�t�kosnak van 3 bev�lthat� k�rty�ja, ezeket elveszi t�le, majd a
	 * k�rty�khoz tartoz� er�s�t�si �rt�kkel t�r vissza. Ha a bev�lt�s nem
	 * sikeres, 0- val t�r vissza.
	 * 
	 * @throws Exception
	 */
	public int exchangeCardsIfPossible() throws Exception {
		if (cardsList.size() < 3)
			return 0;
		int i1 = 0, i2 = 0, i3 = 0;
		for (Card card : cardsList) {
			switch (card.getType()) {
			case INFANTRY:
				i1++;
				break;
			case CAVALRY:
				i2++;
				break;
			case ARTILLERY:
				i3++;
				break;
			default:
				throw new IllegalArgumentException("Invalid Unit Type");

			}

		}
		if (i1 == 3) {
			cardsList.forEach(x -> {
				if (x.getType() == Unit.INFANTRY) {
					deck.Put(x);
				}
			});
			cardsList.removeIf(x -> x.getType() == Unit.INFANTRY);
			return 4;
		} else if (i2 == 3) {
			cardsList.forEach(x -> {
				if (x.getType() == Unit.CAVALRY) {
					deck.Put(x);
				}
			});
			cardsList.removeIf(x -> x.getType() == Unit.CAVALRY);
			return 6;
		} else if (i3 == 3) {
			cardsList.forEach(x -> {
				if (x.getType() == Unit.ARTILLERY) {
					deck.Put(x);
				}
			});
			cardsList.removeIf(x -> x.getType() == Unit.ARTILLERY);
			return 8;
		} else if (i1 > 0 && i2 > 0 && i3 > 0) {
			Boolean b1 = false, b2 = false, b3 = false; // was unit type removed
														// yet
			for (Card card : cardsList) {
				switch (card.getType()) {
				case INFANTRY:
					if (!b1) {
						deck.Put(card);
						cardsList.remove(card);
						b1 = true;
					}
					break;
				case CAVALRY:
					if (!b2) {
						deck.Put(card);
						cardsList.remove(card);
						b2 = true;
					}
					break;
				case ARTILLERY:
					if (!b3) {
						deck.Put(card);
						cardsList.remove(card);
						b3 = true;
					}
					break;
				default:
					throw new IllegalArgumentException("Invalid unit type");
				}

			}
			return 10;
		}
		throw new Exception("Card exchange failed");

	}

	public List<Card> getcards() {
		return cardsList;
	}

	/**
	 * 
	 * @param card
	 */
	public Player putcard(Card card) {
		cardsList.add(card);
		return this;
	}

	public void removeTerritory() {
		territoryCount--;
	}
	
	public int stepDownReinforceUnits()
	{
		return this.unitsLeftToReinforce--;
	}
	
	public void setReinforcementUnits(int unit)
	{
		this.unitsLeftToReinforce = unit;
	}
	
	public int getReinforcementUnits()
	{
		return this.unitsLeftToReinforce;
	}

}