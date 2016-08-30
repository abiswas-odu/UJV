package edu.odu.cs.ujv.GUI;

import edu.odu.cs.ujv.GBParser.FeatureSet;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.lang3.text.WordUtils;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.Qualifier;

import java.util.Map;

/**
 * @author abiswas
 */
public class FsetArc {
    private double arcCenterX;
    private double arcCenterY;
    private double arcLength;
    private double arcRadius;
    private double arcStartAngle;
    private double arcRatio;
    //nucleotides per degree
    private ArcType arcType;
    //ArcType.OPEN
    private Paint arcFill;
    //Paint = null
    private Color arcStroke;
    //Color.xxxx
    private FeatureSet<NucleotideCompound> arcFeatureSet;
    private String Label;
    private Label desc;
    //label object for mouse over events

    private Arc arc;
    private EventHandler<ScrollEvent> onScrollEventHandler;


    /**
     * CONSTRUCTORS
     */
    public FsetArc(FeatureSet<NucleotideCompound> fset, double radius, double centerX, double centerY, double ratio, Color c, Pane pane) {
        this.arcCenterX = centerX;
        this.arcCenterY = centerY;
        this.arcRadius = radius;
        this.arcRatio = ratio;
        this.arcType = ArcType.OPEN;
        this.arcFill = null;
        this.arcStroke = c;
        this.arcFeatureSet = fset;
        this.arcLength = this.calcLength();
        this.arcStartAngle = this.calcStart();
        this.arc = new Arc();

        StringBuilder sb = new StringBuilder();
        sb.append("Feature Type: ")
          .append(this.arcFeatureSet.getType())
          .append("\n")
          .append("Feature Start: ")
          .append(this.arcFeatureSet.getStart())
          .append("\n")
          .append("Feature End: ")
          .append(this.arcFeatureSet.getEnd())
          .append("\n")
          .append("Feature Length: ")
          .append(this.arcFeatureSet.getLength())
          .append("\n");

        this.arcFeatureSet.getFeatureQualifierMap().forEach((qualifierName,qualifier)-> {
            if (qualifier.getValue().length() > 40){
                sb.append(WordUtils.capitalize(qualifierName));
                sb.append(": ");
                sb.append(WordUtils.capitalize(qualifier.getValue().substring(0,20)));
                sb.append("......");
                sb.append("\n");
            } else {
                sb.append(WordUtils.capitalize(qualifierName));
                sb.append(": ");
                sb.append(WordUtils.capitalize(qualifier.getValue()));
                sb.append("\n");
            }
        });

        if(this.getFeatureSet().getSequence().length() > 40)
            sb.append("Sequence: ").append(this.getFeatureSet().getSequence().substring(0,39)).append("...\n");
        else
            sb.append("Sequence: ").append(this.getFeatureSet().getSequence()).append("\n");

        String s = sb.toString();
        Tooltip tp = new Tooltip(s);
        Text dataText = new Text();
        dataText.setText(s);
        arc.setOnMouseEntered(event -> {
            System.out.println("Mouse entered");
            Node node = (Node) event.getSource();
            tp.show(node,event.getScreenX() + 50, event.getScreenY());
        });
        arc.setOnMouseExited(event -> tp.hide());
        arc.setOnMouseClicked(event -> {
            System.out.println("Mouse clicked on arc");
            final Stage secondStage = new Stage();
            secondStage.setTitle("Feature Data");
            StackPane loadGroup = new StackPane();
            Scene loadScene = new Scene(loadGroup,600,200);
            secondStage.setScene(loadScene);
            loadGroup.getChildren().setAll(dataText);
            secondStage.show();
        });
    }


    /*
    public FsetArc(FeatureSet<NucleotideCompound> fset, double radius, double centerX,
                   double centerY, double ratio, Pane pane, EventHandler<ScrollEvent> onScrollEventHandler) {
        this.arcCenterX = centerX;
        this.arcCenterY = centerY;
        this.arcRadius = radius;
        this.arcRatio = ratio;
        this.arcType = ArcType.OPEN;
        this.arcFill = null;
        this.arcStroke = Color.BLACK;
        this.arcFeatureSet = fset;
        this.arcLength = this.calcLength();
        this.arcStartAngle = this.calcStart();
        Arc arc = new Arc();
        arc.setOnScroll(onScrollEventHandler);
        String s = this.getFeatureSet().getDescription() + "\n" +
                this.getFeatureSet().getStart() + "\n" +
                this.getFeatureSet().getEnd() + "\n" +
                this.getFeatureSet().getType();
        Label = null;
        Label desc = new Label("test");
        desc.setLayoutX(this.arcCenterX + 25);
        desc.setLayoutY(this.arcCenterY + 25);
        desc.setVisible(false);
        desc.setWrapText(true);
        desc.setMaxSize(200, 100);
        desc.setStyle("-fx-border-color: black;");
        desc.setText(s);
        pane.getChildren().add(desc);
        arc.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
                mouseEvent -> desc.setVisible(true));
        arc.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
                mouseEvent -> desc.setVisible(false));
        this.arc = arc;
    }
    */

    public FsetArc(FeatureSet<NucleotideCompound> fset, double radius, double centerX, double centerY, double ratio,  Pane pane, EventHandler<ScrollEvent> onScrollEventHandler) {
        this.arcCenterX = centerX;
        this.arcCenterY = centerY;
        this.arcRadius = radius;
        this.arcRatio = ratio;
        this.arcType = ArcType.OPEN;
        this.arcFill = null;
        this.arcStroke = Color.BLACK;
        this.arcFeatureSet = fset;
        this.arcLength = 0;
        //needs equation
        this.arcStartAngle = 0;
        //needs equation
        Label = null;
        Arc arc = new Arc();
        arc.setOnScroll(onScrollEventHandler);
        String s = this.getFeatureSet().getDescription() + "\n" +
                this.getFeatureSet().getStart() + "\n" +
                this.getFeatureSet().getEnd() + "\n" +
                this.getFeatureSet().getType();
        Label desc = new Label("test");
        desc.setLayoutX(this.arcCenterX + 25);
        desc.setLayoutY(this.arcCenterY + 25);
        desc.setVisible(false);
        desc.setWrapText(true);
        desc.setMaxSize(200, 100);
        desc.setStyle("-fx-border-color: black;");
        desc.setText(s);
        pane.getChildren().add(desc);
        arc.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
                mouseEvent -> desc.setVisible(true));
        arc.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
                mouseEvent -> desc.setVisible(false));
        this.arc = arc;
        this.onScrollEventHandler = onScrollEventHandler;
    }

    //constructor with color added in
    public FsetArc(FeatureSet<NucleotideCompound> fset, double radius, double centerX, double centerY, double ratio, Pane pane) {
        this.arcCenterX = centerX;
        this.arcCenterY = centerY;
        this.arcRadius = radius;
        this.arcRatio = ratio;
        this.arcType = ArcType.OPEN;
        this.arcFill = null;
        this.arcStroke = Color.BLACK;
        this.arcFeatureSet = fset;
        this.arcLength = 0;
        //needs equation
        this.arcStartAngle = 0;
        //needs equation
        Label = null;
        Arc arc = new Arc();
        String s = this.getFeatureSet().getDescription() + "\n" +
                this.getFeatureSet().getStart() + "\n" +
                this.getFeatureSet().getEnd() + "\n" +
                this.getFeatureSet().getType();
        Label desc = new Label("test");
        desc.setLayoutX(this.arcCenterX + 25);
        desc.setLayoutY(this.arcCenterY + 25);
        desc.setVisible(false);
        desc.setWrapText(true);
        desc.setMaxSize(200, 100);
        desc.setStyle("-fx-border-color: black;");
        desc.setText(s);
        pane.getChildren().add(desc);
        arc.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
                mouseEvent -> desc.setVisible(true));
        arc.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
                mouseEvent -> desc.setVisible(false));
        this.arc = arc;
    }

    //constructor for tic mark function
    public FsetArc(Color c, double centerX, double centerY, double start, double length, double radius, String s, Pane pane) {
        this.arcCenterX = centerX;
        this.arcCenterY = centerY;
        this.arcRadius = radius;
        this.arcRatio = 0;
        this.arcType = ArcType.OPEN;
        this.arcFill = null;
        this.arcStroke = c;
        this.arcFeatureSet = null;
        this.arcLength = length;
        this.arcStartAngle = start;
        Label = s;
        Label desc = new Label("test");
        desc.setLayoutX(this.arcCenterX + 25);
        desc.setLayoutY(this.arcCenterY + 25);
        desc.setVisible(false);
        desc.setWrapText(true);
        desc.setMaxSize(200, 100);
        desc.setStyle("-fx-border-color: black;");
        pane.getChildren().add(desc);
    }

    //constructor for tic mark function
    public FsetArc(Color c, double centerX, double centerY, double start, double length, double radius, Pane pane) {
        this.arcCenterX = centerX;
        this.arcCenterY = centerY;
        this.arcRadius = radius;
        this.arcRatio = 0;
        this.arcType = ArcType.OPEN;
        this.arcFill = null;
        this.arcStroke = c;
        this.arcFeatureSet = null;
        this.arcLength = length;
        this.arcStartAngle = start;
        Label = null;
        Label desc = new Label("test");
        desc.setLayoutX(this.arcCenterX + 25);
        desc.setLayoutY(this.arcCenterY + 25);
        desc.setVisible(false);
        desc.setWrapText(true);
        desc.setMaxSize(200, 100);
        desc.setStyle("-fx-border-color: black;");
        pane.getChildren().add(desc);
    }
    /** END OF CONSTRUCTORS */
/**
 public void setDisplay(String s){
 this.display = s;
 }
 public String getDisplay (){
 return this.display;
 }
 public void setLabel(String s){
 this.Label = s;
 }
 */
    /**
     * Set Arc length from ratio
     */
    public double calcStart() {
        return ((this.getFeatureSet().getStart()) / (this.getRatio()));
    }

    /**
     * Set start position from ratio
     */
    public double calcLength() {
        return (this.getFeatureSet().getEnd()) / (this.getRatio());
    }

    /**
     * Set Ratio
     */
    public void setRatio(double ratio) {
        this.arcRatio = ratio;
    }

    /**
     * Get Ratio
     */
    public double getRatio() {
        return arcRatio;
    }

    /**
     * Set arcCenterX
     */
    public void setArcCenterX(double arcCenterX) {
        this.arcCenterX = arcCenterX;
    }

    /**
     * Get arcCenterX
     */
    public double getArcCenterX() {
        return arcCenterX;
    }

    /**
     * Set arcCenterY
     */
    public void setArcCenterY(double arcCenterY) {
        this.arcCenterY = arcCenterY;
    }

    /**
     * Get arcCenterY
     */
    public double getArcCenterY() {
        return arcCenterY;
    }

    /**
     * Set arcLength
     */
    public void setArcLength(double arcLength) {
        this.arcLength = arcLength;
    }

    /**
     * Get arcLength
     */
    public double getArcLength() {
        return arcLength;
    }

    /**
     * Set arcRadiusX
     */
    public void setArcRadius(double arcRadius) {
        this.arcRadius = arcRadius;
    }

    /**
     * Get arcRadiusX
     */
    public double getArcRadius() {
        return arcRadius;
    }

    /**
     * Set arcStartAngle
     */
    public void setArcStartAngle(double arcStartAngle) {
        this.arcStartAngle = arcStartAngle;
    }

    /**
     * Get arcStartAngle
     */
    public double getArcStartAngle() {
        return arcStartAngle;
    }

    /**
     * Set ArcType
     */
    public void setArcType(ArcType arcType) {
        this.arcType = arcType;
    }

    /**
     * Get ArcType
     */
    public ArcType getArcType() {
        return arcType;
    }

    /**
     * Set arcFill
     */
    public void setArcFill(Paint arcFill) {
        this.arcFill = arcFill;
    }

    /**
     * get arcFill
     */
    public Paint getArcFill() {
        return arcFill;
    }

    /**
     * set arcStroke
     */
    public void setArcStroke(Color color) {
        this.arcStroke = color;
    }

    /**
     * Get arcStroke
     */
    public Color getArcStroke() {
        return arcStroke;
    }

    /**
     * Set Feature Set
     */
    public void setFeatureSet(FeatureSet<NucleotideCompound> arcFeatureSet) {
        this.arcFeatureSet = arcFeatureSet;
    }

    /**
     * Get Feature Set
     */
    public FeatureSet<NucleotideCompound> getFeatureSet() {
        return arcFeatureSet;
    }

    /**
     * Paint Arc
     */
    public void drawArc(Pane pane) {
        Arc arc = new Arc();
        arc.setCenterX(this.arcCenterX);
        arc.setCenterY(this.arcCenterY);
        arc.setRadiusX(this.arcRadius);
        arc.setRadiusY(this.arcRadius);
        arc.setStartAngle(arcStartAngle);
        arc.setLength(arcLength);
        arc.setType(arcType);
        arc.setFill(arcFill);
        arc.setStroke(arcStroke);
        arc.setStrokeWidth(3);
        pane.getChildren().add(arc);
    }

    public void drawArc(Pane pane, double radiusOffset, double StrokeW, double offset) {
        //Arc arc = new Arc();
        this.arc.setCenterX(this.arcCenterX);
        this.arc.setCenterY(this.arcCenterY);
        this.arc.setRadiusX(this.arcRadius - radiusOffset);
        this.arc.setRadiusY(this.arcRadius - radiusOffset);
        double start = (arcStartAngle + offset);
        if (start > 360) {
            start = start - 360;
        }
        double end = (arcLength - start + offset);
        if (end > 360) {
            end = end - 360;
        }
        this.arc.setStartAngle(360 - start);
        // arcStartAngle + offset
        this.arc.setLength(end);
        //arcLength - arcStartAngle + offset
        this.arc.setType(arcType);
        this.arc.setFill(arcFill);
        this.arc.setStroke(arcStroke);
        this.arc.setStrokeWidth(StrokeW);

        pane.getChildren().add(this.arc);
    }

    public void drawArc(Pane pane, double radiusOffset, double StrokeW, double offset, Group group,Color c) {
        System.out.println("Drawing arch for "+this.arcFeatureSet.getDescription());
        this.arc.setCenterX(this.arcCenterX);
        this.arc.setCenterY(this.arcCenterY);
        this.arc.setRadiusX(this.arcRadius - radiusOffset );
        this.arc.setRadiusY(this.arcRadius - radiusOffset );
        double start = (arcStartAngle + offset);
        if (start > 360) {
            start = start - 360;
        }
        double end = (arcLength - start + offset);
        if (end > 360) {
            end = end - 360;
        }
        this.arc.setStartAngle(360 - start);
        // arcStartAngle + offset
        this.arc.setLength(end);
        //arcLength - arcStartAngle + offset
        this.arc.setType(arcType);
        this.arc.setFill(arcFill);
        this.arc.setStroke(c);
        this.arc.setStrokeWidth(StrokeW);

        group.getChildren().add(this.arc);
    }

    public void drawJoinArc(Pane pane, double radiusOffset, double StrokeW, double offset, Group group) {
        //Arc arc = new Arc();
        this.arc.setCenterX(this.arcCenterX);
        this.arc.setCenterY(this.arcCenterY);
        this.arc.setRadiusX(this.arcRadius - radiusOffset );
        this.arc.setRadiusY(this.arcRadius - radiusOffset );
        double start = (arcStartAngle + offset);
        if (start > 360) {
            start = start - 360;
        }
        double end = (arcLength - start + offset);
        if (end > 360) {
            end = end - 360;
        }
        this.arc.setStartAngle(360 - start);
        // arcStartAngle + offset
        this.arc.setLength(end);
        //arcLength - arcStartAngle + offset
        this.arc.setType(arcType);
        this.arc.setFill(arcFill);
        this.arc.setStroke(Color.BLUE);
        this.arc.setStrokeWidth(StrokeW);

        Arc sideArc = new Arc();
        sideArc.setCenterX(this.arcCenterX);
        sideArc.setCenterY(this.arcCenterY);
        sideArc.setRadiusX(this.arcRadius - radiusOffset);
        sideArc.setRadiusY(this.arcRadius - radiusOffset);
        sideArc.setStartAngle(360 - start);
        sideArc.setLength(end/10000);
        sideArc.setType(arcType);
        sideArc.setFill(arcFill);
        sideArc.setStroke(Color.BLUE);
        sideArc.setStrokeWidth(10);

        group.getChildren().add(this.arc);
        group.getChildren().add(sideArc);
    }

    public void drawComplimentArc(Pane pane, double radiusOffset, double StrokeW) {
        Arc arc = new Arc();
        arc.setCenterX(this.arcCenterX);
        arc.setCenterY(this.arcCenterY);
        arc.setRadiusX(this.arcRadius - radiusOffset);
        arc.setRadiusY(this.arcRadius - radiusOffset);
        arc.setStartAngle(arcStartAngle);
        arc.setLength(arcLength - arcStartAngle);
        arc.setType(arcType);
        arc.setFill(arcFill);
        arc.setStroke(arcStroke);
        arc.setStrokeWidth(StrokeW);
        String s = this.getFeatureSet().getDescription();
        pane.getChildren().add(arc);
    }
}
