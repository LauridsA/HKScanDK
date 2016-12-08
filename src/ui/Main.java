package ui;
	
import java.util.Map;

import dba.DBSingleConnection;
import javafx.application.Application;
import javafx.application.Application.Parameters;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import uiScreen.DailyScreenController;


public class Main extends Application {
	private BorderPane rootLayout;
	private Stage primaryStage;
	private AnchorPane anchorPane;
	private DBSingleConnection dbsincon = new DBSingleConnection();

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		Parameters parameters = getParameters();
		Map<String, String> namedParameters = parameters.getNamed();
		if(namedParameters.containsKey("administrator")){
			 initAdministratorStage();
			 initAdministratorScene();
		}else{
			initStage();
			initScene();
			
			primaryStage.setMaximized(false);
			primaryStage.setFullScreen(false);
		}	
		/*
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/ui/Main.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		*/
	}

	private void initAdministratorStage() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource(".fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void initAdministratorScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("DailyScreen.fxml"));
			anchorPane = (AnchorPane) loader.load();
			
			rootLayout.setCenter(anchorPane);
			((DailyScreenController) loader.getController()).setDatabaseController(dbsincon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	private void initStage() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("screenUI/Main.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void initScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("screenUI/DailyScreen.fxml"));
			anchorPane = (AnchorPane) loader.load();
			
			rootLayout.setCenter(anchorPane);
			((DailyScreenController) loader.getController()).setDatabaseController(dbsincon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
