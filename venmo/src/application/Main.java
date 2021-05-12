package application;
	
import java.lang.ModuleLayer.Controller;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


public class Main extends Application {
		
	public static Stage stage = null;
    @Override
    public void start(Stage stage) throws Exception {
        
    	Parent root = FXMLLoader.load(getClass().getResource("/Ui.fxml"));      
       
    	Scene scene = new Scene(root);
		stage.resizableProperty().setValue(Boolean.FALSE);
		stage.setTitle("Venmo User Interface Rev. 10.0   :   Rel Date  :  May 11, 2021     8:00 PM        ");
		stage.setScene(scene);
		this.stage = stage;
		stage.show();
    }

    
    
	public static void main(String[] args) {
		launch(args);
	}
}
