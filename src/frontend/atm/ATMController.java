package frontend.atm;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import backend.api.Account;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

//Frontend: ATM login
public class ATMController extends BaseControllerATM implements Initializable {

	@FXML
	private Button loginButton;
	@FXML
	private TextField accountIDTF;
	@FXML
	private PasswordField customerPINPF;
	@FXML
	private Label loginMessageL;

	private SimpleStringProperty loginMessage = new SimpleStringProperty("");
	private SimpleStringProperty customerPIN = new SimpleStringProperty("");
	private SimpleStringProperty accountID = new SimpleStringProperty("");

	// pop-up dialog fields for simple user communication (confirm actions & input
	// errors)
	Alert error = new Alert(AlertType.ERROR, "Ungültige Kontonummer");
	Alert wrongInput = new Alert(AlertType.ERROR, "Ungültige Kontonummer");

	public ATMController(ATMMain main) {
		super(main);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// binding the input-fields
		accountIDTF.textProperty().bindBidirectional(this.getAccountID());
		customerPINPF.textProperty().bindBidirectional(this.getCustomerPIN());
		loginMessageL.textProperty().bindBidirectional(this.getLoginMessage());
	}

	// function for handling the login
	@FXML
	public void PressLoginButton(final ActionEvent event) throws IOException {
		// checking if the accountID is correct
		try {
			int pinI = Integer.parseInt(customerPIN.getValue());

			String[] parts = this.accountID.getValue().split("_");
			if (parts.length != 2) {
				error.showAndWait();
				return;
			}
			// loading the right bank according to the provided accountID
			this.main.initializeATMForBank(parts[0]);

			// login and switching scene
			if (this.main.getATM().login(accountID.getValue(), pinI)) {
				loginMessage.setValue("Login erfolgreich");

				this.main.setLoggedInAccount(new Account(accountID.getValue(), 0));
				this.main.setScene("atmOverview");
			} else {
				loginMessage.setValue("Falsche Eingabe");
			}
		} catch (NumberFormatException | NullPointerException e) {
			wrongInput.showAndWait();
		}

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

	@Override
	public void onNavigate(String route) {

	}

}
