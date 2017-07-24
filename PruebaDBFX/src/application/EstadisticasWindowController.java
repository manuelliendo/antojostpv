package application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class EstadisticasWindowController implements Initializable{

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
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	@FXML
	DatePicker fechaInicio;
	@FXML
	DatePicker fechaFinal;
	@FXML
	Label labelTotalIngresos;
	@FXML
	Label labelTotalClientes;
	@FXML
	Label labelMinClientes;
	@FXML
	Label labelMaxClientes;
	@FXML
	Label labelProductoMasVendido;
	@FXML
	Label labelProductoMenosVendido;
	
	CategoryAxis xAxis    = new CategoryAxis();
	NumberAxis yAxis = new NumberAxis();
	DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	@FXML
	BarChart<String, Number> barChartProductos = new BarChart<String,Number>(xAxis,yAxis);
	@FXML
	ComboBox<String> comboboxEstadistica ; 
	ObservableList<String> listaCombobox= FXCollections.observableArrayList("Clientes","Productos","Ingresos");
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		comboboxEstadistica.setItems(listaCombobox);
		comboboxEstadistica.setValue(listaCombobox.get(0));
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
	
	public void PopulateBarChart(ActionEvent event) throws SQLException {
		pst = null;
		rs = null;
		conn = null;
		String query = "";
		if(comboboxEstadistica.getValue().isEmpty())
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor seleccione una de las opciones en la lista");
			alert.showAndWait();
		}
		else{
		switch(comboboxEstadistica.getValue())
		{
		case "Clientes":
			barChartProductos.getData().clear();
			barChartProductos.setTitle("Clientes");
			barChartProductos.getXAxis().setLabel("Fecha");
			barChartProductos.getYAxis().setLabel("Clientes");
			query = "SELECT Clientes,Date FROM Stats WHERE Date BETWEEN  date(?) and date(?)";
			break;
		case "Productos":
			barChartProductos.getData().clear();
			barChartProductos.setTitle("Productos");
			barChartProductos.getXAxis().setLabel("Productos");
			barChartProductos.getYAxis().setLabel("Cantidad");
			query = "SELECT Producto_nombre,Cantidad_Comprada,Fecha_Compra FROM Estadisticas WHERE Fecha_Compra BETWEEN  date(?) and date(?)";
			break;
		case "Ingresos":
			barChartProductos.getData().clear();
			barChartProductos.setTitle("Ingresos(Bs.)");
			barChartProductos.getXAxis().setLabel("Fecha");
			barChartProductos.getYAxis().setLabel("Ingresos(Bs.)");
			query = "SELECT Ingresos_neto,Date FROM Stats WHERE Date BETWEEN  date(?) and date(?)";
			break;
		}
		
		conn = ConnectionDB.Conectar();
		try {
			 
			XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
			pst = conn.prepareStatement(query);
			
//			System.out.println(fechaInicio.getValue().toString());
//			System.out.println(fechaFinal.getValue().toString());
			pst.setString(1, fechaInicio.getValue().toString());
			pst.setString(2, fechaFinal.getValue().toString());
			rs = pst.executeQuery();
			
			while(rs.next()){
				
//				System.out.println("prod id: " + rs.getInt("Producto_Id"));
//				System.out.println(" " + rs.getString("Nombre"));
//				System.out.println("cant: " + rs.getInt("Cantidad_Comprada"));
//				System.out.println("Fecha: " + rs.getString("Fecha_Compra"));
				switch (comboboxEstadistica.getValue()) {
				case "Clientes":
					series.getData().add(new XYChart.Data<String,Number>(rs.getString("Date"),Double.valueOf(rs.getString("Clientes"))));
					labelTotalClientes.setText(String.valueOf(GetClientes(series)));
					labelMaxClientes.setText(getMaxData(series));
					labelMinClientes.setText(getMinData(series));
					break;
				case "Productos":
					series.getData().add(new XYChart.Data<String,Number>(rs.getString("producto_nombre"),Double.valueOf(rs.getInt("Cantidad_Comprada"))));
					labelProductoMasVendido.setText(getMaxData(series));
					labelProductoMenosVendido.setText(getMinData(series));
					break;
				case "Ingresos":
					series.getData().add(new XYChart.Data<String,Number>(rs.getString("Date"),Double.valueOf(rs.getString("Ingresos_neto"))));
					labelTotalIngresos.setText(String.valueOf(GetIngresos(series)));
					break;
				}
			}
		
			
//			series.getData().add(new XYChart.Data<String,Number>("Simple",5));
//			System.out.println(series.getData().get(0));
			barChartProductos.setAnimated(false);
			barChartProductos.getData().add(series);
			for (Series<String,Number> serie: barChartProductos.getData()){
	            for (XYChart.Data<String, Number> item: serie.getData()){
	            	Tooltip tp = new Tooltip(item.getYValue().toString());
	            	item.getNode().setOnMouseClicked(MouseEvent -> {;
	                		tp.show(item.getNode(), MouseEvent.getSceneX(),MouseEvent.getSceneY() );
	                		tp.setAutoHide(true);
	                		tp.setAutoFix(true);

	                });
	                
	                item.getNode().setOnMouseExited(MouseEvent -> {;

                });
	            }
	        }
			
	        
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			pst.close();
			conn.close();
		}
		}
	}

	public Double GetIngresos(XYChart.Series<String,Number> series){
		Double ingreso=0d;
		ObservableList<Data<String,Number>> e;
		e = series.getData();
		for(int i=0;i<e.size();i++)
		{
		ingreso+= (double)(e.get(i).getYValue());
		}
		return ingreso;
	}
	
	public Double GetClientes(XYChart.Series<String,Number> series){
		Double clientes=0d;
		ObservableList<Data<String,Number>> e;
		e = series.getData();
		for(int i=0;i<e.size();i++)
		{
		clientes+= (double)(e.get(i).getYValue());
		}
		return clientes;
	}
	
	public String getMaxData(XYChart.Series<String,Number> series){
		String max="";
		ObservableList<Data<String,Number>> e;
		e = series.getData();
		max = e.get(getMax(series)).getXValue();
		return max;
	}
	public String getMinData(XYChart.Series<String,Number> series){
		String min="";
		ObservableList<Data<String,Number>> e;
		e = series.getData();
		min = e.get(getMin(series)).getXValue();
		return min;
	}
	
	public int getMax(XYChart.Series<String,Number> series){
		double max = 0;
		int maxIndex = 0;
		ObservableList<Data<String,Number>> e;
		e = series.getData();
		for(int i=0;i<e.size();i++)
		{
			if((double) e.get(i).getYValue() > max)
			{
				max = (double)e.get(i).getYValue();
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	public int getMin(XYChart.Series<String,Number> series){
		double min = 0;
		int minIndex = 0;
		ObservableList<Data<String,Number>> e;
		e = series.getData();
		min = (double) e.get(0).getYValue();
		
		for(int i=0;i<e.size();i++)
		{
			if((double) e.get(i).getYValue() < min)
			{
				min = (double)e.get(i).getYValue();
				minIndex = i;
			}
		}
		return minIndex;
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
				getClass().getResource("Pedidos.css").toExternalForm());
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
				getClass().getResource("Productos.css").toExternalForm());
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
				getClass().getResource("Config.css").toExternalForm());
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
				getClass().getResource("Inventario.css").toExternalForm());
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
	}

	
	
	

}
