package uiScreen;

import controller.Controller;
import dba.DBSingleConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class MainController {
	
	@FXML
	private Label dateField;
	
	@FXML
	private Label teamName;
	
	@FXML
	private ProgressBar timerBar;
	
	private Controller ctr = new Controller();

	/**
	 * @return the dateField
	 */
	public Label getDateField() {
		return this.dateField;
	}

	/**
	 * @param dateField the dateField to set
	 */
	public void setDateField(Label dateField) {
		this.dateField = dateField;
	}

	/**
	 * @return the teamName
	 */
	public Label getTeamName() {
		return this.teamName;
	}

	/**
	 * @param teamName the teamName to set
	 */
	public void setTeamName(Label teamName) {
		this.teamName = teamName;
	}
	
	
	public void initialize() {
		dateField.setText(ctr.getFormattedTime(System.currentTimeMillis(), "DD-MM-YYYY"));
	}
	
	
	
	

}
