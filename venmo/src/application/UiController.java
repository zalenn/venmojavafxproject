package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;




public class UiController {
	@FXML
	private TextArea clockTextArea;
	
	@FXML
	private Button helpButton;
	
	@FXML
	private TextArea serverClockTextArea;
	
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
	
	
	
	
	
	
	// Socket Utility Class & Functions 
	
	
	public class socketUtils 
	{
	    Socket 	clientSocket=null;
	    DataOutputStream outToServer=null;
	    BufferedReader inFromServer=null;

		public boolean socketConnect()
		{
			String ipAddress, portString;
			int portNumber;
			boolean rc=false;
			
			try 
			{
		    	File file = new File("config.txt");
		        if (file.exists())
		        {
		           BufferedReader br = new BufferedReader(new FileReader("config.txt"));
		           
	             
	               ipAddress  = br.readLine();
	               portString = br.readLine();
	               portNumber = Integer.parseInt(portString);
	               br.close();
		        }
		        else
		        {        
		           ipAddress  = "localhost"; //127.0.0.1 and "localhost" aren't working
		           portNumber = 3333;
		        }
		        
		        
	  
	           clientSocket  = new Socket(ipAddress, portNumber);
	           
	           outToServer   = new DataOutputStream(clientSocket.getOutputStream());
	           inFromServer  = new BufferedReader(
	   	                       new InputStreamReader(clientSocket.getInputStream()));
	           
	           rc= true;
			}
			catch (ConnectException ex)
			{
				ex.printStackTrace();
			}					
			catch (UnknownHostException ex)
		    {
				ex.printStackTrace();
		    }
			catch (IOException ex) 
		    {
				ex.printStackTrace();
		    }
			
			return rc;
		}
		
		public boolean sendMessage(String msg)
		{
			boolean rc=false;
			
			try 
			{
				outToServer.writeBytes(msg + "\r\n");
				rc = true;
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return rc;
		}
		
		public String recvMessage()
		{
			String msg=null;
			
			try
			{
				msg = inFromServer.readLine();
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return msg;
		}
		
		public boolean closeSocket()
		{
			boolean rc=false;
			
			try
			{
				clientSocket.close();
	                        rc=true;
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return rc;
		}
	}

	
	
	
	////////////////////////////////
	
	// File IO Class
	
	public void wrTransactionData(String dataStr)
	{
   
		FileWriter fwg = null;
	       try 
	       {
	        	// open the file in append write mode
	        	fwg = new FileWriter("transactionLog.txt", true);
	        }
	        catch (IOException e)
	        {
	        	// TODO Auto-generated catch block
	        	e.printStackTrace();
	        }
	   	    
	     BufferedWriter bwg = new BufferedWriter(fwg);
	     PrintWriter outg   = new PrintWriter(bwg);
			
	     String timeStamp = new SimpleDateFormat("MM-dd-yyyy HH.mm.ss").format(new Date());
	        
	     outg.println(timeStamp + " : " + dataStr);
	        
	     outg.close();
}
	
	
	

	// Event Handlers
	
	
	
	public void handlePay() {
		
		// Initialize values
		String recipentString = null; 
		String commentString = null;
		String transactionMoneyString = null;

		// Convert fields to strings 
		recipentString = recipentField.getText().toString();	
		commentString = commentField.getText().toString();	
		transactionMoneyString = transactionMoneyField.getText().toString();
		
		if (recipentString.isEmpty() == true || commentString.isEmpty() == true || transactionMoneyString.isEmpty() == true ) {      // if all fields are not filled display prompt to complete them
			
			  
			  transactionLog.setText("Please fill out all of the fields.");
			
			
	  	    } else {
	  	    	
	  	    	transactionLog.setText("You paid " + recipentString + " $" + transactionMoneyString);
	  	    	wrTransactionData(transactionLog.getText());                                                              // write transaction log data to .txt file

	  	    	
	  	    	
	  	    	 // Send information to server side via sockets
		  	      
		  	      
		  	    Platform.runLater(new Runnable() 
				    {
				        public void run() 
				        {
				        	String rs = null;
				            socketUtils su = new socketUtils();
				            
				            if (su.socketConnect() == true) //this always seems to be false for whatever reason
				            {
				            	
				                 String msg = "Transaction>kiosk#0500000" + "," + "1" + "," + transactionLog.getText();
	       	                     su.sendMessage(msg);				            	
	       	                     String ackOrNack = su.recvMessage();
	       	                
	       	                
	       	                     msg = "quit";
	       	                     su.sendMessage(msg);
	       	                     rs = su.recvMessage();
	       	                
	       	                
	       	                //
	       	                // close the socket connection
	       	                //
	       	                    su.closeSocket();

	       	                    
	       	                if (ackOrNack.startsWith("ACK") == true)
	       	                {
	       	                	transactionLog.setText("Success!    Message was received and processed by the Socket Server!");
				    
	       	                } else {
	       	                	
				            	Alert alert = new Alert(Alert.AlertType.ERROR);
						        alert.setTitle("--- Network Communications Error ---");
						        alert.setHeaderText("Unable to talk to Socket Server!");
						          
						        alert.showAndWait();
				            }
				        }
			    }});	
	        }
	  }	
	  	    	
	  	    	
	  	    	
	  	 
	
	public void handleRequest() {
		
        // Initialize values
		String recipentString = null; 
		String commentString = null;
		String transactionMoneyString = null;
		
		
		
		// Convert fields to strings 
		recipentString = recipentField.getText().toString();	
		commentString = commentField.getText().toString();	
		transactionMoneyString = transactionMoneyField.getText().toString();
		

		if (recipentString.isEmpty() == true || commentString.isEmpty() == true || transactionMoneyString.isEmpty() == true ) {                 // if all fields are not filled display prompt to complete them
			
			  
			  transactionLog.setText("Please fill out all of the fields.");
			
			
	  	    } else {
			
	  	      transactionLog.setText("You requested " + " $" + transactionMoneyString + " from " + recipentString);
	  	      wrTransactionData(transactionLog.getText());                                                                    // write transaction log data to .txt file
	  	       
	  	      

	  	      
            // Send information to server side via sockets
	  	      
	  	      
	  	    Platform.runLater(new Runnable() 
			    {
			        public void run() 
			        {
			        	String rs = null;
			            socketUtils su = new socketUtils();
			            
			            if (su.socketConnect() == true) //this always seems to be false for whatever reason
			            {
			            	
			                 String msg = "Transaction>kiosk#0500000" + "," + "1" + "," + transactionLog.getText();
       	                     su.sendMessage(msg);				            	
       	                     String ackOrNack = su.recvMessage();
       	                
       	                
       	                     msg = "quit";
       	                     su.sendMessage(msg);
       	                     rs = su.recvMessage();
       	                
       	                
       	                //
       	                // close the socket connection
       	                //
       	                    su.closeSocket();

       	                    
       	                if (ackOrNack.startsWith("ACK") == true)
       	                {
       	                	transactionLog.setText("Success! Message was received and processed by the Socket Server!");
			    
       	                } else {
       	                	
			            	Alert alert = new Alert(Alert.AlertType.ERROR);
					        alert.setTitle("--- Network Communications Error ---");
					        alert.setHeaderText("Unable to talk to Socket Server!");
					          
					        alert.showAndWait();
			            }
			        }
			    }});	
        }
    }


	
	public void handleHelp() {
	  
		Platform.runLater(new Runnable() 
		 {
		        public void run() 
		        {
		          Alert alert = new Alert(Alert.AlertType.INFORMATION);
		          alert.setTitle("--- Ticket Kiosk Help Window ---");
		          alert.setHeaderText("Help Screen");
		          
		          String hStr="- Click on   EXIT   button to quit the socket server.\r\n" + 
		        		      "- Click on   Show Log   to display current transaction log file.\r\n" +
		        		      "- Click on   New Kiosk   to create the next ticket kiosk station.\r\n" +
		                      "- Click on   LIST KIOSKS to display current status of kiosks.\r\n";
		          
		          alert.setContentText(hStr);
		          alert.showAndWait();
		        }
		    });
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
						    serverClockTextArea.setText(topMenuStr); 
					               
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
