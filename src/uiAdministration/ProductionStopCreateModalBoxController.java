package uiAdministration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import controller.AdministrationController;
import controller.Controller;
import dba.DBSingleConnection;
import exceptions.DbaException;
import exceptions.PassThroughException;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ProductionStop;
import model.Team;

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
    
    @FXML
    private VBox teamList;
    
    @FXML
    private Label teamName;
    
    @FXML
    private Label fieldTeamStart;

	private Stage stage;
	private AdministrationController ctr = new AdministrationController();
	private Controller Cctr = new Controller(new DBSingleConnection());
	private boolean updater = false;
	private ProductionStop productionStop;
	private AdministrationUiController aUC;
	private Team selectedTeam;
	
	public void initialize() {
		fieldStopDate.setOnAction(event -> {
			dateChange(fieldStopDate.getValue());
		});
		
		descBox.setTextFormatter(new TextFormatter<String>(change ->
				change.getControlNewText().length() <= 250 ? change : null));
		fieldStopDate.setValue(LocalDate.now());
		fieldStopTime.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
		dateChange(LocalDate.now());
		
		
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
		alert.setTitle("Forkert værdi");
		alert.setHeaderText(null);
		alert.setContentText(string);

		alert.showAndWait();
	}

	/**
	 * @param stage the stage to set
	 * @param administrationUiController 
	 * @return 
	 */
	public void setStage(final Stage stage, AdministrationUiController administrationUiController) {
		this.stage = stage;
		this.aUC = administrationUiController;
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
	
   @FXML
   private void addNewStop(){
    	if(!fieldStopTime.getText().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
    		regexModalbox("Tid er forkert. Skal være f.eks. 01:00");
    		return;
    	} else if(fieldStopDate.getValue() == null ||fieldStopDate.getValue().toString().matches("^([1-9]|0[1-9]|1[0-9])-([1-9]|0[1-9]|1[1-9])-(2[0-9]{3})$")) {
	 		regexModalbox("Dato er forkert. Skal være f.eks. 01-01-2001");
    		return;
    		
    		
    	} else if(!fieldStopLength.getText().matches("^(10[0-8][0-9]|[0-9]{3}|[0-9]{2}|[1-9])$")) {
    		regexModalbox("Stoplængde er forkert. Skal være mellem 1 og 1089");
    		return;
    	} else {
    		String stopString = fieldStopTime.getText() + " " + fieldStopDate.getValue();
        	DateFormat df = new SimpleDateFormat("hh:mm yyyy-MM-dd");
        	long stopTime = 0;
    		try {
    			Date date = df.parse(stopString);
    	    	stopTime = date.getTime();
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}

        	
        	int stopLength = Integer.parseInt(fieldStopLength.getText());
        	String stopDescription = descBox.getText();
        	//Team team = null; // TODO DIS DONT WORK BOSS


        	if(updater){
        		try {
					ctr.updateStop(productionStop.getId(), stopTime, stopLength, stopDescription, selectedTeam);
				} catch (DbaException e) {
					showError(e);
				}
            	productionStop.setStopDescription(stopDescription);
        		productionStop.setStopLength(stopLength);
        		productionStop.setStopTime(stopTime);
        		productionStop.setTeam(selectedTeam);
        		//aUC.resetPage();
        		
        	}else{
        		try {
					productionStop = ctr.createStop(stopTime, stopLength, stopDescription, selectedTeam);
				} catch (PassThroughException e) {
					showError(e);
				}
        		aUC.insertNewProductionStopToArray(productionStop);
        	}
        	
        	Stage stage = (Stage) buttonCreateStop.getScene().getWindow();
    		stage.close();
    	}
    	
    }

	public void initUpdate(ProductionStop productionStop) {
		buttonCreateStop.setText("Opdater");
		fieldStopTime.setText(Cctr.getFormattedTime(productionStop.getStopTime(), "HH:mm"));
		fieldStopDate.setValue(Instant.ofEpochMilli(productionStop.getStopTime()).atZone(ZoneId.systemDefault()).toLocalDate());
		fieldStopLength.setText(Integer.toString(productionStop.getStopLength()));
		//teamName.setText(productionStop.getTeam().getTeamName()); // TODO anders wtf
		descBox.setText(productionStop.getStopDescription());
		this.productionStop = productionStop;
		updater = true;
		
	}
	
	private void showError(Exception e) {
		e.printStackTrace();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("FATAL FEJL");
		alert.setHeaderText(null);
		alert.setContentText(e.getMessage());
		alert.showAndWait();
		}
	
    void dateChange(LocalDate localDate) {
    	teamList.getChildren().clear();
    	ArrayList<Team> teamArrayList = null;
		try {
			teamArrayList = Cctr.getTeamList(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
		} catch (PassThroughException e) {
			e.printStackTrace();
		}
    	/*
    	ArrayList<Team> teamArrayList = new ArrayList<>();
    	teamArrayList.add(new Team(1, 132, 456, "nat", 52, 0));
    	teamArrayList.add(new Team(2, 789, 101112, "dag", 1337, 2));
    	*/
    	for (Team team : teamArrayList) {
    		Label l = new Label(team.getTeamName());
    		Label l2 = new Label(Cctr.getFormattedTime(team.getStartTime(),"HH:mm"));
    		BorderPane bp = new BorderPane();
    		Button btnLeft = new Button("Vælg");
        	bp.setLeft(l);
        	bp.setCenter(l2);
        	bp.setRight(btnLeft);
    		btnLeft.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					teamName.setText(team.getTeamName());
					fieldTeamStart.setText(Cctr.getFormattedTime(team.getStartTime(),"HH:mm"));
					selectedTeam = team;
					
				}
			});
    		teamList.getChildren().add(bp);
		}
    	
    	
		
    }
		
	
}
