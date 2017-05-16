package View;

import java.awt.Dimension;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import Model.Player;
import Model.Territory;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
	@FXML private BorderPane borderPane;
	@FXML private StackPane centerStackPane;
	@FXML private Pane pain;
	@FXML private Label lblPlayerName1;
	@FXML private Pane panePlayerColor1;
	@FXML private Label lblPlayerName2;
	@FXML private Pane panePlayerColor2;
	@FXML private Label lblPlayerName3;
	@FXML private Pane panePlayerColor3;
	@FXML private Label lblPlayerName4;
	@FXML private Pane panePlayerColor4;
	@FXML private Label lblPlayerName5;
	@FXML private Pane panePlayerColor5;
	@FXML private TextArea taLog; 
	
	
	private Stage stage;
	private World world;
    final private double BASE_HEIGHT = 600;
    final private double BASE_WIDTH = 815;
    private ArrayList<Label> lblPlayerNameList;
    private ArrayList<Pane> panePlayerColorList;
    boolean isLogOpened;
    Dimension dimLog;
	
	public JFXMainView(Stage stage){

		this.stage = stage;
		
		world = WorldBuilder.create()
        		.mousePressHandler(evt -> {
                	CountryPath countryPath = (CountryPath) evt.getSource();
                	//System.out.println(countryPath.getName());
                	//System.out.println(countryPath.getContent());
                	
                	/*
                	 * 	neighborhood file generation
                	 */
            		/*
                	final EventType<? extends MouseEvent> eventType = evt.getEventType();
                	if (MOUSE_PRESSED == eventType) {
                		
        				String fileName = "neighborhood.properties";
                		if (evt.isPrimaryButtonDown()){
                			try {
                				String msg = "" + Country.valueOf(countryPath.getName()).ordinal() + " ";
                				java.nio.file.Files.write(java.nio.file.Paths.get(fileName), msg.getBytes(), java.nio.file.StandardOpenOption.APPEND);
                			    System.out.println("add");
                			} catch (Exception e) {
                				System.out.println("add error");
                			}
                		} else if (evt.isSecondaryButtonDown()) {
                			try {
                				String msg = "\n";
                				java.nio.file.Files.write(java.nio.file.Paths.get(fileName), msg.getBytes(), java.nio.file.StandardOpenOption.APPEND);
                			    System.out.println("new entry");
                			} catch (Exception e) {
                				System.out.println("new entry error");
                			}
                		} else if (evt.isMiddleButtonDown()) {
                			try {
                				String msg = "fuck";
                				java.nio.file.Files.write(java.nio.file.Paths.get(fileName), msg.getBytes(), java.nio.file.StandardOpenOption.APPEND);
                			    System.out.println("io");
                			} catch (Exception e) {
                				System.out.println("fuck error");
                			}
                		}
                	}
                	*/
                	controller.countrySelected(Country.valueOf(countryPath.getName()));
                })
        		.hoverEnabled(true)
        		.selectionEnabled(true)
                .build();
		
		LoadFXML("/View/fxml/MainView.fxml");
		isLogOpened = false;
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
		
		/*
		imageView.setCache(true);
		imageView.fitWidthProperty().bind(stage.widthProperty());
		imageView.fitHeightProperty().bind(stage.heightProperty());
		*/
		
		// world.maxWidthProperty().bind(stage.widthProperty());
		// world.maxHeightProperty().bind(stage.heightProperty());
		
	    lblPlayerNameList = new ArrayList<>();
	    panePlayerColorList = new ArrayList<>();
	    
	    Collections.addAll(lblPlayerNameList, lblPlayerName1, lblPlayerName2, lblPlayerName3, lblPlayerName4, lblPlayerName5);
	    Collections.addAll(panePlayerColorList, panePlayerColor1, panePlayerColor2, panePlayerColor3, panePlayerColor4, panePlayerColor5);
	    
	}
	
	@Override
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
	public void UpdateConnectedPlayer(List<String> playerList){

		for (int i = 0; i < playerList.size(); i += 1) {
			lblPlayerNameList.get(i).setText(playerList.get(i));
		}
		
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

	public void fit(double height, double width) {
		
		/*
		 * 	Resize the most painful shits to fit stage
		 */
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		world.setMaxHeight(height);
		world.setMaxWidth(width);
		pain.setPrefHeight(height);
		pain.setPrefWidth(width);

		/*
		 * 	Base ratio
		 */
		double ratioX = width / BASE_WIDTH;
		double ratioY = height / BASE_HEIGHT;

		/*
		 * 	Resize player colors
		 */
		for (Pane pane : panePlayerColorList) {

			double newRatioX = pane.getLayoutX() / BASE_WIDTH;
			double newRatioY = pane.getLayoutY() / BASE_HEIGHT;
			
			pane.setLayoutX(width * newRatioX);
			pane.setLayoutY(height * newRatioY);
			pane.setPrefHeight(pane.getPrefHeight()*ratioY);
			pane.setPrefWidth(pane.getPrefWidth()*ratioX);
			
		}

		/*
		 * 	Resize player names
		 */
		for (Label label : lblPlayerNameList) {

			label.setPrefHeight(label.getPrefHeight() * ratioY);
			label.setPrefWidth(label.getPrefWidth()* ratioX);
			label.setLayoutX(label.getLayoutX()* ratioX);
			label.setLayoutY(label.getLayoutY()* ratioY);
			
		}

		/*
		 * 	Resize Log TextArea and button
		 */
		taLog.setLayoutX(width * (taLog.getLayoutX() / BASE_WIDTH));
		taLog.setLayoutY(height * (taLog.getLayoutY() / BASE_HEIGHT));
		taLog.setPrefWidth(taLog.getPrefWidth()* ratioX);
		taLog.setPrefHeight(taLog.getPrefHeight()* ratioY);
		dimLog = new Dimension((int) taLog.getPrefWidth(), (int) taLog.getPrefHeight());
	}

	public void appendLog(String s){
		taLog.appendText("\n");
		taLog.appendText(s);
	}
	
	public boolean isLogOpened() {
		return isLogOpened;
	}

	public void closeLog() {
		taLog.setPrefHeight(dimLog.getHeight());
		isLogOpened = false;
	}

	public void openLog() {
		taLog.setPrefHeight(stage.getHeight()*0.6);
		isLogOpened = true;
	}

}