package View;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import Model.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
	private ArrayList<TextField> tfList;
	
	public JFXAddPlayerView(Stage stage){
		tfList = new ArrayList<TextField>();
		
		this.stage = stage;
		LoadFXML("/View/fxml/AddPlayerView.fxml");
		
	}
	
	@FXML protected void readyOnAction(ActionEvent event){
		
		ArrayList<Color> colorList = new ArrayList<>();
		Collections.addAll(colorList, Color.RED, Color.BLUE, Color.BROWN, Color.YELLOW, Color.GREEN);
		Map<Color, String> map = new LinkedHashMap<Color, String>();
		
		for (int i = 0; i < tfList.size(); i += 1){
			String playerName = tfList.get(i).getText();
			if (playerName.isEmpty()) 
				map.put(colorList.get(i),"Dummy" + (i+1)); 
			else 
				map.put(colorList.get(i), playerName);
		}
		
		controller.switchToMain(map);
	}
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		Collections.addAll(tfList, tfPlayer1, tfPlayer2, tfPlayer3, tfPlayer4, tfPlayer5);
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