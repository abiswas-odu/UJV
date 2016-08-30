package edu.odu.cs.ujv.GUI.views.cells;

import edu.odu.cs.ujv.GBParser.FeatureSet;
import javafx.scene.control.TableCell;
import org.biojava.nbio.core.sequence.template.Compound;

/**
 * Created by jberlin on 10/27/2015.
 */
public class StringCell<C extends Compound> extends TableCell<FeatureSet<C>,String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null){
            setGraphic(null);
            setText(null);
        }else
            setText(item);
    }

}
