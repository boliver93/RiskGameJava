package View;

import java.awt.event.ActionListener;

import Model.Territory;

/**
 * Ezen a View-en valaszthatja ki a jatekos, hogy a sajat teruleten belul hova
 * helyezi at az egysegeit.
 * @author Szabó Dávid
 * @version 1.0
 * @created 19-ápr.-2017 23:11:49
 */
public abstract class RiskTransferView {

	public RiskTransferView(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param controller
	 * @return 
	 */
	public abstract void AddControllerListener(ActionListener controller);

	/**
	 * 
	 * @param from
	 * @param to
	 * @return 
	 */
	public abstract void UpdateViewState(Territory from, Territory to);

}