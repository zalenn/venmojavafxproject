package application;

import java.util.Date;

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

	
	
    private void refreshClock()
    {
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
					   sleep(3000L);
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

	
	
	public void initialize() {
		
        TextArea clockTextArea = new TextArea();
		clockTextArea.setText("hi");
        
        
		refreshClock();
		
		
	}
	
	
	
}
