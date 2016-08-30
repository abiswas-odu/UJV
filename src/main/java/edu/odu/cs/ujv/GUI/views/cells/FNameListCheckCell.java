package edu.odu.cs.ujv.GUI.views.cells;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.cell.CheckBoxListCell;

/**
 * Created by jberlin on 10/31/2015.
 */
public class FNameListCheckCell extends CheckBoxListCell<String> {
    private BooleanProperty selected;
    public FNameListCheckCell(BooleanProperty selected){
        super(p -> selected);
        this.selected = selected;
    }

    public void addCheckSelectionListener(ChangeListener<? super Boolean> changeListener){
        this.selected.addListener(changeListener);
    }

    public void unCheck(){
        this.selected.setValue(false);
    }

    public boolean isChecked(){
        return this.selected.get();
    }

    public void check(){
        this.selected.setValue(true);
    }
}
