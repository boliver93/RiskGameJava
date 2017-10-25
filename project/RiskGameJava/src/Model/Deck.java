package Model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Szabi
 * @version 1.0
 * @created 19-ápr.-2017 23:11:54
 */
public class Deck implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Random r = new Random();

	private Set<Card> cardsSet;

	public Deck() {
		cardsSet = new HashSet<Card>();
		readAllCard();
	}
	
	/*
	 * Read cards from territorycards.properties
	 * 
	 */
	private void readAllCard()
	{
		String line;
		Card tmpCard;
		
		String fullPath = System.getProperty("user.dir") + "\\src\\Model\\res\\territorycards.properties";
		java.nio.file.Path path = java.nio.file.Paths.get(fullPath);
		
		try (
		    java.io.InputStream fis = new java.io.FileInputStream(path.toString());
			java.io.InputStreamReader isr = new java.io.InputStreamReader(fis, java.nio.charset.Charset.forName("UTF-8"));
			java.io.BufferedReader br = new java.io.BufferedReader(isr);
		) {
		    while ((line = br.readLine()) != null) {
		    	String[] strArray = line.split(" ");
		    	int id = Integer.parseInt(strArray[0]);
		    	Unit unit = Unit.valueOf(strArray[1]);

		    	tmpCard = new Card(id,unit);
		    	
		    	cardsSet.add(tmpCard);
		    }      
        } catch (Exception e) {
			System.out.println("Can't find territorycards.properties!");
			e.printStackTrace();
		}
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