package frontend.banking;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainView extends Application {
    MainModel mainModel;
    MainController main;
    HBox box;

    public static void main(String[] args) {
	launch(args);
    }

    @Override
    public void init() throws RemoteException, NotBoundException {
	mainModel = new MainModel(null, null);
	main = new MainController(mainModel);

	LoginModel loginModel = new LoginModel();
	LoginController loginController = new LoginController(main, loginModel);

	mainModel.currentStepProperty().addListener((observable, oldvalue, newvalue) -> {
	    this.box.getChildren().remove((Node) this.mainModel.getPane(oldvalue.intValue()));
	    try {
		this.mainModel.getCurrentPane().onNavigate();
	    } catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    this.box.getChildren().add((Node) this.mainModel.getCurrentPane());
	});

	mainModel.addPane("login", new LoginView(loginController, loginModel));
	mainModel.addPane("overview", new AccountsOverviewView(loginController, loginModel));
    }

    @Override
    public void start(Stage stage) {
	stage.setTitle("E-Banking");
	stage.setWidth(800);
	stage.setHeight(600);

	BorderPane root = new BorderPane();
	box = new HBox();
	box.setMaxWidth(600);
	box.getChildren().add((Node) mainModel.getCurrentPane());

	root.setCenter(box);
	BorderPane.setAlignment(box, Pos.CENTER);

	Scene scene = new Scene(root);
	scene.getStylesheets().add("res/style.css");
	stage.setScene(scene);
	stage.show();
    }

}
