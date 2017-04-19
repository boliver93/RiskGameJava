package View;

import Model.AttackResult;

/**
 * Ezen a View-en keresztul jelolheti meg az egyik jatekos a masik jatekos
 * (ellenfel) azon teruletet, amelyet tamadni szeretne.
 * @author Szab� D�vid
 * @version 1.0
 * @created 19-�pr.-2017 23:11:49
 */
public abstract class RiskAttackView {

	public RiskAttackView(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param controller
	 */
	public abstract AddControllerListener(ActionListener controller);

	/**
	 * 
	 * @param attackResult
	 */
	public abstract UpdateViewState(AttackResult attackResult);

}