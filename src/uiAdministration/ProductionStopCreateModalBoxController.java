package uiAdministration;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ProductionStopCreateModalBoxController {
	@FXML
    private TextArea descBox;

	@FXML
	private Button buttonCancel;
	
	@FXML
	private Button buttonCreateStop;
	
	@FXML
	private DatePicker fieldStopDate;
	
	@FXML
	private TextField fieldStopTIme;
	
	@FXML
	private TextField fieldStopLength;
	
	@FXML 
	private AnchorPane root;

	private Stage stage;
	
	public void initialize() {
		//stage.setOnCloseRequest(e -> closeModalbox());
		Stage stage = (Stage) root.getScene().getWindow();
		//System.out.println(stage.getTitle());
		/*stage.setOnCloseRequest(e -> closeModalbox());*/
		
		System.out.println("test");
	}
	
	@FXML
	private void closeModalbox() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Luk opret nyt stop");
		alert.setHeaderText("Alt data vil blive slettet!");
		alert.setContentText("Luk vinduet?");
		
		ButtonType confirmButton = new ButtonType("ja", ButtonData.YES);
		ButtonType cancelButton = new ButtonType("nej", ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(confirmButton, cancelButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == confirmButton){
			Stage stage = (Stage) buttonCancel.getScene().getWindow();
			stage.close();
		} else {
		    // ... user chose CANCEL or closed the dialog    
		}
	}

	/**
	 * @param stage the stage to set
	 * @return 
	 */
	public void setStage(final Stage stage) {
		this.stage = stage;
	}
	
	
	
}
