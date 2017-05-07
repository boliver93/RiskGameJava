package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Az MVC architektura Model reszet megvalosíto osztalya.
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
	/**
	 * The amount of Units left to Place in the Reinforcement Stage of the current
	 * Player's current turn.
	 */
	private int unitsLeftToReinforce;

	public RiskGameModel(){
		phase = Phase.PlayerRegistration;
		deck = new Deck();
		playersList = new ArrayList<>();
		capturedThisTurn = false;
		map = new Map();
	}

	/**
	 * 
	 * @exception Throwable
	 */
	public void finalize()
	  throws Throwable{

	}

	/**
	 * 
	 * @param player
	 * @throws Exception 
	 */
	public void addPlayerToPlayerList(Player player) throws Exception{
		if(phase != Phase.PlayerRegistration) throw new Exception("Not in PlayerRegistration phase");
		playersList.add(player);
	}

	/**
	 * 
	 * @param defender
	 * @param attacker
	 * @param defendUnits
	 * @param attackUnits    attackUnits
	 * @throws Exception 
	 */
	public Boolean attackTerritory(Territory defender, Territory attacker, int defendUnits, int attackUnits) throws Exception{
		if(phase != phase.Battle) throw new Exception("Game is not in Battle phase");
		if(!checkAttackPossible(defender, attacker, defendUnits, attackUnits))
			return false;
		List<Integer> attackRolls = new ArrayList<Integer>();
		List<Integer> defendRolls = new ArrayList<Integer>();
		for (int i = 0; i < attackUnits; i++) {
			attackRolls.add(r.nextInt(6)+1);
		}
		for (int i = 0; i < defendUnits; i++) {
			defendRolls.add(r.nextInt(6)+1);
		}
		attackRolls.sort(Collections.reverseOrder());
		defendRolls.sort(Collections.reverseOrder());
		while(attackRolls.size() > 0 && defendRolls.size() > 0) {
			int currentAttRoll = attackRolls.get(0);
			int currentDefRoll = defendRolls.get(0);
			attackRolls.remove(0);
			defendRolls.remove(0);
			if(currentDefRoll >= currentAttRoll) {
				attacker.setUnits(attacker.getUnits()-1);
			}
			else {
				defender.setUnits(defender.getUnits()-1);
			}
		}
		if(checkIfCapturedAndConquer(defender)){
			phase = Phase.WaitForUnitCount;
			waitForUnitsTemp[0] = attacker;
			waitForUnitsTemp[1] = defender;
		}
		
		return true;
	}

	/**
	 * Csak akkor támadhat, ha a ter�Eeten elegend? egysége van.
	 * Igazzal ter vissza, ha a tamadas lehetseges.
	 * @param attacker
	 * @param attackerUnits    attackerUnits
	 */
	public Boolean checkAttackPossible(Territory defender, Territory attacker, int defendUnits, int attackUnits){
		return (attacker.getOwner() == currentPlayer
				&& Map.IsNeighbour(attacker, defender)
				&& defender.getUnits() >= defendUnits
				&& attacker.getUnits() >= attackUnits+1
				&& attackUnits <= 3 && attackUnits >= 1
				&& defendUnits <= 2 && defendUnits >= 1);
	}

	/**
	 * 
	 * @param defender    defender
	 */
	public Boolean checkIfCapturedAndConquer(Territory defender){
		if(checkIfTerrotiryIsEmpty(defender)) {
			defender.setOwner(currentPlayer);
			
			return true;
		}
		return false;
	}
	
	public Boolean moveUnits(int units) throws Exception {
		if(phase != Phase.WaitForUnitCount) throw new Exception("Not in Capturing mode");
		if(units > waitForUnitsTemp[0].getUnits()-1) return false;
		waitForUnitsTemp[0].setUnits(waitForUnitsTemp[0].getUnits()-units);
		waitForUnitsTemp[1].setUnits(units);
		return true;
	}

	/**
	 * 
	 * @param territory
	 */
	public boolean checkIfTerrotiryIsEmpty(Territory territory){
		return territory.getUnits() == 0;
	}

	/**
	 * 
	 * @param player
	 * @param territory
	 */
	public boolean checkReinforcePossible(Player player, Territory territory){
		return false;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 */
	public boolean checkTransferPossible(Territory from, Territory to){
		return Map.IsNeighbour(from, to) && from.getOwner() == currentPlayer && to.getOwner() == currentPlayer;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param units
	 */
	public boolean checkTransferPossible(Territory from, Territory to, int units){
		return false;
	}

	/**
	 * Ez a f�Egvény fogja a kockadobásokkal történ? harcot szimulálja.
	 * 
	 * @param attackerUnits
	 * @param defenderUnits    defenderUnits
	 */
	//attackTerritory-ban lekodolva, elfer ott.
//	protected doBattle(int attackerUnits, int defenderUnits){
//
//	}

	/**
	 * Kor vege
	 * 
	 * Returns next player's id
	 */
	public int endTurn(){
		if(capturedThisTurn) {
			playersList.get(currentPlayer).putcard(deck.Draw());
		}
		
		currentPlayer = (currentPlayer + 1) % playersList.size();
		nextPlayer();
		return currentPlayer;
	}

	public Boolean loadGame(){
		return false;
	}

	/**
	 * Initializes the next Player's turn. Calculates the amount of Units to place.
	 */
	protected void nextPlayer(){
		int calculatedVal = playersList.get(currentPlayer).getTerritoryCount() / 3;
		if(calculatedVal < 3) calculatedVal = 3;
		unitsLeftToReinforce = calculatedVal + playersList.get(currentPlayer).getReinforcementBonus();
		phase = Phase.Reinforcement;
	}

	/**
	 * 
	 * @param List
	 */
//	public playerRiskCardPlacedPlace(List){
//
//	}

	/**
	 * 
	 * @param player
	 * @param territory
	 * @throws Exception 
	 */
	public Boolean reinforce(int territoryId) throws Exception{
		if(phase != Phase.Reinforcement) throw new Exception("Reinforce in non-reinforcement phase"); 
		Territory territory = map.getTerritory(territoryId);
		if(territory.getOwner() != currentPlayer)
			return false;
		territory.setUnits(territory.getUnits()+1);
		unitsLeftToReinforce--;
		return true;
	}

	/**
	 * 
	 * @param saveName
	 * @return 
	 */
	public void saveGame(String saveName){
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * 
	 * @param player
	 */
	public void selectStartingPlayer(Player player){
		currentPlayer = playersList.indexOf(player);
	}

//	/**
//	 * 
//	 * @param Name
//	 */
//	public void setPlayerName(String Name){
//		playersList.get(currentPlayer)
//	}
//
//	/**
//	 * 
//	 * @param number
//	 */
//	public setPlayerNumber(int number){
//		
//	}
	
	public Boolean addPlayer(String name, Color color ) {
		if(playersList.stream().anyMatch(x -> x.getColor() == color)) return false;
		playersList.add(new Player(color,name,deck));
		return true;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param units
	 * @throws Exception 
	 */
	public Boolean transfer(Territory from, Territory to, int units) throws Exception{
		if(phase != Phase.Transfer) throw new Exception("Not in Transfer Phase");
		if(from.getOwner() != currentPlayer 
				|| from.getUnits() < units+1
				|| to.getOwner() != currentPlayer
				|| !Map.IsNeighbour(from, to)) return false;
		from.setUnits(from.getUnits()-units);
		to.setUnits(to.getUnits()+units);
		return true;
	}

}