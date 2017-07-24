package application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*CONTROLADOR DE LA VENTANA CONFIGURACION*/

public class ConfigWindowController implements Initializable{

	@FXML
	Button btnProductos;
	@FXML
	Button btnInventarios;
	@FXML
	Button btnEstadisticas;
	@FXML
	Button btnPaP;
	@FXML
	Button btnConfig;
	@FXML
	Label l = new Label();

	public void btnConfigFacturaPressed(ActionEvent event) throws IOException {

		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/ConfigFacturaWindow.fxml"));
		Scene scene = new Scene(root, 630, 540);
		scene.getStylesheets().add(
				getClass().getResource("Add.css").toExternalForm());
		stage.setTitle("Configuracion de factura");
		try{
			String dir = System.getProperty("user.dir");
//			System.out.println(dir + "\\icon.png");
		stage.getIcons().add(new Image("file:" + dir + "\\icon.png"));
		
		}catch(Exception e){
			e.printStackTrace();
		}
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
				getClass().getResource("Productos.css").toExternalForm());
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
				getClass().getResource("Pedidos.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
	
	public void btnInventarioPressed(ActionEvent event) throws IOException{
		Stage stage = (Stage)l.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/InventarioWindow.fxml"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().add(
				getClass().getResource("Inventario.css").toExternalForm());
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
	}

	public void btnEstadisticasPressed(ActionEvent event) throws IOException {
		Stage stage = (Stage)l.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/EstadisticasWindow.fxml"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().add(
				getClass().getResource("Estadisticas.css").toExternalForm());
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
	}
	
	public void btnAgregarUsuariosPressed(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/AddUsuarioWindow.fxml"));
		Scene scene = new Scene(root, 400, 330);
		scene.getStylesheets().add(
				getClass().getResource("Add.css").toExternalForm());
		stage.setTitle("Agregar usuario");
		try{
			String dir = System.getProperty("user.dir");
//			System.out.println(dir + "\\icon.png");
		stage.getIcons().add(new Image("file:" + dir + "\\icon.png"));
		
		}catch(Exception e){
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.show();
	}
	
	public void btnEditarUsuariosPressed(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/EditUsuarioWindow.fxml"));
		Scene scene = new Scene(root, 400, 330);
		scene.getStylesheets().add(
				getClass().getResource("Add.css").toExternalForm());
		stage.setTitle("Editar usuarios");
		try{
			String dir = System.getProperty("user.dir");
//			System.out.println(dir + "\\icon.png");
		stage.getIcons().add(new Image("file:" + dir + "\\icon.png"));
		
		}catch(Exception e){
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.show();
	}
	
	public void btnEliminarUsuariosPressed(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/DeleteUsuarioWindow.fxml"));
		Scene scene = new Scene(root, 400, 155);
		scene.getStylesheets().add(
				getClass().getResource("Add.css").toExternalForm());
		stage.setTitle("Eliminar usuarios");
		try{
			String dir = System.getProperty("user.dir");
//			System.out.println(dir + "\\icon.png");
		stage.getIcons().add(new Image("file:" + dir + "\\icon.png"));
		
		}catch(Exception e){
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		if(Usuario.getPermiso() == 2){
			btnConfig.setDisable(true);
			
		}else{
			if(Usuario.getPermiso() == 3){
				btnConfig.setDisable(true);
				btnProductos.setDisable(true);
				btnEstadisticas.setDisable(true);
				}
		}
		
	}
}
