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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

public class MainMenuController implements Initializable {

	@FXML
	ListView<Categoria> listViewCategorias = new ListView<Categoria>();
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

	ObservableList<Categoria> categorias = FXCollections.observableArrayList();
	ObservableList<Producto> productos = FXCollections.observableArrayList();
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

				if (rs.getBinaryStream("Imagen") == null) {
					categorias.add(new Categoria(rs.getString("Nombre"), rs
							.getInt("Id"), aux));
					aux++;
				} else {
					is = rs.getBinaryStream("Imagen");
					categorias.add(new Categoria(rs.getString("Nombre"), rs
							.getInt("Id"), new Image(is), aux));
					aux++;
				}

			}

			listViewCategorias.setItems(categorias);
			listViewCategorias
					.setCellFactory(param -> new ListCell<Categoria>() {

						@Override
						public void updateItem(Categoria name, boolean empty) {
							ImageView imageview = new ImageView();

							super.updateItem(name, empty);
							if (empty) {
								setText(null);
								setGraphic(null);
							} else {

								if (name.getCount().equals(
										categorias.get(name.getCount())
												.getCount())) {
									imageview.setImage(categorias.get(
											name.getCount()).getImagen());
								}
								setText(null);
								setGraphic(imageview);
							}
						}
					});
			listViewCategorias.getSelectionModel().selectedItemProperty()
					.addListener(new ChangeListener<Categoria>() {

						@Override
						public void changed(
								ObservableValue<? extends Categoria> observable,
								Categoria oldValue, Categoria newValue) {
							Categoria = newValue.getId();
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
		String query2 = "SELECT Productos.Nombre,Productos.Precio,Productos.Imagen  FROM Productos "
				+ "INNER JOIN Categorias  ON Productos.Categoria_id=Categorias.Id AND Productos.Categoria_id=?";

		try {
			if (tilePaneproductos.getChildren().size() > 0) {
				tilePaneproductos.getChildren().clear();
				productos.clear();
			}
			pst = conn.prepareStatement(query2);
			pst.setString(1, String.valueOf(Categoria));
			rs = pst.executeQuery();

			while (rs.next()) {
				if (rs.getBinaryStream("Imagen") == null) {
					productos.add(new Producto(rs.getString("Nombre"), Float
							.valueOf(rs.getString("Precio"))));

				} else {
					is = rs.getBinaryStream("Imagen");
					productos.add(new Producto(rs.getString("Nombre"),
							new ImageView(new Image(is, 100, 100, true, true)),
							Float.valueOf(rs.getString("Precio"))));

				}
			}

			for (int i = 0; i < productos.size(); i++) {

				tilePaneproductos.getChildren().add(
						productos.get(i).getImagen());
				productos.get(i).getImagen()
						.setOnMouseClicked(new EventHandler<MouseEvent>() {

							@Override
							public void handle(MouseEvent event) {

								for (int i = 0; i < productos.size(); i++) {
									ImageView iv = (ImageView) event
											.getSource();
									if (iv.equals(productos.get(i).getImagen()))
										System.out.println(productos.get(i)
												.getNombre());

								}

							}

						});
			}

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
	
	
}
