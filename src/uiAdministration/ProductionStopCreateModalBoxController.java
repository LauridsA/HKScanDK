package uiAdministration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.AdministrationController;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ProductionStopCreateModalBoxController {
	@FXML
    private TextArea descBox;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonCreateStop;

    @FXML
    private DatePicker fieldStopDate;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField fieldStopTime;

    @FXML
    private TextField fieldStopLength;

	private Stage stage;
	
	private AdministrationController ctr = new AdministrationController();
	
	public void initialize() {
		//stage.setOnCloseRequest(e -> closeModalbox());
		//Stage stage = (Stage) root.getScene().getWindow();
		//System.out.println(stage.getTitle());
		/*stage.setOnCloseRequest(e -> closeModalbox());*/
		fieldStopTime.focusedProperty().addListener((arg0, oldValue, newValue) -> checkTime(arg0, oldValue, newValue));
		
		System.out.println("test");
	}
	


	@FXML
	private void closeModalbox() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Luk opret nyt stop");
		alert.setHeaderText("Alt data vil blive slettet!");
		alert.setContentText("Luk vinduet?");
		
		ButtonType confirmButton = new ButtonType("ja", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("nej", ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(confirmButton, cancelButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == confirmButton){
			Stage stage = (Stage) buttonCancel.getScene().getWindow();
			stage.close();
		} else {
		    // ... user chose CANCEL or closed the dialog    
		}
	}

	/**
	 * @param stage the stage to set
	 * @return 
	 */
	public void setStage(final Stage stage) {
		this.stage = stage;
	}
	
	private void checkTime(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
		if(!newValue){
			if(!fieldStopTime.getText().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")){
				fieldStopTime.setText(fieldStopTime.getText().replaceAll("[^0-9:]", ""));
				fieldStopTime.getStyleClass().add("failed");
				System.out.println("failed");
				
			}else{
				fieldStopTime.getStyleClass().remove("failed");
				System.out.println("correct");
			}
		}
		
		
		/*if(text.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")){
			fieldStopTime.getStyleClass().remove("failed");
			System.out.println("correct");
		}else{
			fieldStopTime.getStyleClass().add("failed");
			System.out.println("failed");
		}*/
		
		/* ^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$ */ 
	}
	
	// TODO virker men der er ingen tjek på om det er korrekte input
    @FXML
    void addNewStop(ActionEvent event) {
    	String stopString = fieldStopTime.getText() + " " + fieldStopDate.getValue();
    	DateFormat df = new SimpleDateFormat("hh:mm yyy-MM-dd");
    	long stopTime = 0;
		try {
			Date date = df.parse(stopString);
	    	stopTime = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	int stopLength = Integer.parseInt(fieldStopLength.getText());
    	String stopDescription = descBox.getText();
    	int teamTimeTableId = 9;
    	
    	
    	
    	ctr.createStop(stopTime, stopLength, stopDescription, teamTimeTableId);
    	
    	Stage stage = (Stage) buttonCreateStop.getScene().getWindow();
		stage.close();
    }
	
	
	
}
