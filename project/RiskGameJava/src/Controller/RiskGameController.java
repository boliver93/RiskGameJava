package Controller;

import View.RiskAddPlayerView;
import View.RiskAttackView;
import View.RiskRiskCardView;
import View.RiskMainView;

import java.awt.event.ActionEvent;
import java.util.Observable;

import Model.RiskGameModel;
import View.RiskTransferView;

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

	private RiskAddPlayerView addPlayerView;
	private RiskAttackView attackView;
	private RiskRiskCardView cardView;
	private RiskMainView mainView;
	private RiskGameModel model;
	private RiskTransferView transferView;

	public RiskGameController(){
		mainView = new View.JFXMainView();
		mainView.AddControllerListener(this);
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