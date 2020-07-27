package my.silly.filebrowser;
	
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
			Scene scene = new Scene(root);
			setUserAgentStylesheet(STYLESHEET_CASPIAN);
			primaryStage.setScene(scene);
			primaryStage.setTitle("My silly filebrowser");
			primaryStage.show();
			primaryStage.setOnCloseRequest(e->closeProgram(e,primaryStage));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void closeProgram(WindowEvent e, Stage primaryStage) {
		e.consume();
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to exit?");
		alert.initModality(Modality.APPLICATION_MODAL);
		Optional<ButtonType> option = alert.showAndWait();
		if (option.isPresent() && option.get() == ButtonType.OK) {
			Platform.exit();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
