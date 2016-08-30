package edu.odu.cs.ujv.GUI.views;

import edu.odu.cs.ujv.GBParser.FeatureSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.Qualifier;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by john on 11/6/15.
 */
public class JoinEditView implements Initializable {
    @FXML private Label typeLable;
    @FXML private Label startLable;
    @FXML private Label endLable;
    @FXML private Label lenghtLable;
    @FXML private TextField evidentEnter;
    @FXML private TextField gStartEnter;
    @FXML private TextField gEndEnter;
    @FXML private TextField notesEnter;
    @FXML private TextField jMethodEnter;
    @FXML private TextField jVerificationEnter;
    @FXML private TextField lableEnter;
    @FXML private TextField confirmedEnter;

    private GridPane stateWindow;
    private FeatureSet<NucleotideCompound> featureSet;

    public JoinEditView(FeatureSet<NucleotideCompound> featureSet){
        URL url = this.getClass().getClassLoader().getResource("editfset.fxml");
        System.out.println(url);
        FXMLLoader loadMe = new FXMLLoader(url);
        loadMe.setController(this);
        try{
            this.stateWindow = loadMe.load();
        }catch (IOException ioEx){
            throw  new RuntimeException(ioEx);
        }
        this.featureSet = featureSet;
        init();
    }

    private void init(){
        this.typeLable.setText(featureSet.getType());
        this.startLable.setText(Integer.toString(featureSet.getStart()));
        this.endLable.setText(Integer.toString(featureSet.getEnd()));
        this.lenghtLable.setText(Integer.toString(featureSet.getLength()));
        Map<String,Qualifier> m = featureSet.getFeatureQualifierMap();

        //this.evidentEnter.setText(m.get("join_evidence").getValue());
        final Qualifier je = m.get("join_evidence");
        this.evidentEnter.setText(je == null ? " ":je.getValue());
        this.evidentEnter.textProperty().addListener((text,oldV,newV)->{
            if(!oldV.equals(newV)){
                if(je != null){
                    je.setValue(newV);
                }
            }
        });


        this.gStartEnter.setText(Integer.toString(featureSet.getStart()));
        this.gEndEnter.setText(Integer.toString(featureSet.getEnd()));

        Qualifier n = m.get("notes");
        this.notesEnter.setText( n == null ? " " :  n.getValue());
        this.notesEnter.textProperty().addListener((text,oldV,newV)->{
            if(!oldV.equals(newV)){
                if(n != null){
                    n.setValue(newV);
                }
            }
        });


        Qualifier jm = m.get("join_verification");
        this.jMethodEnter.setText(jm == null ? " ": jm.getValue());
        this.jMethodEnter.textProperty().addListener((text,oldV,newV)->{
            if(!oldV.equals(newV)){
                if(jm != null){
                    jm.setValue(newV);
                }
            }
        });

        Qualifier jv = m.get("join_verification");
        this.jVerificationEnter.setText(jv==null ? " " : jv.getValue());
        this.jVerificationEnter.textProperty().addListener((text,oldV,newV)->{
            if(!oldV.equals(newV)){
                if(jv != null){
                    jv.setValue(newV);
                }
            }
        });

        Qualifier l = m.get("label");
        this.lableEnter.setText(l == null ? " " :  l.getValue());
        this.lableEnter.textProperty().addListener((text,oldV,newV)->{
            if(!oldV.equals(newV)){
                if(l != null){
                    l.setValue(newV);
                }
            }
        });

        Qualifier c = m.get("confirmed");
        this.confirmedEnter.setText(c == null ? " ":c.getValue());
        this.confirmedEnter.textProperty().addListener((text,oldV,newV)->{
            if(!oldV.equals(newV)){
                if(c != null){
                    c.setValue(newV);
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public GridPane getPane(){
        return this.stateWindow;
    }
}
