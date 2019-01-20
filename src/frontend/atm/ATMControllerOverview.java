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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

//Frontend: atm overview
public class ATMControllerOverview extends BaseControllerATM implements Initializable {

	@FXML
	private Button withdraw;
	@FXML
	private TextField amountTF;
	@FXML
	private TableView<Account> accountsT;
	@FXML
	private TableColumn<Account, Double> colBalanceT;
	@FXML
	private TableColumn<Account, String> colAccountT;

	private final ObservableList<Account> accountsObservableList = FXCollections.observableArrayList();

	private SimpleStringProperty amount = new SimpleStringProperty("");
	private SimpleStringProperty bankName = new SimpleStringProperty("");

	// pop-up dialog fields for simple user communication (confirm actions & input
	// errors)
	Alert info = new Alert(AlertType.INFORMATION, "Transaktion ausgefhrt");
	Alert error = new Alert(AlertType.ERROR, "Fehler");
	Alert wrongInput = new Alert(AlertType.ERROR, "Bitte alle Felder korrekt ausfüllen");

	public ATMControllerOverview(ATMMain main) {
		super(main);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// binding the input-fields
		amountTF.textProperty().bindBidirectional(this.getAmount());

		// setting the values for the tableview
		accountsT.setItems(accountsObservableList);

		colBalanceT.setCellValueFactory(new PropertyValueFactory<Account, Double>("balance"));
		colAccountT.setCellValueFactory(new PropertyValueFactory<Account, String>("accountID"));
	}

	// function to withdraw money from an account
	@FXML
	public void WithdrawMoney(final ActionEvent event) throws IOException, NotBoundException {

		// checking if the input is correct
		try {
			double amountD = Double.parseDouble(amount.getValue());
			// withdraw money from logged in account
			if (this.main.getATM().withdraw(this.main.getLoggedInAccount().getAccountID(), amountD)) {
				info.showAndWait();
			} else {
				error.showAndWait();
			}
		} catch (NumberFormatException e) {
			wrongInput.showAndWait();
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

	// function to reload the tableview with account data
	@Override
	public void onNavigate(String route) {
		try {
			this.accountsObservableList
					.setAll(this.main.getATM().showAccount(this.main.getLoggedInAccount().getAccountID()));
			this.accountsT.refresh();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

}
