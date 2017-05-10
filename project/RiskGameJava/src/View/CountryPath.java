package View;

import javafx.scene.control.Tooltip;
import javafx.scene.shape.SVGPath;

/*
 * 	SVG for countries
 * 	@author fodorad
 * 	@version 1.0
 */
public class CountryPath extends SVGPath {
	
    private final String name;
    private final Tooltip tooltip;

    public CountryPath(final String name) {
        this(name, null);
    }
    
    public CountryPath(final String name, final String content) {
        super();
        this.name    = name;
        this.tooltip = new Tooltip(name);
        Tooltip.install(this, tooltip);
        if (null == content) return;
        setContent(content);
    }

    public String getName() { return name; }

    public Tooltip getTooltip() { return tooltip; }
}