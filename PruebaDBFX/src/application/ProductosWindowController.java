package application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ProductosWindowController {

	@FXML
	Label label = new Label();
	public void btnPedidosPressed(ActionEvent event) throws IOException{
		Stage primaryStage = (Stage)label.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/MainMenu.fxml"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
