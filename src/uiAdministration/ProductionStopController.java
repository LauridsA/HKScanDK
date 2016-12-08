package uiAdministration;

import java.text.SimpleDateFormat;
import java.util.Date;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.ProductionStop;


public class ProductionStopController {
	
	@FXML
	private Label timeField;
	
	@FXML
	private Label lengthField;
	
	@FXML
	private Label descField;
	
	Controller ctr = new Controller();
	
	
	public void setFields(ProductionStop element) {
	   timeField.setText(ctr.getFormattedTime(element.getStopTime(), "HH:mm"));
	   lengthField.setText(((Integer)element.getStopLength()).toString());
	   descField.setText(element.getStopDescription());

	}
	
	@Override
	public String toString() {
	    return "Time : " + timeField + " , Length: " + lengthField + " ,desc" + descField;
	}
	

}
