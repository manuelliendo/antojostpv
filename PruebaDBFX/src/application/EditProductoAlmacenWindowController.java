package application;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class EditProductoAlmacenWindowController implements Initializable {

	@FXML
	TextField textfieldNombre;
	@FXML
	TextField textfieldStock;
	@FXML
	TextField textfieldMinima;
	@FXML
	TextField textfieldDeseada;
	@FXML
	TextField textfieldMaxima;
	@FXML
	TextField textfieldPrecio;
	@FXML
	ComboBox<String> comboboxProducto;
	ObservableList<String> listaCombobox = FXCollections.observableArrayList();
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		PopulateCombobox();

	}

	public void PopulateCombobox() {
		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		String query = "SELECT Nombre FROM Almacen";
//		String query = "SELECT Nombre,Stock,Cantidad_minima,Cantidad_deseada,Capacidad,Precio FROM Almacen WHERE Almacen.Nombre = ?";
		listaCombobox.clear();
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				listaCombobox.add(rs.getString("Nombre"));

			}
			comboboxProducto.setItems(listaCombobox);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void PopulateLista() {
		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		String query = "SELECT Nombre,Stock,Cantidad_minima,Cantidad_deseada,Capacidad,Precio FROM Almacen WHERE Almacen.Nombre = ?";
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, comboboxProducto.getValue());
			rs = pst.executeQuery();
			while (rs.next()) {
				textfieldNombre.setText(rs.getString("Nombre"));
				textfieldStock.setText(rs.getString("Stock"));
				textfieldMinima.setText(rs.getString("Cantidad_minima"));
				textfieldDeseada.setText(rs.getString("Cantidad_deseada"));
				textfieldMaxima.setText(rs.getString("Capacidad"));
				textfieldPrecio.setText(rs.getString("Precio"));
			}
			comboboxProducto.setItems(listaCombobox);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	public void BtnGuardarPressed(ActionEvent event) throws SQLException {
		pst = null;
		rs = null;
		conn = null;
		if (textfieldNombre.getText().isEmpty()
				|| textfieldStock.getText().isEmpty()
				|| textfieldMinima.getText().isEmpty()
				|| textfieldDeseada.getText().isEmpty()
				|| textfieldMaxima.getText().isEmpty()
				|| textfieldPrecio.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor ingrese todos los datos correctamente");
			alert.showAndWait();
		} else {
			conn = ConnectionDB.Conectar();
			String query = "UPDATE Almacen SET Nombre =?,Stock =?,Cantidad_minima =?,Cantidad_deseada =?,Capacidad =?,Precio =? WHERE Almacen.Nombre = ?";
			try {

				pst = conn.prepareStatement(query);
				pst.setString(1, textfieldNombre.getText());
				pst.setInt(2, Integer.valueOf(textfieldStock.getText()));
				pst.setInt(3, Integer.valueOf(textfieldMinima.getText()));
				pst.setInt(4, Integer.valueOf(textfieldDeseada.getText()));
				pst.setInt(5, Integer.valueOf(textfieldMaxima.getText()));
				pst.setDouble(6, Double.valueOf(textfieldPrecio.getText()));
				pst.setString(7, comboboxProducto.getValue());
				pst.executeUpdate();
			}

			catch (Exception e) {
				e.printStackTrace();
			} finally {
				pst.close();
				conn.close();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Datos guardados");
				alert.setHeaderText("Éxito");
				alert.setContentText("Datos guardados");
				alert.showAndWait();
				Stage stage = (Stage) textfieldNombre.getScene().getWindow();
				stage.close();
			}
		}
	}
}
