package View;

import Model.Territory;

/**
 * Az MVC architektura Controller reszet megvalos�to osztalya. Ez a View a jatek
 * fokepernyoje, ezen tortenik maga a jatek (egysegek elhelyezese, csata stb.).
 * @author Szab� D�vid
 * @version 1.0
 * @created 19-�pr.-2017 23:11:49
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
	 */
	public abstract AddControllerListener(ActionListener controller);

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
	 */
	public abstract UpdateViewState(Territory territories);

}