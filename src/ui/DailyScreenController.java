package ui;



import controller.Controller;
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
    
    
    /**
     * Method for starting a worker.<br>
     * This worker will update a label from the UI based on a refresRate and fieldType.
     * 
     * @param fieldType The {@link FieldTypes} value which should be pulled
     * @param label The ui {@link Label} which should be updated.
     */
    public void startWorker(FieldTypes fieldType, Label label){
    	Worker speedWorker = new Worker(fieldType);
    	speedWorker.setPeriod(Duration.seconds(ctr.getRefreshRate(fieldType)));
    	speedWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				MyTypeHolder value = (MyTypeHolder)event.getSource().getValue();
				label.setText(value.toString());				
			}
		});
    	speedWorker.start(); 
    }
    
    /**
     * Start the controller
     */
    public void initialize(){
    	startWorker(FieldTypes.SPEED, speedLabel);
    	startWorker(FieldTypes.AVGWEIGHT, avgWeightField);
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

		public Worker(FieldTypes fieldType) {
			this.fieldType = fieldType;
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
					return ctr.getValue(fieldType);
				}
			};
		}
    	
    }
    

}