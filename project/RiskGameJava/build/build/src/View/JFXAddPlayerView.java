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
import javafx.scene.control.Label;
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
	@FXML private TextField tfPlayer6;
	@FXML private Label lblPlayer4;
	@FXML private Label lblPlayer5;
	@FXML private Label lblPlayer6;
	@FXML private Button btnReady;
	@FXML private Button btnAddPlayer;
	@FXML private Button btnDeletePlayer4;
	@FXML private Button btnDeletePlayer5;
	@FXML private Button btnDeletePlayer6;
	
	Stage stage;
	private ArrayList<TextField> tfList;
	
	public JFXAddPlayerView(Stage stage){
		tfList = new ArrayList<TextField>();
		
		this.stage = stage;
		//LoadFXML("/View/fxml/AddPlayerView.fxml");
		LoadFXML("/View/fxml/AddPlayerView.fxml");
	}
	
	@FXML protected void readyOnAction(ActionEvent event){
		
		ArrayList<Color> colorList = new ArrayList<>();
		Collections.addAll(colorList, Color.RED, Color.BLUE, Color.BROWN, Color.YELLOW, Color.GREEN, Color.ORANGE);
		Map<Color, String> map = new LinkedHashMap<Color, String>();
		
		for (int i = 0; i < tfList.size(); i += 1){
			String playerName = tfList.get(i).getText();
			if (!playerName.isEmpty())
				map.put(colorList.get(i), playerName);
			if(playerName.isEmpty() && tfList.get(i).isVisible())
				map.put(colorList.get(i),"Dummy" + (i+1));
		}
		
		controller.switchToMain(map);
	}
	
	@FXML protected void removePlayer4Action(ActionEvent event)
	{
		lblPlayer4.setVisible(false);
		tfPlayer4.setVisible(false);
		btnDeletePlayer4.setVisible(false);
	}
	
	@FXML protected void removePlayer5Action(ActionEvent event)
	{
		lblPlayer5.setVisible(false);
		tfPlayer5.setVisible(false);
		btnDeletePlayer5.setVisible(false);
		btnDeletePlayer4.setVisible(true);
	}
	
	@FXML protected void removePlayer6Action(ActionEvent event)
	{
		lblPlayer6.setVisible(false);
		tfPlayer6.setVisible(false);
		btnDeletePlayer6.setVisible(false);
		btnDeletePlayer5.setVisible(true);
	}
	
	@FXML protected void addPlayerAction(ActionEvent event)
	{
		if(!tfPlayer4.isVisible())
		{
			lblPlayer4.setVisible(true);
			tfPlayer4.setVisible(true);
			btnDeletePlayer4.setVisible(true);
			
		}
		else if (!tfPlayer5.isVisible())
		{
			btnDeletePlayer4.setVisible(false);
			lblPlayer5.setVisible(true);
			tfPlayer5.setVisible(true);
			btnDeletePlayer5.setVisible(true);
		}
		else if (!tfPlayer6.isVisible())
		{
			btnDeletePlayer5.setVisible(false);
			lblPlayer6.setVisible(true);
			tfPlayer6.setVisible(true);
			btnDeletePlayer6.setVisible(true);
		}
	}
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		Collections.addAll(tfList, tfPlayer1, tfPlayer2, tfPlayer3, tfPlayer4, tfPlayer5, tfPlayer6);
		tfPlayer1.setMaxWidth(140);
		tfPlayer2.setMaxWidth(140);
		tfPlayer3.setMaxWidth(140);
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