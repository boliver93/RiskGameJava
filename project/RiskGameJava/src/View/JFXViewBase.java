package View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Controller.RiskGameController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

public abstract class JFXViewBase implements Initializable {

	protected Parent root;
	protected RiskGameController controller;
	
	public JFXViewBase() {}
	
	protected void LoadFXML(String resourceName)
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourceName));
		fxmlLoader.setController(this);
		
        try {
	        root = fxmlLoader.load();
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Parent getRoot() { return root; }
	
	/**
	 * 
	 * @param controller
	 */
	public void AddControllerListener(RiskGameController controller){
		this.controller = controller;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	
}
