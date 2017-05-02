package Controller;

import View.JFXMainView;
import View.JFXAddPlayerView;
import View.JFXAttackView;
import View.JFXRiskCardView;
import View.JFXMainView;
import View.JFXTransferView;

import java.awt.event.ActionEvent;
import java.util.Observable;

import Model.RiskGameModel;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
public class RiskGameController extends java.util.Observable implements java.awt.event.ActionListener, java.util.Observer {

	private final Stage primaryStage;
	private JFXMainView mainView;
	private JFXAddPlayerView addPlayerView;
	private JFXAttackView attackView;
	private JFXRiskCardView cardView;
	private JFXTransferView transferView;
	
	private RiskGameModel model;
	
	
	public RiskGameController(Stage primaryStage){
		this.primaryStage = primaryStage;
	}

	public void finalize() throws Throwable {

	}

	/**
	 * A nezeten tortent esemenyeket (button click, stb) a controller kezeli le.
	 * 
	 * @param e
	 */
	public void actionPerformed(ActionEvent e){

	}
	
    public void showMainView() {
    	mainView = new JFXMainView();
    	mainView.AddControllerListener(this);
    	
    	Parent root = mainView.getRoot();
    	
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

	/**
	 * Eltárolja a modellt és feliratkoztatja magát az eseményeire
	 * @param model
	 */
	public void setModel(RiskGameModel model){
		this.model = model;
		this.model.addObserver(this);
	}

	private void showAttackView(){
		attackView = new View.JFXAttackView();
		attackView.AddControllerListener(this);
	}

	private void showTransferView(){
		transferView = new View.JFXTransferView();
		transferView.AddControllerListener(this);
	}

	/**
	 * Model hivja meg mikor befejezte a feldolgozast. A Controller az uj adatokat
	 * tovabbitja a nezetnek.
	 * 
	 * @param obs
	 * @param obj
	 */
	public void update(Observable obs, Object obj){

	}

}