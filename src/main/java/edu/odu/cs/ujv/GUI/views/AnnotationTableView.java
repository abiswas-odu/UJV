package edu.odu.cs.ujv.GUI.views;

import edu.odu.cs.ujv.GBParser.FeatureSet;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.biojava.nbio.core.sequence.template.Compound;

import java.util.Map;

/**
 * Created by john on 10/30/15.
 */
public class AnnotationTableView<C extends Compound> extends TableView<FeatureSet<C>> {
    private Map<String,ObservableList<FeatureSet<C>>> featuresToDisplay;

    public AnnotationTableView(Map<String,ObservableList<FeatureSet<C>>> featuresToDisplay) {
        super();
        this.featuresToDisplay = featuresToDisplay;
        TableColumn<FeatureSet<C>,String>  type = TableColumnFactory.makeTableColumn("Type","type");
        TableColumn<FeatureSet<C>,Integer> start = TableColumnFactory.makeTableColumn("Start","start");
        TableColumn<FeatureSet<C>,Integer> end = TableColumnFactory.makeTableColumn("End","end");
        TableColumn<FeatureSet<C>,Integer> len = TableColumnFactory.makeTableColumn("Length","length");
        TableColumn<FeatureSet<C>,Boolean> isComp = TableColumnFactory.makeTableColumn("Complement","isComplement");
        this.getColumns().addAll(type,start,end,len,isComp);
    }

    public AnnotationTableView() {
        super();
        TableColumn<FeatureSet<C>,String> type = TableColumnFactory.makeTableColumn("Type","type");
        TableColumn<FeatureSet<C>,Integer> start = TableColumnFactory.makeTableColumn("Start","start");
        TableColumn<FeatureSet<C>,Integer> end = TableColumnFactory.makeTableColumn("End","end");
        TableColumn<FeatureSet<C>,Integer> len = TableColumnFactory.makeTableColumn("Length","length");
        //TableColumn<FeatureDisplayProxy<C>,Boolean> isComp = TableColumnFactory.makeTableColumn("Complement","isComplement");
        this.getColumns().addAll(type,start,end,len);
    }

    public void setFeaturesToDisplay(Map<String, ObservableList<FeatureSet<C>>> featuresToDisplay) {
        this.featuresToDisplay = featuresToDisplay;
    }

    public void display(String name){
        if(this.featuresToDisplay.containsKey(name)){
            this.setItems(featuresToDisplay.get(name));
            //this.refresh();
        }
    }
}
