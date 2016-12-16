package uiAdministration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import org.omg.CORBA.INITIALIZE;

import controller.AdministrationController;
import controller.Controller;
import dba.DBSingleConnection;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.ProductionStop;

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
	
	private Controller Cctr = new Controller(new DBSingleConnection());
	
	private boolean updater = false;

	private ProductionStop productionStop;
	
	public void initialize() {
		//stage.setOnCloseRequest(e -> closeModalbox());
		//Stage stage = (Stage) root.getScene().getWindow();
		//System.out.println(stage.getTitle());
		/*stage.setOnCloseRequest(e -> closeModalbox());*/
		//fieldStopTime.focusedProperty().addListener((arg0, oldValue, newValue) -> checkTime(arg0, oldValue, newValue));
		//fieldStopDate.focusedProperty().addListener((arg0, oldValue, newValue) -> checkDate(arg0, oldValue, newValue));
		descBox.setTextFormatter(new TextFormatter<String>(change ->
				change.getControlNewText().length() <= 250 ? change : null));
		fieldStopDate.setValue(LocalDate.now());
		fieldStopTime.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
	}

	@FXML
	private void closeModalbox() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Luk opret nyt stop");
		alert.setHeaderText(null);
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
	
	private void regexModalbox(String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Forkert v�rdi");
		alert.setHeaderText(null);
		alert.setContentText(string);

		alert.showAndWait();
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
				//fieldStopTime.setText(fieldStopTime.getText().replaceAll("[^0-9:A-Za-Z;-,]", ""));
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
	
	private void checkDate(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
		if(!newValue){
			if(!fieldStopTime.getText().matches("^([1-9]|0[1-9]|1[0-9])-([1-9]|0[1-9]|1[1-9])-(2[0-9]{3})$")){
				//fieldStopTime.setText(fieldStopTime.getText().replaceAll("[^0-9:A-Za-Z;-,]", ""));
				fieldStopTime.getStyleClass().add("failed");
				System.out.println("failed");
				
			}else{
				fieldStopTime.getStyleClass().remove("failed");
				System.out.println("correct");
			}
		}
	}
	
	// TODO virker men der er ingen tjek p� om det er korrekte input
	@FXML
   private void addNewStop(){
    	if(!fieldStopTime.getText().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
    		regexModalbox("Tid er forkert. Skal v�re eks.: 01:00");
    		return;
    	} else if(fieldStopDate.getValue() == null ||fieldStopDate.getValue().toString().matches("^([1-9]|0[1-9]|1[0-9])-([1-9]|0[1-9]|1[1-9])-(2[0-9]{3})$")) {
	 		regexModalbox("Dato er forkert. Skal v�re eks.: 01-01-2001");
    		return;
    		
    		
    	} else if(!fieldStopLength.getText().matches("^(10[0-8][0-9]|[0-9]{3}|[0-9]{2}|[0-9])$")) {
    		//throw new Exception("Stopl�ngde er forkert. Skal v�re eks.: 10 og max 1080");
    		regexModalbox("Stopl�ngde er forkert. Skal v�re eks.: 10 og max 1080");
    		return;
    	} else {
    		String stopString = fieldStopTime.getText() + " " + fieldStopDate.getValue();
        	DateFormat df = new SimpleDateFormat("hh:mm yyyy-MM-dd");
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
        	
        	
        	if(updater){
        		ctr.updateStop(productionStop.getId(), stopTime, stopLength, stopDescription, teamTimeTableId);
        	}else{
        		ctr.createStop(stopTime, stopLength, stopDescription, teamTimeTableId);
        	}
        	
        	Stage stage = (Stage) buttonCreateStop.getScene().getWindow();
    		stage.close();
    	}
    	
    }



	public void initUpdate(ProductionStop productionStop) {
		buttonCreateStop.setText("Updater");
		fieldStopTime.setText(Cctr.getFormattedTime(productionStop.getStopTime(), "HH:mm"));
		fieldStopDate.setValue(Instant.ofEpochMilli(productionStop.getStopTime()).atZone(ZoneId.systemDefault()).toLocalDate());
		fieldStopLength.setText(Integer.toString(productionStop.getStopLength()));
		descBox.setText(productionStop.getStopDescription());
		this.productionStop = productionStop;
		updater = true;
		
	}
	
	
	
}
