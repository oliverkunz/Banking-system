package frontend.atm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	Stage primaryStage;
	Parent root;
	
	FXMLLoader fxmlLoader = new FXMLLoader();
	Controller controller = (Controller) fxmlLoader.getController();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("atm.fxml"));
		Parent root = loader.load();
		this.primaryStage = primaryStage;
		primaryStage.setTitle("ATM Login");
		primaryStage.setScene(new Scene(root, 800, 600));
		primaryStage.show();
		
		Controller controller = loader.getController();
		controller.setMain(this);
	}
	
	public void setScene(Scene scene) {
		primaryStage.setScene(scene);
	}
	
}
