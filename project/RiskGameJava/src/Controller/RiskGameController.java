package Controller;

import View.JFXMainView;
import View.Country;
import View.JFXAddPlayerView;
import View.JFXAttackView;
import View.JFXRiskCardView;
import View.JFXTransferView;

import java.util.Map;
import java.util.Observable;

import Model.Color;
import Model.RiskGameModel;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Az MVC architektura Controller reszet megvalosíto osztalya. A Controller a
 * Modell és a View kozotti kapcsolatert felelos. Rajta keresztul hajtodnak vegre
 * a View-on torteno valtozasok, melyek a Model-t erintik, illetve a Model a
 * Controller-en keresztul modositja a View-t.
 * @author Szabó Dávid
 * @version 1.0
 * @created 19-ápr.-2017 23:11:42
 */
public class RiskGameController extends java.util.Observable implements java.util.Observer {

	/*
	 * 	Stages
	 */
	private final Stage preStage;
	private Stage primaryStage;
	
	/*
	 * 	View objects
	 */
	private JFXMainView mainView;
	private JFXAddPlayerView addPlayerView;
	private JFXAttackView attackView;
	private JFXRiskCardView cardView;
	private JFXTransferView transferView;
	
	private RiskGameModel model;
	private int previouslySelectedTerritory = -1;
	
	public RiskGameController(Stage stage){
		this.preStage = stage;
		this.primaryStage = new Stage();
	}
	
	/**
	 * Eltárolja a modellt és feliratkoztatja magát az eseményeire
	 * @param model
	 */
	public void setModel(RiskGameModel model){
		this.model = model;
		this.model.addObserver(this);
	}
	
    public void countrySelected(Country country) {
    	int territoryID = convertToTerritoryID(country);
    	System.out.println("Controller - Territory selected: " + country.getName() + "/" + territoryID);
    	
    	try {
        	switch(model.getPhase())
        	{
    		case Reinforcement:
    			if (model.reinforce(territoryID))
    				addLog(country.getName() + " reinforced!");
				else
					addLog(country.getName() + " is occupied by another player!");
    			break;
    			
    			
    		case Battle:
    			if (previouslySelectedTerritory != -1)
    			{
					//if (model.checkAttackPossible(territoryID, previouslySelectedTerritory))
    				if (false)
					{
						addLog("Attacking " + country.getName() + "!");
						showAttackView(territoryID, previouslySelectedTerritory);
					}
					else
						addLog(country.getName() + " cannot be attacked!");
					previouslySelectedTerritory = -1;
				}
    			else previouslySelectedTerritory = territoryID;
    			break;
    			
    		case Transfer:
    			if (previouslySelectedTerritory != -1)
    			{
					//if (model.checkTransferPossible(previouslySelectedTerritory, territoryID))
    				if (false)
					{
						addLog("Transferring units to " + country.getName() + "!");
						showTransferView(previouslySelectedTerritory, territoryID);
					}
					else
						addLog("Can't transfer units to " + country.getName() + "!");
					previouslySelectedTerritory = -1;
				}
    			else previouslySelectedTerritory = territoryID;
    			break;
    			
    			
    		case Preparation:
    			if (model.reinforce(territoryID))
    				addLog(country.getName() + " reinforced!");
				else
					addLog(country.getName() + " is occupied by another player!");
    			break;
        	}
		} catch (Exception e) {
			addLog(e.getMessage());
		}
    }

	/*
	 * 	Main View
	 */
    public void showMainView() {
    	
    	mainView = new JFXMainView(primaryStage);
    	mainView.AddControllerListener(this);
    	
    	Parent root = mainView.getRoot();
    	
    	
    	Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    	
    	double ratio = 815.0/600;
    	double ratio2 = 1183.0/883;
    	
    	final double bound = 0.9;
    	final double height = screenBounds.getHeight() * bound;
    	final double width = height * ratio2;
    	
    	Scene mainScene = new Scene(root, width, height);

    	/* resolution check... ah those filthy pixels....
    	mainView.getWorld().setMouseEnterHandler(evt -> {
                	System.out.println(mainScene.getHeight() + " " + mainScene.getWidth());
                });
        */
    	
    	// Scene mainScene = new Scene(root);
    	// mainScene.getStylesheets().add("View/res/world.css");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
    
    /*
     * 	Attack View
     */
	private void showAttackView(int defenderID, int attackerID){
		attackView = new View.JFXAttackView();
		attackView.AddControllerListener(this);
		//attackView.UpdateViewState(defender, attacker);
	}

	/*
	 * 	Transfer View
	 */
	private void showTransferView(int fromID, int toID){
		transferView = new View.JFXTransferView();
		transferView.AddControllerListener(this);
		//transferView.UpdateViewState(from, to);
	}

	/*
	 * 	Add player view
	 */
	public void showAddPlayerView(){
		addPlayerView = new View.JFXAddPlayerView(preStage);
		addPlayerView.AddControllerListener(this);
		
    	Parent root = addPlayerView.getRoot();
    	Scene mainScene = new Scene(root);
    	preStage.setScene(mainScene);
    	preStage.show();
	}

	/*
	 * 	Stage switch
	 * 	Pre -> Main
	 */
	public void switchToMain(Map<String, Color> map){
		preStage.hide();
		try {
			model.addPlayerToPlayerList(map);

			System.out.println(map.toString());
		} catch (Exception e) {
			System.err.println("oh boy");
			e.printStackTrace();
		}
		preStage.close();
		showMainView();
	}
	
	/**
	 * Model hivja meg mikor befejezte a feldolgozast. A Controller az uj adatokat
	 * tovabbitja a nezetnek.
	 * 
	 * @param obs
	 * @param obj
	 */
	@Override
	public void update(Observable obs, Object obj){

	}
	
	//A fõablak naplójába küld egy új bejegyzést
	public void addLog(String log) {
		
	}
	
	
	private int convertToTerritoryID(Country country) {
		return country.ordinal();
	}
	
	private Country convertToCountry(int ID) {
		return Country.values()[ID];
	}

}