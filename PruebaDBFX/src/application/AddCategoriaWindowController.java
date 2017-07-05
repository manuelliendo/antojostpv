package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class AddCategoriaWindowController {

	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	@FXML
	TextField textFieldNombre;
	@FXML
	ImageView imageViewCategoria = new ImageView();
	FileChooser fc;
	File file;

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
		if (textFieldNombre.getText().isEmpty() || file == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor ingrese el nombre e imagen de la categoria");
			alert.showAndWait();
		} else {
			conn = ConnectionDB.Conectar();
			String query = "INSERT INTO Categorias (Nombre,Imagen) VALUES(?,?)";
			try {

				pst = conn.prepareStatement(query);
				pst.setString(1, textFieldNombre.getText());
				FileInputStream fin = new FileInputStream(file);
				pst.setBinaryStream(2, fin, (int) file.length());
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
