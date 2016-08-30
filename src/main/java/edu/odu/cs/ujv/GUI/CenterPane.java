package edu.odu.cs.ujv.GUI;


import java.util.*;


import dejv.commons.jfx.input.GestureModifiers;
import dejv.commons.jfx.input.MouseButtons;
import dejv.commons.jfx.input.MouseModifiers;
import dejv.commons.jfx.input.handler.DragActionHandler;
import dejv.commons.jfx.input.handler.ScrollActionHandler;
import dejv.commons.jfx.input.properties.GestureEventProperties;
import dejv.commons.jfx.input.properties.MouseEventProperties;
import dejv.jfx.zoomfx.ZoomFX;
import edu.odu.cs.ujv.GBParser.FeatureSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;

/**
 * this class is for creating the center pane within the UJV
 * @author jdailey
 */
public class CenterPane extends ZoomFX {
    private List<String> strings;
    private List<Group> groups;
    private double paneRadius;
    private double widthReduction;
    private double heightReduction;
    private FsetArcController fset;
    private Label description;
    private Pane pane;
    private Group group;
    private int offset;
    private List<FeatureArc<NucleotideCompound>> arcs;
    private Map<String,BooleanProperty> archProperties;
    private Map<String, Group> groupedFeature;
    private Map<String, Group> groupedJoins;
    private DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = 0.1d;
    public static final Logger logger = LogManager.getLogger(CenterPane.class);
    private Map<String,Color> fColor;
    private GestureEventProperties zoomFXZoom = new GestureEventProperties(GestureModifiers.none());
    private MouseEventProperties zoomFXPan = new MouseEventProperties(MouseModifiers.none(), MouseButtons.middle());

    /**
     * Mouse wheel handler: zoom to pivot point
     */
    private EventHandler<ScrollEvent> onScrollEventHandler = event -> {
        double scale = this.myScale.get(); // currently we only use Y, same value is used for X
        double oldScale = scale;
        scale *= Math.pow(1.01, event.getDeltaY());
        if (scale <= MIN_SCALE)
            scale = MIN_SCALE;
        else if (scale >= MAX_SCALE)
            scale = MAX_SCALE;
        double f = (scale / oldScale)-1;
        double dx = (event.getSceneX() - (this.getBoundsInParent().getWidth()/2 + this.getBoundsInParent().getMinX()));
        double dy = (event.getSceneY() - (this.getBoundsInParent().getHeight()/2 + this.getBoundsInParent().getMinY()));

        this.setScale( scale);
        this.setPivot(f*dx, f*dy);
        logger.debug("Scence Gesture "+event.getTarget());
    };


    public CenterPane(Double radiusOfPane, double widthReduce, double heightReduce, FsetArcController fsets){
        super();
        this.paneRadius = radiusOfPane;
        this.widthReduction = widthReduce;
        this.heightReduction = heightReduce;
        this.fset = fsets;
        String source = fsets.getArcList().get(0).getFeatureSet().getDescription();
        // this.fset.paintFeature(this,"gene",0.0);
        this.description = new Label (source);
        description.setLayoutX(radiusOfPane - 100);
        description.setLayoutY(radiusOfPane - 50);
        description.setWrapText(true);
        description.setMaxSize(200, 100);
        this.group = new Group();
        this.offset = 0;
        this.strings = new ArrayList<>();
        this.groups = new ArrayList<>();
        drawCenterPane();
    }

    public CenterPane(Map<String,Color> fColor, List<FeatureSet<NucleotideCompound>> features,
                      double radiusOfPane,double widthReduce,double heightReduce){
        super();
        this.paneRadius = radiusOfPane;
        this.widthReduction = widthReduce;
        this.heightReduction = heightReduce;
        this.fset =  new FsetArcController(features, radiusOfPane, radiusOfPane,
                radiusOfPane - 40,this);
        this.group = new Group();
        this.offset = 0;
        this.strings = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.groupedFeature = new HashMap<>();
        this.groupedJoins = new HashMap<>();
        this.fColor = fColor;
        this.setStyle("-fx-background-color: white;");
        for(FeatureSet<NucleotideCompound> fs : features){
            if(fs.getDescription().equals("source")){
                String source = fs.getQualifier("organism").get().getValue();
                this.description = new Label (source);
                description.setLayoutX(radiusOfPane - 100);
                description.setLayoutY(radiusOfPane - 50);
                description.setWrapText(true);
                description.setMaxSize(200, 100);
                description.setFont(Font.font("Cambria",18));
                break;
            }
        }
        this.getContent().add(this.description);
        this.setWidth(1000);
        fset.drawTics(Color.BLACK, paneRadius - 40, paneRadius - 15, paneRadius - 15, this, 45, 0);
        fset.drawJoinTics("join_feature",Color.BLUE, paneRadius-30, paneRadius-10, paneRadius-35, this);

        ScrollActionHandler.with(zoomFXZoom)
                .doOnScroll(event -> this.zoom(event.getDeltaY()))
                .register(this.getViewport());
        DragActionHandler.with(zoomFXPan)
                .doOnDragStart((event) -> this.startPan(event.getSceneX(), event.getSceneY()))
                .doOnDrag((event) -> this.pan(event.getSceneX(), event.getSceneY()))
                .doOnDragFinish((event) -> this.endPan())
                .register(this.getViewport());

        //this.fset.paintFeature(this,"CDS",0.0);
    }

    public void drawCenterPane(){
        Circle circle2 = new Circle();
        circle2.setCenterX(paneRadius);
        circle2.setCenterY(paneRadius);
        circle2.setRadius(paneRadius);
        circle2.setStroke(Color.WHITE);
        circle2.setFill(null);

        /*
        Circle circle = new Circle();
        circle.setCenterX(paneRadius);
        circle.setCenterY(paneRadius);
        circle.setRadius(paneRadius -50);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(5);
        circle.setFill(null);
        Circle circle2 = new Circle();
        circle2.setCenterX(paneRadius);
        circle2.setCenterY(paneRadius);
        circle2.setRadius(paneRadius);
        circle2.setStroke(Color.WHITE);
        circle2.setFill(null);
        */
        /*
        circle.setOnMouseClicked(e -> {

            System.out.println("begin");
            System.out.println(e.getX());
            System.out.println(e.getY());
            double XvalinPane = e.getX() - widthReduction;
            double YvalinPane = e.getY() - heightReduction;
            System.out.println(XvalinPane);
            System.out.println(YvalinPane);

            Label desc = new Label();
            desc.setLayoutX(XvalinPane);
            desc.setLayoutY(YvalinPane);
            desc.setVisible(true);
            desc.setWrapText(true);
            desc.setMaxSize(200, 100);
            desc.setStyle("-fx-border-color: black;");
            this.getChildren().add(desc);
            double rotate = Math.toDegrees(Math.atan2(YvalinPane - paneRadius, XvalinPane - paneRadius));
            if (rotate < 0)
            {
                rotate = 360 + rotate;
            }
            System.out.println(rotate);
            System.out.println("end");


        });
        */
        group.getChildren().add(circle2);
        fset.drawTics(Color.BLACK, paneRadius - 40, paneRadius - 15, paneRadius - 15, this, 45, 0);
        fset.drawJoinTics("join_feature",Color.BLUE, paneRadius-30, paneRadius-10, paneRadius-35, this);
        group.getChildren().add(description);
        this.getContent().add(group);

        // group.setVisible(true);
        // fset.paintFeature(pane,"tRNA",0, group);
        //group.setVisible(false);
    }


    public void setScale( double scale) {
        myScale.set(scale);
    }


    public void setPivot( double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }

    public void setGroupVisible(String s){
        Group g = this.groupedFeature.get(s);
        Color c = this.fColor.get(s);
        if(g == null){
            drawFeature(s,c);
        } else {
            g.setVisible(true);
        }
    }


    public void setGroupInVisible(String s){
        Group g = this.groupedFeature.get(s);
        if(g != null){
            g.setVisible(false);
        }
    }


    public void drawFeature(String s,Color c){
        Group g = this.groupedFeature.get(s);
        if(g == null){
            logger.debug("Drawing group for s "+s);
            g = new Group();
            this.groupedFeature.put(s,g);
            strings.add(s);
            this.getContent().add(g);
            fset.paintFeature(this, s,0, g, strings.size(),c);
        } else {
            logger.debug("DrawFeature make s visible "+s);
            g.setVisible(true);
        }
    }

    public void drawJoin(String s){
        Group g = this.groupedJoins.get(s);
        if(g == null) {
            g = new Group();
            this.groupedJoins.put(s,g);
            fset.paintJoin(this, s,0, g);
            this.getContent().add(g);
        } else {
            g.setVisible(true);
        }
    }

    /*
    public void drawAllFeatures(List<String> featureNameList){
        featureNameList.forEach(this::drawFeature);
    }
    */


    public void removeFeature(String s){
        int found = 0;
        List<String> temp = new ArrayList<>();
        for (int x = 0; x < strings.size(); x++)
        {
            if (strings.get(x).equals(s))
            {
                found = x;
                strings.remove(x);
                groups.get(x).getChildren().clear();
                groups.remove(x);
            }
        }
        for (int x = strings.size(); x < found; x--)
        {
            groups.get(x).getChildren().clear();
            groups.remove(x);
            temp.add(strings.get(x));
            strings.remove(x);
        }
        //drawAllFeatures(temp);

    }



    public void removeAllFeatures(){
        groups.forEach(g -> g.getChildren().clear());
        groups.clear();
    }

    public void hideJoinTick(int indx)
    {
        fset.hideJoinStack(indx);
    }
    public void showJoinTick(int indx)
    {
        fset.showJoinStack(indx);
    }
    public void hideAllJoinTicks()
    {
        fset.hideAllJoinStack();
    }
    public void showAllJoinTicks()
    {
        fset.showAllJoinStack();
    }


}
