package frontend.banking;

import backend.api.Account;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class EBankingScreen extends Pane implements ChangeListener<Account> {
	TableView<Account> table;
	
	public EBankingScreen() {
		this.setId("EBankingScreen");
			
		VBox vbox = new VBox(10.0);
		vbox.setMinHeight(200);
		vbox.setMaxHeight(350);
		vbox.setPadding(new Insets(250, 0, 25, 100));

		TableView<Account> table = new TableView<Account>();
		TableColumn<Account, String> accountCol = new TableColumn<Account, String>("Konto");
		accountCol.setCellValueFactory(new PropertyValueFactory<Account, String>("Konto"));
		accountCol.setMinWidth(350);
		TableColumn<Account, Double> amountCol = new TableColumn<Account, Double>("Betrag");
		amountCol.setCellValueFactory(new PropertyValueFactory<Account, Double>("Betrag"));
		amountCol.setMinWidth(250);
		table.getColumns().add(accountCol);
		table.getColumns().add(amountCol);

		table.getSelectionModel().selectedItemProperty().addListener(this);
		vbox.getChildren().add(table);
		vbox.setAlignment(Pos.CENTER);
		
		getChildren().addAll(vbox);
	}

	public TableView<Account> getTable() {
		return table;
	}

	@Override
	public void changed(ObservableValue<? extends Account> ov, Account oldValue, Account newValue) {
		if (table.getSelectionModel().getSelectedItem() != null) {
			
			//controller.setSelectedItem(newValue);
			//controller.nextScreen();
		}
	}

}
