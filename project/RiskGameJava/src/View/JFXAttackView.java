package View;

import Model.AttackResult;

/**
 * Ezen a View-en keresztul jelolheti meg az egyik jatekos a masik jatekos
 * (ellenfel) azon teruletet, amelyet tamadni szeretne.
 * @author Szabó Dávid
 * @version 1.0
 * @created 19-ápr.-2017 23:11:48
 */
public class JFXAttackView extends JFXViewBase {

	public JFXAttackView(){
		LoadFXML("AttackView.fxml");
	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * 
	 * @param attackResult
	 */
	public void UpdateViewState(AttackResult attackResult){

	}

}