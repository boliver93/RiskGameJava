package View;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
/**
 * 
 * @author Oliver
 *
 */
public class JFXAttackResultView extends JFXViewBase {

	@FXML
	private Label lblPlayerName1;
	
	@FXML
	private Label lblPlayerName2;

	@FXML
	private Label lblAttackRolls1;

	@FXML
	private Label lblAttackRolls2;

	@FXML
	private Label lblAttackRolls3;

	@FXML
	private Label lblDefendRolls1;

	@FXML
	private Label lblDefendRolls2;

	@FXML
	private Label lblDefendRolls3;

	@FXML
	private Button btnExit;
	
	private int attacker;
	private int defender;

	public JFXAttackResultView() {
		LoadFXML("/View/fxml/AttackResultView.fxml");
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnExit.setOnMouseClicked(evt -> {
			//controller exit
			controller.attackShowed(attacker,defender);
		});
	}

	/**
	 * 
	 * @param player1
	 * @param player2
	 * @param attackRolls1
	 * @param attackRolls2
	 * @param attackRolls3
	 * @param defendRolls1
	 * @param defendRolls2
	 * @param defendRolls3
	 */
	public void UpdateViewState(int attacker,int defender,String player1, String player2, int attackRolls1, int attackRolls2, int attackRolls3, int defendRolls1, int defendRolls2){
		this.attacker=attacker;
		this.defender=defender;
		
		lblPlayerName1.setText(player1);
		lblPlayerName2.setText(player2);
		
		lblAttackRolls1.setText(Integer.toString(attackRolls1));
		lblAttackRolls2.setText(Integer.toString(attackRolls2));
		lblAttackRolls3.setText(Integer.toString(attackRolls3));
		
		lblDefendRolls1.setText(Integer.toString(defendRolls1));
		if(defendRolls2 == -1) lblDefendRolls2.setText("None");
		else lblDefendRolls2.setText(Integer.toString(defendRolls2));
	}
}