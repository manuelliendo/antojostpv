package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
import javafx.scene.layout.TilePane;
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
	TextField textFieldOrdenEsp = new TextField();
	
	
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

	
	ObservableList<Categoria> categorias = FXCollections.observableArrayList();
	ObservableList<Producto> productos = FXCollections.observableArrayList();
	ObservableList<ProductoSimple> orden = FXCollections.observableArrayList();
	
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	static InputStream is;
	int aux = 0;
	int Categoria = 1;
	public static String adress = "";

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
										RecibirOrden(productos.get(i).getNombre(),productos.get(i).getPrecio());

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

	public void RecibirOrden(String nombre,Float precio) {
		boolean encontrado = false;
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
		prodEspecial.setCellValueFactory(new PropertyValueFactory<ProductoSimple, String>("Nombre"));
		pedidoEspecial.setCellFactory(TextFieldTableCell.forTableColumn());
		pedidoEspecial.setOnEditCommit(
		    new EventHandler<CellEditEvent<ProductoSimple, String>>() {
		        @Override
		        public void handle(CellEditEvent<ProductoSimple, String> t) {
		        	
		   
		        }
		    }
		);
		tableViewPedido.refresh();

	}

	public void btnDeshacerPressed() {
		for(int i=0;i<orden.size();i++)
		{
			orden.get(i).resetCantidad();
		}
		orden.clear();
		tableViewPedido.refresh();
		total.setText("0 Bs.");

	}

	public void btnOkPressed() throws IOException {
		
		write(); 
		Stage primaryStage  = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/application/Factura.fxml"));
		Scene scene = new Scene(root,500,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void write () throws IOException{
	 	FileOutputStream fout = null;
	 	ObjectOutputStream oos = null;
		 try {
		        // write object to file
			 	fout = new FileOutputStream("nombre.ser");
		        oos = new ObjectOutputStream(fout);
		        oos.writeObject(new ArrayList<ProductoSimple>(orden));
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();}
		 finally {

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
