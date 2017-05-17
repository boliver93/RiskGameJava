package View;

import javafx.scene.control.Tooltip;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

/*
 * 	SVG for countries
 * 	@author fodorad
 * 	@version 1.0
 */
public class CountryPath extends SVGPath {
	
    private final String name;
    private final Tooltip tooltip;
    private final Text units;

    public CountryPath(final String name) {
        this(name, null, 0);
    }
    
    public CountryPath(final String name, final String content, final int unit) {
        super();
        this.name    = name;
        this.tooltip = new Tooltip(name);
        this.units = new Text();
        Tooltip.install(this, tooltip);
        if (null == content) return;
        setContent(content);
        setUnits(unit);
    }

    public String getName() { return name; }

    public Tooltip getTooltip() { return tooltip; }
    
    public Text getText() {
    	return units;
    }
    
    public void setUnits(int u){
        units.setText(Integer.toString(u));
        int cu = (u == -1) ? 0 : u;
        tooltip.setText(name + " (" + Integer.toString(cu) + " unit)");
        updateUnitPosition();
    }
    
    public void updateUnitPosition(){
        units.setLayoutX(this.getLayoutX() + this.getLayoutBounds().getWidth()/2);
        units.setLayoutY(this.getLayoutY() + this.getLayoutBounds().getHeight()/2);
    }
}