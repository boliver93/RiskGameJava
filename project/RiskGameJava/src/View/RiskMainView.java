package View;

import java.awt.event.ActionListener;

import Model.Territory;

/**
 * Az MVC architektura Controller reszet megvalosíto osztalya. Ez a View a jatek
 * fokepernyoje, ezen tortenik maga a jatek (egysegek elhelyezese, csata stb.).
 * @author Szabó Dávid
 * @version 1.0
 * @created 19-ápr.-2017 23:11:49
 */
public abstract class RiskMainView {

	public RiskMainView(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * A Controllert feliratkoztatjuk a nezet komponenseinek esemenyeire (button click,
	 * stb.).
	 * 
	 * @param controller
	 * @return 
	 */
	public abstract void AddControllerListener(ActionListener controller);

	public boolean confirmSave(){
		return false;
	}

	public deleteView(){

	}

	public nextTurn(){

	}

	/**
	 * Frissul a jatekterkep a parameterben kapott adatokkal.
	 * 
	 * @param territories
	 * @return 
	 */
	public abstract void UpdateViewState(Territory territories);

}