package application;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

public class MainMenuController implements Initializable {

	@FXML
	ListView<Integer> listViewCategorias = new ListView<Integer>();
	@FXML
	TilePane tilePaneproductos = new TilePane();
	@FXML
	CheckBox checkBoxLocal = new CheckBox();
	@FXML
	CheckBox checkBoxDelivery = new CheckBox();
	@FXML
	TextField textFieldOrdenEsp = new TextField();
	@FXML
	TableView<String> tableViewPedido = new TableView<String>();
	ObservableList<Integer> countCategorias = FXCollections
			.observableArrayList();
	ObservableList<Integer> IdCategorias = FXCollections
			.observableArrayList();
	ObservableList<Image> imgCategorias = FXCollections.observableArrayList();
	ObservableList<String> nombreCategorias = FXCollections
			.observableArrayList();
	ObservableList<ImageView> imgProductos = FXCollections
			.observableArrayList();
	ObservableList<String> nombreProductos = FXCollections
			.observableArrayList();

	Label label = new Label();
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	static InputStream is;
	int aux = 0;
	int Categoria = 1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		listViewCategorias.setId("listviewPedidos");
		conn = ConnectionDB.Conectar();
		populateCategorias();
		populateProductos();

	}

	public void populateCategorias() {

		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		String query = "SELECT Imagen,Nombre,Id FROM Categorias ";
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();

			while (rs.next()) {

				is = rs.getBinaryStream("Imagen");
				imgCategorias.add(new Image(is));
				IdCategorias.add(rs.getInt("Id"));
				nombreCategorias.add(rs.getString("Nombre"));
				countCategorias.add(aux);
				aux++;

			}

			listViewCategorias.setItems(countCategorias);
			listViewCategorias.setCellFactory(param -> new ListCell<Integer>() {

				@Override
				public void updateItem(Integer name, boolean empty) {
					ImageView imageview = new ImageView();
					super.updateItem(name, empty);
					if (empty) {
						setText(null);
						setGraphic(null);
					} else {

						if (name.equals(countCategorias.get(name)))
							imageview.setImage(imgCategorias.get(name));

						setText(null);
						setGraphic(imageview);
					}
				}
			});
			listViewCategorias.getSelectionModel().selectedItemProperty()
					.addListener(new ChangeListener<Integer>() {

						@Override
						public void changed(
								ObservableValue<? extends Integer> observable,
								Integer oldValue, Integer newValue) {
							Categoria = IdCategorias.get(newValue);
							populateProductos();
						}
					});

		} catch (Exception e) {
			System.err.println(e);
		} finally {
			try {
				rs.close();
				pst.close();
			} catch (Exception e2) {
				System.err.println(e2);

			}
		}

	}

	public void populateProductos() {
		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		String query2 = "SELECT Categorias.Nombre,Productos.Precio,Productos.Imagen  FROM Productos "
				+ "INNER JOIN Categorias  ON Productos.Categoria_id=Categorias.Id AND Productos.Categoria_id=?";
		// String query = "SELECT Imagen,Nombre,Precio FROM Productos WHERE";

		try {
			if (tilePaneproductos.getChildren().size() > 0) {
				tilePaneproductos.getChildren().clear();
				imgProductos.clear();
			}
			// System.out.println("pst: " +pst.isClosed());
			// System.out.println("rs: " +rs.isClosed());
			// System.out.println("conn: " +conn.isClosed());
			pst = conn.prepareStatement(query2);
			pst.setString(1, String.valueOf(Categoria));
			rs = pst.executeQuery();

			while (rs.next()) {
				if (rs.getBinaryStream("Imagen") == null) {
					imgProductos.add(new ImageView(new Image(
							"img/logoAntojos5.jpg", 100, 100, true, true)));
				} else {
					is = rs.getBinaryStream("Imagen");
					imgProductos.add(new ImageView(new Image(is, 100, 100,
							true, true)));
				}
				// System.out.println("Recibe datos");
				// System.out.println("pst: " +pst.isClosed());
				// System.out.println("rs: " +rs.isClosed());
				// System.out.println("conn: " +conn.isClosed());
			}

			for (int i = 0; i < imgProductos.size(); i++) {
				tilePaneproductos.getChildren().add(imgProductos.get(i));
			}

		} catch (Exception e) {
			System.err.println(e);
			// TODO: handle exception
		} finally {
			try {
				// rs.close();
				// pst.close();
			} catch (Exception e2) {
				System.err.println(e2);

			}
		}
	}
}
