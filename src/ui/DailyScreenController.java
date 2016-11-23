package ui;



import java.util.Date;

import org.omg.CORBA.INITIALIZE;

import controller.Controller;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import model.FieldTypes;
import model.MyTypeHolder;

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
    
    public void initialize(){
    	startWorker(3, FieldTypes.SPEED, speedLabel);
    	startWorker(6, FieldTypes.SPEED, avgWeightField);
    }
    
    private static class Worker extends ScheduledService<MyTypeHolder> {
    	private Controller ctr = new Controller();
    	private FieldTypes fieldType;

		public Worker(FieldTypes fieldType) {
			this.fieldType = fieldType;
		}

		@Override
		protected Task<MyTypeHolder> createTask() {
			return new Task<MyTypeHolder>() {
				protected MyTypeHolder call(){
					return ctr.getValue(fieldType);
				}
			};
		}
    	
    }
    

}