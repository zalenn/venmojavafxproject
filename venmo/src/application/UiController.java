package application;

import java.util.Date;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import javafx.scene.control.TextArea;




public class UiController {
	@FXML
	private TextArea clockTextArea;
	@FXML
	private TextArea transactionLog;
	@FXML
	private TextField recipentField;
	@FXML
	private Button payButton;
	@FXML
	private Button requestButton;
	@FXML
	private TextField commentField;

	
	
	
	public void changeTextField () {
		
		clockTextArea.setText("hi");
	}
	
     
	public void initialize() {
		
		Platform.runLater(new Runnable() {
		     @Override
		     public void run() {
		         //Update the control code of JavaFX and put it here
		    	 Date dte = new Date();
		    	 String topMenuStr = "       " + dte.toString();  
		    	 Platform.runLater(() -> clockTextArea.setText(topMenuStr));         
		    	 
		    	 
		     }
		});
	}
	
	
}
