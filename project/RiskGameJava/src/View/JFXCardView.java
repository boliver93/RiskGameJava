package View;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Accordion;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class JFXCardView extends JFXViewBase {

	@FXML
	private TitledPane titledPane;

	HashMap<Integer, String> locJPG;

	File filesJpg[];
	Image images[];
	ImageView imageViews[];
	BufferedImage bufferedImage[];
	TitledPane titledPanes[];
	int numOfCards;
	int initializeNumber;
	String backgroundPath;
	String fullPath;

	public JFXCardView() {
		locJPG = new HashMap<Integer, String>();
		backgroundPath = System.getProperty("user.dir") + "\\src\\View\\img\\territorycards\\risk_card.png";

		loadJPG();

		LoadFXML("/View/fxml/CardsView.fxml");
	}

	private void loadJPG() {
		for (int i = 1; i < 42; i++) {
			fullPath = System.getProperty("user.dir") + "\\src\\View\\img\\territorycards\\effected\\" + i + ".jpg";
			locJPG.put(i, fullPath);
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

		if (numOfCards == 0)
			initializeNumber = numOfCards + 1;
		else
			initializeNumber = numOfCards;

		images = new Image[initializeNumber];
		bufferedImage = new BufferedImage[initializeNumber];
		imageViews = new ImageView[initializeNumber];
		titledPanes = new TitledPane[initializeNumber];

		for (int i = 0; i < initializeNumber; i++) {
			try {
				if (numOfCards == 0)
					bufferedImage[i] = ImageIO.read(new File(backgroundPath));
				else {
					fullPath = locJPG.get(cards.get(i));
					bufferedImage[i] = ImageIO.read(new File(fullPath));
				}

				images[i] = SwingFXUtils.toFXImage(bufferedImage[i], null);
				imageViews[i] = new ImageView();
				imageViews[i].setImage(images[i]);
				imageViews[i].setFitWidth(200);
				imageViews[i].setPreserveRatio(true);
				imageViews[i].setSmooth(true);
				imageViews[i].setCache(true);

				if (numOfCards == 0)
					titledPanes[0] = new TitledPane("Territory cards", imageViews[i]);
				else
					titledPanes[i] = new TitledPane(String.valueOf(i + 1) + ". card", imageViews[i]);

				if (numOfCards == 0 || numOfCards == 1)
					titledPanes[i].setCollapsible(false);
			} catch (IOException e) {
			}
		}

		Accordion accordion = new Accordion();
		accordion.getPanes().addAll(titledPanes);
		accordion.setExpandedPane(titledPanes[0]);

		return accordion;
	}
}