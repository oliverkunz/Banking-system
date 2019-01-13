package frontend.banking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EBankingMain extends Application {
	Stage primaryStage;
	Parent root;
	
	FXMLLoader fxmlLoader = new FXMLLoader();
	EBankingController eBankingController = (EBankingController) fxmlLoader.getController();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ebanking.fxml"));
		
		Parent root = loader.load();
		
		this.primaryStage = primaryStage;
		primaryStage.setTitle("E-Banking");
		primaryStage.setScene(new Scene(root, 900, 600));
		primaryStage.show();
		
		EBankingController eBankingController = loader.getController();
		eBankingController.setEBankingMain(this);
	}
	
	public void setScene(Scene scene) {
		primaryStage.setScene(scene);
	}
	
}
