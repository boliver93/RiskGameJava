package View;

import java.util.HashMap;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/*
 * 	Parent view object builder
 * 	@author fodorad
 * 	@version 1.0
 */
public class WorldBuilder {

	private HashMap<String, Property> properties = new HashMap<>();

	public static final WorldBuilder create() { return new WorldBuilder(); }
	 
    public final WorldBuilder hoverEnabled(final boolean isEnabled) {
        properties.put("hoverEnabled", new SimpleBooleanProperty(isEnabled));
        return this;
    }

    public final WorldBuilder selectionEnabled(final boolean isEnabled) {
        properties.put("selectionEnabled", new SimpleBooleanProperty(isEnabled));
        return this;
    }
    
    public final WorldBuilder mousePressHandler(final EventHandler<MouseEvent> eventHandler) {
        properties.put("mousePressHandler", new SimpleObjectProperty<EventHandler<MouseEvent>>(eventHandler));
        return this;
    }
    
	public final World build() {
    	final World world = new World();
    	
    	for (String key : properties.keySet()) {
    		
    		if ("hoverEnabled".equals(key)) {
    			world.setHoverEnabled(((BooleanProperty) properties.get(key)).get());
            } else if ("selectionEnabled".equals(key)) {
            	world.setSelectionEnabled(((BooleanProperty) properties.get(key)).get());
            } else if ("mousePressHandler".equals(key)) {
            	world.setMousePressHandler(((ObjectProperty<EventHandler<MouseEvent>>) properties.get(key)).get());
    		}
    		
    	}
    	
    	return world;
    }
    
}
