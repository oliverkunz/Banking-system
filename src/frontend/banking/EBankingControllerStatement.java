package frontend.banking;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import backend.api.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

//Frontend: ebanking accoutn statement
public class EBankingControllerStatement extends BaseController implements Initializable {

	// TAbleview
	@FXML
	private Button statementB;
	@FXML
	private Label accountL;
	@FXML
	private TableColumn<Transaction, String> colReceiverT;
	@FXML
	private TableColumn<Transaction, String> colSenderT;
	@FXML
	private TableColumn<Transaction, Double> colAmountT;
	@FXML
	private TableView<Transaction> transactionsT;

	private final ObservableList<Transaction> transactionsObservableList = FXCollections.observableArrayList();

	private SimpleStringProperty account = new SimpleStringProperty("");

	public EBankingControllerStatement(EBankingMain main) {
		super(main);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// binding the input-fields
		accountL.textProperty().bindBidirectional(this.getAccount());

		// setting the values for the tableview
		transactionsT.setItems(transactionsObservableList);

		colAmountT.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));
		colReceiverT.setCellValueFactory(new PropertyValueFactory<Transaction, String>("receiverID"));
		colSenderT.setCellValueFactory(new PropertyValueFactory<Transaction, String>("senderID"));
	}

	// function brings the user back to the overview
	@FXML
	public void BackToOverview(final ActionEvent event) throws IOException {
		this.main.setScene("overview");
	}

	public SimpleStringProperty getAccount() {
		return account;
	}

	public void setAmount(SimpleStringProperty account) {
		this.account = account;
	}

	// refresh values in the tableview
	@Override
	public void onNavigate(String route) {
		this.getMain().refreshData();

		this.transactionsObservableList.setAll(this.main.getSelectedAccount().getTransactions());
		this.transactionsT.refresh();
	}

}
