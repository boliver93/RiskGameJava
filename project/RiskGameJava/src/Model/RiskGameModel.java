package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Az MVC architektura Model reszet megvalosíto osztalya.
 * 
 * @author Szabi
 * @version 1.0
 * @created 19-ápr.-2017 23:11:55
 */
public class RiskGameModel extends java.util.Observable {

	private Deck deck;
	private Map map;
	private List<Player> playersList;
	private int currentPlayer;
	private Boolean capturedThisTurn;
	private Random r = new Random();
	private Phase phase;
	private Territory[] waitForUnitsTemp = new Territory[2];
	private int miscnumber;
	/**
	 * The amount of Units left to Place in the Reinforcement Stage of the
	 * current Player's current turn.
	 */
	private int unitsLeftToReinforce;

	public RiskGameModel() {
		phase = Phase.PlayerRegistration;
		deck = new Deck();
		playersList = new ArrayList<>();
		capturedThisTurn = false;
		map = Map.getInstance();
	}
	
	public Phase getPhase() {
		return phase;
	}
	
	private Phase nextPhase() throws Exception
	{
		if(phase == Phase.PlayerRegistration)
			return phase = Phase.Preparation;
		if(phase == Phase.Preparation)
			return phase = Phase.Reinforcement;
		if(phase == Phase.Reinforcement)
			return phase = Phase.Battle;
		if(phase == Phase.Battle)
			return phase = Phase.Transfer;
		if(phase == Phase.Transfer)
			throw new Exception("Press next player instead.");
			//return phase = Phase.Reinforcement;
		else
			throw new Exception("Not valid phase");
	}

	/**
	 * 
	 * @param player
	 * @throws Exception
	 */
	/**
	 * @param player
	 * @throws Exception
	 */
	public void addPlayerToPlayerList(Player player) throws Exception {
		if (phase != Phase.PlayerRegistration)
			throw new Exception("Not in PlayerRegistration phase");
		playersList.add(player);
		player.addReinforcements(25);
	}
	
	/*
	 * 	Pairs overload
	 */
	public void addPlayerToPlayerList(java.util.Map<Color, String> map) throws Exception {
		for (java.util.Map.Entry<Color, String> entry : map.entrySet()) {
			addPlayerToPlayerList(new Player(entry.getKey(), entry.getValue(), deck));
		}
		miscnumber = 42;
		nextPhase();
	}
	

	/**
	 * 
	 * @param defender
	 * @param attacker
	 * @param defendUnits
	 * @param attackUnits
	 *            attackUnits
	 * @throws Exception
	 */
	//DONE: public boolean attackTerritory(int defenderID, int attackerID, int defendUnits, int attackUnits) kellene a controllernek 
	public Boolean attackTerritory(int defenderID, int attackerID, int defendUnits, int attackUnits)
			throws Exception {
		if (phase != Phase.Battle)
			throw new Exception("Game is not in Battle phase");
		Territory attacker = map.getTerritory(attackerID);
		Territory defender = map.getTerritory(defenderID);
		if (!checkAttackPossible(defender, attacker, defendUnits, attackUnits))
			return false;
		List<Integer> attackRolls = new ArrayList<Integer>();
		List<Integer> defendRolls = new ArrayList<Integer>();
		for (int i = 0; i < attackUnits; i++) {
			attackRolls.add(r.nextInt(6) + 1);
		}
		for (int i = 0; i < defendUnits; i++) {
			defendRolls.add(r.nextInt(6) + 1);
		}
		attackRolls.sort(Collections.reverseOrder());
		defendRolls.sort(Collections.reverseOrder());
		while (attackRolls.size() > 0 && defendRolls.size() > 0) {
			int currentAttRoll = attackRolls.get(0);
			int currentDefRoll = defendRolls.get(0);
			attackRolls.remove(0);
			defendRolls.remove(0);
			if (currentDefRoll >= currentAttRoll) {
				attacker.setUnits(attacker.getUnits() - 1);
			} else {
				defender.setUnits(defender.getUnits() - 1);
			}
		}
		if (checkIfCapturedAndConquer(defender)) {
			phase = Phase.WaitForUnitCount;
			waitForUnitsTemp[0] = attacker;
			waitForUnitsTemp[1] = defender;
		}

		return true;
	}

	/**
	 * Csak akkor támadhat, ha a ter�Eeten elegend? egysége van. Igazzal ter
	 * vissza, ha a tamadas lehetseges.
	 * 
	 * @param attacker
	 * @param attackerUnits
	 *            attackerUnits
	 */
	//DONE: public boolean checkAttackPossible(int defenderID, int attackerID) kellene a controllernek 
	//			csak a szomszédosságot és terület birtokosokat kellene ellenőriznie, unitCount-ot még nem
	public Boolean checkAttackPossible(Territory defender, Territory attacker, int defendUnits, int attackUnits) {
		return (checkAttackPossible(defender.getId(),attacker.getId())
				&& defender.getUnits() >= defendUnits && attacker.getUnits() >= attackUnits + 1 && attackUnits <= 3
				&& attackUnits >= 1 && defendUnits <= 2 && defendUnits >= 1);
	}
	
	public boolean checkAttackPossible(int defenderID, int attackerID) {
		return map.getTerritory(attackerID).getOwner() == currentPlayer && Map.IsNeighbour(attackerID, defenderID);
	}

	/**
	 * 
	 * @param defender
	 *            defender
	 */
	public Boolean checkIfCapturedAndConquer(Territory defender) {
		if (checkIfTerrotiryIsEmpty(defender)) {
			defender.setOwner(currentPlayer);

			return true;
		}
		return false;
	}

	public Boolean moveUnits(int units) throws Exception {
		if (phase != Phase.WaitForUnitCount)
			throw new Exception("Not in Capturing mode");
		if (units > waitForUnitsTemp[0].getUnits() - 1)
			return false;
		waitForUnitsTemp[0].setUnits(waitForUnitsTemp[0].getUnits() - units);
		waitForUnitsTemp[1].setUnits(units);
		return true;
	}

	/**
	 * 
	 * @param territory
	 */
	public boolean checkIfTerrotiryIsEmpty(Territory territory) {
		return territory.getUnits() == 0;
	}

	/**
	 * 
	 * @param player
	 * @param territory
	 */
	public boolean checkReinforcePossible(Player player, Territory territory) {
		return false;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 */
	//public boolean checkTransferPossible(int fromID, int toID) kellene a controllernek 
	public boolean checkTransferPossible(int from, int to) {
		return Map.IsNeighbour(map.getTerritory(from), map.getTerritory(to)) && map.getTerritory(from).getOwner() == currentPlayer && map.getTerritory(to).getOwner() == currentPlayer;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param units
	 */
	public boolean checkTransferPossible(Territory from, Territory to, int units) {
		return false;
	}

	/**
	 * Ez a f�Egvény fogja a kockadobásokkal történ? harcot szimulálja.
	 * 
	 * @param attackerUnits
	 * @param defenderUnits
	 *            defenderUnits
	 */
	// attackTerritory-ban lekodolva, elfer ott.
	// protected doBattle(int attackerUnits, int defenderUnits){
	//
	// }

	/**
	 * Kor vege
	 * 
	 * Returns next player's id
	 */
	public int endTurn() {
		if (capturedThisTurn) {
			playersList.get(currentPlayer).putcard(deck.Draw());
			capturedThisTurn = false;
		}

		currentPlayer = (currentPlayer + 1) % playersList.size();
		nextPlayer();
		return currentPlayer;
	}

	/**
	 * Initializes the next Player's turn. Calculates the amount of Units to
	 * place.
	 */
	protected void nextPlayer() {
		int calculatedVal = playersList.get(currentPlayer).getTerritoryCount() / 3;
		if (calculatedVal < 3)
			calculatedVal = 3;
		unitsLeftToReinforce = calculatedVal + playersList.get(currentPlayer).getReinforcementBonus();
		phase = Phase.Reinforcement;
	}

	/**
	 * 
	 * @param List
	 */
	// public playerRiskCardPlacedPlace(List){
	//
	// }

	/**
	 * 
	 * @param player
	 * @param territory
	 * @throws Exception
	 */
	public Boolean reinforce(int territoryId) throws Exception {
		switch (phase) {
		case Reinforcement: {
			Territory territory = map.getTerritory(territoryId);
			if (territory.getOwner() != currentPlayer)
				return false;
			territory.setUnits(territory.getUnits() + 1);
			unitsLeftToReinforce--;
			if(unitsLeftToReinforce == 0) nextPhase();
			return true;
			}
		case Preparation: {
			Territory territory = map.getTerritory(territoryId);
			if(miscnumber != 0) {
				if (territory.getOwner() != -1) return false;
				territory.setOwner(currentPlayer);
				territory.setUnits(1);
				--miscnumber;
			}
			else {
				if(territory.getOwner() != currentPlayer) return false;
				territory.setUnits(territory.getUnits()+1);
			}
			currentPlayer = currentPlayer+1 % 5;
			if(playersList.get(currentPlayer).getReinforcementBonus() == 0) nextPlayer();
			return true;
			}
		default:
			throw new Exception("You are not in a reinforcement phase!");
		}
	}

	/*
	 * 
	 * @param saveName
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	public boolean saveGame(String saveName) throws IOException {
		SaveData data = new SaveData();
		data.name = playersList.get(currentPlayer).getName();
		data.color = playersList.get(currentPlayer).getColor();
		try {
			ResourceManager.save(data, "1.save");
		} catch (Exception e) {
			throw new IOException(e);
		}
		return true;
	}

	public boolean loadGame() throws Exception {
		try {
			SaveData data = (SaveData) ResourceManager.load("1.save");
			playersList.get(currentPlayer).setName(data.name);
			playersList.get(currentPlayer).setColor(data.color);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return true;
	}

	/**
	 * 
	 * @param List<Player> list
	 */
	public Player selectStartingPlayer(List<Player> list) {
		return list.get(r.nextInt(list.size()));
	}

	// /**
	// *
	// * @param Name
	// */
	// public void setPlayerName(String Name){
	// playersList.get(currentPlayer)
	// }
	//
	// /**
	// *
	// * @param number
	// */
	// public setPlayerNumber(int number){
	//
	// }

	public Boolean addPlayer(String name, Color color) {
		if (playersList.stream().anyMatch(x -> x.getColor() == color))
			return false;
		playersList.add(new Player(color, name, deck));
		return true;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param units
	 * @throws Exception
	 */
	//TODO: public boolean transfer(int fromID, int toID, int units) kellene a controllernek 
	public boolean transfer(int fromID, int toID, int units) throws Exception {
		return transfer(map.getTerritory(fromID),map.getTerritory(toID),units);
	}
	
	
	public Boolean transfer(Territory from, Territory to, int units) throws Exception {
		if (phase != Phase.Transfer)
			throw new Exception("Not in Transfer Phase");
		if (from.getOwner() != currentPlayer || from.getUnits() < units + 1 || to.getOwner() != currentPlayer
				|| !Map.IsNeighbour(from, to))
			return false;
		from.setUnits(from.getUnits() - units);
		to.setUnits(to.getUnits() + units);
		return true;
	}
	
	public Territory getTerritory(int id) {
		return map.getTerritory(id);
	}
	
	public String getPlayerName(int id) {
		return playersList.get(id).getName();
	}
 
}