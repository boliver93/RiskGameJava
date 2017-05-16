package View;

import Model.AttackResult;
import Model.Territory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Ezen a View-en keresztul jelolheti meg az egyik jatekos a masik jatekos
 * (ellenfel) azon teruletet, amelyet tamadni szeretne.
 * @author Szabó Dávid
 * @version 1.0
 * @created 19-ápr.-2017 23:11:48
 */
public class JFXAttackView extends JFXViewBase {
	
	@FXML private Label lblCountryAttacker;
	@FXML private Label lblCountryDefender;
	@FXML private Label lblAttackerName;
	@FXML private Label lblDefenderName;
	@FXML private Label lblAttackerAlive;
	@FXML private Label lblDefenderAlive;

	public JFXAttackView(){
		LoadFXML("/View/fxml/AttackView.fxml");
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * 
	 * @param attackResult
	 */
	public void UpdateViewState(AttackResult attackResult){

	}
	
	public void UpdateViewState(Territory defender, Territory attacker) {
		lblCountryDefender.setText(Country.values()[defender.getId()].toString());
		lblCountryAttacker.setText(Country.values()[attacker.getId()].toString());
		
		//lblDefenderName.setText(defender.getOwner());
		//lblAttackerName.setText(attacker.getOwner());
		
		lblDefenderAlive.setText(Integer.toString(defender.getUnits()));
		lblAttackerAlive.setText(Integer.toString(attacker.getUnits()));
	}

}