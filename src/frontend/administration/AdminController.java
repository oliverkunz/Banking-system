package frontend.administration;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import backend.api.AccountType;
import frontend.common.Customer;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

//Frontend: overview of the administration application
public class AdminController extends AdminBaseController implements Initializable {

	@FXML
	private Button registerButton;
	@FXML
	private Button openButton;
	@FXML
	private Button resolveButton;
	@FXML
	private Button showButton;

	@FXML
	private RadioButton privateRButton;
	@FXML
	private RadioButton savingRButton;

	@FXML
	private TextField lastNameTF;
	@FXML
	private TextField firstNameTF;
	@FXML
	private PasswordField passwordTF;
	@FXML
	private TextField interestTF;
	@FXML
	private TextField dailyLimitTF;
	@FXML
	private TextField monthlyLimitTF;
	@FXML
	private TextField overdraftTF;
	@FXML
	private TextField maxMinusTF;
	@FXML
	private PasswordField pinTF;
	@FXML
	private TextField customerIDaccountTF;
	@FXML
	private TextField accountIDresolveTF;
	@FXML
	private TextField customerIDactionsTF;

	@FXML
	private Label bankNameL;

	// pop-up dialog fields for simple user communication (confirm actions & input
	// errors)
	Alert info = new Alert(AlertType.ERROR, "Es kann nur ein Kontotyp gew�hrt werden");
	Alert wrongInput = new Alert(AlertType.ERROR, "Bitte alle Felder korrekt ausf�llen");
	Alert wrongID = new Alert(AlertType.ERROR, "Kontonummer ist ung�ltig");
	Alert registerAccount = new Alert(AlertType.INFORMATION, "Account erstellt, ID in den Zwischenspeicher kopiert");
	Alert registerCustomer = new Alert(AlertType.INFORMATION, "Kunde erstellt, ID in den Zwischenspeicher kopiert");

	// the clipboard is used to temporarly store the values of the customerID &
	// accountIDs
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
	private SimpleStringProperty customerIDactions = new SimpleStringProperty("");

	public AdminController(AdminMain adminMain) {
		super(adminMain);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// binding the input-fields
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
		customerIDactionsTF.textProperty().bindBidirectional(this.getCustomerIDactions());

		// set correct bankname
		try {
			this.bankNameL.setText(this.adminMain.getAdministration().getBankname());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	// function for customer registration
	@FXML
	public void RegisterCustomer(final ActionEvent event) throws IOException {
		// regex is used for input validation purposes
		String regex = "[a-zA-Z]{0,}";
		// checking if all input-fields are used correctly
		if (firstName.getValue().toString().matches(regex) && lastName.getValue().matches(regex)
				&& !(password.getValue().isEmpty()) && !(firstName.getValue().isEmpty())
				&& !(lastName.getValue().isEmpty())) {

			// customerID as return if the registration was successful
			String customerID = this.adminMain.getAdministration().createCustomer(firstName.getValue(),
					lastName.getValue(), password.getValue());
			content.putString(customerID);
			clipboard.setContent(content);
			registerCustomer.showAndWait();

		} else {
			wrongInput.showAndWait();
		}
	}

	// function for opening an account
	@FXML
	public void OpenAccount(final ActionEvent event) throws IOException {

		// check that only one account type is specified
		if (privateRButton.isSelected() && (savingRButton.isSelected())) {
			info.showAndWait();
		} else {
			double accountBalance = 0;
			try {
				// converting and also checking if the input is correct -> numbers only
				double interestD = Double.parseDouble(interest.getValue());
				double overdraftD = Double.parseDouble(overdraft.getValue());
				double dailyLimitD = Double.parseDouble(dailyLimit.getValue());
				double monthlyLimitD = Double.parseDouble(monthlyLimit.getValue());
				double maxMinusD = Double.parseDouble(maxMinus.getValue());
				int pinI = Integer.parseInt(pin.getValue());
				// getting the accountID of the newly created account as return value
				String accountID = this.adminMain.getAdministration().createAccount(customerIDaccount.getValue(),
						GetAccountType(), accountBalance, interestD, overdraftD, dailyLimitD, monthlyLimitD, maxMinusD,
						pinI);
				content.putString(accountID);
				clipboard.setContent(content);
				registerAccount.showAndWait();
			} catch (NumberFormatException | NullPointerException e) {
				wrongInput.showAndWait();
			}
		}
	}

	// short function to evaluate the account type according to which RadioButton
	// was selected
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

	// function to resolve an previous created account
	@FXML
	public void ResolveAccount(final ActionEvent event) throws IOException {
		try {
			this.adminMain.getAdministration().closeAccount(accountIDresolve.getValue());
		} catch (NumberFormatException | NullPointerException e) {

		}

	}

	// function to give the customer an overview over all his accounts
	@FXML
	public void ShowCustomer(final ActionEvent event) throws IOException {
		this.adminMain.setSelectedCustomer(new Customer(null, null, this.customerIDactions.getValue()));
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

	public SimpleStringProperty getCustomerIDactions() {
		return customerIDactions;
	}

	public void setCustomerIDactions(SimpleStringProperty customerIDactions) {
		this.customerIDactions = customerIDactions;
	}

	@Override
	public void onNavigate(String route) {

	}

}
