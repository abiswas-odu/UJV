package edu.odu.cs.ujv.GUI.views.data;

import edu.odu.cs.ujv.GBParser.FeatureSet;
import edu.odu.cs.ujv.GUI.CenterPane;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.Qualifier;

import java.util.Map;

/**
 * Created by jberlin on 10/26/2015.
 */
public class Join {
    private FeatureSet<NucleotideCompound> featureSet;
    private BooleanProperty selected;
    private StringProperty  jName;
    private IntegerProperty gapStart;
    private IntegerProperty gapEnd;
    private IntegerProperty jStart;
    private IntegerProperty jEnd;
    private StringProperty evidence;

    public Join(FeatureSet<NucleotideCompound> featureSet) {
        this.featureSet = featureSet;
        Boolean isConfirmed=false, manualVerified=false;
        String jEvidence = "",gapStart="-1",gapEnd="-1";
        for (Map.Entry<String, Qualifier> entry : featureSet.getFeatureQualifierMap().entrySet())
        {
            if(entry.getKey().compareTo("confirmed")==0)
                if(entry.getValue().getValue().compareTo("Y")==0)
                    isConfirmed=true;
            if(entry.getKey().compareTo("join_verification")==0)
                if(entry.getValue().getValue().compareTo("Manual")==0)
                    manualVerified=true;
            if(entry.getKey().compareTo("join_evidence")==0)
                jEvidence=entry.getValue().getValue();
            if(entry.getKey().compareTo("gap_start")==0)
                gapStart=entry.getValue().getValue();
            if(entry.getKey().compareTo("gap_end")==0)
                gapEnd=entry.getValue().getValue();
            if(entry.getKey().equals("join_evidence")){
                this.jName = new SimpleStringProperty(entry.getValue().getValue());
                this.evidence = new SimpleStringProperty(entry.getValue().getValue());
            }
        }

        this.selected = new SimpleBooleanProperty(true);
        this.gapStart = new SimpleIntegerProperty(Integer.parseInt(gapStart));
        this.gapEnd = new SimpleIntegerProperty(Integer.parseInt(gapEnd));
        this.jStart = new SimpleIntegerProperty(this.featureSet.getStart());
        this.jEnd = new SimpleIntegerProperty(this.featureSet.getEnd());
        //this.jName = new SimpleStringProperty(jEvidence);
    }

    public Join(FeatureSet<NucleotideCompound> featureSet, CenterPane viewPane, int indx) {
        this.featureSet = featureSet;
        Boolean isConfirmed=false, manualVerified=false;
        String jEvidence = "",gapStart="-1",gapEnd="-1";
        for (Map.Entry<String, Qualifier> entry : featureSet.getFeatureQualifierMap().entrySet())
        {
            if(entry.getKey().compareTo("confirmed")==0)
                if(entry.getValue().getValue().compareTo("Y")==0)
                    isConfirmed=true;
            if(entry.getKey().compareTo("join_verification")==0)
                if(entry.getValue().getValue().compareTo("Manual")==0)
                    manualVerified=true;
            if(entry.getKey().compareTo("join_evidence")==0)
                jEvidence=entry.getValue().getValue();
            if(entry.getKey().compareTo("gap_start")==0)
                gapStart=entry.getValue().getValue();
            if(entry.getKey().compareTo("gap_end")==0)
                gapEnd=entry.getValue().getValue();
            if(entry.getKey().equals("join_evidence")){
                this.jName = new SimpleStringProperty(entry.getValue().getValue());
                this.evidence = new SimpleStringProperty(entry.getValue().getValue());
            }
        }

        this.selected = new SimpleBooleanProperty(!(isConfirmed && manualVerified));
        this.gapStart = new SimpleIntegerProperty(Integer.parseInt(gapStart));
        this.gapEnd = new SimpleIntegerProperty(Integer.parseInt(gapEnd));
        this.jStart = new SimpleIntegerProperty(this.featureSet.getStart());
        this.jEnd = new SimpleIntegerProperty(this.featureSet.getEnd());
        this.selected.addListener((ov, t, t1) -> {
            if(t1)
                viewPane.showJoinTick(indx);
            else
                viewPane.hideJoinTick(indx);
        });

        //this.jName = new SimpleStringProperty(jEvidence);
    }

    public Join(FeatureSet<NucleotideCompound> featureSet,boolean sel, String jstr, int gapS, int gapE, int ovlS, int ovlE){
        this.featureSet = featureSet;
        this.selected = new SimpleBooleanProperty(sel);
        this.gapStart = new SimpleIntegerProperty(gapS);
        this.gapEnd = new SimpleIntegerProperty(gapE);
        this.jStart = new SimpleIntegerProperty(ovlS);
        this.jEnd = new SimpleIntegerProperty(ovlE);
        this.jName = new SimpleStringProperty(jstr);
    }

    public boolean getSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public String getjName() {
        return jName.get();
    }

    public StringProperty jNameProperty() {
        return jName;
    }

    public void setjName(String jName) {
        this.jName.set(jName);
    }

    public int getGapStart() {
        return gapStart.get();
    }

    public IntegerProperty gapStartProperty() {
        return gapStart;
    }

    public void setGapStart(int gapStart) {
        this.gapStart.set(gapStart);
    }

    public int getGapEnd() {
        return gapEnd.get();
    }

    public IntegerProperty gapEndProperty() {
        return gapEnd;
    }

    public void setGapEnd(int gapEnd) {
        this.gapEnd.set(gapEnd);
    }

    public int getjStart() {
        return jStart.get();
    }

    public IntegerProperty jStartProperty() {
        return jStart;
    }

    public void setjStart(int jStart) {
        this.jStart.set(jStart);
    }

    public int getjEnd() {
        return jEnd.get();
    }

    public IntegerProperty jEndProperty() {
        return jEnd;
    }

    public void setjEnd(int jEnd) {
        this.jEnd.set(jEnd);
    }

    public FeatureSet<NucleotideCompound> getFeatureSet() {
        return featureSet;
    }

    public void setFeatureSet(FeatureSet<NucleotideCompound> featureSet) {
        this.featureSet = featureSet;
    }

    public String getEvidence() {
        return evidence.get();
    }

    public StringProperty evidenceProperty() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence.set(evidence);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Join)) return false;

        Join join = (Join) o;

        if (selected != null ? !selected.equals(join.selected) : join.selected != null) return false;
        if (jName != null ? !jName.equals(join.jName) : join.jName != null) return false;
        if (gapStart != null ? !gapStart.equals(join.gapStart) : join.gapStart != null) return false;
        if (gapEnd != null ? !gapEnd.equals(join.gapEnd) : join.gapEnd != null) return false;
        if (jStart != null ? !jStart.equals(join.jStart) : join.jStart != null) return false;
        return !(jEnd != null ? !jEnd.equals(join.jEnd) : join.jEnd != null);

    }

    @Override
    public int hashCode() {
        int result = selected != null ? selected.hashCode() : 0;
        result = 31 * result + (jName != null ? jName.hashCode() : 0);
        result = 31 * result + (gapStart != null ? gapStart.hashCode() : 0);
        result = 31 * result + (gapEnd != null ? gapEnd.hashCode() : 0);
        result = 31 * result + (jStart != null ? jStart.hashCode() : 0);
        result = 31 * result + (jEnd != null ? jEnd.hashCode() : 0);
        return result;
    }
}
