package Model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Az MVC architektura Model reszet megvalosíto osztalya.
 * 
 * @author Szabi
 * @version 1.0
 * @created 19-ápr.-2017 23:11:55
 */
public class RiskGameModel {

	private Deck deck;
	private Map map;
	private List<Player> playersList;
	private int currentPlayer;
	private Boolean capturedThisTurn;
	private Random r = new Random();
	private Phase phase;
	private Territory[] waitForUnitsTemp = new Territory[2];
	private int miscnumber;
	private int circlenumber;
	private int calculatedCircleNumber;
	private boolean hasTransferred;

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
		// player.addReinforcements(9);
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
		// 42 territory
		miscnumber = 42;
		calculateCircle();
		nextPhase();
	}

	private void calculateCircle() throws Exception {
		switch (playersList.size()) {
		case 3:
			calculatedCircleNumber = miscnumber;
			break;
		case 4:
			calculatedCircleNumber = miscnumber + 2;
			break;
		case 5:
			calculatedCircleNumber = miscnumber + 3;
			break;
		case 6:
			calculatedCircleNumber = miscnumber;
			break;
		default:
			throw new Exception("Invalid player number!");
		}
	}

	/**
	 * Return the result of the attack
	 * 
	 * If the attacker have more than 3 units, the fighting attacker units will
	 * reduce to 3 If the defender have more than 2 units, the fighting defender
	 * units will reduce to 2
	 * 
	 * @param defenderID
	 * @param attackerID
	 * @param defendUnits
	 * @param attackUnits
	 * @throws Exception
	 */
	public AttackResult attackTerritoryWithResult(int defenderID, int attackerID, int defendUnits, int attackUnits)
			throws Exception {
		if (phase != Phase.Battle)
			throw new Exception("Game is not in Battle phase");

		Territory attacker = map.getTerritory(attackerID);
		Territory defender = map.getTerritory(defenderID);
		if (!checkAttackPossible(defender, attacker, defendUnits, attackUnits))
			throw new Exception("Can't attack!");

		List<Integer> attackRolls = new ArrayList<Integer>();
		List<Integer> defendRolls = new ArrayList<Integer>();

		List<Integer> original_attackRolls = new ArrayList<Integer>();
		List<Integer> original_defendRolls = new ArrayList<Integer>();

		// Maximum 3 attack units is allowed
		for (int i = 0; i < attackUnits; i++) {
			attackRolls.add(r.nextInt(6) + 1);
		}
		// Maximum 2 defend units is allowed
		for (int i = 0; i < defendUnits; i++) {
			defendRolls.add(r.nextInt(6) + 1);
		}

		attackRolls.sort(Collections.reverseOrder());
		defendRolls.sort(Collections.reverseOrder());

		// deep copy
		for (int i = 0; i < attackRolls.size(); i++) {
			original_attackRolls.add(attackRolls.get(i));
		}

		for (int i = 0; i < defendRolls.size(); i++) {
			original_defendRolls.add(defendRolls.get(i));
		}

		// if defendUnits < 2, add None
		while (original_defendRolls.size() < 3) {
			original_defendRolls.add(-1);
		}

		while (original_attackRolls.size() < 4) {
			original_attackRolls.add(-1);
		}

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
			capturedThisTurn = true;
			phase = Phase.WaitForUnitCount;
			waitForUnitsTemp[0] = attacker;
			waitForUnitsTemp[1] = defender;
		}

		return new AttackResult(attackUnits, defendUnits, original_attackRolls, original_defendRolls);
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
		return map.getTerritory(attackerID).getOwner() == currentPlayer && Map.IsNeighbour(attackerID, defenderID)
				&& map.getTerritory(defenderID).getOwner() != currentPlayer
				&& map.getTerritory(attackerID).getUnits() > 1;
	}

	/**
	 * Capture and conquer checking method
	 * 
	 * @param defender
	 * @return boolean
	 */
	public boolean checkIfCapturedAndConquer(Territory defender) {
		if (checkIfTerrotiryIsEmpty(defender)) {
			playersList.get(defender.getOwner()).removeTerritory();
			playersList.get(currentPlayer).addTerritory();
			defender.setOwner(currentPlayer);
			return true;
		}
		return false;
	}

	private void winGame(int winner) {
		phase = Phase.GameOver;
		//do fancy victory message
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
		if (units > waitForUnitsTemp[0].getUnits() - 1 || units < 1)
			return false;
		waitForUnitsTemp[0].setUnits(waitForUnitsTemp[0].getUnits() - units);
		waitForUnitsTemp[1].setUnits(units);
		phase = Phase.Battle;
		if(playersList.get(currentPlayer).getTerritoryCount() == 42)
			winGame(currentPlayer);
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
	 * Transfer checking method
	 * 
	 * @param from
	 * @param to
	 * @return boolean
	 */
	public boolean checkTransferPossible(int from, int to) {
		if (map.getTerritory(from).getUnits() > 0) {
			return Map.IsNeighbour(map.getTerritory(from), map.getTerritory(to))
					&& map.getTerritory(from).getOwner() == currentPlayer
					&& map.getTerritory(to).getOwner() == currentPlayer;
		} else
			return false;
	}

	/**
	 *
	 * 
	 * Kor vege Returns next player's id
	 * 
	 * @return int
	 * @throws Exception
	 */
	public int endTurn() throws Exception {
		if(phase == Phase.GameOver) return currentPlayer;
		if (capturedThisTurn) {
			playersList.get(currentPlayer).putcard(deck.Draw());
			capturedThisTurn = false;
		}
		if ((playersList.get(currentPlayer).getReinforcementUnits() == 0 && phase != Phase.Preparation
				&& phase != Phase.Reinforcement)
				|| hasTransferred && playersList.get(currentPlayer).getReinforcementUnits() == 0) {
			currentPlayer = (currentPlayer + 1) % playersList.size();
			while(playersList.get(currentPlayer).getTerritoryCount() == 0) {
				currentPlayer = (currentPlayer + 1) % playersList.size();
			}
			nextPlayer();
		} else
			throw new Exception("You cannot do nextplayer!");

		return currentPlayer;
	}

	/**
	 * Initializes the next Player's turn. Calculates the amount of Units to place.
	 * 
	 * @throws Exception
	 */
	protected void nextPlayer() throws Exception {
		Player player = playersList.get(currentPlayer);
		int calculatedVal = player.getTerritoryCount() / 3;
		if (calculatedVal < 3)
			calculatedVal = 3;
		player.exchangeCardsIfPossible();
		player.setReinforcementUnits(calculatedVal + player.getReinforcementBonus());
		phase = Phase.Reinforcement;
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
			if (playersList.get(currentPlayer).getReinforcementUnits() > 0) {
				Territory territory = map.getTerritory(territoryId);
				if (territory.getOwner() != currentPlayer)
					return false;

				territory.setUnits(territory.getUnits() + 1);
				playersList.get(currentPlayer).stepDownReinforceUnits();
				if (playersList.get(currentPlayer).getReinforcementUnits() == 0)
					nextPhase();
				return true;
			} else
				phase = Phase.Battle;
		}
		case Preparation: {
			Territory territory = map.getTerritory(territoryId);
			if (miscnumber != 0) {
				if (territory.getOwner() != -1)
					return false;
				territory.setOwner(currentPlayer);
				playersList.get(currentPlayer).addTerritory();
				territory.setUnits(1);
				--miscnumber;
			} else {
				if (territory.getOwner() != currentPlayer)
					return false;
				territory.setUnits(territory.getUnits() + 1);
			}
			currentPlayer = (currentPlayer + 1) % playersList.size();
			++circlenumber;
			if (circlenumber == calculatedCircleNumber)
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
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public boolean saveGame(File file) throws IOException {

		SaveData data = new SaveData();

		data.phase = this.getPhase();
		data.deck = this.deck;
		data.playersList = this.playersList;
		data.capturedThisTurn = this.capturedThisTurn;
		data.map = Map.getInstance();
		data.currentPlayer = this.currentPlayer;
		data.waitForUnitsTemp = this.waitForUnitsTemp;
		data.territoriesList = Map.getTerritoryList();

		try {
			ResourceManager.save(data, file.getAbsolutePath());
		} catch (Exception e) {
			throw new IOException(e);
		}
		return true;
	}

	/**
	 * Load data method
	 * 
	 * @param file
	 * @return boolean
	 * @throws Exception
	 */
	public boolean loadGame(File file) throws Exception {
		try {
			SaveData data = (SaveData) ResourceManager.load(file.getAbsolutePath());

			this.phase = data.phase;
			this.deck = data.deck;
			this.playersList = data.playersList;
			this.capturedThisTurn = data.capturedThisTurn;
			this.map = data.map;
			this.currentPlayer = data.currentPlayer;
			this.waitForUnitsTemp = data.waitForUnitsTemp;
			this.map.setTerritoryList(data.territoriesList);

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
		return transfer(map.getTerritory(fromID), map.getTerritory(toID), units);
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
		hasTransferred = false;
		if (playersList.get(currentPlayer).getReinforcementUnits() > 0)
			throw new Exception("Cannot transfer because you have reinforcement units left!");
		if (phase != Phase.Transfer)
			throw new Exception("Not in Transfer Phase");
		if (from.getOwner() != currentPlayer || from.getUnits() < units + 1 || to.getOwner() != currentPlayer
				|| !Map.IsNeighbour(from, to))
			return false;
		from.setUnits(from.getUnits() - units);
		to.setUnits(to.getUnits() + units);
		hasTransferred = true;
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
	
	public List<Integer> getPlayerCards(int id) {
		return playersList.get(id).getcards().stream().map(c -> c.getId()).collect(Collectors.toList());
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
			return playersList.get(playerID).getReinforcementUnits();

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
	
	/*
	 * Getter for Players list
	 * 
	 * @return List<Player>
	 */
	public List<String> getPlayers()
	{
		List<String> playersNameList = new ArrayList<String>();
		for(int i=0;i<playersList.size();i++)
			playersNameList.add(playersList.get(i).getName());
		return playersNameList;
	}
}