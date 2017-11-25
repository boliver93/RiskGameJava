package View;

import java.awt.Dimension;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import Controller.RiskGameController;
import Model.Phase;
import Model.Territory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * Az MVC architektura View reszet megvalos�to osztalya. Ez a View a jatek
 * fokepernyoje, ezen tortenik maga a jatek (egysegek elhelyezese, csata stb.).
 * @author Szab� D�vid
 * @version 1.0
 * @created 19-�pr.-2017 23:11:48
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
	@FXML private Label lblPlayerName6;
	@FXML private Pane panePlayerColor6;
	@FXML private TextArea taLog; 

	@FXML private Button btnLog; 
	@FXML private Button btnReinforcePhase;
	@FXML private Button btnBattlePhase;
	@FXML private Button btnTransferPhase;
	@FXML private Button btnNextPlayer;
	@FXML private Button btnSettings;
	@FXML private Button btnSave;
	@FXML private Button btnLoad;
	
	
	private Stage stage;
	private World world;
    final private double BASE_HEIGHT = 600;
    final private double BASE_WIDTH = 815;
    private ArrayList<Label> lblPlayerNameList;
    private ArrayList<Pane> panePlayerColorList;
    private ArrayList<Button> btnControlList;
    boolean isLogOpened;
    boolean isSLOpened;
    Dimension dimLog;
	
	public JFXMainView(Stage stage){

		this.stage = stage;
		
		world = WorldBuilder.create()
        		.mousePressHandler(evt -> {
                	CountryPath countryPath = (CountryPath) evt.getSource();

                	controller.countrySelected(Country.valueOf(countryPath.getName()));
                })
        		.hoverEnabled(false)
        		.selectionEnabled(false)
                .build();
		
		LoadFXML("/View/fxml/MainView.fxml");
		isLogOpened = false;
		isSLOpened = false;
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
		
	    lblPlayerNameList = new ArrayList<>();
	    panePlayerColorList = new ArrayList<>();
	    btnControlList = new ArrayList<>();
	    
	    btnSave.setVisible(false);
	    btnLoad.setVisible(false);
	    
	    Collections.addAll(lblPlayerNameList, lblPlayerName1, lblPlayerName2, lblPlayerName3, lblPlayerName4, lblPlayerName5, lblPlayerName6);
	    Collections.addAll(panePlayerColorList, panePlayerColor1, panePlayerColor2, panePlayerColor3, panePlayerColor4, panePlayerColor5, panePlayerColor6);
	    Collections.addAll(btnControlList, btnBattlePhase, btnReinforcePhase, btnTransferPhase, btnNextPlayer, btnLog, btnSettings, btnSave, btnLoad);
	    
	    btnSave.setOnMouseClicked(evt -> {
	    	/*
	    	 * 	controller fele
	    	 */
	    	FileChooser fileChooser = new FileChooser();
	    	fileChooser.setTitle("Save game...");
	    	fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
	    	fileChooser.setInitialFileName("1.risksave");
	    	fileChooser.getExtensionFilters().add(new ExtensionFilter("Risksave Files", "*.risksave"));
	    	File file = fileChooser.showSaveDialog(stage);
	    	if (file != null) {
                controller.saveGame(file);
            }
	    });
	    
	    btnLoad.setOnMouseClicked(evt -> {
	    	/*
	    	 * 	controller fele
	    	 */
	            
	    	FileChooser fileChooser = new FileChooser();
	    	fileChooser.setTitle("Open save file...");
	    	fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
	    	fileChooser.getExtensionFilters().add(new ExtensionFilter("Risksave Files", "*.risksave"));
	    	File file = fileChooser.showOpenDialog(stage);
	    	if (file != null) {
	    		controller.loadGame(file);
            }
	    });
	    
	    btnTransferPhase.setOnMouseClicked(evt -> {
	    	controller.transferPhaseSelected();
	    });
	    
	    btnNextPlayer.setOnMouseClicked(evt -> {
	    	controller.nextPlayerSelected();
	    });
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
	public void UpdateConnectedPlayers(List<String> playerList){

		for (int i = 0; i < playerList.size(); i += 1)
			lblPlayerNameList.get(i).setText(playerList.get(i));
		
		for(int j=playerList.size();j<lblPlayerNameList.size();j++)
			lblPlayerNameList.get(j).setText("");
	}

	/**
	 * 
	 * @param player
	 */
	public void UpdateCurrentPlayer(int playerID){
		lblPlayerName1.setTextFill(Color.WHITE);
		lblPlayerName2.setTextFill(Color.WHITE);
		lblPlayerName3.setTextFill(Color.WHITE);
		lblPlayerName4.setTextFill(Color.WHITE);
		lblPlayerName5.setTextFill(Color.WHITE);
		
		switch (playerID) {
		case 0: lblPlayerName1.setTextFill(Color.RED); break;
		case 1: lblPlayerName2.setTextFill(Color.RED); break;
		case 2: lblPlayerName3.setTextFill(Color.RED); break;
		case 3: lblPlayerName4.setTextFill(Color.RED); break;
		case 4: lblPlayerName5.setTextFill(Color.RED); break;
		}
	}
	
	public void UpdateCurrentPhase(Phase phase) {
		btnReinforcePhase.setTextFill(Color.BLACK);
		btnBattlePhase.setTextFill(Color.BLACK);
		btnTransferPhase.setTextFill(Color.BLACK);
		
		switch (phase) {
		case Reinforcement:
			btnReinforcePhase.setTextFill(Color.RED);
			break;
		case Battle:
			btnBattlePhase.setTextFill(Color.RED);
			break;
		case Transfer:
			btnTransferPhase.setTextFill(Color.RED);
			break;

		default:
			break;
		}
	}
	
	@FXML
	 private void handleLogButton(ActionEvent event) {

		if (isLogOpened()) closeLog(); else openLog();
		
	}
	
	@FXML
	 private void hangleSettingsButton(ActionEvent event) {
		
		if (isSLOpened()) isSLOpened = false; else isSLOpened = true;
		
	    btnSave.setVisible(isSLOpened);
	    btnLoad.setVisible(isSLOpened);
	    
	}
	
	@FXML
	 private void hangleSaveButton(ActionEvent event) {
		// Initialize handles this
	}
	
	@FXML
	 private void hangleLoadButton(ActionEvent event) {
		// Initialize handles this
	}
	
	@Override
	public void AddControllerListener(RiskGameController controller) {
		super.AddControllerListener(controller);
		this.getWorld().setController(controller);
	}
	
	/**
	 * 
	 * @param territories
	 */
	public void UpdateViewState(Territory territory){
	}
	
	public void UpdateViewState(List<Territory> territories){
	}

	public void fit(double height, double width) {
		
		/*
		 * 	Resize the most painful shits to fit stage
		 */
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		world.setMaxHeight(height);
		world.setMaxWidth(width);
		world.updateUnitPosition();
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
		
		/*
		 * 	Control buttons
		 */
		for (Button button : btnControlList) {

			button.setPrefHeight(button.getPrefHeight() * ratioY);
			button.setPrefWidth(button.getPrefWidth()* ratioX);
			button.setLayoutX(button.getLayoutX()* ratioX);
			button.setLayoutY(button.getLayoutY()* ratioY);
			
		}

	}

	public void updateIcons(int attacker, int defender) {
		world.updateIcons(attacker, defender);
	}
	
	public void appendLog(String s){
		taLog.appendText("\n");
		taLog.appendText(s);
	}
	
	public boolean isLogOpened() {
		return isLogOpened;
	}

	public boolean isSLOpened(){
		return isSLOpened;
	}
	
	public void closeLog() {
		taLog.setPrefHeight(dimLog.getHeight());
		taLog.setLayoutY(taLog.getLayoutY()+stage.getHeight()*0.6);
		isLogOpened = false;
	}

	public void openLog() {
		taLog.setPrefHeight(stage.getHeight()*0.6);
		taLog.setLayoutY(taLog.getLayoutY()-stage.getHeight()*0.6);
		isLogOpened = true;
	}

}