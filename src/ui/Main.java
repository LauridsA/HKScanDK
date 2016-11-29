package ui;
	
import controller.Controller;
import dba.DBSingleConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	private BorderPane rootLayout;
	private Stage primaryStage;
	private AnchorPane anchorPane;
	private DBSingleConnection dbsincon = new DBSingleConnection();

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		initStage();
		initScene();
		primaryStage.setMaximized(false);
		primaryStage.setFullScreen(false);
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
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void initStage() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("Main.fxml"));
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
			loader.setLocation(Main.class.getResource("DailyScreen.fxml"));
			anchorPane = (AnchorPane) loader.load();
			
			rootLayout.setCenter(anchorPane);
			((DailyScreenController) loader.getController()).setDatabaseController(dbsincon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
