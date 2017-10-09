package Model;

import java.io.File;
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
	private boolean hasTransferred;
	private Boolean capturedThisTurn;
	private Random r = new Random();
	private Phase phase;
	private Territory[] waitForUnitsTemp = new Territory[2];
	private int miscnumber;

	/**
	 * The amount of Units left to Place in the Reinforcement Stage of the current
	 * Player's current turn.
	 */

	public RiskGameModel() {
		phase = Phase.PlayerRegistration;
		deck = new Deck();
		playersList = new ArrayList<>();
		capturedThisTurn = false;
		map = Map.getInstance();
	}

	/**
	 * Calculate the next phase
	 * 
	 * @return Phase
	 * @throws Exception
	 */
	private Phase nextPhase() throws Exception {
		if (phase == Phase.PlayerRegistration)
			return phase = Phase.Preparation;
		if (phase == Phase.Preparation)
			return phase = Phase.Reinforcement;
		if (phase == Phase.Reinforcement)
			return phase = Phase.Battle;
		if (phase == Phase.Battle)
			return phase = Phase.Transfer;
		if (phase == Phase.Transfer)
			throw new Exception("Press next player instead.");
		// return phase = Phase.Reinforcement;
		else
			throw new Exception("Not valid phase");
	}

	/**
	 * Battle
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean endBattlePhase() throws Exception {
		if (phase != Phase.Battle)
			throw new Exception("Not even battle phase");
		nextPhase();
		return true;
	}

	/**
	 * Adder for playerList
	 * 
	 * @param player
	 * @throws Exception
	 */
	public void addPlayerToPlayerList(Player player) throws Exception {
		if (phase != Phase.PlayerRegistration)
			throw new Exception("Not in PlayerRegistration phase");
		playersList.add(player);
		// player.addReinforcements(25);
		player.addReinforcements(9);
	}

	/**
	 * Pairs overload
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void addPlayerToPlayerList(java.util.Map<Color, String> map) throws Exception {
		for (java.util.Map.Entry<Color, String> entry : map.entrySet()) {
			addPlayerToPlayerList(new Player(entry.getKey(), entry.getValue(), deck));
		}
		miscnumber = 42;
		nextPhase();
	}

	/**
	 * Return the result of the attack
	 * 
	 * If the attacker have more than 3 units, the fighting attacker units will
	 * reduce to 3 If the defender have more than 2 units, the fighting defender
	 * units will reduce to 2
	 * 
	 * @param defender
	 * @param attacker
	 * @param defendUnits
	 * @param attackUnits
	 *            attackUnits
	 * @throws Exception
	 */
	public boolean attackTerritory(int defenderID, int attackerID, int defendUnits, int attackUnits) throws Exception {
		if (map.getTerritory(defenderID).getUnits() == 0) {
			throw new Exception("No more defender units left :(");
		}
		if (phase != Phase.Battle)
			throw new Exception("Game is not in Battle phase");

		Territory attacker = map.getTerritory(attackerID);
		Territory defender = map.getTerritory(defenderID);
		if (!checkAttackPossible(defender, attacker, defendUnits, attackUnits))
			return false;
		List<Integer> attackRolls = new ArrayList<Integer>();
		List<Integer> defendRolls = new ArrayList<Integer>();

		// Maximum 3 attack units is allowed
		if (attackUnits > 3) {
			for (int i = 0; i < 3; i++) {
				attackRolls.add(r.nextInt(6) + 1);
			}
		} else {
			for (int i = 0; i < attackUnits; i++) {
				attackRolls.add(r.nextInt(6) + 1);
			}
		}
		// Maximum 2 defend units is allowed
		if (defendUnits > 2) {
			for (int i = 0; i < 2; i++) {
				attackRolls.add(r.nextInt(6) + 1);
			}
		} else {
			for (int i = 0; i < defendUnits; i++) {
				defendRolls.add(r.nextInt(6) + 1);
			}
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
	 * Csak akkor támadhat, ha a területen elegendő egysége van. Igazzal tér vissza,
	 * ha a tamadas lehetséges.
	 * 
	 * @param defender
	 * @param attacker
	 * @param defendUnits
	 * @param attackUnits
	 * @return boolean
	 */
	// csak a szomszédosságot és terület birtokosokat kellene ellenőriznie,
	// unitCount-ot még nem
	public boolean checkAttackPossible(Territory defender, Territory attacker, int defendUnits, int attackUnits) {
		return (checkAttackPossible(defender.getId(), attacker.getId()) && defender.getUnits() >= defendUnits
				&& attacker.getUnits() >= attackUnits + 1 && attackUnits <= 3 && attackUnits >= 1 && defendUnits <= 2
				&& defendUnits >= 1);
	}

	/**
	 * Attack checking method
	 * 
	 * @param defenderID
	 * @param attackerID
	 * @return
	 */
	public boolean checkAttackPossible(int defenderID, int attackerID) {
		return map.getTerritory(attackerID).getOwner() == currentPlayer && Map.IsNeighbour(attackerID, defenderID);
	}

	/**
	 * Capture and conquer checking method
	 * 
	 * @param defender
	 * @return boolean
	 */
	public boolean checkIfCapturedAndConquer(Territory defender) {
		if (checkIfTerrotiryIsEmpty(defender)) {
			defender.setOwner(currentPlayer);

			return true;
		}
		return false;
	}

	/**
	 * Move units method
	 * 
	 * @param units
	 * @return boolean
	 * @throws Exception
	 */
	public boolean moveUnits(int units) throws Exception {
		if (phase != Phase.WaitForUnitCount)
			throw new Exception("Not in Capturing mode");
		if (units > waitForUnitsTemp[0].getUnits() - 1)
			return false;
		waitForUnitsTemp[0].setUnits(waitForUnitsTemp[0].getUnits() - units);
		waitForUnitsTemp[1].setUnits(units);
		return true;
	}

	/**
	 * Empty territory checking method
	 * 
	 * @param territory
	 * @return boolean
	 */
	public boolean checkIfTerrotiryIsEmpty(Territory territory) {
		return territory.getUnits() == 0;
	}

	/**
	 * Reinforce checking method
	 * 
	 * @param player
	 * @param territory
	 * @return boolean
	 */
	public boolean checkReinforcePossible(Player player, Territory territory) {
		return false;
	}

	/**
	 * Transfer checking method
	 * 
	 * @param from
	 * @param to
	 * @return boolean
	 */
	public boolean checkTransferPossible(int from, int to) {
		return Map.IsNeighbour(map.getTerritory(from), map.getTerritory(to))
				&& map.getTerritory(from).getOwner() == currentPlayer
				&& map.getTerritory(to).getOwner() == currentPlayer;
	}

	/**
	 * Transfer checking method
	 * 
	 * @param from
	 * @param to
	 * @param units
	 * @return boolean
	 */
	public boolean checkTransferPossible(Territory from, Territory to, int units) {
		return false;
	}

	/**
	 * Ez a függvény fogja a kockadobásokkal történő harcot szimulálni.
	 * 
	 * Kor vege Returns next player's id
	 * 
	 * @return int
	 * @throws Exception 
	 */
	public int endTurn() throws Exception {
		if (phase != Phase.Preparation)
		{
		if (capturedThisTurn) {
			playersList.get(currentPlayer).putcard(deck.Draw());
			capturedThisTurn = false;
		}

		currentPlayer = (currentPlayer + 1) % playersList.size();
		nextPlayer();
		}
		else {
			throw new Exception("Can't do nextplayer because you're in PREPARATION phase!");
		}
		return currentPlayer;
	}

	/**
	 * Initializes the next Player's turn. Calculates the amount of Units to place.
	 */
	protected void nextPlayer() {
		if (phase == Phase.Preparation) {
			int calculatedVal = playersList.get(currentPlayer).getTerritoryCount() / 3;
			if (calculatedVal < 3)
				calculatedVal = 3;
			playersList.get(currentPlayer)
					.setReinforceUnits(calculatedVal + playersList.get(currentPlayer).getReinforcementBonus());
			phase = Phase.Reinforcement;
		}

		if (phase == Phase.Preparation && playersList.get(currentPlayer).getReinforcementBonus() == 0) {
			currentPlayer = (currentPlayer + 1) % playersList.size();
		}

		if (phase == Phase.Reinforcement && playersList.get(currentPlayer).getReinforceUnits() == 0) {
			phase = Phase.Battle;
		}

		if (phase == Phase.Transfer && this.hasTransferred) {
			currentPlayer = (currentPlayer + 1) % playersList.size();
		}
	}

	/**
	 * Reinforce units scanner method
	 * 
	 * @return boolean
	 */
	protected boolean hasAnyPlayerReinforcementUnits() {
		for (int i = 0; i < playersList.size(); i++) {
			if (playersList.get(i).getReinforceUnits() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Reinforce method
	 * 
	 * @param territoryId
	 * @return boolean
	 * @throws Exception
	 */
	public boolean reinforce(int territoryId) throws Exception {
		switch (phase) {
		case Reinforcement: {
			Territory territory = map.getTerritory(territoryId);
			if (territory.getOwner() != currentPlayer)
				return false;
			if (playersList.get(currentPlayer).getReinforceUnits() == 0) {
				// nextPhase();
				nextPlayer();
				return true;
			}
			territory.setUnits(territory.getUnits() + 1);
			playersList.get(currentPlayer).setReinforceUnits(playersList.get(currentPlayer).getReinforceUnits() - 1);
			return true;
		}
		case Preparation: {
			Territory territory = map.getTerritory(territoryId);
			if (miscnumber != 0) {
				if (territory.getOwner() != -1)
					return false;
				territory.setOwner(currentPlayer);
				territory.setUnits(1);
				--miscnumber;
				playersList.get(currentPlayer).addReinforcements(-1);

			} else {
				if (territory.getOwner() != currentPlayer)
					return false;
				territory.setUnits(territory.getUnits() + 1);
				playersList.get(currentPlayer).addReinforcements(-1);
			}
			currentPlayer = (currentPlayer + 1) % 5;
			if (playersList.get(currentPlayer).getReinforcementBonus() == 0)
				nextPlayer();
			return true;
		}
		default:
			throw new Exception("You are not in a reinforcement phase!");
		}
	}

	/**
	 * Save data method
	 * 
	 * @return boolean
	 * @throws IOException
	 */
	public boolean saveGame() throws IOException {

		SaveData data = new SaveData();

		data.phase = this.getPhase();
		data.deck = this.deck;
		data.playersList = this.playersList;
		data.capturedThisTurn = this.capturedThisTurn;
		data.map = this.map;
		data.currentPlayer = this.currentPlayer;
		data.waitForUnitsTemp = this.waitForUnitsTemp;

		try {
			int i = 0;
			String filename = i + ".risksav";
			File f = new File(filename);
			while (!f.exists()) {
				i = i++;
				filename = i + ".risksav";
				f = new File(filename);
			}
			ResourceManager.save(data, filename);
		} catch (Exception e) {
			throw new IOException(e);
		}
		return true;
	}

	/**
	 * Load data method
	 * 
	 * @param filename
	 * @return boolean
	 * @throws Exception
	 */
	public boolean loadGame(String filename) throws Exception {
		try {
			SaveData data = (SaveData) ResourceManager.load(filename);

			this.phase = data.phase;
			this.deck = data.deck;
			this.playersList = data.playersList;
			this.capturedThisTurn = data.capturedThisTurn;
			this.map = data.map;
			this.currentPlayer = data.currentPlayer;
			this.waitForUnitsTemp = data.waitForUnitsTemp;

		} catch (Exception e) {
			throw new Exception(e);
		}
		return true;
	}

	/**
	 * Random select the starting player
	 * 
	 * @param list
	 * @return Player
	 */
	public Player selectStartingPlayer(List<Player> list) {
		return list.get(r.nextInt(list.size()));
	}

	/**
	 * Add player with name and color
	 * 
	 * @param name
	 * @param color
	 * @return boolean
	 */
	public boolean addPlayer(String name, Color color) {
		if (playersList.stream().anyMatch(x -> x.getColor() == color))
			return false;
		playersList.add(new Player(color, name, deck));
		return true;
	}

	/**
	 * 
	 * @param fromID
	 * @param toID
	 * @param units
	 * @return boolean
	 * @throws Exception
	 */
	public boolean transfer(int fromID, int toID, int units) throws Exception {
		hasTransferred = false;
		if (units > 0) {
			return transfer(map.getTerritory(fromID), map.getTerritory(toID), units) & (hasTransferred = true);
		} else
			return false;
	}

	/**
	 * Transfer method
	 * 
	 * @param from
	 * @param to
	 * @param units
	 * @return boolean
	 * @throws Exception
	 */
	public boolean transfer(Territory from, Territory to, int units) throws Exception {
		if (phase != Phase.Transfer)
			throw new Exception("Not in Transfer Phase");
		if (from.getOwner() != currentPlayer || from.getUnits() < units + 1 || to.getOwner() != currentPlayer
				|| !Map.IsNeighbour(from, to))
			return false;
		from.setUnits(from.getUnits() - units);
		to.setUnits(to.getUnits() + units);
		endTurn();
		return true;
	}

	/**
	 * Getter for Territory
	 * 
	 * @param id
	 * @return Territory
	 */
	public Territory getTerritory(int id) {
		return map.getTerritory(id);
	}

	/**
	 * Getter for Player's name
	 * 
	 * @param id
	 * @return String
	 */
	public String getPlayerName(int id) {
		return playersList.get(id).getName();
	}

	/**
	 * Getter for left units in reinforce phase
	 * 
	 * @param playerID
	 * @return int
	 */
	public int getUnitsLeftToReinforce(int playerID) {
		switch (phase) {
		case Preparation:
			return playersList.get(playerID).getReinforcementBonus();
		case Reinforcement:
			return playersList.get(playerID).getReinforceUnits();

		default:
			return 0;
		}

	}

	/**
	 * Getter for current player
	 * 
	 * @return int
	 */
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	/*
	 * Getter for Phase
	 * 
	 * @return phase
	 */
	public Phase getPhase() {
		return phase;
	}

}