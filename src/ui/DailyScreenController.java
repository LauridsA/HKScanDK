package ui;



import controller.Controller;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    
    
    /**
     * Method for starting a worker.<br>
     * This worker will update a label from the UI based on a refresRate and fieldType.
     * 
     * @param refreshRate How often should the {@link ScheduledService} run the {@link Task},
     * @param fieldType The {@link FieldTypes} value which should be pulled
     * @param label The ui {@link Label} which should be updated.
     */
    public void startWorker(int refreshRate, FieldTypes fieldType, Label label){
    	Worker speedWorker = new Worker(fieldType);
    	speedWorker.setPeriod(Duration.seconds(refreshRate));
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
    	startWorker(3, FieldTypes.SPEED, speedLabel);
    	startWorker(6, FieldTypes.SPEED, avgWeightField);
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
		 * Task for 
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