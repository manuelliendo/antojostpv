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

public class EditCategoriaWindowController implements Initializable {

	@FXML
	ComboBox<String> listaCategorias;
	@FXML
	TextField textFieldNombre;
	@FXML
	ImageView imageViewCategoria = new ImageView();
	ObservableList<String> listaNombre = FXCollections.observableArrayList();
	ObservableList<Image> listaImageView = FXCollections.observableArrayList();
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
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
		InputStream is;
		String query = "SELECT Imagen,Nombre FROM Categorias ";
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();

			while (rs.next()) {
				listaNombre.add(rs.getString("Nombre"));
				is = rs.getBinaryStream("Imagen");
				listaImageView.add(new Image(is, 200, 100, true, true));
			}
			listaCategorias.setItems(listaNombre);
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
				imageViewCategoria.setImage(img1);
				imageViewCategoria.setFitHeight(100);
				imageViewCategoria.setFitWidth(200);
			}
		} catch (IOException e) {
		}

	}

	public void ComboBoxSelected(ActionEvent event) {
		int aux = 0;
		aux = listaNombre.indexOf(listaCategorias.getValue());
		textFieldNombre.setText(listaNombre.get(aux));
		imageViewCategoria.setImage(listaImageView.get(aux));
		imageViewCategoria.setFitHeight(100);
		imageViewCategoria.setFitHeight(200);
	}

	public void GuardarBrnPressed(ActionEvent event) throws SQLException {
		pst = null;
		rs = null;
		conn = null;
		if (textFieldNombre.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor ingrese de la categoria");
			alert.showAndWait();
		} else {
			conn = ConnectionDB.Conectar();
			String query = "UPDATE Categorias SET Nombre = ? ,Imagen = ? WHERE Categorias.Nombre = ?";
			String query2 = "UPDATE Categorias SET Nombre = ? WHERE Categorias.Nombre = ?";
			if (file != null) {
				try {

					pst = conn.prepareStatement(query);
					pst.setString(1, textFieldNombre.getText());
					FileInputStream fin = new FileInputStream(file);
					pst.setBinaryStream(2, fin, (int) file.length());
					pst.setString(3, listaCategorias.getValue());
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
					pst.setString(2, listaCategorias.getValue());
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

}
