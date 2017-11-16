package View;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class JFXVictoryView extends JFXViewBase {

	@FXML
	private BorderPane borderPane;
	private String backgroundPath;
	
	public JFXVictoryView()
	{
		backgroundPath = System.getProperty("user.dir") + "\\src\\View\\img\\victory.jpg";
		//borderPane.getCenter().setStyle("-fx-background-image: backgroundPath;-fx-background-size: 500, 500;-fx-background-repeat: no-repeat;");
		LoadFXML("/View/fxml/VictoryView.fxml"); 
	
	    BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
		borderPane.setBackground(new Background(new BackgroundImage(new Image(new File(backgroundPath).toURI().toString()),
		            BackgroundRepeat.NO_REPEAT,
		            BackgroundRepeat.NO_REPEAT,
		            BackgroundPosition.CENTER,
		            bSize)));	    
	}

	@Override
	public Parent getRoot() {
		return root;
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}
}