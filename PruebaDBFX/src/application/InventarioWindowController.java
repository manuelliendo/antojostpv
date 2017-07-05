package application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class InventarioWindowController implements Initializable {

	@FXML
	TableView<Almacen> tableViewInventario;
	@FXML
	TableColumn<Almacen, String> tableColumnNombre;
	@FXML
	TableColumn<Almacen, Integer> tableColumnStock;
	@FXML
	TableColumn<Almacen, Integer> tableColumnCapMinima;
	@FXML
	TableColumn<Almacen, Integer> tableColumnCapDeseada;
	@FXML
	TableColumn<Almacen, Integer> tableColumnCapMaxima;
	@FXML
	TableColumn<Almacen, Double> tableColumnCapPrecio;
	ObservableList<Almacen> listaAlmacen = FXCollections.observableArrayList();
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	InputStream is;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		PopulateTabla();
	}

	public void btnProductosPressed(ActionEvent event) throws IOException {

		Stage stage = (Stage) tableViewInventario.getScene().getWindow();
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
		Stage primaryStage = (Stage) tableViewInventario.getScene().getWindow();
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

	public void btnConfigPressed(ActionEvent event) throws IOException {
		Stage stage = (Stage) tableViewInventario.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/ConfigWindow.fxml"));
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

	public void PopulateTabla() {

		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		String query2 = "SELECT * FROM Almacen";
		listaAlmacen.clear();
		try {
			pst = conn.prepareStatement(query2);
			rs = pst.executeQuery();

			while (rs.next()) {
				listaAlmacen.add(new Almacen(rs.getString("Nombre"),rs.getInt("Stock"),rs.getInt("Cantidad_minima"),rs.getInt("Cantidad_deseada"),rs.getInt("Capacidad"),rs.getDouble("Precio")));
			}
			}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			tableColumnNombre.setCellValueFactory(new PropertyValueFactory<Almacen,String>("nombre"));
			tableColumnStock.setCellValueFactory(new PropertyValueFactory<Almacen,Integer>("stock"));
			tableColumnCapMinima.setCellValueFactory(new PropertyValueFactory<Almacen,Integer>("capacidadMinima"));
			tableColumnCapDeseada.setCellValueFactory(new PropertyValueFactory<Almacen,Integer>("capacidadDeseada"));
			tableColumnCapMaxima.setCellValueFactory(new PropertyValueFactory<Almacen,Integer>("capacidadMaxima"));
			tableColumnCapPrecio.setCellValueFactory(new PropertyValueFactory<Almacen,Double>("Precio"));
			tableViewInventario.setItems(listaAlmacen);
		}
	}

	public void btnAgregarProducto(ActionEvent event) throws IOException {

		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/AddProductoAlmacenWindow.fxml"));
		Scene scene = new Scene(root, 300, 400);
		scene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.showAndWait();
		PopulateTabla();
	}

	public void btnEditarProducto(ActionEvent event) throws IOException {

		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/EditProductoAlmacenWindow.fxml"));
		Scene scene = new Scene(root, 300, 400);
		scene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.showAndWait();
		PopulateTabla();
	}

	public void btnEliminarProducto(ActionEvent event) throws IOException {

		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/DeleteProductoAlmacenWindow.fxml"));
		Scene scene = new Scene(root, 300, 400);
		scene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.showAndWait();
		PopulateTabla();
	}
}
