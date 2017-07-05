package application;

import java.io.FileInputStream;
import java.io.InputStream;
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
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DeleteProductoWindowController implements Initializable{

	@FXML
	ComboBox<String> comboBoxCategorias;
	@FXML
	ComboBox<String> comboBoxProductos;
	ObservableList<String> listaNombreCategoria = FXCollections.observableArrayList();
	ObservableList<Integer> listaIdCategoria = FXCollections.observableArrayList();
	ObservableList<String> listaNombreProducto = FXCollections.observableArrayList();
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		PopulateComboBoxCategorias();
	}
	public void PopulateComboBoxProductos() {
		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		String query = "SELECT Nombre FROM Productos WHERE Productos.Categoria_Id = ?";
		listaNombreProducto.clear();
		try {
			pst = conn.prepareStatement(query);
			pst.setInt(1, listaIdCategoria.get(listaNombreCategoria.indexOf(comboBoxCategorias.getValue())));
			rs = pst.executeQuery();

			while (rs.next()) {
				listaNombreProducto.add(rs.getString("Nombre"));

			}
			comboBoxProductos.setItems(listaNombreProducto);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void PopulateComboBoxCategorias() {
		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		String query = "SELECT Nombre,Id FROM Categorias ";
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();

			while (rs.next()) {
				listaNombreCategoria.add(rs.getString("Nombre"));
				listaIdCategoria.add(rs.getInt("Id"));
			}
			comboBoxCategorias.setItems(listaNombreCategoria);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void GuardarBrnPressed(ActionEvent event) throws SQLException {
		pst = null;
		rs = null;
		conn = null;
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmacion");
		alert.setHeaderText("Borrar producto");
		alert.setContentText("¿Esta seguro que quiere borrar este producto?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			conn = ConnectionDB.Conectar();
			String query = "DELETE FROM Productos WHERE Productos.Nombre = ?";
			
				try {

					pst = conn.prepareStatement(query);
					pst.setString(1, comboBoxProductos.getValue());
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
					alertInfo.setHeaderText("Éxito");
					alertInfo.setContentText("Datos borrados con exito");
					alertInfo.showAndWait();
					Stage stage = (Stage) comboBoxCategorias.getScene()
							.getWindow();
					stage.close();
				}
				}
		else{
		alert.close();	
		}

	}
}
