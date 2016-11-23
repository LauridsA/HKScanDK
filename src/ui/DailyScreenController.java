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
    
    public void setSpeedLabelText(Integer text) {
		this.speedLabel.setText(text.toString());
	}
    
    public void startSpeedLabelWorker(int refreshRate){
    	intWorker speedWorker = new intWorker(FieldTypes.SPEED);
    	speedWorker.setPeriod(Duration.seconds(refreshRate));
    	speedWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("test");
				setSpeedLabelText((Integer) event.getSource().getValue());
				
			}
		});
    	speedWorker.start();
    	
    	setSpeedLabelText(1);
    }
    
    public void initialize(){
    	startSpeedLabelWorker(3);
    }
    
    private static class intWorker extends ScheduledService<Integer> {
    	private Controller ctr = new Controller();
    	private FieldTypes fieldType;
    	private IntegerProperty integer2 = new SimpleIntegerProperty();
    	
    	public final void setInteger(Integer value) {
			integer2.set(value);
		}
    	
    	public final Integer getInteger() {
    		return integer2.get();
    	}

		public intWorker(FieldTypes fieldType) {
			this.fieldType = fieldType;
		}
		
		public final IntegerProperty integerProperty() {
			return integer2;
		}
    	
		@Override
		protected Task<Integer> createTask() {
			return new Task<Integer>() {
				protected Integer call(){
					integer2.set(ctr.getValue(fieldType).getInteger());
					return ctr.getValue(fieldType).getInteger();
				}
			};
		}
    	
    }
    

}