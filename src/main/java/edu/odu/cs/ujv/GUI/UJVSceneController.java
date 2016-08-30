package edu.odu.cs.ujv.GUI;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
 
public class UJVSceneController {
    @FXML private Text actiontarget;
    
    @FXML protected void tabchanged(Event event) {
        System.out.println("tab changed");
        System.out.println("did something");
    }

    @FXML protected void firsttabchanged(Event event) {
    }

    @FXML protected void onBtnCompendiumPress(ActionEvent actionEvent) {
        String location = "http://www.compendiumng.org";
        WebView browser = new WebView();
        WebEngine engine = browser.getEngine();
        engine.load(location);
    }

    @FXML protected void onBtnHtml5test(ActionEvent actionEvent) {
        String location = "http://www.html5test.com";
        WebView browser = new WebView();
        WebEngine engine = browser.getEngine();
        engine.load(location);

    }

    @FXML protected void onBtnGooglePress(ActionEvent actionEvent) {
        String location = "http://www.google.com";
        WebView browser = new WebView();
        WebEngine engine = browser.getEngine();
        engine.load(location);
    }
    
 
    @FXML protected void dosomething(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
    }

}
