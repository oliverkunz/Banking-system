package frontend.banking;
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

public class EBankingController implements Initializable {
	
	EBankingMain eBankingMain = null;
	
	@FXML private Button loginButton;
	@FXML private TextField customerIDTF;
	@FXML private PasswordField passwordPF;
	@FXML private Label loginMessageL;


	
	private SimpleStringProperty loginMessage = new SimpleStringProperty("");
	private SimpleStringProperty password = new SimpleStringProperty("");
	private SimpleStringProperty customerID = new SimpleStringProperty("");
	

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	customerIDTF.textProperty().bindBidirectional(this.getCustomerID());
    	passwordPF.textProperty().bindBidirectional(this.getPassword());
    	loginMessageL.textProperty().bindBidirectional(this.getLoginMessage());
    }
 
	@FXML
    public void PressLoginButton(final ActionEvent event) throws IOException  {
    	if (customerID.getValue().equals("test") && password.getValue().equals("1234")) {
    		loginMessage.setValue("Login erfolgreich"); 	
    	
    		Parent root;
        	root = FXMLLoader.load(getClass().getResource("eBankingOverview.fxml"));  
        	Scene scene = new Scene(root, 900, 600);     
        	this.eBankingMain.setScene(scene);
    	} else {
    		loginMessage.setValue("Falsche Eingaben");
    	}
    	    	
    }
    
    public void setEBankingMain(EBankingMain eBankingMain) {
    	this.eBankingMain = eBankingMain;
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

}
