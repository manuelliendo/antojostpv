package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class DeleteProductoAlmacenWindowController implements Initializable {

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
		// String query =
		// "SELECT Nombre,Stock,Cantidad_minima,Cantidad_deseada,Capacidad,Precio FROM Almacen WHERE Almacen.Nombre = ?";
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

	public void BtnGuardarPressed(ActionEvent event) throws SQLException {
		pst = null;
		rs = null;
		conn = null;

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmacion");
		alert.setHeaderText("Borrar producto");
		alert.setContentText("�Esta seguro que quiere borrar este producto?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			conn = ConnectionDB.Conectar();
			String query = "DELETE FROM Almacen WHERE Almacen.Nombre = ?";

			try {

				pst = conn.prepareStatement(query);
				pst.setString(1, comboboxProducto.getValue());
				pst.executeUpdate();
			}

			catch (Exception e) {
				e.printStackTrace();
			}

			finally {
				pst.close();
				conn.close();
				Alert alertInfo = new Alert(AlertType.INFORMATION);
				alertInfo.setTitle("Datos borrado");
				alertInfo.setHeaderText("�xito");
				alertInfo.setContentText("Datos borrados con exito");
				alertInfo.showAndWait();
				Stage stage = (Stage) comboboxProducto.getScene().getWindow();
				stage.close();
			}
		} else {
			alert.close();
		}
	}
}