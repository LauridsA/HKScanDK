package uiAdministration;



import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AdministrationController {




    @FXML
    private Button createProductuonStop;

    @FXML
    private VBox vboxList;
    
   
    public void initialize(){
    	//createDailyMessageButton.setOnAction(e -> CreateDailyMessage(e));d
    	//testLabel.setText("test123");
    	ChangeLabel();
    	//Label label = new Label("test456");
    	//vboxList.getChildren().add(label);asd
    }
    
    private void ChangeLabel() {
    	createProductuonStop.setOnAction(e->CreateDailyMessage(e));
   

	}
    
    
	private void CreateDailyMessage(ActionEvent e) {
		
	 	for (int i = 0; i < 500; i++) {
    		Label label = new Label("test" + i);
			vboxList.getChildren().add(label);
		}
	}


    
    

}
