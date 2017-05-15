package View;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Ezen a View-en keresztul tortenik a leendo jatekosok hozzaadasa a jatekhoz.
 * @author fodorad
 * @version 1.0
 * @created 19-ápr.-2017 23:11:48
 */
public class JFXAddPlayerView extends JFXViewBase {

	@FXML private TextField tfPlayer1;
	@FXML private TextField tfPlayer2;
	@FXML private TextField tfPlayer3;
	@FXML private TextField tfPlayer4;
	@FXML private TextField tfPlayer5;
	@FXML private Button btnReady;
	
	private Stage stage;
	
	public JFXAddPlayerView(Stage stage){
		
		this.stage = stage;
		LoadFXML("/View/fxml/AddPlayerView.fxml");
		
	}
	
	@FXML protected void readyOnAction(ActionEvent event){
		controller.switchToMain();
	}
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
	}
	
	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * 
	 * @param name
	 */
	public void UpdateViewState(String name){

	}

}