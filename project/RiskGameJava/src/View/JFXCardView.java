package View;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Accordion;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class JFXCardView extends JFXViewBase {

	@FXML
	private TitledPane titledpane;

	HashMap<Integer, String> locJPG;

	File filesJpg[];
	Image images[];
	ImageView imageViews[];
	BufferedImage bufferedImage[];
	TitledPane titledPanes[];
	int numOfCards;

	public JFXCardView() {
		locJPG = new HashMap<Integer, String>();

		loadJPG();

		LoadFXML("/View/fxml/CardsView.fxml");
	}

	private void loadJPG() {
		String fullpath;
		for (int i = 1; i < 42; i++) {
			fullpath = System.getProperty("user.dir") + "\\src\\View\\img\\territorycards\\effected\\" + i + ".jpg";
			System.out.println(fullpath);
			locJPG.put(i, fullpath);
		}
	}

	@Override
	public Parent getRoot() {
		return root;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
	}

	public Accordion UpdateCurrentDeck(List<Integer> cards) {
		numOfCards = cards.size();
		images = new Image[numOfCards];
		bufferedImage = new BufferedImage[numOfCards];
		imageViews = new ImageView[numOfCards];
		titledPanes = new TitledPane[numOfCards];

		for (int i = 0; i < numOfCards; i++) {
			String fullpath = locJPG.get(cards.get(i));

			try {
				bufferedImage[i] = ImageIO.read(new File(fullpath));

				images[i] = SwingFXUtils.toFXImage(bufferedImage[i], null);
				imageViews[i] = new ImageView();
				imageViews[i].setImage(images[i]);
				imageViews[i].setFitWidth(200);
				imageViews[i].setPreserveRatio(true);
				imageViews[i].setSmooth(true);
				imageViews[i].setCache(true);
				titledPanes[i] = new TitledPane(String.valueOf(i), imageViews[i]);
			} catch (IOException e) {
			}
		}
		Accordion accordion = new Accordion();
		accordion.getPanes().addAll(titledPanes);
		if (titledPanes.length > 0)
			accordion.setExpandedPane(titledPanes[0]);
		
		return accordion;
	}
}