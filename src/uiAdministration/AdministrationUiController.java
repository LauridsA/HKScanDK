package uiAdministration;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import controller.AdministrationController;
import controller.Controller;
import exceptions.DbaException;
import exceptions.PassThroughException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.ProductionStop;

public class AdministrationUiController {

    @FXML
    private Button createProductionStop;

    @FXML
    private Pagination pageList;
    
    @FXML
    private Button refreshButton;
    
    private Controller ctr = new Controller();
    private AdministrationController aCtr = new AdministrationController();

	private int perPage = 10;
	
	private ArrayList<ProductionStop> arr;
    
   
    public void initialize(){
    	createProductionStop.setOnAction(e -> createProductionStop(e));
    	initProductionStops();
    }
    
    private void initProductionStops() {
    	try {
			arr = aCtr.getAllStops();
		} catch (PassThroughException e) {
			showError(e);
		}
    	int totalpages = (int) Math.ceil(arr.size()/10D);
    	pageList.setPageCount(totalpages);
    	pageList.setPageFactory(new Callback<Integer, Node>() {
			
			@Override
			public Node call(Integer pages) {
					return createPage(pages, arr);
			}
		});
	}
    
    public Node createPage(int pages, ArrayList<ProductionStop> arr){
    	ScrollPane sPane = new ScrollPane();
    	VBox content = new VBox(10);
    	int startValue = pages * 10;
    	int endValue = startValue + 10;
    	if(arr.size() < endValue){
    	endValue = arr.size();
    	}
    	for (int i = startValue; i < endValue; i++) {
    		try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(AdministrationUiController.class.getResource("/uiAdministration/ProductionStop.fxml"));
				AnchorPane productionStop = (AnchorPane) loader.load();
				content.getChildren().add(productionStop);
				ProductionStopController pctr = ((ProductionStopController) loader.getController());
				System.out.println(i + " " + arr.get(i));
				pctr.setFields(arr.get(i));
				pctr.setParentController(this, content, productionStop);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
    	sPane.setContent(content);
    	sPane.setFitToWidth(true);
     	return sPane;
    }
    
    
    private void createProductionStop(ActionEvent e) {
		Stage dialog = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader(AdministrationUiController.class.getResource("ProductionStopCreateModalbox.fxml"));
			Parent root = (Parent) loader.load();
			ProductionStopCreateModalBoxController pctr = loader.getController();
			pctr.setStage(dialog, this);
			dialog.setScene(new Scene(root));
			dialog.setTitle("Opret produktions stop");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node)e.getSource()).getScene().getWindow());
			dialog.resizableProperty().set(false);
			dialog.showAndWait();
			//reFreshPageContent();		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    public void updateProductionStop(ActionEvent e, ProductionStop productionStop) {
  		Stage dialog = new Stage();
  		try {
  			FXMLLoader loader = new FXMLLoader(AdministrationUiController.class.getResource("ProductionStopCreateModalbox.fxml"));
  			Parent root = (Parent) loader.load();
  			ProductionStopCreateModalBoxController pctr = loader.getController();
  			pctr.setStage(dialog, this);
  			pctr.initUpdate(productionStop);
  			dialog.setScene(new Scene(root));
  			dialog.setTitle("Updater produktions stop");
  			dialog.initModality(Modality.WINDOW_MODAL);
  			dialog.initOwner(((Node)e.getSource()).getScene().getWindow());
  			dialog.resizableProperty().set(false);
  			dialog.showAndWait();
  			//reFreshPageContent();		
  		} catch (IOException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
      }
    
    @FXML
    private void reDrawPages(ActionEvent event) {
    	reFreshPageContent();
    }
    
    
    public void reFreshPageContent (){
    	try {
			arr = aCtr.getAllStops();
		} catch (PassThroughException e) {
			showError(e);
		}
    	resetPage();
    }
  
    public void resetPage() {
    	int totalpages = (int) Math.ceil(arr.size()/10D);
    	pageList.setPageCount(totalpages);
    	pageList.setCurrentPageIndex(0);
	}

	public void removeElement(ProductionStop productionStop) {
		arr.remove(productionStop);
		resetPage();

	}
	
	public void insertNewProductionStopToArray(ProductionStop productionStop) {
		arr.add(productionStop);
		Collections.sort(arr, new Comparator<ProductionStop>() {

			@Override
			public int compare(ProductionStop o1, ProductionStop o2) {
				return o2.getStopTime().compareTo(o1.getStopTime());
			}
		});
		
		
		resetPage();
	}
	private void showError(Exception e) {
		e.printStackTrace();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("FATAL FEJL");
		alert.setHeaderText(null);
		alert.setContentText(e.getMessage());
		alert.showAndWait();
		}
    
    
}
