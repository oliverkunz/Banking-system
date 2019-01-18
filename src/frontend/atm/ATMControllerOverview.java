package frontend.atm;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import backend.api.Account;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ATMControllerOverview extends BaseControllerATM implements Initializable {

    @FXML
    private Button withdraw;
    @FXML
    private TextField amountTF;
    @FXML
    private Label bankNameL;
    @FXML
    private TableView<Account> accountsT;
    @FXML
    private TableColumn<Account, Double> colBalanceT;
    @FXML
    private TableColumn<Account, String> colAccountT;

    private final ObservableList<Account> accountsObservableList = FXCollections.observableArrayList();

    private SimpleStringProperty amount = new SimpleStringProperty("");
    private SimpleStringProperty bankName = new SimpleStringProperty("");

    Alert info = new Alert(AlertType.INFORMATION, "Transaktion ausgeführt");
    Alert error = new Alert(AlertType.ERROR, "Fehler");

    public ATMControllerOverview(Main main) {
	super(main);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	amountTF.textProperty().bindBidirectional(this.getAmount());
	bankNameL.textProperty().bindBidirectional(this.getBankName());

	accountsT.setItems(accountsObservableList);

	colBalanceT.setCellValueFactory(new PropertyValueFactory<Account, Double>("balance"));
	colAccountT.setCellValueFactory(new PropertyValueFactory<Account, String>("accountID"));
    }

    @FXML
    public void WithdrawMoney(final ActionEvent event) throws IOException, NotBoundException {
	double amountD = Double.parseDouble(amount.getValue());
	if (this.main.getATM().withdraw(this.main.getLoggedInAccount().getAccountID(), amountD)) {
	    info.showAndWait();
	} else {
	    error.showAndWait();
	}

	// little hack to reload the account, too lazy to execute the same again
	this.onNavigate("dummy");
    }

    public SimpleStringProperty getAmount() {
	return amount;
    }

    public void setAmount(SimpleStringProperty amount) {
	this.amount = amount;
    }

    public SimpleStringProperty getBankName() {
	return bankName;
    }

    public void setBankName(SimpleStringProperty bankName) {
	this.bankName = bankName;
    }

    @Override
    public void onNavigate(String route) {
	try {
	    this.accountsObservableList
		    .setAll(this.main.getATM().showAccount(this.main.getLoggedInAccount().getAccountID()));
	    this.accountsT.refresh();
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}
