package frontend.banking;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
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

// Frontend: ebanking overview
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

	// pop-up dialog fields for simple user communication (confirm actions & input
	// errors)
	Alert alert = new Alert(AlertType.CONFIRMATION, "Bitte Transaktion bestätigen");
	Alert info = new Alert(AlertType.INFORMATION, "Transaktion ausgeführt");
	Alert errorAborted = new Alert(AlertType.ERROR, "Transaktion abgebrochen");
	Alert errorNegativeAmount = new Alert(AlertType.ERROR, "Negativen Betrag angegeben. Betrag muss positiv sein.");
	Alert errorNotAllowed = new Alert(AlertType.ERROR, "Falscher Empfänger oder Limite erreicht");
	Alert errorNotEmpty = new Alert(AlertType.ERROR, "Empfänger darf nicht leer sein.");
	Alert errorNoAccount = new Alert(AlertType.ERROR, "Bitte wählen Sie ein Konto aus.");

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
		// binding the input-fields
		amountTF.textProperty().bindBidirectional(this.getAmount(), new NumberStringConverter());

		receiverTF.textProperty().bindBidirectional(this.getReceiver());
		bankNameL.textProperty().bindBidirectional(this.getBankName());
		customerNameL.textProperty().bindBidirectional(this.getCustomerName());

		// setting the values for the tableview
		accountsT.setItems(accountsObservableList);

		// implementing the listener for the tableview, so the user select accounts
		accountsT.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				this.main.setSelectedAccount(newSelection);
			}
		});

		colBalanceT.setCellValueFactory(new PropertyValueFactory<Account, Double>("balance"));
		colAccountT.setCellValueFactory(new PropertyValueFactory<Account, String>("accountID"));
	}

	// function to transfer money between accounts
	@FXML
	public void TransferMoney(final ActionEvent event) throws IOException {
		// checking if input is correct
		if (this.getAmount().getValue() <= 0) {
			this.errorNegativeAmount.showAndWait();
			return;
		} else if (this.getReceiver().getValue().length() <= 0) {
			this.errorNotEmpty.showAndWait();
			return;
		} else if (this.main.getSelectedAccount() == null) {
			this.errorNoAccount.showAndWait();
			return;
		}

		// confirmation the action
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			// transfer the money to specified account of own or other bank
			try {
				boolean rmiResult = this.main.getBanking().transfer(this.main.getSelectedAccount().getAccountID(),
						this.getReceiver().getValue(), this.getAmount().getValue(), LocalDate.now());

				if (rmiResult) {
					info.showAndWait();
				} else {
					errorNotAllowed.showAndWait();
				}

				// refresh the tableview
				this.refreshAccounts();
				this.main.setSelectedAccount(null);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		} else {
			errorAborted.showAndWait();
		}
	}

	// function for switching to the account statement
	@FXML
	public void GetAccountStatement(final ActionEvent event) throws IOException {
		if (this.main.getSelectedAccount() == null) {
			errorNoAccount.showAndWait();
			return;
		}

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

	// function used to refresh accounts
	@Override
	public void onNavigate(String route) {
		this.main.setSelectedAccount(null);
		this.refreshAccounts();
	}

	public void refreshAccounts() {
		try {
			List<Account> accounts = this.main.getBanking()
					.showAccounts(this.main.getLoggedInCustomer().getCustomerID());

			accountsObservableList.setAll(accounts);
			accountsT.refresh();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
