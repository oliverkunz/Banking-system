package frontend.banking;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

		private Pane[] panes;
		private int nr = 0;
		private final int numberOfScreens = 3;
		private Button button;

		public static void main(String[] args) {
			launch(args);
		}

		@Override
		public void init() {
			panes = new Pane[numberOfScreens];
			panes[0] = new EBankingLogin();
			panes[1] = new EBankingScreen();
			panes[2] = new EBankingAccount();

		}

		@Override
		public void start(Stage stage) {
			stage.setTitle("E-Banking");
			stage.setWidth(800);
			stage.setHeight(600);

			BorderPane root = new BorderPane();
			HBox box = new HBox();
			box.setMaxWidth(600);
			box.getChildren().add(panes[0]);
			button = new Button("Login");
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					showNextPane(box);
				}
			});
		
			root.setCenter(box);
			BorderPane.setAlignment(box, Pos.CENTER);
			root.setBottom(button);
			BorderPane.setAlignment(button, Pos.BASELINE_CENTER);

			Scene scene = new Scene(root);
			scene.getStylesheets().add("res/style.css");
			stage.setScene(scene);
			stage.show();
		}

		private void showNextPane(HBox box) {
			box.getChildren().remove(panes[nr]);
			nr = ++nr % numberOfScreens;
			if (nr == 0) {
				box.setMaxWidth(800);
				button.setVisible(true);
				button.setText("Login");		
			}
			if (nr == 1) {
				box.setMaxWidth(800);
				button.setVisible(true);
				button.setText("Konto auswählen");
			}
			if (nr == 2) {
				box.setMaxWidth(800);
				button.setVisible(true);
				button.setText("Zurück");		
			}
			box.getChildren().add(panes[nr]);
			
		
		}
	}

