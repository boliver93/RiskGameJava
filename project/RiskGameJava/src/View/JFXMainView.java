package View;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import Model.Player;
import Model.Territory;
import Controller.RiskGameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Az MVC architektura View reszet megvalosíto osztalya. Ez a View a jatek
 * fokepernyoje, ezen tortenik maga a jatek (egysegek elhelyezese, csata stb.).
 * @author Szabó Dávid
 * @version 1.0
 * @created 19-ápr.-2017 23:11:48
 */
public class JFXMainView extends JFXViewBase {
	
	World world;
	
	public JFXMainView(){
		// LoadFXML("MainView.fxml");
		
		world = WorldBuilder.create()
				/*
        		.mousePressHandler(evt -> {
                	CountryPath countryPath = (CountryPath) evt.getSource();
                	System.out.println(countryPath.getName());
                	System.out.println(countryPath.getContent());
                })
                */
        		.hoverEnabled(true)
        		.selectionEnabled(true)
                .build();
	}
	
	@Override
	public Parent getRoot(){
		return world;
	}
	
	public void finalize() throws Throwable {
		super.finalize();
	}
	

	/**
	 * 
	 * @param playerList
	 */
	public void UpdateConnectedPlayer(List<Player> playerList){

	}

	/**
	 * 
	 * @param player
	 */
	public void UpdateStartingPlayer(Player player){

	}

	/**
	 * 
	 * @param territories
	 */
	public void UpdateViewState(Territory territories){

	}

}