package uiAdministration;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import controller.AdministrationController;
import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.ProductionStop;

public class AdministrationUiController {

    @FXML
    private Button createProductuonStop;

    @FXML
    private Pagination pageList;
    
    private Controller ctr = new Controller();
    private AdministrationController aCtr = new AdministrationController();
    
   
    public void initialize(){
	createProductuonStop.setOnAction(e -> createProductuonStop(e));
    	initProductionStops();
    }
    
    private void initProductionStops() {
    	ArrayList<ProductionStop> arr = aCtr.getAllStops();
    	int totalpages = arr.size()/10;
    	pageList.setPageCount(totalpages);
    	pageList.setPageFactory(new Callback<Integer, Node>() {
			
			@Override
			public Node call(Integer pages) {
					return createPage(pages, arr);
			}
		});
	}
    
    public VBox createPage(int pages, ArrayList<ProductionStop> arr){
    	VBox content = new VBox(10);
    	int startValue = pages * 10;
    	for (int i = startValue; i < startValue + 10; i++) {
    		try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(AdministrationUiController.class.getResource("/uiAdministration/ProductionStop.fxml"));
				AnchorPane productionStop = (AnchorPane) loader.load();
				content.getChildren().add(productionStop);
				ProductionStopController pctr = ((ProductionStopController) loader.getController());
				System.out.println(arr.get(i));
				pctr.setFields(arr.get(i));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
    	
    	
    	return content;
    }
    
    private Object createProductuonStop(ActionEvent e) {
	Stage dialog = new Stage();
	try {
		FXMLLoader loader = new FXMLLoader(AdministrationUiController.class.getResource("ProductionStopCreateModalbox.fxml"));
		Parent root = (Parent) loader.load();
		ProductionStopCreateModalBoxController pctr = loader.getController();
		pctr.setStage(dialog);
		dialog.setScene(new Scene(root));
		dialog.setTitle("Some title");
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(((Node)e.getSource()).getScene().getWindow());
		dialog.resizableProperty().set(false);
		dialog.showAndWait();
		
		dialog.setOnCloseRequest(e2 -> closeModalbox(e2));
		
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	return null;
    }
    
    private void closeModalbox(WindowEvent e2) {
		/*Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Luk opret nyt stop");
		alert.setHeaderText("Alt data vil blive slettet!");
		alert.setContentText("Luk vinduet?");
		
		ButtonType confirmButton = new ButtonType("ja", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("nej", ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(confirmButton, cancelButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == confirmButton){
			//(((Node)e2.getSource()).getScene().getWindow()).close
		} else {
		    // ... user chose CANCEL or closed the dialog    
		}
		*/
	}
    
    
}
