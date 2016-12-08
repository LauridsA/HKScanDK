package uiAdministration;



import java.io.IOException;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class AdministrationController {

    @FXML
    private Button createProductuonStop;

    @FXML
    private Pagination pageList;
    
    private String[] mylist = new String[500];
    private Controller ctr = new Controller();
    
   
    public void initialize(){
    	//createDailyMessageButton.setOnAction(e -> CreateDailyMessage(e));d
    	//testLabel.setText("test123");
    	
    	//Label label = new Label("test456");
    	//vboxList.getChildren().add(label);ads
    	
    	
		
    	
    	initProductionStops();
    	
    }
    
    private void initProductionStops() {
    	pageList.setPageCount(ctr.getTotalStops()/10);
    	
    	pageList.setPageFactory(new Callback<Integer, Node>() {
			
			@Override
			public Node call(Integer pages) {
					return createPage(pages);
			}
		});
    	
    	/*
    	createProductuonStop.setOnAction(e->CreateDailyMessage(e));
   		*/

	}
    
    public VBox createPage(int pages){
    	VBox content = new VBox(10);
    	for (int i = 0; i < 5; i++) {
    		try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(AdministrationController.class.getResource("/uiAdministration/ProductionStop.fxml"));
				AnchorPane productionStop = (AnchorPane) loader.load();
				content.getChildren().add(productionStop);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
    	
    	
    	return content;
    }
    
    /*
	private void CreateDailyMessage(ActionEvent e) {
	
		
		
	 	for (int i = 0; i < 10; i++) {
	 		
	 		
	 		try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(AdministrationController.class.getResource("/uiAdministration/ProductionStop.fxml"));
				AnchorPane productionStop = (AnchorPane) loader.load();
				Label label = new Label("test" + i);
				vboxList.getChildren().add(productionStop);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
		}
	}*/


    
    

}
