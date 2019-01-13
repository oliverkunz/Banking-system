package frontend.atm;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import backend.api.Account;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Controller implements Initializable {
	
	Main main = null;
	
	@FXML private Button loginButton;
	@FXML private Button withdraw;
	@FXML private TextField accountIDTF;
	@FXML private TextField amountTF;
	@FXML private PasswordField customerPINPF;
	@FXML private Label loginMessageL;
	@FXML private Label bankNameL;
	@FXML private Label customerNameL;
	@FXML private TableView<Account> accountsT;
	@FXML private TableColumn<Account, Double> colBalanceT;
	@FXML private TableColumn<Account, String> colAccountT;
	@FXML private TableColumn<Account, Double> colLimitT;
	
	private SimpleStringProperty loginMessage = new SimpleStringProperty("");
	private SimpleStringProperty customerPIN = new SimpleStringProperty("");
	private SimpleStringProperty accountID = new SimpleStringProperty("");
	private SimpleStringProperty amount = new SimpleStringProperty("");
	private SimpleStringProperty bankName = new SimpleStringProperty("");
	private SimpleStringProperty customerName = new SimpleStringProperty("");
	//private SimpleStringProperty colBalance = new SimpleStringProperty("");
	//private SimpleStringProperty colAccount = new SimpleStringProperty("");
	//private SimpleStringProperty colLimit = new SimpleStringProperty("");
	

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	accountIDTF.textProperty().bindBidirectional(this.getAccountID());
    	customerPINPF.textProperty().bindBidirectional(this.getCustomerPIN());
    	loginMessageL.textProperty().bindBidirectional(this.getLoginMessage());
    	amountTF.textProperty().bindBidirectional(this.getAmount());
    	bankNameL.textProperty().bindBidirectional(this.getBankName());
    	customerNameL.textProperty().bindBidirectional(this.getCustomerName());
    	//colBalanceT.setCellValueFactory(new PropertyValueFactory<Account, Double>(""));
    	//colAccountT.setCellValueFactory(new PropertyValueFactory<Account, String>(""));
    	//colLimitT.setCellValueFactory(new PropertyValueFactory<Account, Double>(""));
    	//accountsT.setItems(GetTableData());
    }
    
    /*public ObservableList<Account> GetTableData() {
		// Tabellen mit Daten (Account, Balance, Limit) füllen
    	
    	ObservableList<Account> tableData = FXCollections.observableArrayList();
    	//tableData.add("");
		return tableData;
	}*/

	@FXML
    public void PressLoginButton(final ActionEvent event) throws IOException  {
    	if (accountID.getValue().equals("test") && customerPIN.getValue().equals("1234")) {
    		loginMessage.setValue("Login erfolgreich"); 	
    	
    		Parent root;
        	root = FXMLLoader.load(getClass().getResource("atmOverview.fxml"));  
        	Scene scene = new Scene(root, 900, 600);     
        	this.main.setScene(scene);
    	} else {
    		loginMessage.setValue("Falsche Eingabe");
    	}
    	    	
    }
	
	public void SetOverview() {
		bankName.setValue("Raiffeisen");
		//Bank Name setzen anhand von Kontonummer
		customerName.setValue("Oliver Kunz");
		//Kundenname setzen anhand von Kontonummer 
	}
    
    public void WithdrawMoney(final ActionEvent event) throws IOException {
    	
    }
    
    public void setMain(Main main) {
    	this.main = main;
    }
    
    
	public SimpleStringProperty getLoginMessage() {
		return loginMessage;
	}

	public void setLoginMessage(SimpleStringProperty loginMessage) {
		this.loginMessage = loginMessage;
	}

	public SimpleStringProperty getAccountID() {
		return accountID;
	}

	public void setEmail(SimpleStringProperty accountID) {
		this.accountID = accountID;
	}

	public SimpleStringProperty getCustomerPIN() {
		return customerPIN;
	}

	public void setPassword(SimpleStringProperty customerPIN) {
		this.customerPIN = customerPIN;
	}
	
	public SimpleStringProperty getAmount() {
		return amount;
	}

	public void setAmount(SimpleStringProperty amount) {
		this.amount = amount;
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
