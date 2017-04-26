package Model;

import java.util.List;

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
	/**
	 * The amount of Units left to Place in the Reinforcement Stage of the current
	 * Player's current turn.
	 */
	private int unitsLeftToReinforce;

	public RiskGameModel(){

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
	 */
	public void addPlayerToPlayerList(Player player){

	}

	/**
	 * 
	 * @param defender
	 * @param attacker
	 * @param defendUnits
	 * @param attackUnits    attackUnits
	 */
	public attackTerritory(Territory defender, Territory attacker, int defendUnits, int attackUnits){

	}

	/**
	 * Csak akkor támadhat, ha a ter�Eeten elegend? egysége van.
	 * 
	 * @param attacker
	 * @param attackerUnits    attackerUnits
	 */
	public int checkAttackPossible(Territory attacker, int attackerUnits){
		return 0;
	}

	/**
	 * 
	 * @param defender    defender
	 */
	public int checkIfCapturedAndConquer(Territory defender){
		return 0;
	}

	/**
	 * 
	 * @param territory
	 */
	public boolean checkIfTerrotiryIsEmpty(Territory territory){
		return false;
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
		return false;
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
	protected doBattle(int attackerUnits, int defenderUnits){

	}

	public endTurn(){

	}

	public loadGame(){

	}

	/**
	 * Initializes the next Player's turn. Calculates the amount of Units to place.
	 */
	protected nextPlayer(){

	}

	/**
	 * 
	 * @param List
	 */
	public playerRiskCardPlacedPlace(List){

	}

	/**
	 * 
	 * @param player
	 * @param territory
	 */
	public reinforce(Player player, Territory territory){

	}

	/**
	 * 
	 * @param saveName
	 */
	public saveGame(String saveName){

	}

	/**
	 * 
	 * @param player
	 */
	public selectStartingPlayer(Player player){

	}

	/**
	 * 
	 * @param Name
	 */
	public setPlayerName(String Name){

	}

	/**
	 * 
	 * @param number
	 */
	public setPlayerNumber(int number){

	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param units
	 */
	public transfer(Territory from, Territory to, int units){

	}

}