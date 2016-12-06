package ui;



import controller.Controller;
import dba.DBSingleConnection;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;
import model.FieldTypes;
import model.MyTypeHolder;

/**
 * Controller for DailyMessages.fxml
 *
 */
public class DailyScreenController {
	
	Controller ctr = new Controller();

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
    private ScrollPane productionStopPane;

	private DBSingleConnection dbSinCon = new DBSingleConnection();
    
    
    /**
     * Method for starting a worker.<br>
     * This worker will update a label from the UI based on a refresRate and fieldType.
     * 
     * @param fieldType The {@link FieldTypes} value which should be pulled
     * @param label The ui {@link Label} which should be updated.
     */
    public void startWorker(FieldTypes fieldType, Label label){
    	Worker speedWorker = new Worker(fieldType, dbSinCon);
    	speedWorker.setPeriod(Duration.seconds(ctr.getRefreshRate(fieldType)));
    	speedWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				MyTypeHolder value = (MyTypeHolder)event.getSource().getValue();
				if(label != null) {
					label.setText(value.toString());
				}
			}
		});
    	speedWorker.start(); 
    }
    
    /**
     * Start the controller
     */
    public void initialize(){
    	startWorker(FieldTypes.WORKINGTEAM, null);
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
    	//startWorker(FieldTypes.PRODUCTIONSTOPS, productionStopsPane);
    }
    /**
     * The worker who is going to run every so often based on the time given on setup.<br>
     * <p>To specify how often the {@link ScheduledService} should run use {@link ScheduledService#setPeriod(Duration)}. <br>
     * Use {@link ScheduledService#setOnSucceeded(EventHandler)} to handle the event when the task is done.<br> 
     * Use {@link ScheduledService#start()} to start the {@link ScheduledService}.
     * 
     *
     */
    private static class Worker extends ScheduledService<MyTypeHolder> {
    	private Controller ctr = new Controller();
    	private FieldTypes fieldType;
    	private DBSingleConnection dbSinCon;

		public Worker(FieldTypes fieldType, DBSingleConnection dbSinCon) {
			this.fieldType = fieldType;
			this.dbSinCon = dbSinCon;
			this.ctr = new Controller(dbSinCon);
		}
		
		/* (non-Javadoc)
		 * @see javafx.concurrent.Service#createTask()
		 * 
		 */
		
		/**
		 * Task which should be executed
		 */
		@Override
		protected Task<MyTypeHolder> createTask() {
			return new Task<MyTypeHolder>() {
				/* (non-Javadoc)
				 * @see javafx.concurrent.Task#call()
				 */
				protected MyTypeHolder call(){
					MyTypeHolder returnValue = ctr.getValue(fieldType);
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
    

}