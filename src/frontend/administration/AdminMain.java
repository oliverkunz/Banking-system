package frontend.administration;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

import backend.api.Account;
import backend.api.AccountType;
import backend.api.Administration;
import backend.api.Banking;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminMain extends Application {
	HashMap<String, AdminPair<Scene, AdminBaseController>> scenes = new HashMap<>();
	
	Stage primaryStage;
	Parent root;
	
	Banking banking;
	
	Administration administration;
	
	AccountType accountType;
	
	Account selectedAccount = null;
	
	FXMLLoader fxmlLoader = new FXMLLoader();
	AdminController adminController = (AdminController) fxmlLoader.getController();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		Registry registry = LocateRegistry.getRegistry("localhost", 2001);
		banking = (Banking) registry.lookup("ubs");
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
		AdminBaseController adminController = new AdminController(this);
		loader.setController(adminController);
		scenes.put("admin", AdminPair.of(new Scene(loader.load(), 1280, 800), adminController));
		
		loader = new FXMLLoader(getClass().getResource("adminAccount.fxml"));
		adminController = new AdminControllerAccount(this);
		loader.setController(adminController);
		scenes.put("account", AdminPair.of(new Scene(loader.load(), 900, 600), adminController));

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
    
    public Account getSelectedAccount() {
    	return selectedAccount;
    }

    public void setSelectedAccount(Account selectedAccount) {
    	this.selectedAccount = selectedAccount;
    }

}
