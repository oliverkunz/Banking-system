package frontend.banking;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import backend.api.Account;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.NumberStringConverter;

public class EBankingControllerOverview extends BaseController implements Initializable {

    @FXML
    private Button statementB;
    @FXML
    private Button transferB;
    @FXML
    private TextField amountTF;
    @FXML
    private TextField senderTF;
    @FXML
    private TextField receiverTF;
    @FXML
    private Label bankNameL;
    @FXML
    private Label customerNameL;
    @FXML
    private TableColumn<Account, String> colAccountT;
    @FXML
    private TableColumn<Account, Double> colBalanceT;
    @FXML
    private TableView<Account> accountsT;

    private final ObservableList<Account> accountsObservableList = FXCollections.observableArrayList();

    // Tableview -----todo

    Alert alert = new Alert(AlertType.CONFIRMATION, "Bitte Transaktion bestätigen");
    Alert info = new Alert(AlertType.INFORMATION, "Transaktion ausgeführt");
    Alert error = new Alert(AlertType.ERROR, "Transaktion abgebrochen");

    private SimpleDoubleProperty amount = new SimpleDoubleProperty();
    private SimpleStringProperty sender = new SimpleStringProperty("");
    private SimpleStringProperty receiver = new SimpleStringProperty("");
    private SimpleStringProperty bankName = new SimpleStringProperty("");
    private SimpleStringProperty customerName = new SimpleStringProperty("");

    public EBankingControllerOverview(EBankingMain main) {
	super(main);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	amountTF.textProperty().bindBidirectional(this.getAmount(), new NumberStringConverter());

	receiverTF.textProperty().bindBidirectional(this.getReceiver());
	bankNameL.textProperty().bindBidirectional(this.getBankName());
	customerNameL.textProperty().bindBidirectional(this.getCustomerName());

	accountsT.setItems(accountsObservableList);

	accountsT.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	    if (newSelection != null) {
		this.main.setSelectedAccount(newSelection);
	    }
	});

	colBalanceT.setCellValueFactory(new PropertyValueFactory<Account, Double>("balance"));
	colAccountT.setCellValueFactory(new PropertyValueFactory<Account, String>("accountID"));
    }

    @FXML
    public void TransferMoney(final ActionEvent event) throws IOException {

	Optional<ButtonType> result = alert.showAndWait();
	if (result.get() == ButtonType.OK) {

	    try {
		boolean rmiResult = this.main.getBanking().transfer(this.main.selectedAccount.getAccountID(),
			this.getReceiver().getValue(), this.getAmount().getValue(), LocalDate.now());

		this.refreshAccounts();
	    } catch (NotBoundException e) {
		e.printStackTrace();
	    }

	    info.showAndWait();
	} else {
	    error.showAndWait();
	}
    }

    @FXML
    public void GetAccountStatement(final ActionEvent event) throws IOException {
	this.main.setScene("statement");
    }

    public SimpleDoubleProperty getAmount() {
	return amount;
    }

    public void setAmount(SimpleDoubleProperty amount) {
	this.amount = amount;
    }

    public SimpleStringProperty getSender() {
	return sender;
    }

    public void setSender(SimpleStringProperty sender) {
	this.sender = sender;
    }

    public SimpleStringProperty getReceiver() {
	return receiver;
    }

    public void setReceiver(SimpleStringProperty receiver) {
	this.receiver = receiver;
    }

    public SimpleStringProperty getBankName() {
	return bankName;
    }

    public void setBankName(SimpleStringProperty bankName) {
	this.bankName = bankName;
    }

    public SimpleStringProperty getCustomerName() {
	return customerName;
    }

    public void setCustomerName(SimpleStringProperty customerName) {
	this.customerName = customerName;
    }

    @Override
    public void onNavigate(String route) {
	this.refreshAccounts();
    }

    public void refreshAccounts() {
	try {
	    ArrayList<Account> accounts = this.main.getBanking()
		    .showAccounts(this.main.getLoggedInCustomer().getCustomerID());

	    accountsObservableList.setAll(accounts);
	    accountsT.refresh();
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
