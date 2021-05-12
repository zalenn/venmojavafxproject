package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ResourceBundle;

import com.sun.prism.Image;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
	private Button exitButton;
	@FXML
	private TextField commentField;
	@FXML 
	private TextField transactionMoneyField;
	@FXML
	private Pane socialFeed;
	@FXML
	private HBox SocialFeedBox;
	@FXML 
	private ComboBox<String> salvationDonationBox;
	@FXML 
	private ComboBox<String> goodwillDonationBox;
	@FXML 
	private ComboBox<String> awfDonationBox;
	@FXML
	private Button donateSalvationButton;
	@FXML
	private Button donateGoodWillButton;
	@FXML
	private Button donateAWFButton;
 
	
	
	public Queue<String> socialFeedList = new PriorityQueue();
	public Queue<HBox> boxList = new LinkedList<HBox>();
	public Hashtable<String, String> customerInfo = new Hashtable<>();
	public String[] fields     = new String[3];
	public int  counter        = 0; 

	

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
	
	
	
	
	public void getString(String name, String amount, String comment)
	{
		fields[0] = name;
		fields[1] = amount;
		fields[2] = comment;
	}
	

	
	
	
	
	
	
	
	
	
	// Insert Social Media Box here// 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

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

	  	    	
	  	    	Platform.runLater(new Runnable() {
					public void run() {
						String rs = null;
						socketUtils su = new socketUtils();

						String recipentString = recipentField.getText().toString();
						String commentString = commentField.getText().toString();
						String transactionMoneyString = transactionMoneyField.getText().toString();

						if (su.socketConnect() == true) // this always seems to be false for whatever reason
						{

							int amountCounter = 1;
							String msg = "Transaction>Transaction#0500000" + "," + amountCounter + "," + transactionLog.getText();
							amountCounter++;                                              // increase transaction count by 1 after each transaction 
							su.sendMessage(msg);
							String ackOrNack = su.recvMessage();

							msg = "quit";
							su.sendMessage(msg);
							rs = su.recvMessage();

							su.closeSocket();

							msg = "CLIENT: Transaction>transaction#001" + "," + recipentString + "," + commentString + ","
									+ transactionMoneyString;
							fileIO trans = new fileIO();
							trans.wrTransactionData(msg);

							recipentField.setText("");
							commentField.setText("");
							transactionMoneyField.setText("");

						} else {
							String msg = "CLIENT NETWORK ERROR : Transaction>kiosk#001";

							fileIO trans = new fileIO();
							trans.wrTransactionData(msg);

							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("--- Network Communications Error ---");
							alert.setHeaderText("Unable to talk to Socket Server!");

							alert.showAndWait();
						}
					}
				});

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

				Platform.runLater(new Runnable() {
					public void run() {
						String rs = null;
						socketUtils su = new socketUtils();

						String recipentString = recipentField.getText().toString();
						String commentString = commentField.getText().toString();
						String transactionMoneyString = transactionMoneyField.getText().toString();

						if (su.socketConnect() == true) // this always seems to be false for whatever reason
						{
                            int amountCounter = 1;
							String msg = "Transaction>Transaction#0500000" + "," + amountCounter + "," + transactionLog.getText();
							amountCounter++;
							su.sendMessage(msg);
							String ackOrNack = su.recvMessage();

							msg = "quit";
							su.sendMessage(msg);
							rs = su.recvMessage();

							su.closeSocket();

							msg = "CLIENT: Transaction>transaction#001" + "," + recipentString + "," + commentString + ","
									+ transactionMoneyString;
							fileIO trans = new fileIO();
							trans.wrTransactionData(msg);

							recipentField.setText("");
							commentField.setText("");
							transactionMoneyField.setText("");

						} else {
							String msg = "CLIENT NETWORK ERROR : Transaction>kiosk#001";

							fileIO trans = new fileIO();
							trans.wrTransactionData(msg);

							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("--- Network Communications Error ---");
							alert.setHeaderText("Unable to talk to Socket Server!");

							alert.showAndWait();
						}
					}
				}); 	      
         
        }
    }
	
	
	
	
	public void handleSalvation() {
	
		String addtoSocialFeed = null;
			
		if (salvationDonationBox.getValue() == null) {
			
			
			
			 transactionLog.setText("Please select an amount to donate");
			
		} else {

			
		    transactionLog.setText("You donated " + salvationDonationBox.getValue() + " to the Salvation Army ");
	    	wrTransactionData(transactionLog.getText());                                                              // write transaction log data to .txt file

	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	Platform.runLater(new Runnable() {
			public void run() {
				String rs = null;
				socketUtils su = new socketUtils();

				if (su.socketConnect() == true) // this always seems to be false for whatever reason
				{

					int amountCounter = 1;
					String msg = "Transaction>Transaction#0500000" + "," + amountCounter + "," + transactionLog.getText();
					amountCounter++;                                              // increase transaction count by 1 after each transaction 
					su.sendMessage(msg);
					String ackOrNack = su.recvMessage();

					msg = "quit";
					su.sendMessage(msg);
					rs = su.recvMessage();

					su.closeSocket();

					msg = "CLIENT: Transaction>transaction#001" + "," + "Salvation Army recieved" + salvationDonationBox.getValue();
;					fileIO trans = new fileIO();
					trans.wrTransactionData(msg);

				} else {
					
					String msg = "CLIENT NETWORK ERROR : Transaction>kiosk#001";

					fileIO trans = new fileIO();
					trans.wrTransactionData(msg);

					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("--- Network Communications Error ---");
					alert.setHeaderText("Unable to talk to Socket Server!");

					alert.showAndWait();
				}
			}
		});
	
		}
	
	
	}
	
	
	
	public void handleGoodWill() {
	
		
		String addtoSocialFeed = null;
		
		if (goodwillDonationBox.getValue() == null) {
			
       transactionLog.setText("Please select an amount to donate");
			
		} else {

			
		    transactionLog.setText("You donated " + goodwillDonationBox.getValue() + " to Goodwill");
	    	wrTransactionData(transactionLog.getText());                                                              // write transaction log data to .txt file

	    	
	    	
	    	
       
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	Platform.runLater(new Runnable() {
			public void run() {
				String rs = null;
				socketUtils su = new socketUtils();

				if (su.socketConnect() == true) // this always seems to be false for whatever reason
				{

					int amountCounter = 1;
					String msg = "Transaction>Transaction#0500000" + "," + amountCounter + "," + transactionLog.getText();
					amountCounter++;                                              // increase transaction count by 1 after each transaction 
					su.sendMessage(msg);
					String ackOrNack = su.recvMessage();

					msg = "quit";
					su.sendMessage(msg);
					rs = su.recvMessage();

					su.closeSocket();

					msg = "CLIENT: Transaction>transaction#001" + "," + "Goodwill recieved" + goodwillDonationBox.getValue();
;					fileIO trans = new fileIO();
					trans.wrTransactionData(msg);

				} else {
					
					String msg = "CLIENT NETWORK ERROR : Transaction>kiosk#001";

					fileIO trans = new fileIO();
					trans.wrTransactionData(msg);

					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("--- Network Communications Error ---");
					alert.setHeaderText("Unable to talk to Socket Server!");

					alert.showAndWait();
				}
			}
		});
		
		
		}
			
	}
	
	
	public void handleAWF() {
	
		String addtoSocialFeed = null;
		
		
		
		if (awfDonationBox.getValue() == null) {
			
			
			
			 transactionLog.setText("Please select an amount to donate");
			
		} else {

			
		    transactionLog.setText("You donated " + awfDonationBox.getValue() + " to the African Wildlife Foundation ");
	    	wrTransactionData(transactionLog.getText());                                                              // write transaction log data to .txt file

	    	
	    	Platform.runLater(new Runnable() {
			public void run() {
				String rs = null;
				socketUtils su = new socketUtils();

				if (su.socketConnect() == true) // this always seems to be false for whatever reason
				{

					int amountCounter = 1;
					String msg = "Transaction>Transaction#0500000" + "," + amountCounter + "," + transactionLog.getText();
					amountCounter++;                                              // increase transaction count by 1 after each transaction 
					su.sendMessage(msg);
					String ackOrNack = su.recvMessage();

					msg = "quit";
					su.sendMessage(msg);
					rs = su.recvMessage();

					su.closeSocket();

					msg = "CLIENT: Transaction>transaction#001" + "," + "African Wildlife Foundation recieved" + awfDonationBox.getValue();
;					fileIO trans = new fileIO();
					trans.wrTransactionData(msg);

				} else {
					
					String msg = "CLIENT NETWORK ERROR : Transaction>kiosk#001";

					fileIO trans = new fileIO();
					trans.wrTransactionData(msg);

					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("--- Network Communications Error ---");
					alert.setHeaderText("Unable to talk to Socket Server!");

					alert.showAndWait();
				}
			}
		});
	
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
		          
		          String hStr="- Click on   EXIT   button to quit Venmo .\r\n" + 
		        		      "- Click on   PAY   to complete transaction and send money to recipient.\r\n" +
		        		      "- Click on   REQUEST   button to complete transaction and request money to recipient .\r\n";
		          
		          alert.setContentText(hStr);
		          alert.showAndWait();
		        }
		    });
	}

	
	
	
	public void handleExit()
	  {
		  Platform.runLater(new Runnable() 
			 {
			        public void run() 
			        {
			           Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			           
			           alert.setTitle("Confirmation Dialog");
			           alert.setHeaderText("EXIT confirmation dialog");
			           alert.setContentText("Are you sure you want to exit Venmo?");

			           Optional<ButtonType> result = alert.showAndWait();
			           
			           if (result.get() == ButtonType.OK)
			           {
				          // writeHashTableData();
				           System.exit(0);
			           }
			           else 
			           {
			               // ... user chose CANCEL or closed the dialog
			           }
			        }
			    });	
		}
	  

	public void initialize() {

		
		
		// Combobox list for Donations 
		ObservableList<String> donationAmountList = FXCollections.observableArrayList("$5", "$10", "$15", "$20");
		
		salvationDonationBox.setItems(donationAmountList);
		goodwillDonationBox.setItems(donationAmountList);
		awfDonationBox.setItems(donationAmountList);

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
