package uiScreen;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.ProductionStop;

public class ScreenPStopCtr {
	
	@FXML
	private Label timeField;
	
	@FXML
	private Label lengthField;
	
	@FXML
	private Label descField;
	
	private DailyScreenController dailyScreenController;
	private AnchorPane productionStopPane;
	private VBox vbox;
	private ProductionStop productionStop;
	private DailyScreenController dCtr = new DailyScreenController();
	private Controller ctr = new Controller();
	
	public void setFields(ProductionStop element) {
		this.productionStop = element;
		timeField.setText(ctr.getFormattedTime(element.getStopTime(), "dd-MM-yy HH:mm"));
		lengthField.setText(((Integer)element.getStopLength()).toString());
		descField.setText(element.getStopDescription());

	}
	
	public void setParentController(DailyScreenController dailyScreenController, VBox content, AnchorPane productionStop) {
		this.dailyScreenController = dailyScreenController;
		this.productionStopPane = productionStop;
		this.vbox = content;
	}
	
	@Override
	public String toString() {
	    return "Time : " + timeField + " , Length: " + lengthField + " ,desc" + descField;
	}

	public DailyScreenController getdCtr() {
		return dCtr;
	}

	public void setdCtr(DailyScreenController dCtr) {
		this.dCtr = dCtr;
	}

}
