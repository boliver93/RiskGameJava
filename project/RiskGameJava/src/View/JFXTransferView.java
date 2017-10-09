package View;

import java.net.URL;
import java.util.ResourceBundle;

import Model.Territory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

/**
 * Ezen a View-en valaszthatja ki a jatekos, hogy a sajat teruleten belul hova
 * helyezi at az egysegeit.
 * @author Szabó Dávid
 * @version 1.0
 * @created 19-ápr.-2017 23:11:49
 */
public class JFXTransferView extends JFXViewBase {
	
	@FXML private Label lblPlayerName;
	@FXML private Label lblFromCountryName;
	@FXML private Label lblFromQuantity;
	@FXML private Label lblToCountryName;
	@FXML private Label lblToQuantity;
	
	@FXML private Slider sldSoilderQuantity;
	@FXML private Button btnAccept;
	
	private int fromID;
	private int toID;

	public JFXTransferView(){
		LoadFXML("/View/fxml/TransferView.fxml");
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		
		btnAccept.setOnMouseClicked(evt -> {
			Double amount = sldSoilderQuantity.getValue();
			
			controller.transferAccepted(fromID, toID, amount.intValue());
		});
		
	}

	/**
	 * 
	 * @param from
	 * @param to
	 */
	public void UpdateViewState(String player, Territory from, Territory to){
		lblPlayerName.setText(player);
		
		lblFromCountryName.setText(Country.values()[from.getId()].toString());
		lblToCountryName.setText(Country.values()[to.getId()].toString());
		fromID = from.getId();
		toID = to.getId();
		
		lblFromQuantity.setText(Integer.toString(from.getUnits()));
		lblToQuantity.setText(Integer.toString(to.getUnits()));
		
		sldSoilderQuantity.setMax(from.getUnits() - 1);
	}

}