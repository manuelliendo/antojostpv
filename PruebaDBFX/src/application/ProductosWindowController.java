package application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProductosWindowController implements Initializable {

	@FXML
	Button btnProductos;
	@FXML
	Button btnInventarios;
	@FXML
	Button btnEstadisticas;
	@FXML
	Button btnPaP;
	@FXML
	Button btnConfig;
	@FXML
	Label label = new Label();
	@FXML
	ListView<Categoria> listViewCategorias = new ListView<Categoria>();
	@FXML
	TilePane tilePaneproductos = new TilePane();
	@FXML
	TableView<Producto> receta;
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
		if(Usuario.getPermiso() == 2){
			btnConfig.setDisable(true);
			
		}else{
			if(Usuario.getPermiso() == 3){
				btnConfig.setDisable(true);
				btnProductos.setDisable(true);
				btnEstadisticas.setDisable(true);
				}
		}
		conn = ConnectionDB.Conectar();
		populateCategorias();
		populateProductos();
		
	}

	public void populateCategorias() {

		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		categorias.clear();
		aux = 0;
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

									HBox.setHgrow(box, Priority.ALWAYS);
									box.getChildren().addAll(imageview, label);
									box.setAlignment(Pos.CENTER_LEFT);
									box.setId("boxCategoria");
									box.autosize();
									label.autosize();
									imageview.autosize();
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
		productos.clear();
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
				Label l = new Label(productos.get(i).getNombre());
				p.setId("productos");
				p.setAlignment(Pos.CENTER);
				p.getChildren().addAll(productos.get(i).getImagen(),l);
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
										System.out.println("poblar recetas");

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

	public void btnPedidosPressed(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) listViewCategorias.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/MainMenu.fxml"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Scene scene = new Scene(root, width, height - 50);
		scene.getStylesheets().add(
				getClass().getResource("Pedidos.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

	public void btnConfigPressed (ActionEvent event) throws IOException{
		Stage stage = (Stage)listViewCategorias.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/ConfigWindow.fxml"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().add(
				getClass().getResource("Config.css").toExternalForm());
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
	}

	public void btnEstadisticasPressed(ActionEvent event) throws IOException {
		Stage stage = (Stage)listViewCategorias.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/EstadisticasWindow.fxml"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().add(
				getClass().getResource("Estadisticas.css").toExternalForm());
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
	}
	
	public void btnInventarioPressed(ActionEvent event) throws IOException{
		Stage stage = (Stage)listViewCategorias.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/InventarioWindow.fxml"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().add(
				getClass().getResource("Inventario.css").toExternalForm());
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
	}
	
	public void agregarCategoriaBtnPressed(ActionEvent event) throws IOException {

		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/AddCategoriaWindow.fxml"));
		Scene scene = new Scene(root, 300, 400);
		scene.getStylesheets().add(
				getClass().getResource("Add.css").toExternalForm());
		stage.setTitle("Agregar categoria nueva");
		try{
			String dir = System.getProperty("user.dir");
//			System.out.println(dir + "\\icon.png");
		stage.getIcons().add(new Image("file:" + dir + "\\icon.png"));
		
		}catch(Exception e){
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.show();
	}

	public void editarCategoriaBtnPressed(ActionEvent event) throws IOException {

		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/EditCategoriaWindow.fxml"));
		Scene scene = new Scene(root, 300, 400);
		scene.getStylesheets().add(
				getClass().getResource("Add.css").toExternalForm());
		stage.setTitle("Editar categoria");
		try{
			String dir = System.getProperty("user.dir");
//			System.out.println(dir + "\\icon.png");
		stage.getIcons().add(new Image("file:" + dir + "\\icon.png"));
		
		}catch(Exception e){
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.show();
		
	}

	public void eliminarCategoriaBtnPressed(ActionEvent event) throws IOException {

		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/DeleteCategoriaWindow.fxml"));
		Scene scene = new Scene(root, 300, 200);
		scene.getStylesheets().add(
				getClass().getResource("Add.css").toExternalForm());
		stage.setTitle("Eliminar categoria");
		try{
			String dir = System.getProperty("user.dir");
//			System.out.println(dir + "\\icon.png");
		stage.getIcons().add(new Image("file:" + dir + "\\icon.png"));
		
		}catch(Exception e){
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.show();
	}
	
	public void agregarProductoBtnPressed(ActionEvent event) throws IOException {

		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/AddProductoWindow.fxml"));
		Scene scene = new Scene(root, 300, 400);
		scene.getStylesheets().add(
				getClass().getResource("Add.css").toExternalForm());
		stage.setTitle("Agregar producto nuevo");
		try{
			String dir = System.getProperty("user.dir");
//			System.out.println(dir + "\\icon.png");
		stage.getIcons().add(new Image("file:" + dir + "\\icon.png"));
		
		}catch(Exception e){
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.showAndWait();
		populateCategorias();
	}
	
	public void EditarProductoBtnPressed(ActionEvent event) throws IOException {

		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/EditProductoWindow.fxml"));
		Scene scene = new Scene(root, 320, 450);
		scene.getStylesheets().add(
				getClass().getResource("Add.css").toExternalForm());
		stage.setTitle("Editar producto");
		try{
			String dir = System.getProperty("user.dir");
//			System.out.println(dir + "\\icon.png");
		stage.getIcons().add(new Image("file:" + dir + "\\icon.png"));
		
		}catch(Exception e){
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.showAndWait();
		populateCategorias();
	}
	public void EliminarProductoBtnPressed(ActionEvent event) throws IOException {

		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/DeleteProductoWindow.fxml"));
		Scene scene = new Scene(root, 300, 210);
		scene.getStylesheets().add(
				getClass().getResource("Add.css").toExternalForm());
		stage.setTitle("Eliminar producto");
		try{
			String dir = System.getProperty("user.dir");
//			System.out.println(dir + "\\icon.png");
		stage.getIcons().add(new Image("file:" + dir + "\\icon.png"));
		
		}catch(Exception e){
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.showAndWait();
		populateCategorias();
	}
	
	
}
