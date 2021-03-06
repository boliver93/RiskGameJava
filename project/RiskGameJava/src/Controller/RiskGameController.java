package Controller;

import View.JFXMainView;
import View.Country;
import View.JFXAddPlayerView;
import View.JFXAttackResultView;
import View.JFXAttackView;
import View.JFXCardView;
//import View.JFXRiskCardView;
import View.JFXTransferView;
import View.JFXVictoryView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Model.AttackResult;
import Model.Color;
import Model.Phase;
import Model.RiskGameModel;
import Model.Territory;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Az MVC architektura Controller reszet megvalos�to osztalya. A Controller a
 * Modell �s a View kozotti kapcsolatert felelos. Rajta keresztul hajtodnak
 * vegre a View-on torteno valtozasok, melyek a Model-t erintik, illetve a Model
 * a Controller-en keresztul modositja a View-t.
 * 
 * @author Szab� D�vid
 * @version 1.0
 * @created 19-�pr.-2017 23:11:42
 */
public class RiskGameController extends java.util.Observable {

	/*
	 * Stages
	 */
	private final Stage preStage;
	private Stage primaryStage;
	private Stage popupStage;
	private Stage titledPaneStage;
	private Stage victoryStage;

	/*
	 * View objects
	 */
	private JFXMainView mainView;
	private JFXAddPlayerView addPlayerView;
	private JFXAttackView attackView;
	// private JFXRiskCardView cardView;
	private JFXTransferView transferView;
	private JFXAttackResultView attackResultView;

	
	/*
	 * CardView stuffs
	 */
    private JFXCardView cardView;
    private Group cardGroup;
    private Scene cardScene;
    
    /*
     * VictoryView stuffs
     */
	private JFXVictoryView victoryView;

	private RiskGameModel model;
	private int previouslySelectedTerritory = -1;
	private Phase lastPhaseUpdate;
	private int lastPlayerUpdate;

	public RiskGameController(Stage stage) {
		this.preStage = stage;
		this.primaryStage = new Stage();
		this.popupStage = new Stage();
        this.victoryStage = new Stage();
		
		this.titledPaneStage = new Stage();
        this.titledPaneStage.setTitle("Cards");     

		stage.setResizable(false);
		primaryStage.setTitle("Risk");
		primaryStage.setResizable(false);
		primaryStage.xProperty().addListener(evt -> moveCardView());
		primaryStage.yProperty().addListener(evt -> moveCardView());
		primaryStage.setOnHiding(evt -> { if (titledPaneStage != null) titledPaneStage.setIconified(true); primaryStage.requestFocus(); });
		primaryStage.setOnShowing(evt -> { if (titledPaneStage != null) titledPaneStage.setIconified(false); primaryStage.requestFocus(); });
	
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setOnCloseRequest(evt -> {
			// Dont't let th User close the Transfer window when waiting for Unit Count
			if (model.getPhase() == Phase.WaitForUnitCount)
				evt.consume();
		});

	}
	
	private boolean isVictory()
	{
		if(lastPhaseUpdate == Phase.GameOver) return true;
		return false;
	}

	/**
	 * Elt�rolja a modellt �s feliratkoztatja mag�t az esem�nyeire
	 * 
	 * @param model
	 */
	public void setModel(RiskGameModel model) {
		this.model = model;
		lastPhaseUpdate = model.getPhase();
		lastPlayerUpdate = model.getCurrentPlayer();
	}

	public Phase getPhase() {
		return model.getPhase();
	}

	public int getPreviouslySelectedTerritory() {
		return  previouslySelectedTerritory;
	}
	
	public void countrySelected(Country country) {
		int territoryID = convertToTerritoryID(country);
		mainView.updateIcons(-1, -1);

		try {
			switch (model.getPhase()) {
			case Reinforcement:
				reinforce(territoryID, country.getName());
				break;

			case Battle:
				if (previouslySelectedTerritory != -1) {
					if (model.checkAttackPossible(territoryID, previouslySelectedTerritory)) {
						addLog("Attacking " + country.getName() + "!");
						mainView.updateIcons(previouslySelectedTerritory, territoryID);
						showAttackView(territoryID, previouslySelectedTerritory);
					} else {
						addLog(country.getName() + " cannot be attacked!");
						mainView.updateIcons(-1, -1);
					}
					previouslySelectedTerritory = -1;
				} else {
					addLog("Attacking from " + country.getName() + "...");
					previouslySelectedTerritory = territoryID;
					mainView.updateIcons(previouslySelectedTerritory, -1);
				}
				break;

			case Transfer:
				if (previouslySelectedTerritory != -1) {
					if (model.checkTransferPossible(previouslySelectedTerritory, territoryID)) {
						addLog("Transferring units to " + country.getName() + "!");
						showTransferView(previouslySelectedTerritory, territoryID);
					} else
						addLog("Can't transfer units to " + country.getName() + "!");
					previouslySelectedTerritory = -1;
				} else
					previouslySelectedTerritory = territoryID;
				break;

			case Preparation:
				reinforce(territoryID, country.getName());
				break;

			default:
				break;
			}
		} catch (Exception e) {
			addLog(e.getMessage());
		}

		UpdateCurrentPlayer();
	}

	private void reinforce(int territoryID, String country) throws Exception {
		if (model.reinforce(territoryID)) {
			Territory territory = model.getTerritory(territoryID);
			addLog(country + " reinforced by " + model.getPlayerName(territory.getOwner()) + "! Units guarding: "
					+ territory.getUnits() + "\nUnits left to reinforce: "
					+ model.getUnitsLeftToReinforce(territory.getOwner()));
			UpdateTerritoryOnMap(territoryID);
		} else
			addLog(country + " is occupied by another player!");
	}

	public void transferPhaseSelected() {
		closePopupWindow();
		previouslySelectedTerritory = -1;

		try {
			model.endBattlePhase();
			UpdateCurrentPlayer();
		} catch (Exception e) {
			addLog(e.getMessage());
		}
	}

	public void nextPlayerSelected() {
		closePopupWindow();
		previouslySelectedTerritory = -1;

		try {
			model.endTurn();
			UpdateCurrentPlayer();
			UpdateCurrentDeck();
		} catch (Exception e) {
			addLog(e.getMessage());
		}
	}
	
	public void showVictoryView()
	{
		victoryView = new JFXVictoryView();
		victoryView.AddControllerListener(this);
		Parent root = victoryView.getRoot();

		Scene victoryScene = new Scene(root, 500,185);
	    victoryStage.initStyle(StageStyle.UNDECORATED);
		victoryStage.setScene(victoryScene);
	    victoryStage.show();
	}

	/*
	 * Main View
	 */
	public void showMainView() {
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double ratio = 815.0 / 600;
		final double bound = 0.9;
		final double height = Math.ceil(screenBounds.getHeight() * bound);
		final double width = Math.ceil(height * ratio);

		mainView = new JFXMainView(primaryStage);
		mainView.AddControllerListener(this);
		mainView.fit(height, width);

		Parent root = mainView.getRoot();
		Scene mainScene = new Scene(root, width, height);

		mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(final KeyEvent keyEvent) {

				if (keyEvent.getCode() == KeyCode.SPACE) {

					System.out.println("space hit");
					if (mainView.isLogOpened()) {
						mainView.closeLog();
					} else {
						mainView.openLog();
					}
					keyEvent.consume();
				}
			}
		});

		primaryStage.setScene(mainScene);
	    primaryStage.setOnCloseRequest(e -> Platform.exit());
		primaryStage.show();
		
		showCardView();
	}
	
	public void showCardView() {
		List<Integer> cards = model.getPlayerCards((model.getCurrentPlayer()));

		cardView = new View.JFXCardView();
		cardView.AddControllerListener(this);
		
		Accordion accordion = cardView.UpdateCurrentDeck(cards);
		
	    cardScene = new Scene(new Group(), 202,360) ;
	    cardGroup = (Group)cardScene.getRoot();
	    cardGroup.getChildren().add(accordion);
	    titledPaneStage.setScene(cardScene);
	    titledPaneStage.initStyle(StageStyle.UNDECORATED);

	    moveCardView();
	    titledPaneStage.show();
	}
	
	public void moveCardView() {
		if (titledPaneStage != null && primaryStage != null) {
		    titledPaneStage.setX(primaryStage.getX()-200);
		    titledPaneStage.setY(primaryStage.getY()+primaryStage.getHeight()/4);
		}
	}

	/*
	 * Attack View
	 */
	private void showAttackView(int defenderID, int attackerID) {
		attackView = new View.JFXAttackView();
		attackView.AddControllerListener(this);
		Parent root = attackView.getRoot();
		Scene attackScene = new Scene(root);
		popupStage.setScene(attackScene);
		popupStage.show();

		Territory defender = model.getTerritory(defenderID);
		Territory attacker = model.getTerritory(attackerID);
		String defenderPlayer = model.getPlayerName(defender.getOwner());
		String attackerPlayer = model.getPlayerName(attacker.getOwner());
		attackView.UpdateViewState(defenderPlayer, attackerPlayer, defender, attacker);
	}

	public void attackAccepted(int defender, int attacker, int defenderUnits, int attackerUnits) {
		Country defenderCountry = convertToCountry(defender);
		Country attackerCountry = convertToCountry(attacker);

		try {
			AttackResult attackResult = model.attackTerritoryWithResult(defender, attacker, defenderUnits,
					attackerUnits);
			addLog(attackerCountry.getName() + " attacked " + defenderCountry.getName() + "!");

			Territory defenderTerritory = model.getTerritory(defender);
			Territory attackerTerritory = model.getTerritory(attacker);
			String defenderPlayer = model.getPlayerName(defenderTerritory.getOwner());
			String attackerPlayer = model.getPlayerName(attackerTerritory.getOwner());
			closePopupWindow();
			showAttackResultView();
			attackResultView.UpdateViewState(attacker,defender,attackerPlayer, defenderPlayer, attackResult);
			attackView.UpdateViewState(defenderPlayer, attackerPlayer, defenderTerritory, attackerTerritory);
			
			//GameOver
			if(isVictory()) showVictoryView();
			
			/*
			if (model.getPhase() == Phase.WaitForUnitCount)
				showTransferView(attacker, defender);
			*/
		} catch (Exception e) {
			addLog(e.getMessage());
		}
	}
	
	private void showAttackResultView() {
		attackResultView = new View.JFXAttackResultView();
		attackResultView.AddControllerListener(this);
		Parent root = attackResultView.getRoot();
		Scene attackResultScene = new Scene(root);
		popupStage.setScene(attackResultScene);
		popupStage.show();
	}

	public void attackRetreat() {
		closePopupWindow();
	}

	/*
	 * Transfer View
	 */
	private void showTransferView(int fromID, int toID) {
		transferView = new View.JFXTransferView();
		transferView.AddControllerListener(this);
		Parent root = transferView.getRoot();
		Scene transferScene = new Scene(root);
		popupStage.setScene(transferScene);
		popupStage.show();

		Territory from = model.getTerritory(fromID);
		Territory to = model.getTerritory(toID);
		String player = model.getPlayerName(from.getOwner());
		transferView.UpdateViewState(player, from, to);
	}

	public void transferAccepted(int from, int to, int units) {
		Country fromCountry = convertToCountry(from);
		Country toCountry = convertToCountry(to);

		try {
			if (model.getPhase() == Phase.WaitForUnitCount) {
				if (model.moveUnits(units > 0 ? units : 1)) {
					addLog(units + " transferred from " + fromCountry.getName() + " to " + toCountry.getName() + "!");
					closePopupWindow();
				} else
					addLog("Failed to transfer " + units + " units from " + fromCountry.getName() + " to "
							+ toCountry.getName() + "!");
			} else {
				if (model.transfer(from, to, units)) {
					addLog(units + " transferred from " + fromCountry.getName() + " to " + toCountry.getName() + "!");
					closePopupWindow();
				} else
					addLog("Failed to transfer " + units + " units from " + fromCountry.getName() + " to "
							+ toCountry.getName() + "!");
			}

			UpdateCurrentPlayer();
			if(isVictory()) showVictoryView();
		} catch (Exception e) {
			addLog(e.getMessage());
		}
	}

	public void attackShowed(int attacker, int defender) {
		closePopupWindow();
		if (model.getPhase() == Phase.WaitForUnitCount)
			showTransferView(attacker, defender);
		else
			showAttackView(defender,attacker);
	}

	/*
	 * Add player view
	 */
	public void showAddPlayerView() {
		addPlayerView = new View.JFXAddPlayerView(preStage);
		addPlayerView.AddControllerListener(this);

		Parent root = addPlayerView.getRoot();
		Scene mainScene = new Scene(root);
		preStage.setScene(mainScene);
		preStage.show();
	}

	/*
	 * Stage switch Pre -> Main
	 */

	public void switchToMain(Map<Color, String> map) {

		preStage.hide();

		try {
			model.addPlayerToPlayerList(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		preStage.close();
		showMainView();

		List<String> result = map.entrySet().stream().map(x -> x.getValue()).collect(Collectors.toList());
		mainView.UpdateConnectedPlayers(result);
		UpdateAllTerritoriesOnMap();
		UpdateCurrentPlayer();
		UpdateCurrentDeck();
	}
	
	private void UpdateCurrentDeck() {
		List<Integer> cards = model.getPlayerCards((model.getCurrentPlayer()));
		Accordion accordion = cardView.UpdateCurrentDeck(cards);
		
		cardGroup.getChildren().clear();
		
	    cardGroup.getChildren().add(accordion);
	}

	private void UpdateTerritoryOnMap(int territoryID) {
		Territory territory = model.getTerritory(territoryID);
		mainView.UpdateViewState(territory);

		// addLog(convertToCountry(territoryID).getName() + ": " +
		// model.getPlayerName(territory.getOwner()) + " - " + territory.getUnits());
	}

	private void UpdateAllTerritoriesOnMap() {
		List<Territory> territories = new ArrayList<Territory>();
		for (int i = 0; i < 42; i++)
			territories.add(model.getTerritory(i));

		mainView.UpdateViewState(territories);
	}

	private void UpdateCurrentPlayer() {
		mainView.UpdateCurrentPlayer(model.getCurrentPlayer());
		UpdateCurrentPhase();
	}

	private void UpdateCurrentPhase() {
		if (lastPhaseUpdate != model.getPhase() || lastPlayerUpdate != model.getCurrentPlayer())
			addLog("\nPlayer: " + model.getPlayerName(model.getCurrentPlayer()) + " - " + model.getPhase());

		mainView.UpdateCurrentPhase(model.getPhase());
		lastPhaseUpdate = model.getPhase();		
		lastPlayerUpdate = model.getCurrentPlayer();
	}

	private void closePopupWindow() {
		if (popupStage.isShowing()) {
			popupStage.hide();
			popupStage.close();
		}
	}

	// A f�ablak napl�j�ba k�ld egy �j bejegyz�st
	public void addLog(String log) {
		System.out.println(log);
		mainView.appendLog(log);
	}

	public static List<Territory> getTerritoryData() {
		return Model.Map.getTerritoryList();
	}

	private int convertToTerritoryID(Country country) {
		return country.ordinal();
	}

	private Country convertToCountry(int ID) {
		return Country.values()[ID];
	}

	public void saveGame(File saveFile) {
		if (saveFile != null)
			try {
				model.saveGame(saveFile);
			} catch (IOException e) {
				addLog("Save error! " + e.getLocalizedMessage());
			}
	}

	public void loadGame(File loadFile) {
		if (loadFile != null)
			try {
				model.loadGame(loadFile);
				mainView.UpdateConnectedPlayers(model.getPlayers());
				UpdateCurrentPlayer();
				UpdateCurrentDeck();
			} catch (Exception e) {
				addLog("Load error! " + e.getLocalizedMessage());
			}
	}

}
