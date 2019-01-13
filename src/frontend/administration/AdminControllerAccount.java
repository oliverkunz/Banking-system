package frontend.administration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class AdminControllerAccount implements Initializable {
	
	AdminMain adminMain = null;
	
	@FXML private Button implementButton;
	@FXML private Button backButton;
	
	@FXML private RadioButton depositRButton;
	@FXML private RadioButton withdrawRButton;
	
	@FXML private TextField accountNumberTF;
	@FXML private TextField amountTF;
	
	@FXML private Label customerNameL;

	//TAbleview todo
	
	private SimpleStringProperty accountNumber = new SimpleStringProperty("");
	private SimpleStringProperty amount = new SimpleStringProperty("");
	private SimpleStringProperty customerName = new SimpleStringProperty("");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	accountNumberTF.textProperty().bindBidirectional(this.getAccountNumber());
    	amountTF.textProperty().bindBidirectional(this.getAmount());
    	customerNameL.textProperty().bindBidirectional(this.getCustomerName());

    }
 
	@FXML
    public void TransferMoney(final ActionEvent event) throws IOException  {
    	//Logic for register a customer
    	    	
    }
	
	@FXML
    public void OpenAccount(final ActionEvent event) throws IOException  {
    	//Logic for opening an account
    	    	
    }
	
	@FXML
    public void ResolveAccount(final ActionEvent event) throws IOException  {
    	//Logic for resolving an account
    	    	
    }
	
	@FXML
    public void ShowOverview(final ActionEvent event) throws IOException  {
		//Kontonummer logic
		
		Parent root;
    	root = FXMLLoader.load(getClass().getResource("admin.fxml"));  
    	Scene scene = new Scene(root, 1280, 900);     
    	this.adminMain.setScene(scene);
    	    	
    }
    
    public void setAdminMain(AdminMain adminMain) {
    	this.adminMain = adminMain;
    }

	public SimpleStringProperty getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(SimpleStringProperty accountNumber) {
		this.accountNumber = accountNumber;
	}

	public SimpleStringProperty getAmount() {
		return amount;
	}

	public void setAmount(SimpleStringProperty amount) {
		this.amount = amount;
	}

	public SimpleStringProperty getCustomerName() {
		return customerName;
	}

	public void setCustomerName(SimpleStringProperty customerName) {
		this.customerName = customerName;
	}
	

}
