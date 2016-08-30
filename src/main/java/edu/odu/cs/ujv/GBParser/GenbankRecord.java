package edu.odu.cs.ujv.GBParser;


import edu.odu.cs.ujv.GUI.FeatureArc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.biojava.nbio.core.sequence.AccessionID;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.DNASequence.DNAType;
import org.biojava.nbio.core.sequence.template.Compound;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @param <C>
 */
public class GenbankRecord<C extends Compound> implements Iterable<FeatureSet<C>> {
    private String recordName;
    private DNASequence genBankRecordSequence;
    private List<FeatureSet<C>> recordFeatureList;
    private List<String> featureNames;
    private Map<String, ObservableList<FeatureSet<C>>> grouped;
    private Map<String, ObservableList<FeatureArc<C>>> arcsGrouped;

    public GenbankRecord(String recordName,DNASequence genBankRecordSequence, List<FeatureSet<C>> recordFeatureList,
                         Collection<String> featureNames){
        this.genBankRecordSequence = genBankRecordSequence;
        this.recordFeatureList = recordFeatureList;
        this.recordName = recordName;
        this.featureNames = new ArrayList<>(featureNames);
        this.grouped = this.recordFeatureList.stream()
                .collect(Collectors.groupingBy(FeatureSet::getDescription,
                                Collectors.toCollection(FXCollections::observableArrayList)));
        /*
        this.arcsGrouped = this.recordFeatureList.stream()
                .collect(Collectors.groupingBy(FeatureSet::getDescription,
                        Collectors.mapping(FeatureSet::makeFeaureArc,
                                Collectors.toCollection(FXCollections::observableArrayList))));
                                */
    }

    public String getRecordName(){
        return this.recordName;
    }

    public int getGCCount(){
       return this.genBankRecordSequence.getGCCount();
    }

    public AccessionID getAccensionID(){
        return this.genBankRecordSequence.getAccession();
    }

    public DNAType getDNAType(){
        return this.genBankRecordSequence.getDNAType();
    }

    public List<FeatureSet<C>> getRecordFeatureList(){
        return this.recordFeatureList;
    }

    public List<FeatureSet<C>> getFeaturesByDescription(String description){
        return this.recordFeatureList.stream()
                .filter(fs -> fs.getDescription().equals(description)).collect(Collectors.toList());
    }

    public List<FeatureSet<C>> getFeaturesByType(String type){
        return this.recordFeatureList.parallelStream()
                .filter(fs -> fs.getType().equals(type)).sorted().collect(Collectors.toList());
    }

    public List<FeatureSet<C>> getFeaturesByPredicate(Predicate<? super FeatureSet<C>> by){
        return this.recordFeatureList.stream().filter(by).collect(Collectors.toList());
    }

    public List<String> getFeatureNameList(){
        return featureNames;
    }


    public Optional<ObservableList<FeatureSet<C>>> getFeaturesGroupedByName(String name) {
        return Optional.ofNullable(grouped.get(name));
    }


    public Map<String,ObservableList<FeatureSet<C>>> getFeaturesGrouped(){
        return grouped;
    }
    /**
     * Get each feature in the record group by some classifier
     * @param classifier the grouping classifier
     * @param <T> the type of the grouped by
     * @return {@code Map<T,List<FeatureSet<C>>>}
     * @see Collectors#groupingBy(Function)
     */
    public <T> Map<T,List<FeatureSet<C>>> getFeaturesGroupBy(Function<? super FeatureSet<C>,T> classifier){
        return this.recordFeatureList.stream().collect(Collectors.groupingBy(classifier));
    }

    public DNASequence getGenBankRecordSequence() {
        return genBankRecordSequence;
    }

    @Override
    public Iterator<FeatureSet<C>> iterator() {
        return this.recordFeatureList.iterator();
    }

    @Override
    public Spliterator<FeatureSet<C>> spliterator() {
        return Spliterators.spliterator(this.recordFeatureList.iterator(),
                this.recordFeatureList.size(), Spliterator.SIZED);
    }

    public Stream<FeatureSet<C>> stream(){
        return StreamSupport.stream(spliterator(),false);
    }

    public Stream<FeatureSet<C>> parallelStream(){
        return StreamSupport.stream(spliterator(),true);
    }

    public Map<String,ObservableList<FeatureArc<C>>> getArcsGrouped(){
        return this.arcsGrouped;
    }

    @Override
    public String toString() {
        return this.recordName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenbankRecord)) return false;

        GenbankRecord<?> that = (GenbankRecord<?>) o;

        if (recordName != null ? !recordName.equals(that.recordName) : that.recordName != null) return false;
        if (genBankRecordSequence != null ? !genBankRecordSequence.equals(that.genBankRecordSequence) : that.genBankRecordSequence != null)
            return false;
        if (recordFeatureList != null ? !recordFeatureList.equals(that.recordFeatureList) : that.recordFeatureList != null)
            return false;
        return !(featureNames != null ? !featureNames.equals(that.featureNames) : that.featureNames != null);

    }

    @Override
    public int hashCode() {
        int result = recordName != null ? recordName.hashCode() : 0;
        result = 31 * result + (genBankRecordSequence != null ? genBankRecordSequence.hashCode() : 0);
        result = 31 * result + (recordFeatureList != null ? recordFeatureList.hashCode() : 0);
        result = 31 * result + (featureNames != null ? featureNames.hashCode() : 0);
        return result;
    }
}
