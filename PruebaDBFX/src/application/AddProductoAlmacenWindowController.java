package application;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AddProductoAlmacenWindowController {

	@FXML
	TextField textfieldNombre;
	@FXML
	TextField textfieldStock;
	@FXML
	TextField textfieldMinima;
	@FXML
	TextField textfielDeseada;
	@FXML
	TextField textfieldMaxima;
	@FXML
	TextField textfieldPrecio;
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	public void BtnGuardarPressed(ActionEvent event) throws SQLException {
		pst = null;
		rs = null;
		conn = null;
		if (textfieldNombre.getText().isEmpty()
				|| textfieldStock.getText().isEmpty()
				|| textfieldMinima.getText().isEmpty()
				|| textfielDeseada.getText().isEmpty()
				|| textfieldMaxima.getText().isEmpty()
				|| textfieldPrecio.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor ingrese todos los datos correctamente");
			alert.showAndWait();
		} else {
			conn = ConnectionDB.Conectar();
			String query = "INSERT INTO Almacen (Nombre,Stock,Cantidad_minima,Cantidad_deseada,Capacidad,Precio) VALUES(?,?,?,?,?,?)";
			try {

				pst = conn.prepareStatement(query);
				pst.setString(1, textfieldNombre.getText());
				pst.setInt(2, Integer.valueOf(textfieldStock.getText()));
				pst.setInt(3, Integer.valueOf(textfieldMinima.getText()));
				pst.setInt(4, Integer.valueOf(textfielDeseada.getText()));
				pst.setInt(5, Integer.valueOf(textfieldMaxima.getText()));
				pst.setDouble(6, Double.valueOf(textfieldPrecio.getText()));
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
