package View;

import javafx.geometry.Bounds;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private final ImageView iv;
    
    public CountryPath(final String name) {
        this(name, null, 0);
    }
    
    public CountryPath(final String name, final String content, final int unit) {
        super();
        this.name    = name;
        this.tooltip = new Tooltip(name);

        Tooltip.install(this, tooltip);
        // if (null == content) return;
        setContent(content);

        Bounds bb = this.getLayoutBounds();
		double x = bb.getMinX() + bb.getWidth()/2.0;
		double y = bb.getMinY() + bb.getHeight()/2.0;
        this.units = new Text(x, y, Integer.toString(0));
        
        this.iv = new ImageView();
        this.iv.setX(x); 
        this.iv.setY(y); 
        this.iv.setFitWidth(50);
        this.iv.setFitHeight(50);
        this.iv.setPreserveRatio(true);
        this.iv.setVisible(true);

        setUnits(unit);
    }

    public String getName() { return name; }

    public Tooltip getTooltip() { return tooltip; }
    
    public Text getText() {
    	return units;
    }
    
    public ImageView getImageView() {
    	return iv;
    }
    
    public void setImageView(Image img) {
    	this.iv.setImage(img);
    }
    
    public void setUnits(int u){
        int cu = (u == -1) ? 0 : u;
        units.setText(Integer.toString(cu));
        tooltip.setText(name + " (" + Integer.toString(cu) + " unit)");
        updateUnitPosition();
    }
    
    public void updateUnitPosition(){
        Bounds bb = this.getLayoutBounds();
		double x = bb.getMinX() + bb.getWidth()/2.0;
		double y = bb.getMinY() + bb.getHeight()/2.0;
		
		int l = units.getText().length();
		if (l == 1)
			units.setX(x);
		else if (l == 2) 
			units.setX(x - 6);
		else {  
			units.setX(x - 10);
			// world dominance
		}	
		units.setY(y);
		
		if (iv != null) {
			iv.setX(x - 35);
			iv.setY(y - 50);
		}
    }
}