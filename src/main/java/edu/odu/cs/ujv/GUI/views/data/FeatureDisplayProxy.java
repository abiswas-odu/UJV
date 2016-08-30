package edu.odu.cs.ujv.GUI.views.data;

import edu.odu.cs.ujv.GBParser.FeatureSet;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.biojava.nbio.core.sequence.template.Compound;


/**
 * Created by john on 10/30/15.
 */
public class FeatureDisplayProxy<C extends Compound> {
    private FeatureSet<C> featureSet;
    private StringProperty  description;
    private StringProperty  type;
    private IntegerProperty start;
    private IntegerProperty end;
    private IntegerProperty length;
    private BooleanProperty isComplement;
    private BooleanProperty isSelected;

    public FeatureDisplayProxy(FeatureSet<C> featureSet){
        this.featureSet = featureSet;
        this.description = new SimpleStringProperty(featureSet.getDescription());
        this.start = new SimpleIntegerProperty(featureSet.getStart());
        this.end = new SimpleIntegerProperty(featureSet.getEnd());
        this.isComplement = new SimpleBooleanProperty(featureSet.isComplement());
        this.isSelected = new SimpleBooleanProperty(false);
        this.type = new SimpleStringProperty(featureSet.getType());
        this.length = new SimpleIntegerProperty(featureSet.getLength());
    }

    public FeatureSet<C> getFeatureSet() {
        return featureSet;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public int getStart() {
        return start.get();
    }

    public IntegerProperty startProperty() {
        return start;
    }

    public int getEnd() {
        return end.get();
    }

    public IntegerProperty endProperty() {
        return end;
    }

    public boolean getIsComplement() {
        return isComplement.get();
    }

    public BooleanProperty isComplementProperty() {
        return isComplement;
    }

    public boolean getIsSelected() {
        return isSelected.get();
    }

    public BooleanProperty isSelectedProperty() {
        return isSelected;
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public int getLength() {
        return length.get();
    }

    public IntegerProperty lengthProperty() {
        return length;
    }

    public void addIsSeletedChangeListener(ChangeListener<? super Boolean> listener){
        this.isSelected.addListener(listener);
    }

}

