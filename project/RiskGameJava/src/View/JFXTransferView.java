package View;

import Model.Territory;

/**
 * Ezen a View-en valaszthatja ki a jatekos, hogy a sajat teruleten belul hova
 * helyezi at az egysegeit.
 * @author Szabó Dávid
 * @version 1.0
 * @created 19-ápr.-2017 23:11:49
 */
public class JFXTransferView extends JFXViewBase {

	public JFXTransferView(){
		LoadFXML("TransferView.fxml");
	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * 
	 * @param from
	 * @param to
	 */
	public void UpdateViewState(Territory from, Territory to){

	}

}