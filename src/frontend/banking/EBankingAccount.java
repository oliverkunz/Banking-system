package frontend.banking;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class EBankingAccount extends Pane {
		
	public EBankingAccount() {
		this.setId("EBankingAccount");
				
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(250, 0, 25, 100)); //(top/right/bottom/left)
	    grid.setHgap(10);
	    grid.setVgap(10);
			
	    
	    // Kontoauszug
	    
			
	    Button withdraw = new Button("Geld überweisen");			
		TextField amount = new TextField();
		amount.setPromptText("Betrag");
		TextField iban = new TextField();
		iban.setPromptText("Empfänger IBAN");

				
		    
	    grid.add(withdraw,0,3);grid.add(amount,1,3);grid.add(iban,2,3);
				

		getChildren().addAll(grid);
	}

}
