package edu.odu.cs.ujv.GUI.views;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by john on 10/30/15.
 */
public class TableColumnFactory {

    public static <S,T> TableColumn<S,T> makeTableColumn(String name,String onProperty){
        TableColumn<S,T> tc = new TableColumn<>(name);
        tc.setCellValueFactory(new PropertyValueFactory<>(onProperty));
        return tc;
    }
}

