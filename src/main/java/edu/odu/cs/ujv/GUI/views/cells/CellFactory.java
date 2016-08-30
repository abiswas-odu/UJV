package edu.odu.cs.ujv.GUI.views.cells;

import edu.odu.cs.ujv.GBParser.FeatureSet;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import org.biojava.nbio.core.sequence.template.Compound;

/**
 * Created by jberlin on 10/27/2015.
 */
public class CellFactory {
    public static <C extends Compound> TableCell<FeatureSet<C>,Integer> makeIntegerCell(){
        return new IntegerCell<>();
    }

    public static <C extends Compound> TableCell<FeatureSet<C>,String> makeStringCell(){
        return new StringCell<>();
    }

    public static <S,T> PropertyValueFactory<S,T> makePropertyValueFactory(String onProperty){
        return new PropertyValueFactory<>(onProperty);
    }


}
