package frontend.atm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ATMScreen extends Pane {
	
	public ATMScreen() {
		this.setId("ATMScreen");
		
		GridPane grid = new GridPane();
	    grid.setAlignment(Pos.CENTER);
	    grid.setPadding(new Insets(250, 0, 25, 100)); //(top/right/bottom/left)
	    grid.setHgap(10);
	    grid.setVgap(10);
		
		
		Button showAccount = new Button("Kontoauszug");
		Button withdraw = new Button("Geld abheben");
		
		TextField amount = new TextField();
		amount.setPromptText("Betrag");

		
        grid.add(showAccount,0,2); grid.add(amount,1,2);
	    grid.add(withdraw,0,3);
		

		getChildren().addAll(grid);
	}

}
