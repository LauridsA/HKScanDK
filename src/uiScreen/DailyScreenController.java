package uiScreen;

import java.io.IOException;

import controller.Controller;
import dba.DBSingleConnection;
import exceptions.ControllerException;
import exceptions.DbaException;
import exceptions.PassThroughException;
import exceptions.UiException;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.FieldTypes;
import model.MyTypeHolder;

/**
 * Controller for DailyMessages.fxml
 */
public class DailyScreenController {
	
	private Controller ctr = new Controller();

    @FXML
    private Label expectedFinishTimeField;

    @FXML
    private Label totalSlaugtherAmountField;

    @FXML
    private Label slaugtherAmountNightField;

    @FXML
    private Label stopNightField;

    @FXML
    private Label dayExpectedField;

    @FXML
    private Label planedSlaugtherAmountField;

    @FXML
    private Label slaugtherAmountDayField;

    @FXML
    private Label expectedPerHourField;

    @FXML
    private Label speedLabel;

    @FXML
    private Label stopDayField;

    @FXML
    private Label avgWeightField;

    @FXML
    private Label organicField;
    
    @FXML
    private ScrollPane dailyMsgScrollPane;
    
    @FXML
    private ScrollPane productionStopScrollPane;
    
	private Label dateField;
	
	private Label teamName;
	
	@FXML
	private ProgressBar timerBar;

	private DBSingleConnection dbSinCon = new DBSingleConnection();
    
	/**
     * Method for starting a worker.<br>
     * @param fieldType The {@link FieldTypes} value which should be pulled.
     */
	public void startWorker(FieldTypes fieldType) {
		Worker speedWorker = new Worker(fieldType, dbSinCon);
    	try {
			speedWorker.setPeriod(Duration.seconds(ctr.getRefreshRate(fieldType)));
		} catch (PassThroughException e) {
			e.printStackTrace();
			showError(e);
		}
    	speedWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				// Empty for now...
			}
		});
    	
    	speedWorker.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				Throwable throwable = speedWorker.getException();
				if(throwable instanceof ControllerException || throwable instanceof DbaException || throwable instanceof PassThroughException || throwable instanceof UiException){
					showError((Exception) throwable);
				}
				
			}
		});
    	
    	speedWorker.start(); 
	}
	
	private void showError(Exception e) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("FATAL FEJL");
		alert.setHeaderText(null);
		alert.setContentText(e.getMessage());
		alert.showAndWait();
		}

	/**
     * Method for starting a worker.<br>
     * This worker will update a label from the UI based on a refresh rate and fieldType.
     * @param fieldType The {@link FieldTypes} value which should be pulled
     * @param label The UI {@link Label} which should be updated.
     */
    public void startWorker(FieldTypes fieldType, Label label){
    	Worker speedWorker = new Worker(fieldType, dbSinCon);
    	try {
			speedWorker.setPeriod(Duration.seconds(ctr.getRefreshRate(fieldType)));
		} catch (PassThroughException e) {
			e.printStackTrace();
			showError(e);
		}
    	speedWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				MyTypeHolder value = (MyTypeHolder)event.getSource().getValue();
				if(label != null) {
					label.setText(value.toString());
				}
			}
		});
    	
    	speedWorker.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				Throwable throwable = speedWorker.getException();
				if(throwable instanceof ControllerException || throwable instanceof DbaException || throwable instanceof PassThroughException || throwable instanceof UiException){
					showError((Exception) throwable);
				}
				
			}
		});
    	
    	speedWorker.start(); 
    }
    
    /**
     * Method for starting a worker.<br>
     * This worker will update a scroll pane from the UI based on a refresh rate and fieldType.
     * @param fieldType The {@link FieldTypes} value which should be pulled.
     * @param scrollpane The UI {@link ScrollPane} which should be updated.
     */
    public void startWorker(FieldTypes fieldType, ScrollPane scrollPane){
    	Worker speedWorker = new Worker(fieldType, dbSinCon);
    	try {
			speedWorker.setPeriod(Duration.seconds(ctr.getRefreshRate(fieldType)));
		} catch (PassThroughException e) {
			e.printStackTrace();
			showError(e);
		}
    	speedWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				MyTypeHolder value = (MyTypeHolder)event.getSource().getValue();
				if(scrollPane != null) {
				    	VBox content = new VBox(10);
				    	for (int i = 0; i < value.getpList().size(); i++) {
				    		try {
								FXMLLoader loader = new FXMLLoader();
								loader.setLocation(DailyScreenController.class.getResource("/uiScreen/ProductionStop.fxml"));
								AnchorPane productionStop = (AnchorPane) loader.load();
								content.getChildren().add(productionStop);
								ScreenPStopCtr pCtr = ((ScreenPStopCtr) loader.getController());
								DailyScreenController dCtr = new DailyScreenController();
								pCtr.setFields(value.getpList().get(i));
								pCtr.setParentController(dCtr, content, productionStop);
							} catch (IOException e) {
								e.printStackTrace();
							}
							
						}
				    	scrollPane.setContent(content);
				    	scrollPane.setFitToHeight(true);
				    	scrollPane.setFitToWidth(true); 
				}
			}
		});
    	
    	speedWorker.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				Throwable throwable = speedWorker.getException();
				if(throwable instanceof ControllerException || throwable instanceof DbaException || throwable instanceof PassThroughException || throwable instanceof UiException){
					((Exception) throwable).printStackTrace();
					showError((Exception) throwable);
				}
				
			}
		});
    	
    	speedWorker.start(); 
    }
    
    /**
     * Start the controller
     */
    public void initialize(){
    	startWorker(FieldTypes.WORKINGTEAM);
    	startWorker(FieldTypes.SPEED, speedLabel);
    	startWorker(FieldTypes.AVGWEIGHT, avgWeightField);
    	startWorker(FieldTypes.ORGANIC, organicField);
    	startWorker(FieldTypes.SLAUGTHERAMOUNTDAY, slaugtherAmountDayField);
    	startWorker(FieldTypes.SLAUGTHERAMOUNTNIGHT, slaugtherAmountNightField);
    	startWorker(FieldTypes.EXPECTEDPERHOUR, expectedPerHourField);
    	startWorker(FieldTypes.DAYEXPECTED, dayExpectedField);
    	startWorker(FieldTypes.STOPDAY, stopDayField);
    	startWorker(FieldTypes.STOPNIGHT, stopNightField);
    	startWorker(FieldTypes.CURRENTSLAUGHTERAMOUNT, totalSlaugtherAmountField);
    	startWorker(FieldTypes.TOTALSLAUGTHERAMOUNT, planedSlaugtherAmountField);
    	startWorker(FieldTypes.EXPECTEDFINISH, expectedFinishTimeField);
    	//startWorker(FieldTypes.DAILYMESSAGES, dailyMsgScrollPane);
    	startWorker(FieldTypes.PRODUCTIONSTOPS, productionStopScrollPane);
    	//dateField.setText(ctr.getFormattedTime(System.currentTimeMillis(), "DD-MM-YYYY"));
    	
    }
    /**
     * The worker who is going to run every so often based on the time given on setup.<br>
     * <p>To specify how often the {@link ScheduledService} should run use {@link ScheduledService#setPeriod(Duration)}. <br>
     * Use {@link ScheduledService#setOnSucceeded(EventHandler)} to handle the event when the task is done.<br> 
     * Use {@link ScheduledService#start()} to start the {@link ScheduledService}.
     */
    private static class Worker extends ScheduledService<MyTypeHolder> {
    	private Controller ctr = new Controller();
    	private FieldTypes fieldType;
    	@SuppressWarnings("unused")
		private DBSingleConnection dbSinCon;

		public Worker(FieldTypes fieldType, DBSingleConnection dbSinCon) {
			this.fieldType = fieldType;
			this.dbSinCon = dbSinCon;
			this.ctr = new Controller(dbSinCon);
		}
		
		/**
		 * Task which should be executed
		 */
		@Override
		protected Task<MyTypeHolder> createTask() {
			return new Task<MyTypeHolder>()  {
				/* (non-Javadoc)
				 * @see javafx.concurrent.Task#call()
				 */
				protected MyTypeHolder call() throws PassThroughException{
					MyTypeHolder returnValue = null;
						returnValue = ctr.getValue(fieldType);
						System.out.println(fieldType + " Value: " + returnValue);
					return returnValue;
				}
			};
		}
    	
    }

	public void setDatabaseController(DBSingleConnection dbsincon) {
		this.dbSinCon = dbsincon;
	}
	
	public DBSingleConnection getDbSinCon() {
		return dbSinCon;
	}

	public void setHeaderLabels(Label teamName, Label dateField) {
		this.teamName = teamName;
		this.dateField = dateField;
		
	}
    
}