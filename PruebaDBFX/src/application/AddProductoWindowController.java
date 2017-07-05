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

public class AddProductoWindowController implements Initializable{

	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	@FXML
	TextField textFieldNombre;
	@FXML
	TextField textFieldPrecio;
	@FXML
	ImageView imageViewCategoria = new ImageView();
	@FXML
	ComboBox<String> comboBoxNombre = new ComboBox<String>();
	ObservableList<String> listaNombre = FXCollections.observableArrayList();
	ObservableList<Integer> listaId = FXCollections.observableArrayList();
	FileChooser fc;
	File file;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		PopulateComboBox();
		
	}
	public void PopulateComboBox() {
		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		String query = "SELECT Nombre,Id FROM Categorias ";
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();

			while (rs.next()) {
				listaNombre.add(rs.getString("Nombre"));
				listaId.add(rs.getInt("Id"));
			}
			comboBoxNombre.setItems(listaNombre);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void BuscarBtnPressed(ActionEvent event) {
		fc = new FileChooser();
		fc.setTitle("Buscar imagen");
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All Images", "*.jpg","*.png"),
				new FileChooser.ExtensionFilter("JPG(.jpg)", "*.jpg"),
				new FileChooser.ExtensionFilter("PNG(.png)", "*.png"));
		file = fc.showOpenDialog(new Stage());

		BufferedImage img;
		try {

			if(file!=null){
			img = ImageIO.read(file);
			Image img1 = SwingFXUtils.toFXImage(img, null);
			imageViewCategoria.setImage(img1);
			imageViewCategoria.setFitHeight(100);
			imageViewCategoria.setFitWidth(100);
			}
		} catch (IOException e) {
		}

	}

	public void GuardarBrnPressed(ActionEvent event) throws SQLException {
		pst = null;
		rs = null;
		conn = null;
		if (textFieldNombre.getText().isEmpty() || textFieldPrecio.getText().isEmpty() || file == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor ingrese el nombre, precio e imagen de la categoria");
			alert.showAndWait();
		} else {
			conn = ConnectionDB.Conectar();
			String query = "INSERT INTO Productos (Nombre,Imagen,Categoria_Id,Precio) VALUES(?,?,?,?)";
			try {

				pst = conn.prepareStatement(query);
				pst.setString(1, textFieldNombre.getText());
				FileInputStream fin = new FileInputStream(file);
				pst.setBinaryStream(2, fin, (int) file.length());
				pst.setInt(3, listaId.get(listaNombre.indexOf(comboBoxNombre.getValue())));
				pst.setDouble(4, Double.valueOf(textFieldPrecio.getText()));
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
				Stage stage = (Stage) textFieldNombre.getScene().getWindow();
				stage.close();
			}
		}
	}

	
}
