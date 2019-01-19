package frontend.administration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import backend.api.AccountType;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class AdminController extends AdminBaseController implements Initializable {
		
	@FXML private Button registerButton;
	@FXML private Button openButton;
	@FXML private Button resolveButton;
	@FXML private Button showButton;
	
	@FXML private RadioButton privateRButton;
	@FXML private RadioButton savingRButton;
	
	@FXML private TextField lastNameTF;
	@FXML private TextField firstNameTF;
	@FXML private PasswordField passwordTF;
	@FXML private TextField interestTF;
	@FXML private TextField dailyLimitTF;
	@FXML private TextField monthlyLimitTF;
	@FXML private TextField overdraftTF;
	@FXML private TextField maxMinusTF;
	@FXML private TextField pinTF;
	@FXML private TextField customerIDaccountTF;
	@FXML private TextField accountIDresolveTF;
	@FXML private TextField accountIDactionsTF;
	
	Alert info = new Alert(AlertType.ERROR, "Es kann nur ein Kontotyp gewährt werden");
	Alert wrongInput = new Alert(AlertType.ERROR, "Bitte alle Felder korrekt ausfüllen");
	Alert wrongID = new Alert(AlertType.ERROR, "Kontonummer ist ungültig");
	Alert registerAccount = new Alert(AlertType.INFORMATION,"Account erstellt, ID in den Zwischenspeicher kopiert");
	Alert registerCustomer = new Alert(AlertType.INFORMATION, "Kunde erstellt, ID in den Zwischenspeicher kopiert");

	
	final Clipboard clipboard = Clipboard.getSystemClipboard();
	final ClipboardContent content = new ClipboardContent();

	private SimpleStringProperty lastName = new SimpleStringProperty("");
	private SimpleStringProperty firstName = new SimpleStringProperty("");
	private SimpleStringProperty password = new SimpleStringProperty("");
	private SimpleStringProperty interest = new SimpleStringProperty();
	private SimpleStringProperty dailyLimit = new SimpleStringProperty();
	private SimpleStringProperty monthlyLimit = new SimpleStringProperty();
	private SimpleStringProperty overdraft = new SimpleStringProperty();
	private SimpleStringProperty maxMinus = new SimpleStringProperty();
	private SimpleStringProperty pin = new SimpleStringProperty();
	private SimpleStringProperty customerIDaccount = new SimpleStringProperty("");
	private SimpleStringProperty accountIDresolve = new SimpleStringProperty("");
	private SimpleStringProperty accountIDactions = new SimpleStringProperty("");
	
	public AdminController(AdminMain adminMain) {
		super(adminMain);
	}

    @Override
    public void initialize(URL location, ResourceBundle resources) {  	
    	lastNameTF.textProperty().bindBidirectional(this.getLastName());
    	firstNameTF.textProperty().bindBidirectional(this.getFirstName());
    	passwordTF.textProperty().bindBidirectional(this.getPassword());
    	interestTF.textProperty().bindBidirectional(this.getInterest());
    	dailyLimitTF.textProperty().bindBidirectional(this.getDailyLimit());
    	monthlyLimitTF.textProperty().bindBidirectional(this.getMonthlyLimit());
    	overdraftTF.textProperty().bindBidirectional(this.getOverdraft());
    	maxMinusTF.textProperty().bindBidirectional(this.getMaxMinus());
    	pinTF.textProperty().bindBidirectional(this.getPin());
    	customerIDaccountTF.textProperty().bindBidirectional(this.getCustomerIDaccount());
    	accountIDresolveTF.textProperty().bindBidirectional(this.getAccountIDresolve());
    	accountIDactionsTF.textProperty().bindBidirectional(this.getAccountIDactions());  
    }
       
	@FXML
    public void RegisterCustomer(final ActionEvent event) throws IOException  {
		
		String regex="[a-zA-Z]{0,}";
    	if (firstName.getValue().toString().matches(regex) && lastName.getValue().matches(regex) && !(password.getValue().isEmpty()) && !(firstName.getValue().isEmpty()) && !(lastName.getValue().isEmpty())) {
    		
    		String customerID = this.adminMain.getAdministration().createCustomer(firstName.getValue(), lastName.getValue(), password.getValue());
    		content.putString(customerID);
    		clipboard.setContent(content);
    		
        	registerCustomer.showAndWait();
        	
    	} else {
    		wrongInput.showAndWait();
    	}    	
    }
	
	@FXML
    public void OpenAccount(final ActionEvent event) throws IOException  {
		if (privateRButton.isSelected() && (savingRButton.isSelected())) {
			info.showAndWait();
		} else {
			double accountBalance = 0;
			try {
				double interestD = Double.parseDouble(interest.getValue());
				double overdraftD = Double.parseDouble(overdraft.getValue());
				double dailyLimitD = Double.parseDouble(dailyLimit.getValue());
				double monthlyLimitD = Double.parseDouble(monthlyLimit.getValue());
				double maxMinusD = Double.parseDouble(maxMinus.getValue());
				int pinI = Integer.parseInt(pin.getValue());
				String accountID = this.adminMain.getAdministration().createAccount(customerIDaccount.getValue(), GetAccountType(), accountBalance, interestD, overdraftD, dailyLimitD, monthlyLimitD, maxMinusD, pinI);
	    		content.putString(accountID);
	    		clipboard.setContent(content);
				registerAccount.showAndWait();
			} catch(NumberFormatException | NullPointerException e) {
				wrongInput.showAndWait();
			}			
		} 	    	
    }
	
	public AccountType GetAccountType() {
		AccountType accountType = null;
		if (privateRButton.isSelected()) {
			accountType = accountType.PRIVATE;
		}
		if (savingRButton.isSelected()) {
			accountType = accountType.SAVINGS;
		}
		return accountType;
	} 
	
	
	@FXML
    public void ResolveAccount(final ActionEvent event) throws IOException  {
    	try {
    		this.adminMain.getAdministration().closeAccount(accountIDresolve.getValue());
    	} catch (NumberFormatException | NullPointerException e) {
    		
    	}  	    	
    }
	
	@FXML
    public void ShowAccount(final ActionEvent event) throws IOException  {

			this.adminMain.setSelectedAccount(this.adminMain.getAdministration().showAccount(accountIDactions.getValue())); 

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

	public SimpleStringProperty getMaxMinus() {
		return maxMinus;
	}

	public void setMaxMinus(SimpleStringProperty maxMinus) {
		this.maxMinus = maxMinus;
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

	@Override
	public void onNavigate(String route) {
		
	}
    
}
