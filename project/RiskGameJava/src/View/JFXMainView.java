package View;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import Controller.RiskGameController;
import Model.Player;
import Model.Territory;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Az MVC architektura View reszet megvalosíto osztalya. Ez a View a jatek
 * fokepernyoje, ezen tortenik maga a jatek (egysegek elhelyezese, csata stb.).
 * @author Szabó Dávid
 * @version 1.0
 * @created 19-ápr.-2017 23:11:48
 */
public class JFXMainView extends JFXViewBase {
	
	@FXML private ImageView imageView;
	@FXML private ImageView imageViewL;
	@FXML private ImageView imageViewR;
	@FXML private BorderPane borderPane;
	@FXML private StackPane centerStackPane;
	
	private Stage stage;
	private World world;
    private GraphicsContext gc;
	
	public JFXMainView(Stage stage){

		this.stage = stage;
		
		world = WorldBuilder.create()
        		.mousePressHandler(evt -> {
                	CountryPath countryPath = (CountryPath) evt.getSource();
                	//System.out.println(countryPath.getName());
                	//System.out.println(countryPath.getContent());
                	controller.countrySelected(Country.valueOf(countryPath.getName()));
                })
        		.hoverEnabled(true)
        		.selectionEnabled(true)
                .build();
		
		LoadFXML("/View/res/MainView.fxml");
		
	}
	
	@Override
	public Parent getRoot(){
		return root;
	}
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		
		centerStackPane.getStyleClass().add("root");
		centerStackPane.setAlignment(Pos.CENTER);
		centerStackPane.getChildren().addAll(world);
		
		imageView.setCache(true);
		imageView.fitWidthProperty().bind(stage.widthProperty());
		imageView.fitHeightProperty().bind(stage.heightProperty());
		
		world.maxWidthProperty().bind(stage.widthProperty());
		world.maxHeightProperty().bind(stage.heightProperty());
		
	}
	
	public void finalize() throws Throwable {
		super.finalize();
	}
	
	public World getWorld() {
		return world;
	}

	/**
	 * 
	 * @param playerList
	 */
	public void UpdateConnectedPlayer(List<Player> playerList){

	}

	/**
	 * 
	 * @param player
	 */
	public void UpdateStartingPlayer(Player player){

	}

	/**
	 * 
	 * @param territories
	 */
	public void UpdateViewState(Territory territories){

	}

}