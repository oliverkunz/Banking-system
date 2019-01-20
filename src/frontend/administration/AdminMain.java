package frontend.administration;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import backend.api.Account;
import backend.api.AccountType;
import backend.api.Administration;
import frontend.common.Customer;
import frontend.common.Pair;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminMain extends Application {
    Map<String, Pair<Scene, AdminBaseController>> scenes = new HashMap<>();

    Stage primaryStage;
    Parent root;

    Administration administration;

    AccountType accountType;

    frontend.common.Customer selectedCustomer = null;

    FXMLLoader fxmlLoader = new FXMLLoader();
    AdminController adminController = (AdminController) fxmlLoader.getController();

    static String bankName;

    public static void main(String[] args) {
	AdminMain.bankName = args[0];
	launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
	this.primaryStage = primaryStage;

	Registry registry = LocateRegistry.getRegistry("localhost", 2001);
	administration = (Administration) registry.lookup(AdminMain.bankName);

	FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
	AdminBaseController adminController = new AdminController(this);
	loader.setController(adminController);
	scenes.put("admin", Pair.of(new Scene(loader.load(), 1280, 800), adminController));

	loader = new FXMLLoader(getClass().getResource("adminAccount.fxml"));
	adminController = new AdminControllerAccount(this);
	loader.setController(adminController);
	scenes.put("account", Pair.of(new Scene(loader.load(), 900, 600), adminController));

	primaryStage.setTitle("Administration");
	primaryStage.setScene(scenes.get("admin").getFirstValue());
	primaryStage.show();

    }

    public void setScene(String name) {
	primaryStage.setScene(this.scenes.get(name).getFirstValue());
	this.scenes.get(name).getSecondValue().onNavigate(name);
    }

    public Administration getAdministration() {
	return this.administration;
    }

    public AccountType getAccountType() {
	return this.accountType;
    }

    public Customer getSelectedCustomer() {
	return selectedCustomer;
    }

    public void setSelectedCustomer(frontend.common.Customer customer) {
	this.selectedCustomer = customer;
    }

}
