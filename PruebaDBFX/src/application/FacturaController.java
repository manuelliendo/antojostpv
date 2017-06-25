package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
	ObservableList<ProductoSimple> orden = FXCollections.observableArrayList();
	Float auxCuenta = (float) 0;
	Float auxCambio = (float) 0;


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			read();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
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

	public void getNit() {
		// TODO buscar en base de datos usuario con el nit, sino insert en la bd
		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		String query = "SELECT Razon_Social FROM Cliente WHERE (Cliente.Nit = ?)";
		String query2 = "INSERT INTO Cliente (Nombre,Apellido,Direccion,Email,User,Nit,Razon_Social,Referencia_Direccion,Telefono) VALUES ('John','Doe','Calle 2 Zona 2 # 321','mail1@mail.com','JohnDoe','123456','Doe','casa gris','69512345')";
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

	public void emitirFactura() throws ClassNotFoundException, IOException, WriterException {
		// TODO: imprimir factura
		// TODO: facturacion electronica
		// TODO: subir a base de datos
		EmisionFactura.Emitir(textFieldNit.getText(), textfieldNombre.getText(), labelTotal.getText(),textFieldMontoPagado.getText(),labelCambio.getText(),"cajero");
		Stage stage = (Stage) textFieldNit.getScene().getWindow();

		orden.clear();
		stage.close();
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
