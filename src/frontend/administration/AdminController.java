package frontend.administration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import backend.api.AccountType;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.converter.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

public class AdminController extends AdminBaseController implements Initializable {
		
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
	@FXML private TextField accountOverdraftTF;
	@FXML private TextField pinTF;
	@FXML private TextField customerIDaccountTF;
	@FXML private TextField accountIDresolveTF;
	@FXML private TextField accountIDactionsTF;
	
	Alert info = new Alert(AlertType.INFORMATION, "Es kann nur ein Kontotyp gewährt werden");
	Alert register = new Alert(AlertType.INFORMATION, "Kunde erstellt");
	
	private SimpleStringProperty lastName = new SimpleStringProperty("");
	private SimpleStringProperty firstName = new SimpleStringProperty("");
	private SimpleStringProperty password = new SimpleStringProperty("");
	private SimpleStringProperty customerIDregister = new SimpleStringProperty("");
	private SimpleDoubleProperty interest = new SimpleDoubleProperty();
	private SimpleDoubleProperty dailyLimit = new SimpleDoubleProperty();
	private SimpleDoubleProperty monthlyLimit = new SimpleDoubleProperty();
	private SimpleDoubleProperty overdraft = new SimpleDoubleProperty();
	private SimpleDoubleProperty accountOverdraft = new SimpleDoubleProperty();
	private SimpleIntegerProperty pin = new SimpleIntegerProperty();
	private SimpleStringProperty customerIDaccount = new SimpleStringProperty("");
	private SimpleStringProperty accountIDresolve = new SimpleStringProperty("");
	private SimpleStringProperty accountIDactions = new SimpleStringProperty("");
	
	public AdminController(AdminMain adminMain) {
		super(adminMain);
	}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	//Textfield to double Binding doesnt work
    	
    	lastNameTF.textProperty().bindBidirectional(this.getLastName());
    	firstNameTF.textProperty().bindBidirectional(this.getFirstName());
    	passwordTF.textProperty().bindBidirectional(this.getPassword());
    	customerIDregisterTF.textProperty().bindBidirectional(this.getCustomerIDregister());
    	interestTF.textProperty().bindBidirectional(this.getInterest());
    	dailyLimitTF.textProperty().bindBidirectional(this.getDailyLimit());
    	monthlyLimitTF.textProperty().bindBidirectional(this.getMonthlyLimit());
    	overdraftTF.textProperty().bindBidirectional(this.getOverdraft());
    	accountOverdraftTF.textProperty().bindBidirectional(this.getAccountOverdraft());
    	pinTF.textProperty().bindBidirectional(this.getPin());
    	
    	//DoubleStringConverter converter =  new DoubleStringConverter();
    	//Bindings.bindBidirectional(pinTF.textProperty(), getPin(), (DoubleStringConverter)converter);
    	customerIDaccountTF.textProperty().bindBidirectional(this.getCustomerIDaccount());
    	accountIDresolveTF.textProperty().bindBidirectional(this.getAccountIDresolve());
    	accountIDactionsTF.textProperty().bindBidirectional(this.getAccountIDactions());
   
    }
       
 
	@FXML
    public void RegisterCustomer(final ActionEvent event) throws IOException  {
    	this.adminMain.getAdministration().createCustomer(firstName.getValue(), lastName.getValue(), password.getValue());
    	register.showAndWait();
    	
    }
	
	@FXML
    public void OpenAccount(final ActionEvent event) throws IOException  {
		if (privateRButton.isSelected() && (savingRButton.isSelected())) {
			info.showAndWait();
		} else {
			double accountBalance = 0;
			this.adminMain.getAdministration().createAccount(customerIDaccount.getValue(), GetAccountType(), accountBalance, interest.getValue().doubleValue(), overdraft.getValue().doubleValue(), dailyLimit.getValue().doubleValue(), monthlyLimit.getValue().doubleValue(), accountOverdraft.getValue().doubleValue(), pin.getValue().intValue());
		}
    	    	
    }
	
	public AccountType GetAccountType() {
		AccountType accountType;
		if (privateRButton.isSelected()) {
			//set accountType PRIVATE
		}
		if (savingRButton.isSelected()) {
			//accountType.SAVINGS;
		}
		return accountType;
	} 
	
	
	@FXML
    public void ResolveAccount(final ActionEvent event) throws IOException  {
    	this.adminMain.getAdministration().closeAccount(accountIDresolve.getValue());
    	    	
    }
	
	@FXML
    public void ShowAccount(final ActionEvent event) throws IOException  {
		this.adminMain.getAdministration().showAccount(accountIDactions.getValue());
		    
    	this.adminMain.setScene("account");
    	    	
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

	public SimpleDoubleProperty getInterest() {
		return interest;
	}

	public void setInterest(SimpleDoubleProperty interest) {
		this.interest = interest;
	}

	public SimpleDoubleProperty getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(SimpleDoubleProperty dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public SimpleDoubleProperty getMonthlyLimit() {
		return monthlyLimit;
	}

	public void setMonthlyLimit(SimpleDoubleProperty monthlyLimit) {
		this.monthlyLimit = monthlyLimit;
	}

	public SimpleDoubleProperty getOverdraft() {
		return overdraft;
	}

	public void setOverdraft(SimpleDoubleProperty overdraft) {
		this.overdraft = overdraft;
	}

	public SimpleDoubleProperty getAccountOverdraft() {
		return accountOverdraft;
	}

	public void setAccountOverdraft(SimpleDoubleProperty accountOverdraft) {
		this.accountOverdraft = accountOverdraft;
	}

	public SimpleIntegerProperty getPin() {
		return pin;
	}

	public void setPin(SimpleIntegerProperty pin) {
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

	@Override
	public void onNavigate(String route) {
		
	}
    
}
