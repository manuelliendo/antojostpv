package application;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuController implements Initializable {

	@FXML
	ListView<Categoria> listViewCategorias = new ListView<Categoria>();
	@FXML
	TilePane tilePaneproductos = new TilePane();
	@FXML
	RadioButton radioButtonLocal = new RadioButton();
	@FXML
	RadioButton radioButtonDelivery = new RadioButton();
	@FXML
	TableView<ProductoSimple> tableViewPedido = new TableView<ProductoSimple>();
	@FXML
	TableView<ProductoSimple> tableViewPedidoEspecial = new TableView<ProductoSimple>();
	@FXML
	TableColumn<ProductoSimple, String> prod;
	@FXML
	TableColumn<ProductoSimple, Integer> cant;
	@FXML
	TableColumn<ProductoSimple, Float> precioUnit;
	@FXML
	TableColumn<ProductoSimple, Float> precioTotal;
	@FXML
	TableColumn<ProductoSimple, String> prodEspecial;
	@FXML
	TableColumn<ProductoSimple, String> pedidoEspecial;
	@FXML
	Label total;
	@FXML
	AnchorPane root;

	ObservableList<Categoria> categorias = FXCollections.observableArrayList();
	ObservableList<Producto> productos = FXCollections.observableArrayList();
	ObservableList<ProductoSimple> orden = FXCollections.observableArrayList();

	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	InputStream is;
	int aux = 0;
	int Categoria = 1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
							.getInt("Id"), new Image(is, 200, 100, true, true),
							aux));
					aux++;
				}

			}

			listViewCategorias.setItems(categorias);
			listViewCategorias
					.setCellFactory(param -> new ListCell<Categoria>() {

						@Override
						public void updateItem(Categoria name, boolean empty) {
							HBox box = new HBox();
							ImageView imageview = new ImageView();
							Label label = new Label();
							super.updateItem(name, empty);
							if (empty) {
								setText(null);
								setGraphic(null);
							} else {

								if (name.getCount().equals(
										categorias.get(name.getCount())
												.getCount())) {
									label.setText(categorias.get(
											name.getCount()).getNombre());
									imageview.setImage(categorias.get(
											name.getCount()).getImagen());

									box.getChildren().addAll(imageview,label);
									box.setAlignment(Pos.CENTER_LEFT);
									box.setId("boxCategoria");
									label.setId("labelCategoria");
									setText(null);
									setGraphic(box);
								}
								
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

				VBox p = new VBox();
				p.setId("productos");
				p.setAlignment(Pos.CENTER);
				p.getChildren().add(productos.get(i).getImagen());
				// tilePaneproductos.getChildren().add(
				// productos.get(i).getImagen());
				tilePaneproductos.getChildren().add(p);
				tilePaneproductos.setId("panelProductos");
				productos.get(i).getImagen()
						.setOnMouseClicked(new EventHandler<MouseEvent>() {

							@Override
							public void handle(MouseEvent event) {

								for (int i = 0; i < productos.size(); i++) {

									ImageView iv = (ImageView) event
											.getSource();
									if (iv.equals(productos.get(i).getImagen()))
										RecibirOrden(productos.get(i)
												.getNombre(), productos.get(i)
												.getPrecio());

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

	public void RecibirOrden(String nombre, Float precio) {
		boolean encontrado = false;
		Stage stage = (Stage) root.getScene().getWindow();
		stage.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if (newValue == false) {
					btnDeshacerPressed();
				}

			}

		});
		float auxtotal = 0;
		if (orden.size() == 0) {
			orden.add(new ProductoSimple(nombre, precio));
			for (int i = 0; i < orden.size(); i++) {
				auxtotal += orden.get(i).getPrecioTotal();

			}
		} else {
			for (int i = 0; i < orden.size(); i++) {
				if (nombre.equals(orden.get(i).getNombre())) {
					orden.get(i).addCantidad();
					encontrado = true;
					break;
				}

			}
			if (encontrado == false) {
				orden.add(new ProductoSimple(nombre, precio));
			}

			for (int i = 0; i < orden.size(); i++) {
				auxtotal += orden.get(i).getPrecioTotal();
			}

		}
		total.setText(String.valueOf(auxtotal) + " Bs.");
		prod.setCellValueFactory(new PropertyValueFactory<ProductoSimple, String>(
				"Nombre"));
		cant.setCellValueFactory(new PropertyValueFactory<ProductoSimple, Integer>(
				"Cantidad"));
		precioUnit
				.setCellValueFactory(new PropertyValueFactory<ProductoSimple, Float>(
						"Precio"));
		precioTotal
				.setCellValueFactory(new PropertyValueFactory<ProductoSimple, Float>(
						"precioTotal"));
		tableViewPedido.setItems(orden);
		tableViewPedidoEspecial.setItems(orden);
		prodEspecial
				.setCellValueFactory(new PropertyValueFactory<ProductoSimple, String>(
						"Nombre"));
		pedidoEspecial.setCellFactory(TextFieldTableCell.forTableColumn());

		pedidoEspecial
				.setOnEditStart(new EventHandler<CellEditEvent<ProductoSimple, String>>() {

					@Override
					public void handle(CellEditEvent<ProductoSimple, String> t) {
						System.out.println(t.getTablePosition().getRow());
						((ProductoSimple) t.getTableView().getItems()
								.get(t.getTablePosition().getRow()))
								.setEspecial(t.getNewValue());
						;

					}

				});

		tableViewPedido.refresh();
		tableViewPedidoEspecial.refresh();

	}

	public void btnDeshacerPressed() {
		for (int i = 0; i < orden.size(); i++) {
			orden.get(i).resetCantidad();
		}
		orden.clear();
		tableViewPedido.refresh();
		total.setText("0 Bs.");

	}

	public void btnOkPressed() throws IOException {

		write();
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/Factura.fxml"));
		Scene scene = new Scene(root, 500, 600);
		scene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void write() throws IOException {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			// write object to file
			fout = new FileOutputStream("nombre.ser");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(new ArrayList<ProductoSimple>(orden));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {

			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
