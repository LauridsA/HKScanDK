package uiAdministration;

import java.util.Optional;

import controller.AdministrationController;
import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.ProductionStop;


public class ProductionStopController {
	
	@FXML
	private Label timeField;
	
	@FXML
	private Label lengthField;
	
	@FXML
	private Label descField;
		
	private ProductionStop productionStop;
	
	Controller ctr = new Controller();
	AdministrationController aCtr = new AdministrationController();

	private AdministrationUiController administrationUiController;

	private AnchorPane productionStop3;

	private VBox vbox;	
	
	public void setFields(ProductionStop element) {
		this.productionStop = element;
		timeField.setText(ctr.getFormattedTime(element.getStopTime(), "dd-MM-yy HH:mm"));
		lengthField.setText(((Integer)element.getStopLength()).toString());
		descField.setText(element.getStopDescription());
		
		

	}
	
    @FXML
    private void removeStop(ActionEvent event) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Slet produktionsstop");
		alert.setHeaderText(null);
		alert.setContentText("Vil du slette produktionsstoppet fra " + ctr.getFormattedTime(productionStop.getStopTime(), "HH:mm dd-MM-yyyy"));
		
		ButtonType confirmButton = new ButtonType("ja", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("nej", ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(confirmButton, cancelButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == confirmButton){
			aCtr.deleteStop(productionStop.getId());
	    	vbox.getChildren().remove(productionStop3);
		} else {
		    // ... user chose CANCEL or closed the dialog    
		}
    	
    }
    

    @FXML
    private void updateStop(ActionEvent event) {
    	administrationUiController.updateProductionStop(event, productionStop);
    }
    
	
	@Override
	public String toString() {
	    return "Time : " + timeField + " , Length: " + lengthField + " ,desc" + descField;
	}

	public void setParentController(AdministrationUiController administrationUiController, VBox content, AnchorPane productionStop3) {
		this.administrationUiController = administrationUiController;
		this.productionStop3 = productionStop3;
		this.vbox = content;
}
	

}
