package View;

import java.awt.event.ActionListener;

import Model.AttackResult;

/**
 * Ezen a View-en keresztul jelolheti meg az egyik jatekos a masik jatekos
 * (ellenfel) azon teruletet, amelyet tamadni szeretne.
 * @author Szabó Dávid
 * @version 1.0
 * @created 19-ápr.-2017 23:11:49
 */
public abstract class RiskAttackView {

	public RiskAttackView(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param controller
	 * @return 
	 */
	public abstract void AddControllerListener(ActionListener controller);

	/**
	 * 
	 * @param attackResult
	 * @return 
	 */
	public abstract void UpdateViewState(AttackResult attackResult);

}