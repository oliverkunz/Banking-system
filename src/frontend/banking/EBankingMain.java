package frontend.banking;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import backend.api.Account;
import backend.api.Banking;
import frontend.common.Customer;
import frontend.common.Pair;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EBankingMain extends Application {
	// manage changing scenes
	Map<String, Pair<Scene, BaseController>> scenes = new HashMap<>();

	Stage primaryStage;
	Parent root;

	Banking banking;

	// used to hand over the customerID from the overview to the account statement
	Customer loggedInCustomer = null;
	Account selectedAccount = null;

	// initialize fxml
	FXMLLoader fxmlLoader = new FXMLLoader();
	EBankingController eBankingController = (EBankingController) fxmlLoader.getController();

	Registry registry;

	// evaluate bankname from property file
	static String bankName;

	public static void main(String[] args) {
		EBankingMain.bankName = args[0];
		launch(args);
	}

	// loading the registry and all used fxml screens
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

		Registry registry = LocateRegistry.getRegistry("localhost", 2001);
		banking = (Banking) registry.lookup(EBankingMain.bankName);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("ebanking.fxml"));
		BaseController controller = new EBankingController(this);
		loader.setController(controller);
		scenes.put("login", Pair.of(new Scene(loader.load(), 900, 600), controller));

		loader = new FXMLLoader(getClass().getResource("ebankingOverview.fxml"));
		controller = new EBankingControllerOverview(this);
		loader.setController(controller);
		scenes.put("overview", Pair.of(new Scene(loader.load(), 900, 600), controller));

		loader = new FXMLLoader(getClass().getResource("ebankingStatement.fxml"));
		controller = new EBankingControllerStatement(this);
		loader.setController(controller);
		scenes.put("statement", Pair.of(new Scene(loader.load(), 1280, 800), controller));

		primaryStage.setTitle("E-Banking");
		primaryStage.setScene(scenes.get("login").getFirstValue());
		primaryStage.show();
	}

	// function for changeing the scene
	public void setScene(String name) {
		primaryStage.setScene(this.scenes.get(name).getFirstValue());
		this.scenes.get(name).getSecondValue().onNavigate(name);
	}

	public Banking getBanking() {
		return this.banking;
	}

	public Customer getLoggedInCustomer() {
		return loggedInCustomer;
	}

	public void setLoggedInCustomer(Customer loggedInCustomer) {
		this.loggedInCustomer = loggedInCustomer;
	}

	public Account getSelectedAccount() {
		return selectedAccount;
	}

	public void setSelectedAccount(Account selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	public String getBankName() {
		return bankName;
	}

	// function for refreshing account data
	public void refreshData() {
		if (selectedAccount != null) {
			try {
				this.setSelectedAccount(this.banking.showAccount(this.selectedAccount.getAccountID()));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

}
