package jedi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainWindow extends Application {
	
	@Override
	public void start(Stage window) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/FXML/MainWindow.fxml"));
		StackPane stackPane = loader.load();
		Scene scene = new Scene(stackPane);
		scene.getStylesheets().add(this.getClass().getResource("/CSS/MainWindow.css").toExternalForm());
		window.setTitle("System zarz¹dzania Jedi");
		window.setResizable(false);
		window.setScene(scene);
		window.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}// MainWindow
