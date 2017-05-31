package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.glass.events.KeyEvent;

import javafx.animation.PathTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FacturaController implements Initializable {

	@FXML
	TextField nit;
	@FXML
	TextField nombre;
	@FXML
	TextField montoPagado;
	@FXML
	TableView<ProductoSimple> ordenTotal;
	@FXML
	TableColumn<ProductoSimple, String> columnProducto;
	@FXML
	TableColumn<ProductoSimple, String> columnEspecial;
	@FXML
	Label total;
	@FXML
	Label cambio;
	ObservableList<ProductoSimple> orden = FXCollections.observableArrayList();
	Float auxCuenta = (float) 0;
	Float auxCambio = (float) 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			read();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ordenTotal.setItems(orden);
			columnProducto
					.setCellValueFactory(new PropertyValueFactory<ProductoSimple, String>(
							"Nombre"));
			columnEspecial
					.setCellValueFactory(new PropertyValueFactory<ProductoSimple, String>(
							"especial"));
		}

		for (int i = 0; i < orden.size(); i++) {
			auxCuenta += orden.get(i).getPrecioTotal();
		}

		total.setText(String.valueOf(auxCuenta) + "Bs.");
	}

	
	public void emitirFactura() {

	}

	public void getCambio(){
		Float auxMonto;
		auxMonto = Float.valueOf(montoPagado.getText());
		auxCambio = auxMonto - auxCuenta;
		
		cambio.setText(String.valueOf(auxCambio));
	}
	public void read() throws IOException, ClassNotFoundException {
		FileInputStream fin;
		try {
			fin = new FileInputStream("nombre.ser");
			ObjectInputStream ois = new ObjectInputStream(fin);
			List<ProductoSimple> list = (List<ProductoSimple>) ois.readObject();
			orden = FXCollections.observableList(list);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
