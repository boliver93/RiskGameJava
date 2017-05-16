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

	public AttackResult(int survivedAtt,int survivedDef, List<Integer> aDiceList, List<Integer> bDiceList) {
		attackerDicesList = aDiceList;
		attackerSurvivedUnits = survivedAtt;
		defenderDicesList = bDiceList;
		defenderSurvivedUnits = survivedDef;
	}

	@Override
	public void finalize() throws Throwable {

	}

}