package uiAdministration;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.lang.model.element.Element;

import controller.AdministrationController;
import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

	private ProductionStop productionStop2;

	private AnchorPane productionStop3;	
	
	public void setFields(ProductionStop element) {
		this.productionStop = element;
		timeField.setText(ctr.getFormattedTime(element.getStopTime(), "dd-MM-yy HH:mm"));
		lengthField.setText(((Integer)element.getStopLength()).toString());
		descField.setText(element.getStopDescription());

	}
	
    @FXML
    void removeStop(ActionEvent event) {
    	aCtr.deleteStop(productionStop.getId());
    	//administrationUiController.reFreshPageContent();
    	administrationUiController.removeElement(productionStop, pro);
    }
	
	@Override
	public String toString() {
	    return "Time : " + timeField + " , Length: " + lengthField + " ,desc" + descField;
	}

	public void setParentController(AdministrationUiController administrationUiController2, VBox content, AnchorPane productionStop3) {
		this.administrationUiController = administrationUiController;
		this.productionStop3 = productionStop3;
		this.vbox 
	}
	

}
