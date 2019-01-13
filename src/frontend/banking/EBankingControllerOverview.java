package frontend.banking;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EBankingControllerOverview implements Initializable {

	EBankingMain eBankingMain = null;
	
	@FXML private Button statementB;
	@FXML private Button transferB;
	@FXML private TextField amountTF;
	@FXML private TextField senderTF;
	@FXML private TextField receiverTF;
	@FXML private Label bankNameL;
	@FXML private Label customerNameL;
	
	Alert alert = new Alert(AlertType.CONFIRMATION, "Bitte Transaktion bestätigen");
	Alert info = new Alert(AlertType.INFORMATION, "Transaktion ausgeführt");
	Alert error = new Alert(AlertType.ERROR, "Transaktion abgebrochen");
	
	private SimpleStringProperty amount = new SimpleStringProperty("");
	private SimpleStringProperty sender = new SimpleStringProperty("");
	private SimpleStringProperty receiver = new SimpleStringProperty("");
	private SimpleStringProperty bankName = new SimpleStringProperty("");
	private SimpleStringProperty customerName = new SimpleStringProperty("");
	

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	amountTF.textProperty().bindBidirectional(this.getAmount());
    	senderTF.textProperty().bindBidirectional(this.getSender());
    	receiverTF.textProperty().bindBidirectional(this.getReceiver());
    	bankNameL.textProperty().bindBidirectional(this.getBankName());
    	customerNameL.textProperty().bindBidirectional(this.getCustomerName());
    }
 
	@FXML
    public void TransferMoney(final ActionEvent event) throws IOException  {
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    
			//---- Transaction logic
			
			info.showAndWait();
		} else {
			error.showAndWait();
		}
    }
	
	@FXML
    public void GetAccountStatement(final ActionEvent event) throws IOException  {
		//Logic
		
		Parent root;
    	root = FXMLLoader.load(getClass().getResource("eBankingStatement.fxml"));  
    	Scene scene = new Scene(root, 900, 600);     
    	this.eBankingMain.setScene(scene);
    }
    
    public void setEBankingMain(EBankingMain eBankingMain) {
    	this.eBankingMain = eBankingMain;
    }
    
    
	public SimpleStringProperty getAmount() {
		return amount;
	}

	public void setAmount(SimpleStringProperty amount) {
		this.amount = amount;
	}

	public SimpleStringProperty getSender() {
		return sender;
	}

	public void setSender(SimpleStringProperty sender) {
		this.sender = sender;
	}

	public SimpleStringProperty getReceiver() {
		return receiver;
	}

	public void setReceiver(SimpleStringProperty receiver) {
		this.receiver = receiver;
	}
	
	public SimpleStringProperty getBankName() {
		return bankName;
	}

	public void setBankName(SimpleStringProperty bankName) {
		this.bankName = bankName;
	}

	public SimpleStringProperty getCustomerName() {
		return customerName;
	}

	public void setCustomerName(SimpleStringProperty customerName) {
		this.customerName = customerName;
	}

}
