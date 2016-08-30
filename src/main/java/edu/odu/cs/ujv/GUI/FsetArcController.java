package edu.odu.cs.ujv.GUI;

import java.awt.Point;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

import dejv.jfx.zoomfx.ZoomFX;
import edu.odu.cs.ujv.GUI.views.JoinEditView;
import javafx.beans.property.BooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import edu.odu.cs.ujv.GBParser.FeatureSet;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.lang3.text.WordUtils;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.Qualifier;

public class FsetArcController {
    private List<FsetArc> arcList;
    private Map<String,BooleanProperty> archProperties;
    private Map<String,List<FsetArc>> farcsGrouped;
    private EventHandler<ScrollEvent> onScrollEventHandler;
    private List<StackPane> joinStackList;
    private double nucleotideRatio;

    /**
     * CONSTRUCTOR for drawing tics
     */
    public FsetArcController() {
        arcList = null;
        nucleotideRatio = 0.0;
    }

    /**
     * CONSTRUCTOR that takes an List <FeatureSet> with a color specifications
     *
     * @param flist
     * @param c
     * @param x
     * @param y
     * @param radius
     * @param pane
     */
    public FsetArcController(List<FeatureSet<NucleotideCompound>> flist, Color c, double x, double y, double radius, Pane pane) {
        nucleotideRatio = (flist.get(0).getEnd() / 360.00);
        arcList = new ArrayList<>();
        flist.forEach(fs -> arcList.add(new FsetArc(fs, radius, x, y, nucleotideRatio, c, pane)));
    }

    /**
     * Constructor that takes an ArrayList <FeatureSet> with no color specifications
     */
    public FsetArcController(List<FeatureSet<NucleotideCompound>> flist, double x, double y, double radius, Pane pane) {
        nucleotideRatio = (flist.get(0).getEnd() / 360.00);
       // arcList = new ArrayList<>();
        this.joinStackList = new ArrayList<>();
        Random rn = new Random();
        farcsGrouped = flist.stream()
                .collect(Collectors.groupingBy(FeatureSet::getDescription,
                        Collectors.mapping(fs ->
                                new FsetArc(fs, radius, x, y, nucleotideRatio,
                                        Color.color(rn.nextDouble(),rn.nextDouble(),rn.nextDouble()),pane),
                                Collectors.toList())));
        /*
		 flist.forEach(fs ->
                arcList.add(new FsetArc(fs, radius, x, y, nucleotideRatio, pane))
        );
        */
    }

    public FsetArcController(List<FeatureSet<NucleotideCompound>> flist, double x, double y,
                             double radius, Pane pane,EventHandler<ScrollEvent> onScrollEventHandler) {
        nucleotideRatio = (flist.get(0).getEnd() / 360.00);
        arcList = new ArrayList<>();
        flist.forEach(fs ->
                arcList.add(new FsetArc(fs, radius, x, y, nucleotideRatio, pane,onScrollEventHandler))
        );

    }

    /**
     * Tick mark function
     */
    public void drawTics(Color c, double centerX, double centerY, double radius, ZoomFX p, double distance, double offset) {
        int maxDigitLen = NumberFormat.getNumberInstance(Locale.US).format((int) (360.0 * nucleotideRatio)).length();
        double degree = offset;
        double start = 0;
        double drawn = offset;
        while (degree < (360 + offset)) {
            if (degree > 360) {
                drawn = degree - 360;
            } else {
                drawn = degree;
            }

            Line line = new Line();
            line.setStartX(0.0f);
            line.setStartY(0.0f);
            line.setEndX(20.0f);
            line.setEndY(20.0f);
            StackPane stack = new StackPane();
            double nucleotide = Math.round(start * nucleotideRatio);
            String s = NumberFormat.getNumberInstance(Locale.US).format((int) (nucleotide));
            int diffLetterLen = (int) Math.ceil((maxDigitLen - s.length()) / 2);
            String padding = " ";
            for (int i = 0; i < diffLetterLen; i++)
                padding += "  ";

            Label letter = new Label(padding + s + padding);
            stack.getChildren().add(line);
            letter.setFont(Font.font(Font.getFontNames().get(2), 15));
            stack.getChildren().add(letter);
            letter.setStyle("-fx-background-color: white;-fx-border-color: transparent transparent transparent transparent;-fx-border-width: 2px");
            letter.setOpacity(1);
            letter.setRotate(180);

            stack.setAlignment(Pos.BASELINE_CENTER);
            line.setRotate(45);
            Point XY = findXY(centerX, centerY, radius, drawn);
            stack.setLayoutX(XY.getX());
            stack.setLayoutY(XY.getY());
            stack.setRotate(drawn + 270);
            p.getContent().add(stack);
            degree = degree + distance;
            start = start + distance;
        }

    }

    public double findTextX(double radius, double angle) {
        double radian = Math.toRadians(angle);
        double textX = radius + (radius * Math.cos(radian));
        Double toBeTruncated = Math.cos(radian);
        double truncatedDouble = new BigDecimal(toBeTruncated).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (truncatedDouble > 0) {
            textX = textX + 10;
        }
        if (truncatedDouble < 0) {
            textX = textX - 10;
        }
        //if (truncatedDouble == 0.0){
        //textX = textX - 10;
        //}
        return textX;
    }

    public double findTextY(double radius, double angle) {
        double radian = Math.toRadians(angle);
        double textY = radius + (radius * Math.sin(radian));
        Double toBeTruncated = Math.sin(radian);
        Double truncatedDouble = new BigDecimal(toBeTruncated).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

        if (truncatedDouble > 0) {
            textY = textY + 10;
        }
        if (truncatedDouble < 0) {
            textY = textY - 10;
        }
        //if (truncatedDouble == 0){
        //textY = textY - 10;
        //}
        return textY;
    }

    /**
     * function for finding x and y values for text, returns a point
     */
    public Point findXY(double centerX, double centerY, double radius, double degree) {
        double Y = centerY + (radius * Math.sin(Math.toRadians(degree)));
        double X = centerX + (radius * Math.cos(Math.toRadians(degree)));
        Point P = new Point();
        P.setLocation(X, Y);
        return P;
    }

    public void topPaneTics(Pane p, int angle, double width, double offset) {
        Double toBeTruncatedMax = (360 * nucleotideRatio) / 1000000.0;
        Double truncatedDoubleMax = new BigDecimal(toBeTruncatedMax).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        int maxDigitLen = NumberFormat.getNumberInstance(Locale.US).format((double) (truncatedDoubleMax)).length();

        for (int i = 0; i <= 360.0; i = (i + angle)) {
            StackPane stack = new StackPane();
            Double toBeTruncated = (i * nucleotideRatio) / 1000000.0;
            Double truncatedDouble = new BigDecimal(toBeTruncated).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            String s = Double.toString(truncatedDouble);
            int diffLetterLen = (int) Math.ceil((maxDigitLen - s.length()) / 2);
            String padding = " ";
            for (int j = 0; j < diffLetterLen; j++)
                padding += "  ";
            Line line = new Line();
            line.setStartX(0.0f);
            line.setStartY(20.0f);
            line.setEndX(0.0f);
            line.setEndY(50.0f);
            stack.getChildren().add(line);
            double positionX = (width / 360.0) * i + 18.5;
            Label val = new Label(padding + s + "M" + padding);
            val.setFont(Font.font(Font.getFontNames().get(2), 15));
            stack.getChildren().add(val);
            val.setStyle("-fx-background-color: white;-fx-border-color: transparent transparent transparent transparent;-fx-border-width: 2px");
            val.setOpacity(1);
            stack.setAlignment(Pos.BASELINE_CENTER);
            stack.setLayoutX(positionX);
            stack.setLayoutY(50);
            //stack.setRotate(180);
            p.getChildren().add(stack);
        }

    }

    public void topPaneTics(Pane p, double angle, double width, double offset) {
        double positionY = 30;
        double lineLength = width - 400;
        double lineStartX = 50;
        Double toBeTruncatedMax = (360 * nucleotideRatio) / 1000000.0;
        Double truncatedDoubleMax = new BigDecimal(toBeTruncatedMax).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        int maxDigitLen = NumberFormat.getNumberInstance(Locale.US).format((double) (truncatedDoubleMax)).length();


        for (double i = 0; i <= 360; i = (i + angle)) {
            StackPane stack = new StackPane();
            Double toBeTruncated = (i * nucleotideRatio) / 1000000.0;
            Double truncatedDouble = new BigDecimal(toBeTruncated).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            String s = Double.toString(truncatedDouble);
            int diffLetterLen = (int) Math.ceil((maxDigitLen - s.length()) / 2);
            String padding = " ";
            for (int j = 0; j < diffLetterLen; j++)
                padding += "  ";
            Line line = new Line();
            line.setStartX(0.0f);
            line.setStartY(20.0f);
            line.setEndX(0.0f);
            line.setEndY(50.0f);
            stack.getChildren().add(line);
            double positionX = (lineLength / 360) * i + 18.5;
            Label val = new Label(padding + s + "M" + padding);
            val.setFont(Font.font(Font.getFontNames().get(2), 15));
            stack.getChildren().add(val);
            val.setStyle("-fx-background-color: white;-fx-border-color: transparent transparent transparent transparent;-fx-border-width: 2px");
            val.setOpacity(1);
            stack.setAlignment(Pos.BASELINE_CENTER);
            stack.setLayoutX(positionX);
            stack.setLayoutY(50);
            //stack.setRotate(180);
            p.getChildren().add(stack);
        }

    }

    /**
     * Function to change a features radius across the entire List of Arcs
     */
    public void adjustFeatureRadius(String feature, double radius) {
        for (int i = 0; i < arcList.size(); i++) {
            if (arcList.get(i).getFeatureSet().getDescription().equals(feature)) {
                arcList.get(i).setArcRadius(radius);
            }
        }
    }

    /**
     * Function to change a features Color across the entire List of Arcs
     */
    public void adjustFeatureColor(String feature, Color c) {
        farcsGrouped.get(feature).forEach(fsetArc -> fsetArc.setArcStroke(c));

    }

    /**
     * Function to change the radius of all arcs
     */
    public void changeArcListRadius(double radius) {
        for (int i = 0; i < arcList.size(); i++) {
            arcList.get(i).setArcRadius(radius);
        }
    }

    /**
     * Function to change the center of all arcs
     */
    public void changeArcListCenter(double x, double y) {
        for (int i = 0; i < arcList.size(); i++) {
            arcList.get(i).setArcCenterX(x);
            arcList.get(i).setArcCenterY(y);
        }
    }

    /**
     * Function to draw only arcs of a given feature
     */
    public void paintFeature(ZoomFX p, String s, double offset, Group group, int radoffset,Color c) {

        farcsGrouped.get(s).forEach(fsetArc -> fsetArc.drawArc(p, (radoffset * 20 + 20), 5, 0, group,c));
        /*
        for (int i = 0; i < arcList.size(); i++) {
            if (arcList.get(i).getFeatureSet().getDescription().equals(s)) {

                arcList.get(i).drawArc(p, (radoffset * 20 + 20), 5, 0, group);
            }
        }
        */
    }







    /**
     * Function to draw only arcs of a given feature
     */
    public void paintJoin(Pane p, String s, double offset, Group group) {
        farcsGrouped.get(s).forEach(fsetArc -> fsetArc.drawJoinArc(p,20, 5, 0, group));
        /*
        for (int i = 0; i < arcList.size(); i++) {
            if (arcList.get(i).getFeatureSet().getDescription().equals(s)) {

                arcList.get(i).drawJoinArc(p,20, 5, 0, group);
            }
        }
        */
    }


    /**
     * Function to draw only arcs of a given feature
     */
    public void paintFeature(Pane p, String s, double offset) {
        for (int i = 0; i < arcList.size(); i++) {
            if (arcList.get(i).getFeatureSet().getDescription().equals(s)) {
                arcList.get(i).drawArc(p, 20, 5, offset);
            }
        }
    }

    public void drawJoinTics(String joinFeatureName, Color c, double centerX, double centerY, double radius, ZoomFX p) {
        List<FsetArc> joinList = new ArrayList<>();
       this.farcsGrouped.get(joinFeatureName).forEach(fsetArc -> {
           double drawn = fsetArc.getArcStartAngle();
            Boolean isConfirmed=false, manualVerified=false;
            final StringBuilder sb = new StringBuilder();
            sb.append("Feature Type: ")
                    .append(fsetArc.getFeatureSet().getType())
                    .append("\n")
                    .append("Feature Start: ")
                    .append(fsetArc.getFeatureSet().getStart())
                    .append("\n").append("Feature End: ")
                    .append(fsetArc.getFeatureSet().getEnd())
                    .append("\n").append("Feature Length: ")
                    .append(fsetArc.getFeatureSet().getLength())
                    .append("\n");
            Map <String,Qualifier> m = fsetArc.getFeatureSet().getFeatureQualifierMap();

            for (Map.Entry<String, Qualifier> entry : m.entrySet())
            {
                if(entry.getKey().equals("confirmed"))
                    if(entry.getValue().getValue().equals("Y"))
                        isConfirmed=true;
                if(entry.getKey().equals("join_verification"))
                    if(entry.getValue().getValue().equals("Manual"))
                        manualVerified=true;
                    if (entry.getValue().getValue().length() > 40){
                        sb.append(WordUtils.capitalize(entry.getKey()));
                        sb.append(": ");
                        sb.append(WordUtils.capitalize(entry.getValue().getValue().substring(0,20)));
                        sb.append("......");
                        sb.append("\n");
                    } else {
                        sb.append(WordUtils.capitalize(entry.getKey()));
                        sb.append(": ");
                        sb.append(WordUtils.capitalize(entry.getValue().getValue()));
                        sb.append("\n");
                    }

            }
           final JoinEditView jev = new JoinEditView(fsetArc.getFeatureSet());
            if(fsetArc.getFeatureSet().getSequence().length() > 40)
                sb.append("Sequence: ").append(fsetArc.getFeatureSet().getSequence().substring(0, 39)).append("...\n");
            else
                sb.append("Sequence: ").append(fsetArc.getFeatureSet().getSequence()).append("\n");
            String s = sb.toString();
            final Tooltip tp = new Tooltip(s);
            final Text dataText = new Text();
            dataText.setText(s);
            Image img;
            if(isConfirmed && manualVerified)
                img = new Image(this.getClass().getClassLoader().getResourceAsStream("DrawingBlue.png"));
            else
                img = new Image(this.getClass().getClassLoader().getResourceAsStream("DrawingRed.png"));


            ImageView imgView = new ImageView(img);
            imgView.setImage(img);
            imgView.setFitWidth(60);
            imgView.setPreserveRatio(true);
            imgView.setSmooth(true);
            imgView.setCache(true);
            imgView.setOnMouseEntered(event -> {
                Node node = (Node) event.getSource();
                tp.show(node,event.getScreenX() + 50, event.getScreenY());
            });

            imgView.setOnMouseExited(event -> tp.hide());
            imgView.setOnMouseClicked(event -> {
                final Stage secondStage = new Stage();
                secondStage.setTitle("Join Data");
                //StackPane loadGroup = new StackPane();
                GridPane loadGroup = jev.getPane();
                Scene loadScene = new Scene(jev.getPane());
                secondStage.setScene(loadScene);
                //loadGroup.getChildren().setAll(dataText);
                secondStage.show();
            });


            StackPane stack = new StackPane();
            stack.getChildren().add(imgView);
            Point XY = findXY(centerX, centerY, radius, drawn);
            stack.setPrefSize(10, 10);
            stack.setLayoutX(XY.getX());
            stack.setLayoutY(XY.getY());
            stack.setRotate(drawn + 270);
            joinStackList.add(stack);
            p.getContent().add(stack);

                stack.setVisible(true);
            joinList.add(fsetArc);

        });


        Circle circle = new Circle();
        circle.setCenterX(joinList.get(0).getArcCenterX());
        circle.setCenterY(joinList.get(0).getArcCenterY());
        circle.setRadius(joinList.get(0).getArcCenterX()-50);
        circle.setStroke(Color.PINK);
        circle.setFill(null);
        circle.setStrokeWidth(5);
        p.getContent().add(circle);
        for(int i = 1; i < joinList.size(); i++)
        {
            Arc arc = new Arc();
            arc.setCenterX(joinList.get(i).getArcCenterX());
            arc.setCenterY(joinList.get(i).getArcCenterX());
            arc.setRadiusX(joinList.get(i).getArcCenterX()-50);
            arc.setRadiusY(joinList.get(i).getArcCenterX()-50);

            double start = (joinList.get(i-1).calcLength());
            double end = (joinList.get(i).calcStart());
            arc.setStartAngle(360-start);
            arc.setLength(start-end);
            arc.setType(joinList.get(i).getArcType());
            arc.setFill(joinList.get(i).getArcFill());
            arc.setStroke(joinList.get(i).getArcStroke());
            arc.setStrokeWidth(5);
            arc.setFill(null);
            p.getContent().add(arc);
        }


    }

    public void hideJoinStack(int indx)
    {
        joinStackList.get(indx).setVisible(false);
    }
    public void showJoinStack(int indx)
    {
        joinStackList.get(indx).setVisible(true);
    }
    public void hideAllJoinStack()
    {
        for(int i=0;i<joinStackList.size();i++)
            joinStackList.get(i).setVisible(false);
    }
    public void showAllJoinStack()
    {
        for(int i=0;i<joinStackList.size();i++)
            joinStackList.get(i).setVisible(true);
    }



    /**
     * Draw the arcList
     */
    public void paintArcList(Pane p) {
        for (int i = 0; i < arcList.size(); i++) {
            arcList.get(i).drawArc(p);
        }
    }
    /** Setters and Getters */
    /**
     * Set arcList
     */
    public void setArcList(List<FsetArc> arcList) {
        this.arcList = arcList;
    }

    /**
     * Get arcList
     */
    public List<FsetArc> getArcList() {
        return arcList;
    }

    /**
     * Set Nucleotide Ratio
     */
    public void setNucleotideRatio(double nucleotideRatio) {
        this.nucleotideRatio = nucleotideRatio;
    }

    /**
     * Get Nucleotide Ratio
     */
    public double getNucleotideRatio() {
        return nucleotideRatio;
    }

}
