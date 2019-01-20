package frontend.administration;

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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminControllerAccount extends AdminBaseController implements Initializable {

	@FXML
	private Button implementButton;
	@FXML
	private Button backButton;

	@FXML
	private RadioButton depositRButton;
	@FXML
	private RadioButton withdrawRButton;

	@FXML
	private TextField amountTF;

	@FXML
	private TableColumn<Account, String> colAccountT;

	@FXML
	private TableColumn<Account, Double> colBalanceT;

	@FXML
	private TableView<Account> accountsT;

	private final ObservableList<Account> accountsObservableList = FXCollections.observableArrayList();

	Alert info = new Alert(AlertType.INFORMATION, "Es kann nur eine Aktion gewählt werden");
	Alert transaction = new Alert(AlertType.INFORMATION, "Transaktion durchgeführt");
	Alert wrongInput = new Alert(AlertType.ERROR, "Bitte alle Felder korrekt ausfüllen");
	Alert errorInvalid = new Alert(AlertType.ERROR, "Kundennummer ungltig.");
	Alert errorNotSelected = new Alert(AlertType.ERROR, "Bitte whlen Sie ein Konto aus.");

	private SimpleStringProperty amount = new SimpleStringProperty("");

	Account selectedAccount = null;

	public AdminControllerAccount(AdminMain adminMain) {
		super(adminMain);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		amountTF.textProperty().bindBidirectional(this.getAmount());

		accountsT.setItems(accountsObservableList);

		accountsT.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				this.selectedAccount = newSelection;
			}
		});

		colBalanceT.setCellValueFactory(new PropertyValueFactory<Account, Double>("balance"));
		colAccountT.setCellValueFactory(new PropertyValueFactory<Account, String>("accountID"));
	}

	@FXML
	public void TransferMoney(final ActionEvent event) throws IOException, NotBoundException {
		if (this.selectedAccount == null) {
			errorNotSelected.showAndWait();
			return;
		}

		if (depositRButton.isSelected() && withdrawRButton.isSelected()) {
			info.showAndWait();
		} else {

			try {
				if (depositRButton.isSelected()) {
					double amountD = Double.parseDouble(amount.getValue());
					this.adminMain.getAdministration().deposit(this.selectedAccount.getAccountID(), amountD);
				}
				if (withdrawRButton.isSelected()) {
					double amountD = Double.parseDouble(amount.getValue());
					this.adminMain.getAdministration().withdraw(this.selectedAccount.getAccountID(), amountD);
				}
				transaction.showAndWait();
			} catch (NumberFormatException | NullPointerException e) {
				wrongInput.showAndWait();
			}
			this.onNavigate("");
			this.selectedAccount = null;
		}
	}

	@FXML
	public void ShowOverview(final ActionEvent event) throws IOException {
		this.adminMain.setScene("admin");
	}

	public SimpleStringProperty getAmount() {
		return amount;
	}

	public void setAmount(SimpleStringProperty amount) {
		this.amount = amount;
	}

	@Override
	public void onNavigate(String route) {
		try {
			accountsObservableList.setAll(this.adminMain.getAdministration()
					.showAccounts(this.adminMain.getSelectedCustomer().getCustomerID()));
		} catch (RemoteException e) {
			this.errorInvalid.showAndWait();
		}
	}

}
