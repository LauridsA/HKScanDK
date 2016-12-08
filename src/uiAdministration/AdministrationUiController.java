package uiAdministration;



import java.io.IOException;
import java.util.ArrayList;

import controller.AdministrationController;
import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
}
