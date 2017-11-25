package View;

import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class JFXVictoryView extends JFXViewBase {

	@FXML
	private BorderPane borderPane;
	private URL backgroundPath;

	public JFXVictoryView() {
		backgroundPath = JFXVictoryView.class.getResource("/View/img/victory.jpg");

		LoadFXML("/View/fxml/VictoryView.fxml");

		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);

		borderPane.setBackground(new Background(new BackgroundImage(new Image(backgroundPath.toString()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize)));
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