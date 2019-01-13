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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class AdminController implements Initializable {
	
	AdminMain adminMain = null;
	
	@FXML private Button registerButton;
	@FXML private Button openButton;
	@FXML private Button resolveButton;
	@FXML private Button showButton;
	
	@FXML private RadioButton privateRButton;
	@FXML private RadioButton savingRButton;
	
	@FXML private TextField lastNameTF;
	@FXML private TextField firstNameTF;
	@FXML private TextField passwordTF;
	@FXML private TextField customerIDregisterTF;
	@FXML private TextField interestTF;
	@FXML private TextField dailyLimitTF;
	@FXML private TextField monthlyLimitTF;
	@FXML private TextField overdraftTF;
	@FXML private TextField accountIDTF;
	@FXML private TextField pinTF;
	@FXML private TextField customerIDaccountTF;
	@FXML private TextField accountIDresolveTF;
	@FXML private TextField accountIDactionsTF;


	
	private SimpleStringProperty lastName = new SimpleStringProperty("");
	private SimpleStringProperty firstName = new SimpleStringProperty("");
	private SimpleStringProperty password = new SimpleStringProperty("");
	private SimpleStringProperty customerIDregister = new SimpleStringProperty("");
	private SimpleStringProperty interest = new SimpleStringProperty("");
	private SimpleStringProperty dailyLimit = new SimpleStringProperty("");
	private SimpleStringProperty monthlyLimit = new SimpleStringProperty("");
	private SimpleStringProperty overdraft = new SimpleStringProperty("");
	private SimpleStringProperty accountID = new SimpleStringProperty("");
	private SimpleStringProperty pin = new SimpleStringProperty("");
	private SimpleStringProperty customerIDaccount = new SimpleStringProperty("");
	private SimpleStringProperty accountIDresolve = new SimpleStringProperty("");
	private SimpleStringProperty accountIDactions = new SimpleStringProperty("");
	

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	lastNameTF.textProperty().bindBidirectional(this.getLastName());
    	firstNameTF.textProperty().bindBidirectional(this.getFirstName());
    	passwordTF.textProperty().bindBidirectional(this.getPassword());
    	customerIDregisterTF.textProperty().bindBidirectional(this.getCustomerIDregister());
    	interestTF.textProperty().bindBidirectional(this.getInterest());
    	dailyLimitTF.textProperty().bindBidirectional(this.getDailyLimit());
    	monthlyLimitTF.textProperty().bindBidirectional(this.getMonthlyLimit());
    	overdraftTF.textProperty().bindBidirectional(this.getOverdraft());
    	accountIDTF.textProperty().bindBidirectional(this.getAccountID());
    	pinTF.textProperty().bindBidirectional(this.getPin());
    	customerIDaccountTF.textProperty().bindBidirectional(this.getCustomerIDaccount());
    	accountIDresolveTF.textProperty().bindBidirectional(this.getAccountIDresolve());
    	accountIDactionsTF.textProperty().bindBidirectional(this.getAccountIDactions());
    }
 
	@FXML
    public void RegisterCustomer(final ActionEvent event) throws IOException  {
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
    public void ShowAccount(final ActionEvent event) throws IOException  {
		//Kontonummer logic
		
		Parent root;
    	root = FXMLLoader.load(getClass().getResource("adminAccount.fxml"));  
    	Scene scene = new Scene(root, 900, 600);     
    	this.adminMain.setScene(scene);
    	    	
    }
    
    public void setAdminMain(AdminMain adminMain) {
    	this.adminMain = adminMain;
    }

	public SimpleStringProperty getLastName() {
		return lastName;
	}

	public void setLastName(SimpleStringProperty lastName) {
		this.lastName = lastName;
	}

	public SimpleStringProperty getFirstName() {
		return firstName;
	}

	public void setFirstName(SimpleStringProperty firstName) {
		this.firstName = firstName;
	}

	public SimpleStringProperty getPassword() {
		return password;
	}

	public void setPassword(SimpleStringProperty password) {
		this.password = password;
	}

	public SimpleStringProperty getCustomerIDregister() {
		return customerIDregister;
	}

	public void setCustomerIDregister(SimpleStringProperty customerIDregister) {
		this.customerIDregister = customerIDregister;
	}

	public SimpleStringProperty getInterest() {
		return interest;
	}

	public void setInterest(SimpleStringProperty interest) {
		this.interest = interest;
	}

	public SimpleStringProperty getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(SimpleStringProperty dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public SimpleStringProperty getMonthlyLimit() {
		return monthlyLimit;
	}

	public void setMonthlyLimit(SimpleStringProperty monthlyLimit) {
		this.monthlyLimit = monthlyLimit;
	}

	public SimpleStringProperty getOverdraft() {
		return overdraft;
	}

	public void setOverdraft(SimpleStringProperty overdraft) {
		this.overdraft = overdraft;
	}

	public SimpleStringProperty getAccountID() {
		return accountID;
	}

	public void setAccountID(SimpleStringProperty accountID) {
		this.accountID = accountID;
	}

	public SimpleStringProperty getPin() {
		return pin;
	}

	public void setPin(SimpleStringProperty pin) {
		this.pin = pin;
	}

	public SimpleStringProperty getCustomerIDaccount() {
		return customerIDaccount;
	}

	public void setCustomerIDaccount(SimpleStringProperty customerIDaccount) {
		this.customerIDaccount = customerIDaccount;
	}

	public SimpleStringProperty getAccountIDresolve() {
		return accountIDresolve;
	}

	public void setAccountIDresolve(SimpleStringProperty accountIDresolve) {
		this.accountIDresolve = accountIDresolve;
	}

	public SimpleStringProperty getAccountIDactions() {
		return accountIDactions;
	}

	public void setAccountIDactions(SimpleStringProperty accountIDactions) {
		this.accountIDactions = accountIDactions;
	}
    
}
