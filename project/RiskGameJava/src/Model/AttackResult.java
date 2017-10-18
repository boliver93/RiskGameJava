package Model;

import java.util.List;

/**
 * A tamadas eredmenyet megado fuggveny.
 * 
 * @author Szabó Dávid
 * @version 1.0
 * @created 19-ápr.-2017 23:11:54
 */
public class AttackResult {

	private List<Integer> attackerDicesList;
	private int attackerSurvivedUnits;
	private List<Integer> defenderDicesList;
	private int defenderSurvivedUnits;

	public AttackResult(int attackerSurvivedUnits,int defenderSurvivedUnits, List<Integer> attackerDicesList, List<Integer> defenderDicesList) {
		this.attackerSurvivedUnits = attackerSurvivedUnits;
		this.defenderSurvivedUnits = defenderSurvivedUnits;
		this.attackerDicesList = attackerDicesList;
		this.defenderDicesList = defenderDicesList;
	}
	
	public List<Integer> getAttackerDicesList()
	{
		return attackerDicesList;
	}
	
	public List<Integer> getDefenderDicesList()
	{
		return defenderDicesList;
	}
}