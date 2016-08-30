package edu.odu.cs.ujv.GUI;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.odu.cs.ujv.GBParser.FeatureSet;
import edu.odu.cs.ujv.GBParser.FileLoadingService;
import edu.odu.cs.ujv.GBParser.GenbankRecord;
import edu.odu.cs.ujv.GUI.views.GenomeTab;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import edu.odu.cs.ujv.GUI.views.data.Join;

/*
TODO Color choosing options
TODO CheckBocks are in a list per checkBock group
TODO Continual redrawing function that allows custimaztion of what is redrawn
TODO HighRez screencap: center area 600dbpi +, tiff gif, png, jpg
TODO Sidepanel worked out
check out chimarea

*/
public class StartGUI extends Application {
    private static final Logger logger = LogManager.getLogger(StartGUI.class);
    protected TabPane tabPane;
    public static void main(String[] args) {
        Application.launch(StartGUI.class, args);
    }

    @Override
    public void start(Stage primaryStage) {
        tabPane = new TabPane();
        final Tab newTab = new Tab("+");
        newTab.setClosable(false);
        tabPane.getTabs().add(newTab);
        createAndSelectNewTab(tabPane, "New Tab", primaryStage);


        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldSelectedTab, newSelectedTab) -> {
            if (newSelectedTab == newTab) {
                createAndSelectNewTab(tabPane, "New Tab " + (tabPane.getTabs().size()), primaryStage);
            } else {
                System.out.println("Tab Selection changed" + tabPane.getSelectionModel().getSelectedIndex());
            }
        });


        final BorderPane root = new BorderPane();
        root.setCenter(tabPane);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        scene.setOnKeyPressed(keyPress->{
            KeyCode keyCode = keyPress.getCode();
            GenomeTab gt = (GenomeTab)this.tabPane.getSelectionModel().getSelectedItem();
            if(keyCode.equals(KeyCode.ADD) || keyCode.equals(KeyCode.EQUALS)){
                gt.zoom();
            } else if(keyCode.equals(KeyCode.MINUS) || keyCode.equals(KeyCode.SUBTRACT)){
                gt.zoomOut();
            } else if (keyCode.equals(KeyCode.SPACE)){
                gt.resetZoom();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("Unverified Join Viewer");
        primaryStage.show();
    }


    private Tab createAndSelectNewTab(final TabPane tabPane, final String title, Stage primaryStage) {
        Tab tab = new GenomeTab(title);
        tabPane.getTabs().add(tab);
        tab.closableProperty().bind(Bindings.size(tabPane.getTabs()).greaterThan(2));
        tabPane.getSelectionModel().select(tab);
        return tab;
    }
}
