package frontend.atm;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import backend.api.Account;
import backend.api.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Controller extends BaseControllerATM implements Initializable {
	
	@FXML private Button loginButton;
	@FXML private TextField accountIDTF;
	@FXML private PasswordField customerPINPF;
	@FXML private Label loginMessageL;
	
	private SimpleStringProperty loginMessage = new SimpleStringProperty("");
	private SimpleStringProperty customerPIN = new SimpleStringProperty("");
	private SimpleStringProperty accountID = new SimpleStringProperty("");
	
	public Controller(Main main) {
		super(main);
	}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	accountIDTF.textProperty().bindBidirectional(this.getAccountID());
    	customerPINPF.textProperty().bindBidirectional(this.getCustomerPIN());
    	loginMessageL.textProperty().bindBidirectional(this.getLoginMessage());
    }

	@FXML
    public void PressLoginButton(final ActionEvent event) throws IOException  {
		int pinI = Integer.parseInt(customerPIN.getValue());
    	if (this.main.getATM().login(accountID.getValue(), pinI)) {
    		loginMessage.setValue("Login erfolgreich"); 	
    	

			this.main.setLoggedInAccount(accountID.getValue(), null, null); // hooooooow tf
    		this.main.setScene("atmOverview");
    	} else {
    		loginMessage.setValue("Falsche Eingabe");
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
