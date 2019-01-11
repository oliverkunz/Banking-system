package frontend.atm;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller implements Initializable {
	
	Main main = null;
	
	@FXML
    private Button loginButton;
	@FXML
    private TextField customerIDTF;
	@FXML
    private PasswordField customerPINPF;
	@FXML
    private Label loginMessageL;
	
	
	private SimpleStringProperty loginMessage = new SimpleStringProperty("");
	private SimpleStringProperty customerID = new SimpleStringProperty("");
	private SimpleStringProperty customerPIN = new SimpleStringProperty("");
	

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	customerIDTF.textProperty().bindBidirectional(this.getCustomerID());
    	customerPINPF.textProperty().bindBidirectional(this.getCustomerPIN());
    	loginMessageL.textProperty().bindBidirectional(this.getLoginMessage());
    }
    
    @FXML
    public void PressLoginButton(final ActionEvent event) throws IOException  {
    	if (customerID.getValue().equals("test") && customerPIN.getValue().equals("1234")) {
    		loginMessage.setValue("Login erfolgreich");  	
    	
    		Parent root;
        	root = FXMLLoader.load(getClass().getResource("atmOverview.fxml"));  
        	Scene scene = new Scene(root);     
        	this.main.setScene(scene);
    	} else {
    		loginMessage.setValue("Falsche Eingabe");
    	}
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

	public SimpleStringProperty getCustomerID() {
		return customerID;
	}

	public void setEmail(SimpleStringProperty customerID) {
		this.customerID = customerID;
	}

	public SimpleStringProperty getCustomerPIN() {
		return customerPIN;
	}

	public void setPassword(SimpleStringProperty customerPIN) {
		this.customerPIN = customerPIN;
	}
    
}
