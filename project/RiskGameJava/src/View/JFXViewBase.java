package View;

import java.io.IOException;

import Controller.RiskGameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class JFXViewBase {

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
	
}
