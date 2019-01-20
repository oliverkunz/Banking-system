package frontend.atm;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import backend.api.ATM;
import backend.api.Account;
import frontend.common.Pair;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ATMMain extends Application {
	// manage chaning scenes
	Map<String, Pair<Scene, BaseControllerATM>> scenes = new HashMap<>();

	Stage primaryStage;
	Parent root;

	// used to hand over the accountID from the login to the account overview
	Account loggedInAccount = null;

	ATM atm;

	// initialize fxml
	FXMLLoader fxmlLoader = new FXMLLoader();
	ATMController controller = (ATMController) fxmlLoader.getController();

	Registry registry;

	public static void main(String[] args) {
		launch(args);
	}

	// loading the registry and all used fxml screens
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

		registry = LocateRegistry.getRegistry("localhost", 2001);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("atm.fxml"));
		BaseControllerATM controller = new ATMController(this);
		loader.setController(controller);
		scenes.put("atmLogin", Pair.of(new Scene(loader.load(), 900, 600), controller));

		loader = new FXMLLoader(getClass().getResource("atmOverview.fxml"));
		controller = new ATMControllerOverview(this);
		loader.setController(controller);
		scenes.put("atmOverview", Pair.of(new Scene(loader.load(), 900, 600), controller));

		primaryStage.setTitle("ATM");
		primaryStage.setScene(scenes.get("atmLogin").getFirstValue());
		primaryStage.show();

	}

	// function for changeing the scene
	public void setScene(String name) {
		primaryStage.setScene(this.scenes.get(name).getFirstValue());
		this.scenes.get(name).getSecondValue().onNavigate(name);
	}

	// getters and setters to hand over important information between the scenes
	public ATM getATM() {
		return this.atm;
	}

	public Account getLoggedInAccount() {
		return loggedInAccount;
	}

	public void setLoggedInAccount(Account loggedInAccount) {
		this.loggedInAccount = loggedInAccount;
	}

	// function to evaluate which bank should be loaded according to provided
	// accountID
	public void initializeATMForBank(String bankId) {
		try {
			atm = (ATM) this.registry.lookup(bankId);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}

}
