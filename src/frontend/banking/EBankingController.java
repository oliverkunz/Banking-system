package frontend.banking;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import frontend.common.Customer;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

//Frontend: overview of the ebanking application
public class EBankingController extends BaseController implements Initializable {

	@FXML
	private Button loginButton;
	@FXML
	private TextField customerIDTF;
	@FXML
	private PasswordField passwordPF;
	@FXML
	private Label loginMessageL;
	@FXML
	private Label bankNameL;

	private SimpleStringProperty loginMessage = new SimpleStringProperty("");
	private SimpleStringProperty password = new SimpleStringProperty("");
	private SimpleStringProperty customerID = new SimpleStringProperty("");

	public EBankingController(EBankingMain main) {
		super(main);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// binding the input-fields
		customerIDTF.textProperty().bindBidirectional(this.getCustomerID());
		passwordPF.textProperty().bindBidirectional(this.getPassword());
		loginMessageL.textProperty().bindBidirectional(this.getLoginMessage());
		// Setting the right bankname according to property file
		try {
			bankNameL.setText(this.main.getBanking().getBankname());
		} catch (RemoteException e) {
		}
	}

	// function for login to th ebanking
	@FXML
	public void PressLoginButton(final ActionEvent event) throws IOException {
		// checking user input ang login to the right account
		if (this.main.getBanking().login(customerID.getValue(), password.getValue())) {
			loginMessage.setValue("Login erfolgreich");

			this.main.setLoggedInCustomer(new Customer(null, null, customerID.getValue()));
			this.main.setScene("overview");
		} else {
			loginMessage.setValue("Falsche Eingaben");
		}

	}

	public SimpleStringProperty getLoginMessage() {
		return loginMessage;
	}

	public void setLoginMessage(SimpleStringProperty loginMessage) {
		this.loginMessage = loginMessage;
	}

	public SimpleStringProperty getCustomerID() {
		return customerID;
	}

	public void setEmail(SimpleStringProperty customerID) {
		this.customerID = customerID;
	}

	public SimpleStringProperty getPassword() {
		return password;
	}

	public void setPassword(SimpleStringProperty password) {
		this.password = password;
	}

	@Override
	public void onNavigate(String route) {
		this.main.refreshData();
	}

}
