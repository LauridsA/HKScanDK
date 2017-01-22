package ui;
	
import java.util.Map;

import dba.DBSingleConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import uiScreen.DailyScreenController;
import uiScreen.MainController;


public class Main extends Application {
	private BorderPane rootLayout;
	private Stage primaryStage;
	private AnchorPane anchorPane;
	private DBSingleConnection dbsincon = new DBSingleConnection();
	private Label teamName;
	private Label dateField;

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		Parameters parameters = getParameters();
		Map<String, String> namedParameters = parameters.getNamed();
		if(namedParameters.containsKey("admin")){
			 initAdministratorStage();
			 initAdministratorScene();
		}else{
			initStage();
			initScene();
			
			primaryStage.setMaximized(false);
			primaryStage.setFullScreen(false);
		}	
		
	}

	/**
	 * Initializes the administration part of the UI<br>
	 * Creates the javaFX stage used to hold the scene<br>
	 * Done through runtime argument: --administrator=1
	 */
	private void initAdministratorStage() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/uiAdministration/Main.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource("/uiAdministration/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Initializes the administration part of the UI<br>
	 * This part sets the scene on to the stage<br>
	 * Done through runtime argument: --administrator=1
	 */
	private void initAdministratorScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/uiAdministration/Administration.fxml"));
			anchorPane = (AnchorPane) loader.load();
			
			rootLayout.setCenter(anchorPane);
			//((DailyScreenController) loader.getController()).setDatabaseController(dbsincon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Main entry point for the program<br>
	 * Passes parameters to launch the correct UI
	 * @param args leave blank for dailyScreen <br>
	 * or use <i>--administrator=1</i> to initialize adminUI
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Initializes the dailyScreen part of the UI<br>
	 * Creates the javaFX stage used to hold the scene
	 */
	private void initStage() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/uiScreen/Main.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			//scene.getStylesheets().add(getClass().getResource("/uiScreen/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			//teamName = ((MainController) loader.getController()).getTeamName();
			//dateField = ((MainController) loader.getController()).getDateField();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Initializes the dailyScreen part of the UI<br>
	 * Creates and sets the scene to the javaFX stage
	 */
	private void initScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/uiScreen/DailyScreen.fxml"));
			anchorPane = (AnchorPane) loader.load();
			
			rootLayout.setCenter(anchorPane);
			DailyScreenController sctr = ((DailyScreenController) loader.getController());
			sctr.setDatabaseController(dbsincon);
			//sctr.setHeaderLabels(teamName, dateField);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
