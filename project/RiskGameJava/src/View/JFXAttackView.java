package View;

import java.net.URL;
import java.util.ResourceBundle;

import Model.AttackResult;
import Model.Territory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

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
	
	@FXML private Button btnAttack;
	@FXML private Button btnRetreat;
	@FXML private ProgressBar barUnits;
	
	private int defenderID;
	private int attackerID;
	
	private int attackerUnits;
	private int defenderUnits;

	public JFXAttackView(){
		LoadFXML("/View/fxml/AttackView.fxml");
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	@Override
    public void initialize(URL location, ResourceBundle resources) {
		btnAttack.setOnMouseClicked(evt -> {
			//TODO: slider? 
			controller.attackAccepted(defenderID, attackerID, defenderUnits, attackerUnits);
		});
		
		btnRetreat.setOnMouseClicked(evt -> {
			controller.attackRetreat();
		});
	}
	
	/**
	 * 
	 * @param attackResult
	 */
	public void UpdateViewState(AttackResult attackResult){

	}
	
	public void UpdateViewState(String defenderName, String attackerName, Territory defender, Territory attacker) {
		lblCountryDefender.setText(Country.values()[defender.getId()].toString());
		lblCountryAttacker.setText(Country.values()[attacker.getId()].toString());
		defenderID = defender.getId();
		attackerID = attacker.getId();
		
		lblDefenderName.setText(defenderName);
		lblAttackerName.setText(attackerName);
		
		defenderUnits = defender.getUnits();
		attackerUnits = attacker.getUnits();
		
		lblDefenderAlive.setText(Integer.toString(defenderUnits));
		lblAttackerAlive.setText(Integer.toString(attackerUnits));
	}

}