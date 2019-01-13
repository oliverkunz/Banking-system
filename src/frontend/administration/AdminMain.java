package frontend.administration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminMain extends Application {
	Stage primaryStage;
	Parent root;
	
	FXMLLoader fxmlLoader = new FXMLLoader();
	AdminController adminController = (AdminController) fxmlLoader.getController();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
		
		Parent root = loader.load();
		
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Administration");
		primaryStage.setScene(new Scene(root, 1280, 800));
		primaryStage.show();
		
		AdminController adminController = loader.getController();
		adminController.setAdminMain(this);
	}
	
	public void setScene(Scene scene) {
		primaryStage.setScene(scene);
	}

}
