package Model;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * A jatek terkepet adja meg.
 * 
 * @author Szabi
 * @version 1.0
 * @created 19-ápr.-2017 23:11:55
 */
public class Map {

	private List<Territory> territoriesList;
	private static Boolean[][] neighbourhood = new Boolean[42][42]; // TODO what
																	// is this
																	// number
																	// even

	public Map() {
		territoriesList = new ArrayList<Territory>();
		// TODO Fill in the territories with them list?
		// TODO fill in the neighbourhood set possibly from file
		for (int i = 0; i < neighbourhood.length; i++) {
			for (int j = 0; j < neighbourhood[i].length; j++) {
				neighbourhood[i][j] = false; // TODO replace this with useful values
			}
		}
	}

	/**
	 * 
	 * @param elso
	 * @param masodik
	 */
	public static boolean IsNeighbour(Territory first, Territory second) {
		return neighbourhood[first.getId()][second.getId()];
	}

	public Territory getTerritory(int id) {
		return territoriesList.get(id);
	}

}