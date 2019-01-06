package frontend.banking;

import backend.api.Account;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
		vbox.setMinHeight(100);
		vbox.setMaxHeight(250);

		table = new TableView<Account>();
		TableColumn<Account, Long> idCol = new TableColumn<Account, Long>("Konto");
		idCol.setCellValueFactory(new PropertyValueFactory<Account, Long>("Konto"));
		idCol.setMinWidth(180);
		TableColumn<Account, String> titleCol = new TableColumn<Account, String>("Betrag");
		titleCol.setCellValueFactory(new PropertyValueFactory<Account, String>("Betrag"));
		titleCol.setMinWidth(250);
		table.getColumns().add(idCol);
		table.getColumns().add(titleCol);

		table.getSelectionModel().selectedItemProperty().addListener(this);
		vbox.getChildren().add(table);
		vbox.setAlignment(Pos.CENTER);
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
