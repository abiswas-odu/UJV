package edu.odu.cs.ujv.GUI.views.cells;

import edu.odu.cs.ujv.GUI.views.data.Join;
import javafx.scene.control.TableCell;

/**
 * Created by jberlin on 10/27/2015.
 */
public class IntegerEditCell extends TableCell<Join,Integer> {

    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null){
            setGraphic(null);
            setText(null);
        }else
            setText(item.toString());
    }

    @Override
    public void commitEdit(Integer newValue) {
        super.commitEdit(newValue);
        System.out.println(this.getTableRow().getItem().toString());
    }
}
