package frontend.atm;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import backend.api.ATM;
import backend.api.Account;
import frontend.common.Pair;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    HashMap<String, Pair<Scene, Controller>> scenes = new HashMap<>();

    Stage primaryStage;
    Parent root;

    Account loggedInAccount = null;

    ATM atm;

    FXMLLoader fxmlLoader = new FXMLLoader();
    Controller controller = (Controller) fxmlLoader.getController();

    public static void main(String[] args) {
	launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
	this.primaryStage = primaryStage;

	Registry registry = LocateRegistry.getRegistry("localhost", 2001);
	atm = (ATM) registry.lookup("ubs");

	FXMLLoader loader = new FXMLLoader(getClass().getResource("atm.fxml"));
	Controller controller = new Controller(this);
	loader.setController(controller);
	scenes.put("atmLogin", Pair.of(new Scene(loader.load(), 900, 600), controller));

	loader = new FXMLLoader(getClass().getResource("atmOverview.fxml"));
	controller = new Controller(this);
	loader.setController(controller);
	scenes.put("atmOverview", Pair.of(new Scene(loader.load(), 900, 600), controller));

	primaryStage.setTitle("ATM");
	primaryStage.setScene(scenes.get("atmLogin").getFirstValue());
	primaryStage.show();

    }

    public void setScene(String name) {
	primaryStage.setScene(this.scenes.get(name).getFirstValue());
	this.scenes.get(name).getSecondValue().onNavigate(name);
    }

    public ATM getATM() {
	return this.atm;
    }

    public Account getLoggedInAccount() {
	return loggedInAccount;
    }

    public void setLoggedInAccount(Account loggedInAccount) {
	this.loggedInAccount = loggedInAccount;
    }

}
