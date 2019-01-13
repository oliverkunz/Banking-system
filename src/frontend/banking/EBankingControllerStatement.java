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

public class EBankingControllerStatement implements Initializable {

EBankingMain eBankingMain = null;
	
//TAbleview
	@FXML private Button statementB;
	@FXML private Label accountL;
	
	private SimpleStringProperty account = new SimpleStringProperty("");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	accountL.textProperty().bindBidirectional(this.getAccount());
    }
 	
	@FXML
    public void BackToOverview(final ActionEvent event) throws IOException  {
		Parent root;
    	root = FXMLLoader.load(getClass().getResource("eBankingOverview.fxml"));  
    	Scene scene = new Scene(root, 900, 600);     
    	this.eBankingMain.setScene(scene);
    }
    
    public void setEBankingMain(EBankingMain eBankingMain) {
    	this.eBankingMain = eBankingMain;
    }
    
    
	public SimpleStringProperty getAccount() {
		return account;
	}

	public void setAmount(SimpleStringProperty account) {
		this.account = account;
	}

}
