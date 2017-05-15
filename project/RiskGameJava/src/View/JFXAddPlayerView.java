package View;


/**
 * Ezen a View-en keresztul tortenik a leendo jatekosok hozzaadasa a jatekhoz.
 * @author fodorad
 * @version 1.0
 * @created 19-ápr.-2017 23:11:48
 */
public class JFXAddPlayerView extends JFXViewBase {

	public JFXAddPlayerView(){
		LoadFXML("/View/fxml/AddPlayerView.fxml");
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}


	/**
	 * 
	 * @param name
	 */
	public void UpdateViewState(String name){

	}

}