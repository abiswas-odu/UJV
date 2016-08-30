package edu.odu.cs.ujv.GUI;


import edu.odu.cs.ujv.GBParser.FeatureSet;
import edu.odu.cs.ujv.GUI.views.data.FeatureDisplayProxy;
import javafx.beans.property.BooleanProperty;
import javafx.scene.shape.Arc;
import org.biojava.nbio.core.sequence.template.Compound;

/**
 * Created by jberlin on 10/9/2015.
 */
public class FeatureArc<C extends Compound> extends Arc {
    private FeatureSet<C> arcFeatureSet;
    private double arcRadius;
    private double ratio;
    private BooleanProperty isSelected;

    public FeatureArc(FeatureSet<C> arcFeatureSet){
        this.arcFeatureSet = arcFeatureSet;
        this.isSelected = arcFeatureSet.isSelectedProperty();
    }

    public FeatureArc(FeatureSet<C> arcFeatureSet, double radius, double centerX, double centerY, double ratio){
        super();
        this.isSelected = arcFeatureSet.isSelectedProperty();
        this.arcFeatureSet = arcFeatureSet;
        this.arcRadius = radius;
        this.ratio = ratio;
        setCenterX(centerX);
        setCenterY(centerY);
        setRadiusX(radius);
        setRadiusY(radius);
    }

    public void changeDraw(double radiusOffset, double StrokeW, double offset){
        setRadiusX(this.arcRadius - radiusOffset);
        setRadiusY(this.arcRadius - radiusOffset);
        double start = (calcStart() + offset);
        if (start > 360) {
            start = start - 360;
        }
        double end = (calcLength() - start + offset);
        if (end > 360) {
            end = end - 360;
        }
        setStartAngle(360 - start);
        // arcStartAngle + offset
        setLength(end);
        //arcLength - arcStartAngle + offset
        setStrokeWidth(StrokeW);
    }

    public void drawAsJoinArch(double radiusOffset, double StrokeW, double offset){

    }

    public double calcLength() {
        return this.arcFeatureSet.getEnd() / this.ratio;
    }


    public double calcStart() {
        return this.arcFeatureSet.getStart() / this.ratio;
    }

    public String getFeatureDesicription(){
        return this.arcFeatureSet.getDescription();
    }

    public boolean featureDescriptionEquals(String description){
        return this.arcFeatureSet.getDescription().equals(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeatureArc)) return false;

        FeatureArc<?> that = (FeatureArc<?>) o;

        if (Double.compare(that.arcRadius, arcRadius) != 0) return false;
        if (Double.compare(that.ratio, ratio) != 0) return false;
        return !(arcFeatureSet != null ? !arcFeatureSet.equals(that.arcFeatureSet) : that.arcFeatureSet != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = arcFeatureSet != null ? arcFeatureSet.hashCode() : 0;
        temp = Double.doubleToLongBits(arcRadius);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ratio);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
