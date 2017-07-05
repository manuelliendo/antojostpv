package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditProductoWindowController implements Initializable{

	@FXML
	ComboBox<String> comboBoxCategorias;
	@FXML
	ComboBox<String> comboBoxProductos;
	@FXML
	TextField textFieldNombre;
	@FXML
	TextField textFieldPrecio;
	@FXML
	ImageView imageViewProducto = new ImageView();
	ObservableList<String> listaNombreCategoria = FXCollections.observableArrayList();
	ObservableList<Integer> listaIdCategoria = FXCollections.observableArrayList();
	ObservableList<String> listaNombreProducto = FXCollections.observableArrayList();
	ObservableList<Double> listaPrecio = FXCollections.observableArrayList();
	ObservableList<Image> listaImageView = FXCollections.observableArrayList();
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	FileChooser fc;
	File file;
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
	
	
	public void BuscarBtnPressed(ActionEvent event) {
		fc = new FileChooser();
		fc.setTitle("Buscar imagen");
		fc.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("All Images", "*.jpg",
						"*.png"),
						new FileChooser.ExtensionFilter("JPG(.jpg)", "*.jpg"),
						new FileChooser.ExtensionFilter("PNG(.png)", "*.png"));
		file = fc.showOpenDialog(new Stage());

		BufferedImage img;
		try {

			if (file != null) {
				img = ImageIO.read(file);
				Image img1 = SwingFXUtils.toFXImage(img, null);
				imageViewProducto.setImage(img1);
				imageViewProducto.setFitHeight(100);
				imageViewProducto.setFitWidth(200);
			}
		} catch (IOException e) {
		}

	}
	
	public void GuardarBrnPressed(ActionEvent event) throws SQLException {
		pst = null;
		rs = null;
		conn = null;
		if (textFieldNombre.getText().isEmpty() || textFieldPrecio.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor ingrese el nombre y precio del producto");
			alert.showAndWait();
		} else {
			conn = ConnectionDB.Conectar();
			String query = "UPDATE Productos SET Nombre = ? ,Imagen = ?,Precio = ? WHERE Productos.Nombre = ?";
			String query2 = "UPDATE Productos SET Nombre = ?,Precio = ? WHERE Productos.Nombre = ?";
			if (file != null) {
				try {

					pst = conn.prepareStatement(query);
					pst.setString(1, textFieldNombre.getText());
					FileInputStream fin = new FileInputStream(file);
					pst.setBinaryStream(2, fin, (int) file.length());
					pst.setDouble(3, Double.valueOf(textFieldPrecio.getText()));
					pst.setString(4, comboBoxProductos.getValue());
					pst.executeUpdate();
				}

				catch (Exception e) {
					e.printStackTrace();
				}

				finally {
					pst.close();
					conn.close();
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Datos guardados");
					alert.setHeaderText("Éxito");
					alert.setContentText("Datos guardados");
					alert.showAndWait();
					Stage stage = (Stage) textFieldNombre.getScene()
							.getWindow();
					stage.close();
				}
			} else {
				try {

					pst = conn.prepareStatement(query2);
					pst.setString(1, textFieldNombre.getText());
					pst.setDouble(2, Double.valueOf(textFieldPrecio.getText()));
					pst.setString(3, comboBoxProductos.getValue());
					pst.executeUpdate();
				}

				catch (Exception e) {
					e.printStackTrace();
				}

				finally {
					pst.close();
					conn.close();
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Datos guardados");
					alert.setHeaderText("Éxito");
					alert.setContentText("Datos guardados");
					alert.showAndWait();
					Stage stage = (Stage) textFieldNombre.getScene()
							.getWindow();
					stage.close();
				}
			}
		}
	}
	
	public void PopulateProductoSelected() {
		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		InputStream is;
		String query = "SELECT Imagen,Nombre,Precio FROM Productos WHERE Productos.Nombre = ?";
		try {
			pst = conn.prepareStatement(query);
			pst.setString(1, comboBoxProductos.getValue());
			rs = pst.executeQuery();

			while (rs.next()) {
				textFieldNombre.setText(rs.getString("Nombre"));
				textFieldPrecio.setText(String.valueOf(rs.getDouble("Precio")));
				is = rs.getBinaryStream("Imagen");
				imageViewProducto.setImage(new Image(is, 200, 100, true, true));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
