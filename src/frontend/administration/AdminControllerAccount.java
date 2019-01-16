package frontend.administration;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.util.ResourceBundle;

import backend.api.Account;
import frontend.banking.BaseController;
import frontend.banking.EBankingMain;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminControllerAccount extends AdminBaseController implements Initializable {
	
	@FXML private Button implementButton;
	@FXML private Button backButton;
	
	@FXML private RadioButton depositRButton;
	@FXML private RadioButton withdrawRButton;
	
	@FXML private TextField accountNumberTF;
	@FXML private TextField amountTF;
	
    @FXML private TableColumn<Account, String> colAccountT;
  
    @FXML private TableColumn<Account, Double> colBalanceT;
    
    @FXML private TableView<Account> accountsT;

    private final ObservableList<Account> accountsObservableList = FXCollections.observableArrayList();
    
    Alert info = new Alert(AlertType.INFORMATION, "Es kann nur eine Aktion gewählt werden");
	
	private SimpleStringProperty accountNumber = new SimpleStringProperty("");
	private SimpleStringProperty amount = new SimpleStringProperty("");
	
	public AdminControllerAccount(AdminMain adminMain) {
		super(adminMain);
	}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	accountNumberTF.textProperty().bindBidirectional(this.getAccountNumber());
    	amountTF.textProperty().bindBidirectional(this.getAmount());
    	
    	accountsT.setItems(accountsObservableList);

    	accountsT.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
    	    if (newSelection != null) {
    		this.adminMain.setSelectedAccount(newSelection);
    	    }
    	});

    	colBalanceT.setCellValueFactory(new PropertyValueFactory<Account, Double>("balance"));
    	colAccountT.setCellValueFactory(new PropertyValueFactory<Account, String>("accountID"));
    }
 
	@FXML
    public void TransferMoney(final ActionEvent event) throws IOException, NotBoundException  {
		if (depositRButton.isSelected() && withdrawRButton.isSelected()) {
			info.showAndWait();
		} else {
		
			if (depositRButton.isSelected()) {
				double amountD = Double.parseDouble(amount.getValue());
				this.adminMain.getAdministration().deposit(this.adminMain.getSelectedAccount().getAccountID(), amountD);
			}
			if (withdrawRButton.isSelected()) {
				double amountD = Double.parseDouble(amount.getValue());
				this.adminMain.getAdministration().withdraw(this.adminMain.getSelectedAccount().getAccountID(), amountD);
				this.adminMain.getAdministration().deposit(accountNumber.getValue(), amountD);
				//geld auf angegebenes Konto vom gewählten Konto senden
			}
		}  	    	
    }
	
	@FXML
    public void ShowOverview(final ActionEvent event) throws IOException {	
    	this.adminMain.setScene("admin");	    	
    }

	public SimpleStringProperty getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(SimpleStringProperty accountNumber) {
		this.accountNumber = accountNumber;
	}

	public SimpleStringProperty getAmount() {
		return amount;
	}

	public void setAmount(SimpleStringProperty amount) {
		this.amount = amount;
	}

	@Override
	public void onNavigate(String route) {
		
	}
	

}
