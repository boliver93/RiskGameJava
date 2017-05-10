package View;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import Model.Player;
import Model.Territory;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

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
	
	private World world;
    private GraphicsContext gc;
	
	public JFXMainView(){

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
		
		imageView.fitWidthProperty().bind(centerStackPane.widthProperty());
		imageView.fitHeightProperty().bind(centerStackPane.heightProperty());
		
		centerStackPane.getChildren().add(world);
		
		world.prefWidthProperty().bind(centerStackPane.widthProperty());
		world.prefHeightProperty().bind(centerStackPane.heightProperty());

		// imageViewL.fitHeightProperty().bind(centerStackPane.heightProperty());
		// imageViewR.fitHeightProperty().bind(centerStackPane.heightProperty());
	}
	
	public void finalize() throws Throwable {
		super.finalize();
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