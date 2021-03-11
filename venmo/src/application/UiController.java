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
	@FXML 
	private TextField transactionMoneyField;

	
	public void handlePay() {
		

		String recipentString = null; 
		String commentString = null;
		String transactionMoneyString = null;

		recipentString = recipentField.getText().toString();	
		commentString = commentField.getText().toString();	
		transactionMoneyString = transactionMoneyField.getText().toString();
		
		if (recipentString.isEmpty() == true || commentString.isEmpty() == true || transactionMoneyString.isEmpty() == true ) {
			
			  
			  transactionLog.setText("Please fill out all of the fields.");
			
			
	  	    } else {
	  	    	
	  	    	transactionLog.setText("You paid " + recipentString + " $" + transactionMoneyString);

		}
	}
	
	
	public void handleRequest() {
		

		String recipentString = null; 
		String commentString = null;
		String transactionMoneyString = null;
		
		recipentString = recipentField.getText().toString();	
		commentString = commentField.getText().toString();	
		transactionMoneyString = transactionMoneyField.getText().toString();
		

		if (recipentString.isEmpty() == true || commentString.isEmpty() == true || transactionMoneyString.isEmpty() == true ) {
			
			  
			  transactionLog.setText("Please fill out all of the fields.");
			
			
	  	    } else {
			
	  	      transactionLog.setText("You requested " + " $" + transactionMoneyString + "from" + recipentString);
		         
		         
		         
	  	    }
	}
	

	public void initialize() {
		
	
		Platform.runLater(new Runnable() {
		     @Override
		     public void run() {
		         //Update the control code of JavaFX and put it here
	 
		    	 // Real Time Clock Code
		    	 Thread refreshClock = new Thread()
				   {  
					  public void run()
					  {	 
						while (true)
						{
							Date dte = new Date();
				
							String topMenuStr = "       " + dte.toString();					      
						    clockTextArea.setText(topMenuStr); 
					               
						    try
						    {
							   sleep(300L);
						    }
						    catch (InterruptedException e) 
						    {
							   // TODO Auto-generated catch block
							   e.printStackTrace();
						    }
						  
			            }  // end while ( true )
						 
				    } // end run thread
				 };

			     refreshClock.start();
		    }


		});
	}
	
}
