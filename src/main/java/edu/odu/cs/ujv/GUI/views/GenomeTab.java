package edu.odu.cs.ujv.GUI.views;

import edu.odu.cs.ujv.GBParser.FeatureSet;
import edu.odu.cs.ujv.GBParser.FileLoadingService;
import edu.odu.cs.ujv.GBParser.GenbankRecord;
import edu.odu.cs.ujv.GUI.CenterPane;
import edu.odu.cs.ujv.GUI.FeatureArc;
import edu.odu.cs.ujv.GUI.FsetArcController;
import edu.odu.cs.ujv.GUI.views.cells.FNameListCheckCell;
import edu.odu.cs.ujv.GUI.views.data.Join;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jberlin on 10/4/2015.
 */
public class GenomeTab extends Tab {
    private static Logger logger = LogManager.getLogger(GenomeTab.class);
    final private int widthReduce = 370;
    final private int heightReduce = 230;
    private VBox tabVBox;
    private GridPane tabGrid;
    private Pane topPane;
    private Pane genomeDisplayPane;
    private Pane lSideMidPane;
    private Pane lSideTopPane;
    private Button zoomOut;
    private Button zoomIn;
    private CheckBox joinAllCheck;
    private CheckBox joinNoneCheck;
    private CheckBox featureAllCheck;
    private CheckBox featureNoneCheck;
    //this was line 292 in startgui
    private ComboBox<String> featureComoboBox;
    private Label joinCountLabel;
    private Label featureCountLabel;
    private MenuBar tabMenuBar;
    private FileLoadingService fileLoadingService;
    private GenbankRecord<NucleotideCompound> genbankRecord;
    private List<FeatureSet<NucleotideCompound>> featureList;
    private List<String> featureNameList;
    private VBox featureNameBox;
    private TableView<Join> joinTableView;
    private AnnotationTableView<NucleotideCompound> annotationTableView;
    private ListView<String> featureNameLV;
    private List<FNameListCheckCell> fNameListCheckCells;
    private double widthofsidePanes = 0;
    private double heightofvertPanes = 0;
    private double radiusofPane;
    private double nucleotideRatio;
    private Map<String,Color> fColor;
            /*System.getProperty("os.name").contains("Windows") ?
            "A:\\UJV\\Unverified_Join_Viewer\\src\\main\\resources\\NC_010612.gbk" :
            "/home/john/IdeaProjects/Unverified_Join_Viewer/src/main/resources/NC_010612.gbk";
            */
    private String filePath;
    private FsetArcController genomeDrawer;
    private CenterPane centerPane;
    private boolean loaded;

    public GenomeTab(String title) {
        super(title);
        this.filePath = this.getClass().getClassLoader().getResource("SC89.gb").getFile();
        this.fileLoadingService = new FileLoadingService();
        this.tabVBox = new VBox();
        this.setContent(this.tabVBox);
        this.tabGrid = new GridPane();
        makeMenus();
        makeDataViews(Screen.getPrimary().getVisualBounds().getWidth(),
                Screen.getPrimary().getVisualBounds().getHeight());
        loaded = false;
    }

    public CenterPane getCenterPane(){
        return this.centerPane;
    }





    private void loadAction() {
        final Stage secondStage = new Stage();
        secondStage.setTitle("Loading file");
        Group loadGroup = new Group();
        Scene loadScene = new Scene(loadGroup, 300, 100);
        secondStage.setScene(loadScene);
        ProgressBar progressBar = new ProgressBar();
        loadGroup.getChildren().add(progressBar);
        progressBar.setPrefWidth(300);
        progressBar.setPrefHeight(100);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        secondStage.show();
        File fileToLoad = new File(filePath);//fileChooser.showOpenDialog(secondStage);
        if (fileToLoad.exists())
            System.out.println("Its there");
        if (fileToLoad != null) {
            fileLoadingService.setFileToLoad(fileToLoad);
            fileLoadingService.setOnSucceeded(success -> {
                System.out.println("I won");
                @SuppressWarnings("unchecked")
                List<GenbankRecord<NucleotideCompound>> genbankRecords =
                        (List<GenbankRecord<NucleotideCompound>>) success.getSource().getValue();
                genbankRecord = genbankRecords.get(0);
                List<String> featureNameList = genbankRecord.getFeatureNameList();
                fNameListCheckCells = new ArrayList<>(featureNameList.size() + 1);
                featureCountLabel.setText("Feature Count: " + featureNameList.size() + "    ");
                this.annotationTableView.setFeaturesToDisplay(genbankRecord.getFeaturesGrouped());
                this.annotationTableView.display("misc_feature");
                this.populateAnnotationLV(featureNameList);
                String tabNameStr = fileToLoad.getName().substring(0, fileToLoad.getName().indexOf("."));
                if (tabNameStr.length() > 10)
                    tabNameStr = tabNameStr.substring(0, 9) + "...";
                List<FeatureSet<NucleotideCompound>> joins = this.genbankRecord.getFeaturesByType("join_feature");
                List<Join> joinList = new ArrayList<>();

                for(int i=0;i<joins.size();i++){
                    Join j = new Join(joins.get(i),this.centerPane,i);
                    joinList.add(j);
                }
                this.joinCountLabel.setText("Join Count: "+joins.size()+"    ");
                this.joinTableView.setItems(FXCollections.observableArrayList(joinList));
                nucleotideRatio = genbankRecord.getRecordFeatureList().get(0).getEnd() / 360.00;
                this.setText(tabNameStr);
                //Map<String, ObservableList<FeatureArc<NucleotideCompound>>> grouped = genbankRecord.getArcsGrouped();
                 fColor = new HashMap<>();
                Random rn = new Random();
                for(int i=0;i<featureNameList.size();i++)
                {
                    double r = rn.nextDouble();
                    double g = rn.nextDouble();
                    double b = rn.nextDouble();
                    Color c = Color.color(r,g,b);
                    fColor.put(featureNameList.get(i), c);
                }

                this.fNameListCheckCells.forEach(cell -> {
                    cell.setTextFill(fColor.get(cell.getItem()));
                });

                genomeDrawer =
                        new FsetArcController(genbankRecord.getRecordFeatureList(), radiusofPane, radiusofPane,
                                radiusofPane - 40, genomeDisplayPane);
                this.centerPane = new CenterPane(fColor,genbankRecord.getRecordFeatureList(),
                        radiusofPane,widthReduce - widthofsidePanes,heightReduce - heightofvertPanes);
                this.centerPane.drawCenterPane();
                //this.centerPane.setPrefWidth(1000);
                this.centerPane.getViewport().setStyle("-fx-background-color: WHITE");
                this.tabGrid.add(this.centerPane, 1, 1, 6, 5);


                //drawViewPane(this.genomeDrawer, this.genomeDisplayPane);

                drawTopPane(this.genomeDrawer, topPane, Screen.getPrimary().getBounds().getWidth());

                //this.centerPane.drawFeature("gene");
                //this.centerPane.drawJoin("tRNA");
                secondStage.close();
                loaded = true;

            });
            fileLoadingService.start();
        }
    }

    private void makeMenus() {
        this.tabMenuBar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem load = new MenuItem("Import");
        load.setOnAction(lAction -> loadAction());
        file.getItems().add(load);
        Menu edit = new Menu("Edit");
        Menu view = new Menu("View");
        MenuItem zoomIn = new MenuItem("Zoom In");
        MenuItem zoomOut = new MenuItem("Zoom Out");
        MenuItem reset = new MenuItem("Reset");
        reset.setOnAction(action-> resetZoom());
        zoomIn.setOnAction(actionEvent -> zoom());
        zoomOut.setOnAction(actionEvent -> zoomOut());
        view.getItems().addAll(zoomIn, zoomOut,reset);
        Menu tools = new Menu("Tools");
        Menu help = new Menu("Help");
        this.tabMenuBar.getMenus().addAll(file, edit, view, tools, help);
        this.tabVBox.getChildren().add(this.tabMenuBar);
    }

    public void resetZoom() {
        this.centerPane.zoomFactorProperty().set(1.0);
    }

    public void zoomOut() {
        this.centerPane.zoomFactorProperty()
                .set(this.centerPane.zoomFactorProperty().get() * 0.75);
    }

    private Pane buildLSideMidPane(double height) {
        lSideMidPane = new Pane();
        lSideMidPane.setPrefSize(widthReduce, height - heightReduce);
        lSideMidPane.setStyle("-fx-background-color: white;-fx-border-color: white;-fx-border-width: 2px;");

        VBox box = new VBox();
        VBox boxTitle1 = new VBox();
        boxTitle1.setPrefWidth(widthReduce);
        Label topLabel = new Label("Join Features");
        topLabel.setStyle("-fx-font-size: 15;");
        //"-fx-font-size: 15; -fx-border-color: lightblue; -fx-border-width: 2; -fx-border-radius: 5; -fx-border-style: dotted; -fx-border-insets: 5;"
        boxTitle1.setStyle("-fx-font-size: 15; -fx-border-color: lightblue; -fx-border-width: 2; -fx-border-radius: 5; -fx-border-style: dotted; -fx-border-insets: 5;");
        boxTitle1.setAlignment(Pos.CENTER);
        boxTitle1.getChildren().add(topLabel);
        box.getChildren().add(boxTitle1);

        HBox joinFInfo = new HBox();
        joinFInfo.setPrefWidth(widthReduce);
        //TODO Make checkbock class members and logic surrounding it
        this.joinCountLabel = new Label("Join Count: 555  ");
        this.joinAllCheck = new CheckBox(" All      ");
        this.joinAllCheck.setAllowIndeterminate(false);
        this.joinAllCheck.selectedProperty().addListener((selectedProp, oldValue, newValue) -> {
            System.out.println("Join all check was selected the old val= " + oldValue + " the new val is " + newValue);
            if(newValue)
                this.centerPane.showAllJoinTicks();
        });

        this.joinNoneCheck = new CheckBox(" None");
        this.joinNoneCheck.setAllowIndeterminate(false);
        this.joinNoneCheck.selectedProperty().addListener((selectedProp, oldValue, newValue) -> {
            System.out.println("Join none check was selected the old val= " + oldValue + " the new val is " + newValue);
            if(newValue)
                this.centerPane.hideAllJoinTicks();
        });

        joinFInfo.setAlignment(Pos.CENTER);
        joinFInfo.getChildren().addAll(this.joinCountLabel, this.joinAllCheck, this.joinNoneCheck);
        joinFInfo.setStyle("-fx-border-color: lightblue; -fx-border-width: 0; -fx-border-radius: 5; -fx-border-style: dotted; -fx-border-insets: 10;");
        box.getChildren().add(joinFInfo);

        this.joinTableView = new TableView<>();
        this.joinTableView.setEditable(true);
        this.joinTableView.prefHeightProperty().bind(new SimpleDoubleProperty(8.0*25.0+45.0));
        TableColumn<Join, Boolean> selectedJoin = new TableColumn<>("Select");
        selectedJoin.setCellValueFactory(new PropertyValueFactory<>("selected"));
        selectedJoin.setCellFactory(p -> {
           CheckBoxTableCell<Join,Boolean> joinCheck = new CheckBoxTableCell<>(s -> new SimpleBooleanProperty(false));
            joinCheck.selectedProperty().addListener((boolProperty, wasSelected, isSelected)  -> {
                if (isSelected) {
                    this.centerPane.showJoinTick(joinCheck.getIndex());

                    /*
                    fNameListCheckCells.forEach(fnc -> {
                        //had npe once do not want it happen again during this operation
                        if (fnc != null && fnc.isChecked()) {
                            if (!fnc.getItem().equals(fname))
                                fnc.unCheck();
                        }
                    });
                    */
                }
                if(wasSelected){
                    this.centerPane.hideJoinTick(joinCheck.getIndex());
                }

            });
            return joinCheck;
        });

        TableColumn<Join, Integer> gapSCol = new TableColumn<>("Evidence");
        gapSCol.setCellValueFactory(new PropertyValueFactory<>("evidence"));
        TableColumn<Join, Integer> gapECol = new TableColumn<>("Join Start");
        gapECol.setCellValueFactory(new PropertyValueFactory<>("jStart"));
        TableColumn<Join, Integer> ovlSCol = new TableColumn<>("Join End");
        ovlSCol.setCellValueFactory(new PropertyValueFactory<>("jEnd"));
        TableColumn<Join, Integer> ovlECol = new TableColumn<>("Overlap End");
        ovlECol.setCellValueFactory(new PropertyValueFactory<>("overlapEnd"));

        this.joinTableView.getColumns().addAll(selectedJoin, gapSCol, gapECol, ovlSCol, ovlECol);
        this.joinTableView.setFixedCellSize(25.0);

        this.joinTableView.prefHeightProperty().bind(new SimpleIntegerProperty(8 * 25 + 40));
        VBox tableBox = new VBox();
        tableBox.setPrefWidth(widthReduce);
        tableBox.getChildren().add(this.joinTableView);
        ScrollPane tableScroll = new ScrollPane();
        tableScroll.setContent(tableBox);
        box.getChildren().add(tableScroll);

        VBox boxTitle2 = new VBox();
        boxTitle2.setPrefWidth(widthReduce);
        Label topLabel2 = new Label("Other Annotations");
        topLabel2.setStyle("-fx-font-size: 15;");
        boxTitle2.setStyle("-fx-font-size: 15; -fx-border-color: lightblue; -fx-border-width: 2; -fx-border-radius: 5; -fx-border-style: dotted; -fx-border-insets: 5;");
        boxTitle2.setAlignment(Pos.CENTER);
        boxTitle2.getChildren().add(topLabel2);
        box.getChildren().add(boxTitle2);

        HBox otherFInfo = new HBox();
        joinFInfo.setPrefWidth(widthReduce);
        this.featureCountLabel = new Label("Feature Count");
        this.featureAllCheck = new CheckBox(" All      ");
        this.featureAllCheck.setAllowIndeterminate(false);
        this.featureAllCheck.selectedProperty().addListener((boolProp, wasSelected, isSelected) -> {
            logger.debug("Feature All Checkbox property firing was selected? " + wasSelected + " isSelected? " + isSelected);
            fNameListCheckCells.forEach(FNameListCheckCell::check);
            if(this.featureNoneCheck.isSelected()){
                this.featureNoneCheck.setSelected(false);
            }
        });

        this.featureNoneCheck = new CheckBox(" None");
        this.featureNoneCheck.setAllowIndeterminate(false);
        this.featureNoneCheck.selectedProperty().addListener((boolProp, wasSelected, isSelected) -> {
            logger.debug("Feature None Checkbox property firing was selected? " + wasSelected + " isSelected? " + isSelected);
            fNameListCheckCells.forEach(FNameListCheckCell::unCheck);
            if(this.featureAllCheck.isSelected()){
                this.featureAllCheck.setSelected(false);
            }
        });
        otherFInfo.setAlignment(Pos.CENTER);
        otherFInfo.getChildren().addAll(this.featureCountLabel, this.featureAllCheck, this.featureNoneCheck);
        otherFInfo.setStyle("-fx-border-color: lightblue; -fx-border-width: 0; -fx-border-radius: 5; -fx-border-style: dotted; -fx-border-insets: 10;");
        box.getChildren().add(otherFInfo);


        featureNameLV = new ListView<>();
        featureNameLV.setPrefSize(widthReduce, 250);
        this.featureNameLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        this.featureNameLV.setEditable(false);
        this.featureNameLV.setCellFactory(cf -> {

            final FNameListCheckCell cell = new FNameListCheckCell(new SimpleBooleanProperty(false));
            fNameListCheckCells.add(cell);
            //this is the listener to display the selected annotation information in the annotation table view
            cell.selectedProperty().addListener((boolProperty, wasSelected, isSelected) -> {
                logger.debug(cell.getItem() + " wasSelected? " + wasSelected + " isSelected? " + isSelected);
                if (isSelected) {
                    this.annotationTableView.display(cell.getItem());
                }
            });

            //this the listener for the checkbox for the other annotations
            cell.addCheckSelectionListener((boolProperty, wasSelected, isSelected) -> {
                String fname = cell.getItem();
                logger.debug(fname + " wasChecked? " + wasSelected + " isSelected? " + isSelected);
                if (isSelected) {
                    this.centerPane.setGroupVisible(cell.getItem());
                    /*
                    fNameListCheckCells.forEach(fnc -> {
                        //had npe once do not want it happen again during this operation
                        if (fnc != null && fnc.isChecked()) {
                            if (!fnc.getItem().equals(fname))
                                fnc.unCheck();
                        }
                    });
                    */
                }
                if(wasSelected)
                    this.centerPane.setGroupInVisible(cell.getItem());

            });
            return cell;
        });

        box.getChildren().add(featureNameLV);

        this.annotationTableView = new AnnotationTableView<>();
        this.annotationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.annotationTableView.prefHeightProperty().bind(new SimpleDoubleProperty(8.0*25.0+45.0));
        VBox tableOtherBox = new VBox();
        tableOtherBox.setPrefWidth(widthReduce);
        tableOtherBox.getChildren().add(this.annotationTableView);
        ScrollPane tableOtherScroll = new ScrollPane();
        tableOtherScroll.setContent(tableOtherBox);
        box.getChildren().add(tableOtherBox);
        lSideMidPane.getChildren().add(box);
        return lSideMidPane;
    }

    private Pane buildLSideTopPane() {
        lSideTopPane = new Pane();
        lSideTopPane.setPrefSize(widthReduce, 100.0);
        lSideTopPane.setStyle("-fx-background-color: white;-fx-border-color:red;-fx-border-width: 2px;");

        VBox box = new VBox();
        HBox joinFInfo = new HBox();
        joinFInfo.setPrefWidth(widthReduce);
        Label joinName = new Label("Join Feature   ");
        this.featureComoboBox = new ComboBox<>();
        joinFInfo.setAlignment(Pos.CENTER);
        joinFInfo.getChildren().addAll(joinName, this.featureComoboBox);
        joinFInfo.setStyle("-fx-border-color: lightblue; -fx-border-width: 0; -fx-border-radius: 5; -fx-border-style: dotted; -fx-border-insets: 10;");
        box.getChildren().add(joinFInfo);

        HBox qAcccessBtns = new HBox();
        qAcccessBtns.setPrefWidth(widthReduce);


        Image imageZoomIn = new Image(this.getClass().getClassLoader().getResourceAsStream("zoom_in.png"));
        Image imageZoomOut = new Image(this.getClass().getClassLoader().getResourceAsStream("zoom_out.png"));
        ImageView imageZoomOutView = new ImageView(imageZoomOut);
        imageZoomOutView.setFitHeight(30);
        imageZoomOutView.setPreserveRatio(true);
        imageZoomOutView.setSmooth(true);
        imageZoomOutView.setCache(true);

        ImageView imageZoomInView = new ImageView(imageZoomIn);
        imageZoomInView.setFitHeight(30);
        imageZoomInView.setPreserveRatio(true);
        imageZoomInView.setSmooth(true);
        imageZoomInView.setCache(true);


        Button btnZoomIn = new Button();
        btnZoomIn.setPrefSize(30, 30);
        btnZoomIn.setGraphic(imageZoomInView);
        btnZoomIn.setOnAction(actionEvent -> zoom());
        Button btnZoomOut = new Button();
        btnZoomOut.setOnAction(actionEvent -> zoomOut());
        btnZoomOut.setPrefSize(30, 30);
        btnZoomOut.setGraphic(imageZoomOutView);
        qAcccessBtns.setAlignment(Pos.CENTER_LEFT);
        qAcccessBtns.getChildren().addAll(btnZoomIn, btnZoomOut);
        qAcccessBtns.setStyle("-fx-border-color: lightblue; -fx-border-width: 0; -fx-border-radius: 5; -fx-border-style: dotted; -fx-border-insets: 10;");
        box.getChildren().add(qAcccessBtns);

        lSideTopPane.getChildren().add(box);
        return lSideTopPane;
    }

    public void zoom() {
        this.centerPane.zoomFactorProperty()
                .set(this.centerPane.zoomFactorProperty().get() * 1.25);
    }

    private void makeDataViews(double width, double height) {
        this.tabGrid.setPrefSize(width, height);

        //VBox box = new VBox();
        //HBox joinFInfo = new HBox();
        this.tabGrid.add(buildLSideTopPane(), 0, 0, 1, 1);

        this.tabGrid.add(buildLSideMidPane(height), 0, 1, 1, 1);

        genomeDisplayPane = new Pane();
        if ((width - widthReduce) > (height - heightReduce)) {
            double X = ((width - widthReduce) - (height - heightReduce));
            double w1 = X / 2;

            widthofsidePanes = w1;
            /** need to make 3 panes, 1 top in row 1, 1 bottom in row 4, and 1 middle in row 3 that we place our circle on.*/
            /** Also need to set radius */
            Pane tempSidePaneL = new Pane();
            tempSidePaneL.setPrefSize(w1, height - heightReduce);

            tempSidePaneL.setStyle("-fx-background-color: white;");
            this.tabGrid.add(tempSidePaneL, 1, 0, 1, 4);

            Pane tempSidePaneR = new Pane();
            tempSidePaneR.setPrefSize(w1, height - heightReduce);
            tempSidePaneR.setStyle("-fx-background-color: white;");
            this.tabGrid.add(tempSidePaneR, 3, 0, 1, 4);


            genomeDisplayPane.setPrefSize(w1, w1);
            genomeDisplayPane.setStyle("-fx-background-color: white;");

            genomeDisplayPane.setPrefSize(height - heightReduce, height - heightReduce);
            //this.tabGrid.add(genomeDisplayPane, 2, 1, 1, 1);
            radiusofPane = (height - 230) / 2;
        } else {
            // this.tabGrid.add(genomeDisplayPane, 2, 1, 1, 1);
        }

    /*
        Pane bottomPane = new Pane();
        bottomPane.setPrefSize(width, 30);
        bottomPane.setStyle("-fx-background-color: white;-fx-border-width: 2px");
        this.tabGrid.add(bottomPane, 0, 2, 4, 1);
    */

        topPane = new Pane();
        topPane.setPrefSize(width - widthReduce, 100);
        topPane.setStyle("-fx-background-color: white;-fx-border-color: #2e8b57;-fx-border-width: 2px");
        this.tabGrid.add(topPane, 1, 0, 3, 1);

        this.tabVBox.getChildren().addAll(this.tabGrid);
        //this.tabVBox.getChildren().add(this.joinTableView);
    }

    public void topPaneTics(Pane p, int angle, double width, double offset) {
        Line redLine = LineBuilder.create()
                .startX(50)
                .startY(50)
                .endX(width - widthReduce - 50)
                .endY(50)
                .strokeWidth(1.0f)
                .build();
        p.getChildren().add(redLine);
        Double toBeTruncatedMax = (360 * nucleotideRatio) / 1000000.0;
        Double truncatedDoubleMax = new BigDecimal(toBeTruncatedMax).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        int maxDigitLen = NumberFormat.getNumberInstance(Locale.US).format((double) (truncatedDoubleMax)).length();
        for (int i = 0; i <= 360.0; i = (i + angle)) {
            StackPane stack = new StackPane();
            Double toBeTruncated = (i * nucleotideRatio) / 1000000.0;
            Double truncatedDouble = new BigDecimal(toBeTruncated).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            String s = Double.toString(truncatedDouble);
            int diffLetterLen = (int) Math.ceil((maxDigitLen - s.length()) / 2);
            String padding = " ";
            for (int j = 0; j < diffLetterLen; j++)
                padding += "  ";
            Line line = new Line();
            line.setStartX(0.0);
            line.setStartY(20.0);
            line.setEndX(0.0);
            line.setEndY(50.0);
            stack.getChildren().add(line);
            double positionX = (width / 360.0) * i + 18.5;
            Label val = new Label(padding + s + "M" + padding);
            val.setFont(Font.font(Font.getFontNames().get(2), 15));
            stack.getChildren().add(val);
            val.setStyle("-fx-background-color: white;-fx-border-color: transparent transparent transparent transparent;-fx-border-width: 2px");
            val.setOpacity(1);
            stack.setAlignment(Pos.BASELINE_CENTER);
            stack.setLayoutX(positionX);
            stack.setLayoutY(50);
            //stack.setRotate(180);
            p.getChildren().add(stack);
        }

    }


    public void drawTopPane(FsetArcController fsetArcController, Pane pane, double w) {
        Line redLine = LineBuilder.create()
                .startX(50)
                .startY(50)
                .endX(w - widthReduce - 50)
                .endY(50)
                .strokeWidth(1.0f)
                .build();
        pane.getChildren().add(redLine);
        fsetArcController.topPaneTics(pane, 45, w - widthReduce - 100, 0);
    }

    private void populateAnnotationLV(List<String> featureNameList) {
        this.featureNameLV.setItems(FXCollections.observableArrayList(featureNameList));
        featureNameList.forEach(fn -> this.featureComoboBox.getItems().add(fn));
    }
}
