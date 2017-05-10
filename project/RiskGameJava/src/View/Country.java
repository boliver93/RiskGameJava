package View;

import javafx.scene.paint.Color;

/*
 * 	Enumerator for territories' view objects
 * 	@author fodorad
 * 	@version 1.0
 */
public enum Country {
	
	AFGHANISTAN, ALASKA, ALBERTA, ARGENTINA, BRAZIL, 
	CENTRAL_AFRICA, CENTRAL_AMERICA, CHINA, 
	EAST_AFRICA, EASTERN_AUSTRALIA, EASTERN_CANADA, EASTERN_UNITED_STATES, EGYPT, 
	GREENLAND, GREAT_BRITAIN, 
	ICELAND, INDIA, INDONESIA, IRKUTSK, 
	JAPAN, 
	KAMCHATKA, 
	MADAGASCAR, MIDDLE_EAST, MONGOLIA, 
	NEW_GUINEA, NORTH_AFRICA, NORTHERN_EUROPE, NORTHWEST_TERRITORY, 
	ONTARIO, 
	PERU, 
	RUSSIA, 
	SCANDINAVIA, SIBERIA, SOUTH_AFRICA, SOUTH_ASIA, SOUTHERN_EUROPE, 
	URAL, 
	VENEZUELA, 
	WESTERN_AUSTRALIA, WESTERN_EUROPE, WESTERN_UNITED_STATES, 
	YAKUTSK;

    private Color color;

    Country() {
        color = null;
    }

    public String getName() {
    	return name();
    }

    public Color getColor() {
    	return color;
    }
    
    public void setColor(final Color color) {
    	this.color = color;
    }
    
}