package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A jatek terkepet adja meg.
 * 
 * @author Szabi
 * @version 1.0
 * @created 19-�pr.-2017 23:11:55
 */
public class Map implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Map map = new Map();
	
	private static List<Territory> territoriesList;
	private static Boolean[][] neighbourhood; 

	private Map() {
		neighbourhood = new Boolean[42][42];
		territoriesList = new ArrayList<Territory>();

		for (Country country : Country.values()) {
			territoriesList.add(new Territory(country.ordinal()));
		}
		
		for (int i = 0; i < neighbourhood.length; i++) {
			for (int j = 0; j < neighbourhood[i].length; j++) {
				neighbourhood[i][j] = false;
			}
		}
		readTerritories();
	}
	
	public static Map getInstance(){
		return map;
	}

	/**
	 * 
	 * @param elso
	 * @param masodik
	 */
	public static boolean IsNeighbour(Territory first, Territory second) {
		return neighbourhood[first.getId()][second.getId()];
	}
	
	public static boolean IsNeighbour(int fst, int snd) {
		return neighbourhood[fst][snd];
	}

	public Territory getTerritory(int id) {
		return territoriesList.get(id);
	}
	
	public static List<Territory> getTerritoryList(){
		return territoriesList;
	}
	
	protected void setTerritoryList(List<Territory> territoriesList)
	{
		Map.territoriesList = territoriesList;
	}
	
	private void readTerritories() {
		String line;
		try {
		
		    java.io.InputStream fis = Map.class.getResourceAsStream("/Model/res/neighbourhood.properties");
			java.io.InputStreamReader isr = new java.io.InputStreamReader(fis, java.nio.charset.Charset.forName("UTF-8"));
			java.io.BufferedReader br = new java.io.BufferedReader(isr);

		    while ((line = br.readLine()) != null) {
		    	String[] strArray = line.split(" ");
		    	int key1 = Integer.parseInt(strArray[0]);
		    	for(int i = 1; i < strArray.length; i++) {
		    	    int key2 = Integer.parseInt(strArray[i]);
		    	    neighbourhood[key1][key2] = true;
		    	}
		    }      
        } catch (Exception e) {
			System.out.println("Can't find neighbourhood.properties!");
			e.printStackTrace();
		}
	}

	/*
	 * 	Tmp solution.
	 *  Sorry. I'm tired.
	 */
	private enum Country {

		AFGHANISTAN, ALASKA, ALBERTA, ARGENTINA, BRAZIL, 
		CENTRAL_AFRICA, CENTRAL_AMERICA, CHINA, 
		EAST_AFRICA, EASTERN_AUSTRALIA, EASTERN_CANADA, EASTERN_UNITED_STATES, EGYPT, 
		GREENLAND, GREAT_BRITAIN, 
		ICELAND, INDIA, INDONESIA, IRKUTSK, 
		JAPAN, 
		KAMCHATKA, 
		MADAGASCAR, MIDDLE_EAST, MONGOLIA, 
		NEW_GUINEA, NORTH_AFRICA, NORTHERN_EUROPE, NORTHWEST_TERRITORY, 
		ONTARIO, 
		PERU, 
		RUSSIA, 
		SCANDINAVIA, SIBERIA, SOUTH_AFRICA, SOUTH_ASIA, SOUTHERN_EUROPE, 
		URAL, 
		VENEZUELA, 
		WESTERN_AUSTRALIA, WESTERN_EUROPE, WESTERN_UNITED_STATES, 
		YAKUTSK;
		
	}

}