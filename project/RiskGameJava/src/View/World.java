package View;

import static javafx.scene.input.MouseEvent.MOUSE_ENTERED;
import static javafx.scene.input.MouseEvent.MOUSE_EXITED;
import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;
import static javafx.scene.input.MouseEvent.MOUSE_RELEASED;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import Controller.RiskGameController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

/*
 * 	Parent view object
 * 	@author fodorad
 * 	@version 1.0
 */
public class World extends Region {
	/*
	 * Controller
	 */
	RiskGameController controller;
	
	/*
	 * 	Window specific fields
	 */
	private static final String 	lowResProperties 	= "View/svg/lowResSvg.properties";
	private static final double 	preferredWidth 		= 815;
	private static final double 	preferredHeight 	= 600;
    private static final double     aspectRatio     	= preferredHeight / preferredWidth;
	private Pane pane;
	private Pane unitPane;
	private Pane imgPane;
	private Group group;
	private double width;
	private double height;
	
    /*
     * 	CSS specific fields
     */
	private static final StyleablePropertyFactory<World> FACTORY          = new StyleablePropertyFactory<>(Region.getClassCssMetaData());
	private static final CssMetaData<World, Color>       BACKGROUND_COLOR = FACTORY.createColorCssMetaData("-background-color", s -> s.backgroundColor, Color.web("#000066"), false);
    private        final StyleableProperty<Color>        backgroundColor;
    private static final CssMetaData<World, Color>       FILL_COLOR = FACTORY.createColorCssMetaData("-fill-color", s -> s.fillColor, Color.web("#FFFFFF"), false);
    private        final StyleableProperty<Color>        fillColor;
    private static final CssMetaData<World, Color>       STROKE_COLOR = FACTORY.createColorCssMetaData("-stroke-color", s -> s.strokeColor, Color.BLACK, false);
    private        final StyleableProperty<Color>        strokeColor;
    private static final CssMetaData<World, Color>       HOVER_COLOR = FACTORY.createColorCssMetaData("-hover-color", s -> s.hoverColor, Color.web("#d9f2e5"), false);
    private        final StyleableProperty<Color>        hoverColor;
    private static final CssMetaData<World, Color>       PRESSED_COLOR = FACTORY.createColorCssMetaData("-pressed-color", s -> s.pressedColor, Color.web("#ff9900"), false);
    private        final StyleableProperty<Color>        pressedColor;
    private static final CssMetaData<World, Color>       SELECTED_COLOR = FACTORY.createColorCssMetaData("-selected-color", s-> s.selectedColor, Color.web("#ff9900"), false);
    private        final StyleableProperty<Color>        selectedColor;
    private static final double							 OPACITY = 0.4d;
    
    /*
     * 	SVG and country  specific fields
     */
    private              ObjectProperty<Country>         selectedCountry;
    private              Country                         formerSelectedCountry;
    protected            Map<String, List<CountryPath>>  countryPaths;
    private Properties resolutionProperties;
    private Image defend;
    private Image attack;
    private int clickCounter;
    
    /*
     * 	Events and event handler specific fields
     */
    private              BooleanProperty                 hoverEnabled;
    private              BooleanProperty                 selectionEnabled;
    protected            EventHandler<MouseEvent>        _mouseEnterHandler;
    protected            EventHandler<MouseEvent>        _mousePressHandler;
    protected            EventHandler<MouseEvent>        _mouseReleaseHandler;
    protected            EventHandler<MouseEvent>        _mouseExitHandler;
    private              EventHandler<MouseEvent>        mouseEnterHandler;
    private              EventHandler<MouseEvent>        mousePressHandler;
    private              EventHandler<MouseEvent>        mouseReleaseHandler;
    private              EventHandler<MouseEvent>        mouseExitHandler;

    /*
     * 	Constructor
     */
    public World() {
    	
    	/*
    	 * 	Set svg paths
    	 */
    	resolutionProperties = readProperties(World.lowResProperties);
        countryPaths         = createCountryPaths();
        
    	/*
    	 * 	Set colors
    	 */
    	backgroundColor      = new StyleableObjectProperty<Color>(BACKGROUND_COLOR.getInitialValue(World.this)) {
            @Override protected void invalidated() { setBackground(new Background(new BackgroundFill(get(), CornerRadii.EMPTY, Insets.EMPTY))); }
            @Override public Object getBean() { return World.this; }
            @Override public String getName() { return "backgroundColor"; }
            @Override public CssMetaData<? extends Styleable, Color> getCssMetaData() { return BACKGROUND_COLOR; }
        };
        
        fillColor            = new StyleableObjectProperty<Color>(FILL_COLOR.getInitialValue(World.this)) {
            @Override protected void invalidated() { setFillAndStroke(); }
            @Override public Object getBean() { return World.this; }
            @Override public String getName() { return "fillColor"; }
            @Override public CssMetaData<? extends Styleable, Color> getCssMetaData() { return FILL_COLOR; }
        };
        
        strokeColor          = new StyleableObjectProperty<Color>(STROKE_COLOR.getInitialValue(World.this)) {
            @Override protected void invalidated() { setFillAndStroke(); }
            @Override public Object getBean() { return World.this; }
            @Override public String getName() { return "strokeColor"; }
            @Override public CssMetaData<? extends Styleable, Color> getCssMetaData() { return STROKE_COLOR; }
        };
        
        hoverColor           = new StyleableObjectProperty<Color>(HOVER_COLOR.getInitialValue(World.this)) {
            @Override protected void invalidated() { }
            @Override public Object getBean() { return World.this; }
            @Override public String getName() { return "hoverColor"; }
            @Override public CssMetaData<? extends Styleable, Color> getCssMetaData() { return HOVER_COLOR; }
        };
        pressedColor         = new StyleableObjectProperty<Color>(PRESSED_COLOR.getInitialValue(this)) {
            @Override protected void invalidated() {}
            @Override public Object getBean() { return World.this; }
            @Override public String getName() { return "pressedColor"; }
            @Override public CssMetaData<? extends Styleable, Color> getCssMetaData() { return PRESSED_COLOR; }
        };
        selectedColor        = new StyleableObjectProperty<Color>(SELECTED_COLOR.getInitialValue(this)) {
            @Override protected void invalidated() {}
            @Override public Object getBean() { return World.this; }
            @Override public String getName() { return "selectedColor"; }
            @Override public CssMetaData<? extends Styleable, Color> getCssMetaData() { return SELECTED_COLOR; }
        };
        
        hoverEnabled         = new BooleanPropertyBase(true) {
            @Override protected void invalidated() {}
            @Override public Object getBean() { return World.this; }
            @Override public String getName() { return "hoverEnabled"; }
        };
        
        selectionEnabled     = new BooleanPropertyBase(false) {
            @Override protected void invalidated() {}
            @Override public Object getBean() { return World.this; }
            @Override public String getName() { return "selectionEnabled"; }
        };
        
        selectedCountry      = new ObjectPropertyBase<Country>() {
            @Override protected void invalidated() {}
            @Override public Object getBean() { return World.this; }
            @Override public String getName() { return "selectedCountry"; }
        };
        
        /*
         * 	Create panes
         */
        pane                 = new Pane();
        unitPane             = new Pane();
        imgPane             = new Pane();
        group                = new Group();

        /*
         * 	Set event handlers
         */
        _mouseEnterHandler   = evt -> handleMouseEvent(evt, mouseEnterHandler);
        _mousePressHandler   = evt -> handleMouseEvent(evt, mousePressHandler);
        _mouseReleaseHandler = evt -> handleMouseEvent(evt, mouseReleaseHandler);
        _mouseExitHandler    = evt -> handleMouseEvent(evt, mouseExitHandler);
        
        initGraphics();
        registerPropertyListeners();
    }
    
    /*
     * 	Init
     */
    private void initGraphics() {
    	
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 ||
            Double.compare(getWidth(), 0.0) <= 0 || Double.compare(getHeight(), 0.0) <= 0) {
        	
            if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                setPrefSize(getPrefWidth(), getPrefHeight());
            } else {
                setPrefSize(preferredWidth, preferredHeight);
            }
            
        }
        
        InputStream defendStream = this.getClass().getResourceAsStream("/View/img/shield.png");
        InputStream attackStream = this.getClass().getResourceAsStream("/View/img/sword.png");
        this.defend = new Image(defendStream);
        this.attack = new Image(attackStream);
        this.clickCounter = 0;
        
        getStyleClass().add("world");

        Color fill   = getFillColor();
        Color stroke = getStrokeColor();
        
        countryPaths.forEach((name, pathList) -> {
        	
            Country country = Country.valueOf(name);
            
            pathList.forEach(path -> {
                path.setFill(null == country.getColor() ? fill : country.getColor());
                path.setStroke(stroke);
                path.setStrokeWidth(0.2);
                
                path.setOnMouseEntered(new WeakEventHandler<>(_mouseEnterHandler));
                path.setOnMousePressed(new WeakEventHandler<>(_mousePressHandler));
                path.setOnMouseReleased(new WeakEventHandler<>(_mouseReleaseHandler));
                path.setOnMouseExited(new WeakEventHandler<>(_mouseExitHandler));

                Circle circle = new Circle(path.getText().getX()+2, path.getText().getY()-5, 12);
                circle.setFill(Color.WHITE);
                circle.setStrokeWidth(1);
                circle.setStroke(Color.BLACK);
                circle.setPickOnBounds(false);
                circle.setMouseTransparent(true);
                unitPane.getChildren().add(circle);
                
                Text text = path.getText();
                text.setPickOnBounds(false);
                text.setMouseTransparent(true);
                unitPane.getChildren().add(text);
                
                ImageView iv = path.getImageView();
                iv.setPickOnBounds(false);
                iv.setMouseTransparent(true);
                imgPane.getChildren().add(iv);
                
                path.updateUnitPosition();
            });
            
            pane.getChildren().addAll(pathList);
            
        });

        group.getChildren().add(pane);
        group.getChildren().add(imgPane);
        group.getChildren().add(unitPane);
        //group.setOpacity(OPACITY);
        pane.setOpacity(OPACITY);
        imgPane.setOpacity(1d);
        unitPane.setOpacity(1d);
        
        getChildren().setAll(group);
        this.setPickOnBounds(false);
        pane.setPickOnBounds(false);
        unitPane.setPickOnBounds(false);
        imgPane.setPickOnBounds(false);
        // setBackground(new Background(new BackgroundFill(getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    // alternative
    public void updateUnitPosition() {
    	
    	countryPaths.forEach((name, pathList) -> {
        	
            pathList.forEach(path -> { path.updateUnitPosition(); });
            
        });
    	
    }
    
    /*
     * 	Property listeners
     */
    private void registerPropertyListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
    }
    
    /*
     * Add controller
     */
    public void setController(RiskGameController controller) {
    	this.controller = controller;
    }
    
    /*
     * 	Resize event
     */
    private void resize() {
    	
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();

        if (aspectRatio * width > height) {
            width = 1 / (aspectRatio / height);
        } else if (1 / (aspectRatio / height) > width) {
            height = aspectRatio * width;
        }

        if (width > 0 && height > 0) {
            
            pane.setCache(true);
            pane.setCacheHint(CacheHint.SCALE);

            unitPane.setCache(true);
            unitPane.setCacheHint(CacheHint.SCALE);
            
            imgPane.setCache(true);
            imgPane.setCacheHint(CacheHint.SCALE);
            
            pane.setScaleX(width / preferredWidth);
            pane.setScaleY(height / preferredHeight);

            unitPane.setScaleX(width / preferredWidth);
            unitPane.setScaleY(height / preferredHeight);

            imgPane.setScaleX(width / preferredWidth);
            imgPane.setScaleY(height / preferredHeight);
            
            group.resize(width, height);
            group.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);

            pane.setCache(false);
            unitPane.setCache(false);
            imgPane.setCache(false);
            
        }
    }
    
    /*
     * 	Mouse event listener
     */
    private void handleMouseEvent(final MouseEvent event, final EventHandler<MouseEvent> eventHandler) {
        setFillAndStroke();
        
        final CountryPath       countryPath 	= (CountryPath) event.getSource();
        final String            countryName 	= countryPath.getName();
        final Country           country      	= Country.valueOf(countryName);
        final List<CountryPath> paths        	= countryPaths.get(countryName);
        final EventType<? extends MouseEvent> eventType = event.getEventType();

        
        if (MOUSE_ENTERED == eventType) {
        	
            if (isHoverEnabled()) {
	            Color color = isSelectionEnabled() && country.equals(getSelectedCountry()) ? getSelectedColor() : getHoverColor();
	            for (SVGPath path : paths) { path.setFill(color); }
            }
            
        } else if (MOUSE_PRESSED == eventType) {
        	
        	/*
            Model.Phase phase = this.controller.getPhase();
            this.clickCounter += 1;

            if (clickCounter % 2 == 1) {
            	
            	countryPaths.forEach((name, pathList) -> {
                    pathList.forEach(path -> { path.setImageView(null); });
                });
            	
            }
            
            if (phase == Model.Phase.Battle) {
            	countryPath.setImageView(attack);
            } 
            */ 
        	
            if (isSelectionEnabled()) {
            	
                Color color;
                
                if (null == getSelectedCountry()) {
                    setSelectedCountry(country);
                    color = getSelectedColor();
                    
                } else {
                    color = null == getSelectedCountry().getColor() ? getFillColor() : getSelectedCountry().getColor();
                    
                }
                
                for (SVGPath path : countryPaths.get(getSelectedCountry().getName())) { path.setFill(color); }
                
            } else {
            	
                if (isHoverEnabled()) {
                	for (SVGPath path : paths) { path.setFill(getPressedColor()); }
                }	
                
            }
            
        } else if (MOUSE_RELEASED == eventType) {
        	
            Color color;
            
            if (isSelectionEnabled()) {
            	
                if (formerSelectedCountry == country) {
                    setSelectedCountry(null);
                    color = null == country.getColor() ? getFillColor() : country.getColor();
                } else {
                    setSelectedCountry(country);
                    color = getSelectedColor();
                }
                
                formerSelectedCountry = getSelectedCountry();
                
            } else {
            	
                color = getHoverColor();
                
            }

            setFillAndStroke();
            
            if (isHoverEnabled()) {
            	
            	for (SVGPath path : paths) { path.setFill(color); }
            	
            }
            
        } else if (MOUSE_EXITED == eventType) {
        	
            if (isHoverEnabled()) {
            	Color color = isSelectionEnabled() && country.equals(getSelectedCountry()) ? getSelectedColor() : getFillColor();
	            
            	for (SVGPath path : paths) {
	                path.setFill(null == country.getColor() || country == getSelectedCountry() ? color : country.getColor());
	            }
            }
            
        }
        
        
        if (null != eventHandler) eventHandler.handle(event);
    }
    
    public void updateIcons(int attacker, int defender) {
    	countryPaths.forEach((name, pathList) -> {
            pathList.forEach(path -> { 
            	
            	if ((attacker != -1) && (Country.values()[attacker] == Country.valueOf(path.getName()))) 
            		path.setImageView(attack);
            	else if ((defender != -1) && (Country.values()[defender] == Country.valueOf(path.getName())))
            		path.setImageView(defend);
            	else
            		path.setImageView(null); 
            
            });
        });
    	
    	
    	
    }
    
    /*
     * 	Fill and stroke the countries
     */
    private void setFillAndStroke() {
    	
    	List<Model.Territory> data = Controller.RiskGameController.getTerritoryData();
    	ArrayList<Color> colorList = new ArrayList<>();
		Collections.addAll(colorList, Color.RED, Color.BLUE, Color.BLACK, Color.YELLOW, Color.LIME, Color.FUCHSIA);
    	
		/*
        countryPaths.keySet().forEach(name -> {
            Country country = Country.valueOf(name);
            
            for (Model.Territory t : data) {
            	if (country.ordinal() == t.getId()) {
            		if (t.getOwner() != -1) country.setColor(
            	            country == getSelectedCountry() ? getSelectedColor() : colorList.get(t.getOwner()));
            		
            		country.setPopulation(t.getUnits());
            		
            		break;
            	}
            }
            
            setCountryFillAndStroke(country, null == country.getColor() ? getFillColor() : country.getColor(), getStrokeColor());
        });
        */
        
		countryPaths.forEach((name, pathList) -> {
            Country country = Country.valueOf(name);
            
            for (Model.Territory t : data) {
            	if (country.ordinal() == t.getId()) {
            		if (t.getOwner() != -1) country.setColor(
            	            country == getSelectedCountry() ? getSelectedColor() : colorList.get(t.getOwner()));
            		
            		country.setPopulation(t.getUnits());
            		
            		pathList.forEach(path -> {
            			path.setUnits(t.getUnits());
            		});
            		
            		break;
            	}
            	
            }
            
            setCountryFillAndStroke(country, null == country.getColor() ? getFillColor() : country.getColor(), getStrokeColor());
        });
		
    }
    
    /*
     * 	Fill and stroke the countries
     */
    private void setCountryFillAndStroke(final Country country, final Color fillColor, final Color strokeColor) {
        List<CountryPath> paths = countryPaths.get(country.getName());
        for (CountryPath path : paths) {
            path.setFill(fillColor);
            path.setStroke(strokeColor);
        }
        
    }
    
    /*
     * 	Load svg properties file
     */
    private Properties readProperties(final String svgPropertiesFilename) {
        final ClassLoader loader     = Thread.currentThread().getContextClassLoader();
        final Properties  properties = new Properties();
        
        try(InputStream resourceStream = loader.getResourceAsStream(svgPropertiesFilename)) {
        	properties.load(resourceStream);
        } catch (IOException exception) {
            System.out.println(exception);
            System.out.println("properties fajl olvasasat elb... hibáztad. :)");
        }
        
        return properties;
    }
    
    /*
     * 	Generate svg paths
     */
    private Map<String, List<CountryPath>> createCountryPaths() {
        Map<String, List<CountryPath>> countryPaths = new HashMap<>();
        
        resolutionProperties.forEach((key, value) -> {
            String            name     = key.toString();
            List<CountryPath> pathList = new ArrayList<>();
            
            for (String path : value.toString().split(";")) { pathList.add(new CountryPath(name, path, 0)); }
            
            countryPaths.put(name, pathList);
        });
        
        return countryPaths;
    }
    
    /*
     * 	Getters and setters
     */
    public void setMouseEnterHandler(final EventHandler<MouseEvent> eventHandler){
    	mouseEnterHandler = eventHandler;
    }
    
    public void setMousePressHandler(final EventHandler<MouseEvent> eventHandler){
    	mousePressHandler = eventHandler;
    }
    
    public void setMouseReleaseHandler(final EventHandler<MouseEvent> eventHandler){
    	mouseReleaseHandler = eventHandler;
    }
    
    public void setMouseExitHandler(final EventHandler<MouseEvent> eventHandler){
    	mouseExitHandler = eventHandler;
    }
    
    public Color getBackgroundColor(){ 
    	return backgroundColor.getValue();
    }
    
    public Color getFillColor(){ 
    	return fillColor.getValue();
    }
    
    public Color getStrokeColor(){ 
    	return strokeColor.getValue();
    }
    
    public Color getHoverColor(){ 
    	return hoverColor.getValue();
    }
    
    public Color getSelectedColor(){ 
    	return selectedColor.getValue();
    }
    
    public Color getPressedColor(){ 
    	return pressedColor.getValue();
    }
    
    public boolean isHoverEnabled(){
    	return hoverEnabled.get();
    }
    
    public void setHoverEnabled(final boolean isEnabled){
    	hoverEnabled.set(isEnabled);
    }
    
    public boolean isSelectionEnabled(){
    	return selectionEnabled.get();
    }
    
    public void setSelectionEnabled(final boolean isEnabled){ 
    	selectionEnabled.set(isEnabled);
    }
    
    public Country getSelectedCountry() {
    	return selectedCountry.get(); 
    }
    
    public void setSelectedCountry(final Country country) { 
    	selectedCountry.set(country); 
    }
}
