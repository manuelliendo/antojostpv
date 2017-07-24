package application;

import java.awt.print.PrinterException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import com.google.zxing.WriterException;




/*CONTROLADOR DE VENTANA DE EMISION DE FACTURA*/
public class FacturaController implements Initializable {

	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	@FXML
	TextField textFieldNit;
	@FXML
	TextField textfieldNombre;
	@FXML
	TextField textFieldMontoPagado;
	@FXML
	TableView<ProductoSimple> tableViewOrdenTotal;
	@FXML
	TableColumn<ProductoSimple, String> tableColumnProducto;
	@FXML
	TableColumn<ProductoSimple, String> tableColumnEspecial;
	@FXML
	Label labelTotal;
	@FXML
	Label labelCambio;
	@FXML
	RadioButton radioButtonEfectivo;
	@FXML
	RadioButton radioButtonTarjeta;
	ObservableList<ProductoSimple> orden = FXCollections.observableArrayList();
	Float auxCuenta = (float) 0;
	Float auxCambio = (float) 0;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		textFieldMontoPagado.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("[.\\d*]")) {
                textFieldMontoPagado.setText(newValue.replaceAll("[^\\d.]", ""));
                
            }
			getCambio();
		});
		try {
			read();
		} catch (ClassNotFoundException | IOException e) {
			
			e.printStackTrace();
		} finally {
			tableViewOrdenTotal.setItems(orden);
			tableColumnProducto
					.setCellValueFactory(new PropertyValueFactory<ProductoSimple, String>(
							"Nombre"));
			tableColumnEspecial
					.setCellValueFactory(new PropertyValueFactory<ProductoSimple, String>(
							"especial"));
		}

		for (int i = 0; i < orden.size(); i++) {
			auxCuenta += orden.get(i).getPrecioTotal();
		}

		labelTotal.setText(String.valueOf(auxCuenta));
	}

	public void addCliente(ActionEvent event){
		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		
		String query2 = "INSERT INTO Cliente (Nit,Razon_social) VALUES (? ,?)";
		
		try {
			pst = conn.prepareStatement(query2);
			pst.setString(1, textFieldNit.getText());
			pst.setString(2, textfieldNombre.getText());
			pst.executeUpdate();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Configuración de usuario");
			alert.setHeaderText("Éxito");
			alert.setContentText("Cliente guardado");
			alert.showAndWait();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void getNit() {
		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		String query = "SELECT Razon_Social FROM Cliente WHERE (Cliente.Nit = ?)";
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, textFieldNit.getText());
			rs = pst.executeQuery();
		
			while (rs.next()) {

				textfieldNombre.setText(rs.getString("Razon_Social"));
			}
		} catch (Exception e) {
		}

	}
	
	public void emitirFactura() throws ClassNotFoundException, IOException, WriterException, PrinterException {
		int opcionMetodo;
		if(textFieldNit.getText().isEmpty() || textfieldNombre.getText().isEmpty())
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor ingrese el NIT y la razón social");
			alert.showAndWait();
		}
		else{
		if(!radioButtonEfectivo.isSelected() && !radioButtonTarjeta.isSelected())
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor seleccione el método de pago");
			alert.showAndWait();
		}else{
		if(radioButtonEfectivo.isSelected())
		{
			opcionMetodo = 0;
		}else{
			opcionMetodo = 1;
		}
		if(textFieldMontoPagado.getText().isEmpty()){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese el monto a ser pagado");
			alert.showAndWait();
		}else{
			double aux = Double.valueOf(labelCambio.getText());
			if(aux<0)
			{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error");
				alert.setContentText("El pago debe ser mayor o igual al monto");
				alert.showAndWait();
			}
			else{
			EmisionFactura.Emitir(textFieldNit.getText(), textfieldNombre.getText(), labelTotal.getText(),textFieldMontoPagado.getText(),labelCambio.getText(),Usuario.getNombreUsuario());
			Estadisticas e = new Estadisticas();
			try {
				e.GuardarEstadisticas(opcionMetodo, labelTotal.getText(), orden);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Stage stage = (Stage) textFieldNit.getScene().getWindow();

			orden.clear();
			stage.close();
			}
			}
		}
		}
	}

	public void getCambio() {
		Float auxMonto;
		if (textFieldMontoPagado.getText().isEmpty()) {
			auxMonto = (float) 0;
		} else {
			auxMonto = Float.valueOf(textFieldMontoPagado.getText());
		}
		
		auxCambio = auxMonto - auxCuenta;
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat df2 = new DecimalFormat("#.##",otherSymbols);
		labelCambio.setText(df2.format(auxCambio));
	}

	public void read() throws IOException, ClassNotFoundException {
		FileInputStream fin;
		try {
			fin = new FileInputStream("nombre.ser");
			ObjectInputStream ois = new ObjectInputStream(fin);
			List<ProductoSimple> list = (List<ProductoSimple>) ois.readObject();
			orden = FXCollections.observableList(list);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}
