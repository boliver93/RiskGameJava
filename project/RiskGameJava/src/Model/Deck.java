package Model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Szabi
 * @version 1.0
 * @created 19-ápr.-2017 23:11:54
 */
public class Deck {
	Random r = new Random();

	private Set<Card> cardsSet;

	public Deck() {
		cardsSet = new HashSet<Card>();
	}

	/**
	 * 
	 * @exception Throwable
	 */
	@Override
	public void finalize() throws Throwable {

	}

	/**
	 * A kihuzott kartya eltunik a paklibol
	 */
	public Card Draw() {
		Card currentDraw = cardsSet.stream().skip(r.nextInt(cardsSet.size())).findFirst().get();
		cardsSet.remove(currentDraw);
		return currentDraw;
	}

	/**
	 * 
	 * @param kartya
	 *            kartya
	 */
	public Deck Put(Card card) {
		cardsSet.add(card);
		return this;
	}

}