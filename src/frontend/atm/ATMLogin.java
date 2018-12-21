package frontend.atm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


public class ATMLogin extends Pane{
	
	public ATMLogin() {
		this.setId("ATMLogin");
		
	    GridPane grid = new GridPane();
	    grid.setAlignment(Pos.CENTER);
	    grid.setPadding(new Insets(250, 25, 25, 150)); //(top/right/bottom/left)

	    Label l1 = new Label("Kontonummer");
	    Label l2 = new Label("PIN");
	    Label l3 = new Label("");
	    
	    TextField kontonummer = new TextField();
	    PasswordField pin = new PasswordField();
	    
	    grid.add(l1,0,1); grid.add(kontonummer,1,1);
	    grid.add(l2,0,2); grid.add(pin,1,2);
	    grid.add(l3,0,3);
	    
	    getChildren().addAll(grid);
	}
}
