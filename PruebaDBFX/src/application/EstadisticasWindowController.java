package application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EstadisticasWindowController implements Initializable{

	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	@FXML
	DatePicker fechaInicio;
	@FXML
	DatePicker fechaFinal;
	@FXML
	Label totalIngresos;
	@FXML
	Label totalClientes;
	@FXML
	Label minClientes;
	@FXML
	Label maxClientes;
	@FXML
	Label productoMasVendido;
	@FXML
	Label productoMenosVendido;
	
	CategoryAxis xAxis    = new CategoryAxis();
	NumberAxis yAxis = new NumberAxis();
	
	@FXML
	BarChart<String, Number> barChartProductos = new BarChart<String,Number>(xAxis,yAxis);
	@FXML
	ComboBox<String> comboboxEstadistica ; 
	ObservableList<String> listaCombobox= FXCollections.observableArrayList("Clientes","Productos","Ingresos"); 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		comboboxEstadistica.setItems(listaCombobox);
		
	}
	
	public void PopulateBarChart(ActionEvent event) throws SQLException {
		pst = null;
		rs = null;
		conn = null;
		barChartProductos.getData().clear();
		barChartProductos.setTitle("Stats");
		barChartProductos.getXAxis().setLabel("Productos");
		barChartProductos.getYAxis().setLabel("Cantidad");
		conn = ConnectionDB.Conectar();
		String query = "SELECT Productos.Nombre,Estadisticas.Fecha_Compra,Estadisticas.Cantidad_Comprada,Estadisticas.Producto_id FROM Estadisticas"+ 
		" INNER JOIN  Productos ON Productos.Id = Estadisticas.Producto_Id WHERE Fecha_Compra BETWEEN  date(?) and date(?)";
		
//		String query = "SELECT Fecha_Compra,Producto_Id,Cantidad_Comprada FROM Estadisticas ";
		try {
			 
			XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
			pst = conn.prepareStatement(query);
			System.out.println(fechaInicio.getValue().toString());
			System.out.println(fechaFinal.getValue().toString());
			pst.setString(1, fechaInicio.getValue().toString());
			pst.setString(2, fechaFinal.getValue().toString());
			rs = pst.executeQuery();
			
			while(rs.next()){
				System.out.println("prod id: " + rs.getInt("Producto_Id"));
				System.out.println(" " + rs.getString("Nombre"));
				System.out.println("cant: " + rs.getInt("Cantidad_Comprada"));
				System.out.println("Fecha: " + rs.getString("Fecha_Compra"));
				
				series.getData().add(new XYChart.Data<String,Number>(rs.getString("Nombre"),rs.getInt("Cantidad_Comprada")));
			}
		
			
//			series.getData().add(new XYChart.Data<String,Number>("Simple",5));
//			System.out.println(series.getData().get(0));
			barChartProductos.setAnimated(false);
			barChartProductos.getData().add(series);
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			pst.close();
			conn.close();
		}
	}

	public void btnPedidosPressed(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) fechaInicio.getScene().getWindow();
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

	public void btnProductosPressed(ActionEvent event) throws IOException {

		Stage stage = (Stage) fechaFinal.getScene().getWindow();
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

	public void btnConfigPressed(ActionEvent event) throws IOException {
		Stage stage = (Stage) fechaFinal.getScene().getWindow();
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

	public void btnInventarioPressed(ActionEvent event) throws IOException {
		Stage stage = (Stage) fechaFinal.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/InventarioWindow.fxml"));
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

	
	

}
