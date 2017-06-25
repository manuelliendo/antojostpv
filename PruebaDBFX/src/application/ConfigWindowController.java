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

/*CONTROLADOR DE LA VENTANA CONFIGURACION*/

public class ConfigWindowController {

	@FXML
	Label l = new Label();

	public void btnUsuariosPressed(ActionEvent event) {

	}

	public void btnConfigFacturaPressed(ActionEvent event) throws IOException {

		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/ConfigFacturaWindow.fxml"));
		Scene scene = new Scene(root, 630, 540);
		scene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void btnProductosPressed(ActionEvent event) throws IOException {

		Stage stage = (Stage) l.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/ProductosWindow.fxml"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
	}

	public void btnPedidosPressed(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) l.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/MainMenu.fxml"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Scene scene = new Scene(root, width, height - 50);
		scene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
}
