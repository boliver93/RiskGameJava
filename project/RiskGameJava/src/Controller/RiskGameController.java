package Controller;

import View.RiskAddPlayerView;
import View.RiskAttackView;
import View.RiskRiskCardView;
import View.RiskMainView;
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
public class RiskGameController {

	private RiskAddPlayerView addPlayerView;
	private RiskAttackView attackView;
	private RiskRiskCardView cardView;
	private RiskMainView mainView;
	private RiskGameModel model;
	private RiskTransferView transferView;

	public RiskGameController(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * A nezeten tortent esemenyeket (button click, stb) a controller kezeli le.
	 * 
	 * @param e
	 */
	public actionPerformed(ActionEvent e){

	}

	/**
	 * 
	 * @param model
	 */
	public setModel(RiskGameModel model){

	}

	private showAttackView(){

	}

	private showTransferView(){

	}

	/**
	 * Model hivja meg mikor befejezte a feldolgozast. A Controller az uj adatokat
	 * tovabbitja a nezetnek.
	 * 
	 * @param obs
	 * @param obj
	 */
	public update(Observable obs, Object obj){

	}

}