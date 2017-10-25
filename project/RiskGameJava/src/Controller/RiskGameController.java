package Controller;

import View.JFXMainView;
import View.Country;
import View.JFXAddPlayerView;
import View.JFXAttackResultView;
import View.JFXAttackView;
//import View.JFXRiskCardView;
import View.JFXTransferView;

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
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Az MVC architektura Controller reszet megvalosíto osztalya. A Controller a
 * Modell és a View kozotti kapcsolatert felelos. Rajta keresztul hajtodnak
 * vegre a View-on torteno valtozasok, melyek a Model-t erintik, illetve a Model
 * a Controller-en keresztul modositja a View-t.
 * 
 * @author Szabó Dávid
 * @version 1.0
 * @created 19-ápr.-2017 23:11:42
 */
public class RiskGameController extends java.util.Observable {

	/*
	 * Stages
	 */
	private final Stage preStage;
	private Stage primaryStage;
	private Stage popupStage;

	/*
	 * View objects
	 */
	private JFXMainView mainView;
	private JFXAddPlayerView addPlayerView;
	private JFXAttackView attackView;
	// private JFXRiskCardView cardView;
	private JFXTransferView transferView;
	private JFXAttackResultView attackResultView;

	private RiskGameModel model;
	private int previouslySelectedTerritory = -1;
	private Phase lastPhaseUpdate;
	private int lastPlayerUpdate;

	public RiskGameController(Stage stage) {
		this.preStage = stage;
		this.primaryStage = new Stage();
		this.popupStage = new Stage();

		stage.setResizable(false);
		primaryStage.setResizable(false);
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setOnCloseRequest(evt -> {
			// Dont't let th User close the Transfer window when waiting for Unit Count
			if (model.getPhase() == Phase.WaitForUnitCount)
				evt.consume();
		});

	}

	/**
	 * Eltárolja a modellt és feliratkoztatja magát az eseményeire
	 * 
	 * @param model
	 */
	public void setModel(RiskGameModel model) {
		this.model = model;
		lastPhaseUpdate = model.getPhase();
		lastPlayerUpdate = model.getCurrentPlayer();
		// this.model.addObserver(this);
	}

	public void countrySelected(Country country) {
		int territoryID = convertToTerritoryID(country);
		// System.out.println("Controller - Territory selected: " + country.getName() +
		// "/" + territoryID);

		try {
			switch (model.getPhase()) {
			case Reinforcement:
				reinforce(territoryID, country.getName());
				break;

			case Battle:
				if (previouslySelectedTerritory != -1) {
					if (model.checkAttackPossible(territoryID, previouslySelectedTerritory)) {
						addLog("Attacking " + country.getName() + "!");
						showAttackView(territoryID, previouslySelectedTerritory);
					} else
						addLog(country.getName() + " cannot be attacked!");
					previouslySelectedTerritory = -1;
				} else {
					addLog("Attacking from " + country.getName() + "...");
					previouslySelectedTerritory = territoryID;
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
		} catch (Exception e) {
			addLog(e.getMessage());
		}
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

		// mainScene.getStylesheets().add("View/res/world.css");
		primaryStage.setScene(mainScene);
		primaryStage.show();

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
			// TODO: attackView.UpdateViewState(attackResult);
			// Temporal code instead:
			Territory defenderTerritory = model.getTerritory(defender);
			Territory attackerTerritory = model.getTerritory(attacker);
			String defenderPlayer = model.getPlayerName(defenderTerritory.getOwner());
			String attackerPlayer = model.getPlayerName(attackerTerritory.getOwner());
			closePopupWindow();
			showAttackResultView();
			attackResultView.UpdateViewState(attacker,defender,attackerPlayer, defenderPlayer, attackResult);
			attackView.UpdateViewState(defenderPlayer, attackerPlayer, defenderTerritory, attackerTerritory);
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
		UpdateCurrentDeck();
	}

	private void UpdateCurrentPhase() {
		if (lastPhaseUpdate != model.getPhase() || lastPlayerUpdate != model.getCurrentPlayer())
			addLog("\nPlayer: " + model.getPlayerName(model.getCurrentPlayer()) + " - " + model.getPhase());

		mainView.UpdateCurrentPhase(model.getPhase());
		lastPhaseUpdate = model.getPhase();
		lastPlayerUpdate = model.getCurrentPlayer();
	}
	
	private void UpdateCurrentDeck() {
		List<Integer> cards = model.getPlayerCards((model.getCurrentPlayer()));
		mainView.UpdateCurrentDeck(cards);
	}

	private void closePopupWindow() {
		if (popupStage.isShowing()) {
			popupStage.hide();
			popupStage.close();
		}
	}

	// A fõablak naplójába küld egy új bejegyzést
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
			} catch (Exception e) {
				addLog("Load error! " + e.getLocalizedMessage());
			}
	}

}
