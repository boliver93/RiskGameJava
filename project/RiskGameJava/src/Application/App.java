package Application;

import Controller.RiskGameController;
import Model.RiskGameModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

	@Override
    public void start(Stage primaryStage) throws Exception {
    	
    	RiskGameModel model = new RiskGameModel();
    	RiskGameController controller = new RiskGameController(primaryStage);

    	controller.setModel(model);
    	controller.showMainView();
    }

    public static void main(String[] args) {
        launch(args);
    }
	
}
