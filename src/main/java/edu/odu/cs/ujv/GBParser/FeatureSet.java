package edu.odu.cs.ujv.GBParser;


import edu.odu.cs.ujv.GUI.FeatureArc;
import edu.odu.cs.ujv.GUI.views.data.FeatureDisplayProxy;
import javafx.beans.property.*;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.Strand;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.features.Qualifier;
import org.biojava.nbio.core.sequence.location.template.Location;
import org.biojava.nbio.core.sequence.location.template.Point;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * This class represents a feature for a genome
 * @param <C> the {@code Compound} type the FeatureSet is associated with
 * @see Compound
 * @see FeatureInterface
 */
public class FeatureSet<C extends Compound> implements Comparable<FeatureSet<C>> {
    private StringProperty  description;
    private StringProperty  type;
    private IntegerProperty start;
    private IntegerProperty end;
    private IntegerProperty length;
    private BooleanProperty isComplement;
    private BooleanProperty isSelected;
    private String nucleotideSeq;
    private Strand featureStrand;
    private Location featureLocation;
    private FeatureInterface<AbstractSequence<C>, C> genbankRepresintation;
    private Map<String,Qualifier> featureQualifierMap;


    /**
     * Create a new instance of FeatureSet
     */
    public FeatureSet(){}

    /**
     * Create a new instance of FeatureSet
     * @param theSequence the entire sequence
     * @param featureInterface the feature this featureSet is associated with
     * @see FeatureInterface
     * @see DNASequence
     */
    public FeatureSet(DNASequence theSequence, FeatureInterface<AbstractSequence<C>, C> featureInterface) {
        this.description = new SimpleStringProperty(featureInterface.getDescription());
        this.featureLocation = featureInterface.getLocations();
        this.featureQualifierMap = featureInterface.getQualifiers();
        this.featureStrand = this.featureLocation.getStrand();
        this.nucleotideSeq = theSequence
                .getSequenceAsString(featureLocation.getStart().getPosition(),
                        featureLocation.getEnd().getPosition(),this.featureStrand);
        this.isComplement = new SimpleBooleanProperty(this.featureStrand.getStringRepresentation().equals("-"));
        this.genbankRepresintation = featureInterface;
        this.start = new SimpleIntegerProperty(getStart());
        this.end = new SimpleIntegerProperty(getEnd());
        this.isSelected = new SimpleBooleanProperty(false);
        this.type = new SimpleStringProperty(getType());
        this.length = new SimpleIntegerProperty(getLength());
    }

    /**
     * Get the description of this {@code FeatureSet}
     * @return the description of this {@code FeatureSet}
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Set the description for this FeatureSet
     * @param description the description of this {@code FeatureSet}
     */
    public void setDescription(String description) {
        this.description.setValue(description);
    }

    /**
     * Get the sequence associated with this {@code FeatureSet}
     * @return the sequence for this {@code FeatureSet}
     */
    public String getSequence() {
        return nucleotideSeq;
    }

    public void setSequence(String nucleotideSeq) {
        this.nucleotideSeq = nucleotideSeq;
    }

    public boolean isComplement() {
        return isComplement.get();
    }

    /**
     * Set the flag denoting if this {@code FeatureSet} is an complement
     * @param isComplement {@code true}if this {@code featureSet} is an complement {@code false} otherwise
     */
    public void setIsComplement(boolean isComplement) {
        this.isComplement.setValue(isComplement);
    }

    /**
     * Get the location object associated with this feature
     * @return {@link Location} of this {@code FeatureSet}
     */
    public Location getFeatureLocation() {
        return featureLocation;
    }

    public void setFeatureLocation(Location featureLocation) {
        this.featureLocation = featureLocation;
    }

    /**
     * Get the attributes({@code Qualifier}) of this {@link FeatureSet}
     * @return the {@link Map}{@code<String,Qualifier>}  containing the {@code Qualifier} of this feature set
     * @see Qualifier
     */
    public Map<String, Qualifier> getFeatureQualifierMap() {
        return featureQualifierMap;
    }

    public void setFeatureQualifierMap(Map<String, Qualifier> featureQualifierMap) {
        this.featureQualifierMap = featureQualifierMap;
    }

    public Set<Map.Entry<String,Qualifier>> getMappedQualifiers(){
        return this.featureQualifierMap.entrySet();
    }

    /**
     * Get a Qualifier mapped to this {@code FeatureSet}
     * @param qualifierKey the key to Qualifier
     * @return {@code Optional<Qualifier>} the qualifier mapped to the qualifierKey
     * @see Optional
     * @see Qualifier
     */
    public Optional<Qualifier> getQualifier(String qualifierKey) {
        return Optional.ofNullable(this.featureQualifierMap.get(qualifierKey));
    }

    /**
     * Get the {@code Strand} associated with this {@code FeatureSet}
     * @return the {@code Strand} associated with this {@code FeatureSet}
     * @see Strand
     */
    public Strand getFeatureStrand() {
        return featureStrand;
    }

    /**
     * Set the {@code Strand} associated with this {@code FeatureSet}
     * @param featureStrand the {@code Strand} associated with this {@code FeatureSet}
     */
    public void setFeatureStrand(Strand featureStrand) {
        this.featureStrand = featureStrand;
    }

    public FeatureInterface<AbstractSequence<C>, C> getGenbankRepresintation() {
        return genbankRepresintation;
    }

    public void setGenbankRepresintation(FeatureInterface<AbstractSequence<C>, C> genbankRepresintation) {
        this.genbankRepresintation = genbankRepresintation;
    }

    /**
     * Get the sequence associated with this {@link FeatureSet}
     * @return the sequence associated with this {@code FeatureSet}
     */
    public String getNucleotideSeq() {
        return nucleotideSeq;
    }

    public void setNucleotideSeq(String nucleotideSeq) {
        this.nucleotideSeq = nucleotideSeq;
    }

    /**
     * Start of the location for this feature
     */
    public Point getStartPoint(){
        return this.featureLocation.getStart();
    }

    public Integer getStart(){
        return this.featureLocation.getStart().getPosition();
    }

    public Integer getEnd(){
        return this.featureLocation.getEnd().getPosition();
    }

    public Point getEndPoint() {
        return this.featureLocation.getEnd();
    }

    public Integer getLength(){
        return this.featureLocation.getLength();
    }

    public String getSource(){
        return this.genbankRepresintation.getSource();
    }

    public String getType(){
        return this.genbankRepresintation.getType();
    }

    /**
     * Returns true if the position of this feature is meant to represent a point between
     * two points such as 78^79. Only valid if start and stop are next to
     * each other.
     */
    public boolean isBetweenCompounds() {
        return this.featureLocation.isBetweenCompounds();
    }

    /**
     * Returns true if the location of the feature is considered to be complex meaning
     * the location is actually composed of sub-locations.
     */
    public boolean isComplex() {
        return this.featureLocation.isComplex();
    }



    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty typeProperty() {
        return type;
    }

    public IntegerProperty startProperty() {
        return start;
    }

    public IntegerProperty endProperty() {
        return end;
    }

    public IntegerProperty lengthProperty() {
        return length;
    }

    public BooleanProperty isComplementProperty() {
        return isComplement;
    }

    public BooleanProperty isSelectedProperty() {
        return isSelected;
    }


    public FeatureArc<C> makeFeaureArc(){
        return new FeatureArc<>(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeatureSet)) return false;

        FeatureSet<?> that = (FeatureSet<?>) o;
        if (isComplement != that.isComplement) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (nucleotideSeq != null ? !nucleotideSeq.equals(that.nucleotideSeq) : that.nucleotideSeq != null)
            return false;
        if (featureStrand != that.featureStrand) return false;
        if (featureLocation != null ? !featureLocation.equals(that.featureLocation) : that.featureLocation != null)
            return false;
        if (genbankRepresintation != null ? !genbankRepresintation.equals(that.genbankRepresintation) : that.genbankRepresintation != null)
            return false;
        return !(featureQualifierMap != null ? !featureQualifierMap.equals(that.featureQualifierMap) : that.featureQualifierMap != null);
    }



    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (nucleotideSeq != null ? nucleotideSeq.hashCode() : 0);
        result = 31 * result + (featureStrand != null ? featureStrand.hashCode() : 0);
        result = 31 * result + (isComplement.get() ? 1 : 0);
        result = 31 * result + (featureLocation != null ? featureLocation.hashCode() : 0);
        result = 31 * result + (genbankRepresintation != null ? genbankRepresintation.hashCode() : 0);
        result = 31 * result + (featureQualifierMap != null ? featureQualifierMap.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(FeatureSet<C> otherFeatureSet) {
        int comp = this.getStart().compareTo(otherFeatureSet.getStart());
        if(comp != 0)
            return comp;
        else
            return this.getEnd().compareTo(otherFeatureSet.getEnd());
    }

    @Override
    public String toString(){
        return this.nucleotideSeq;
    }
}
