package View;

import java.net.URL;
import java.util.ResourceBundle;

import Model.Territory;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.AmbientLight;
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
			int from = Integer.parseInt(lblFromCountryName.getText());
			int to = Integer.parseInt(lblToCountryName.getText());
			Double amount = sldSoilderQuantity.getValue();
			
			controller.transferAccepted(from, to, amount.intValue());
		});
		
	}

	/**
	 * 
	 * @param from
	 * @param to
	 */
	public void UpdateViewState(Territory from, Territory to){
		//lblPlayerName.setText(from.getOwner());
		
		lblFromCountryName.setText(Country.values()[from.getId()].toString());
		lblToCountryName.setText(Country.values()[to.getId()].toString());
		
		lblFromQuantity.setText(Integer.toString(from.getUnits()));
		lblToQuantity.setText(Integer.toString(to.getUnits()));
	}

}