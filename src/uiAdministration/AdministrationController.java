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
import javafx.util.Duration;

public class AdministrationController {

    @FXML
    private Button createDailyMessageButton;

    @FXML
    private ScrollPane dailyMessagePane;

    @FXML
    private Label testLabel;
    
   
    public void initialize(){
    	//createDailyMessageButton.setOnAction(e -> CreateDailyMessage(e));
    	//testLabel.setText("test123");
    	ChangeLabel();
    }
    
    private void ChangeLabel() {
    	createDailyMessageButton.setOnAction(e->CreateDailyMessage(e));

	}
    
    
	private void CreateDailyMessage(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}


    
    

}
