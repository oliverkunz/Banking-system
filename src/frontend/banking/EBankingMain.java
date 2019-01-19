package frontend.banking;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

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
    HashMap<String, Pair<Scene, BaseController>> scenes = new HashMap<>();

    Stage primaryStage;
    Parent root;

    Banking banking;

    Customer loggedInCustomer = null;
    Account selectedAccount = null;

    FXMLLoader fxmlLoader = new FXMLLoader();
    EBankingController eBankingController = (EBankingController) fxmlLoader.getController();

    Registry registry;

    static String bankName;

    public static void main(String[] args) {
	EBankingMain.bankName = args[0];
	launch(args);
    }

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
	scenes.put("statement", Pair.of(new Scene(loader.load(), 900, 600), controller));

	primaryStage.setTitle("E-Banking");
	primaryStage.setScene(scenes.get("login").getFirstValue());
	primaryStage.show();
    }

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

    public void refreshData() {
	if (selectedAccount != null) {
	    try {
		this.setSelectedAccount(this.banking.showAccount(this.selectedAccount.getAccountID()));
	    } catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}
