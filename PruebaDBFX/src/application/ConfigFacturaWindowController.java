package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/*CONTROLADOR - VENTANA DE CONFIGURACION DE FACTURA*/
public class ConfigFacturaWindowController implements Initializable {

	@FXML
	TextField textFieldNombreRest;
	@FXML
	TextField textFieldSucursal;
	@FXML
	TextField textFieldDireccion;
	@FXML
	TextField textFieldTelefono;
	@FXML
	TextField textFieldNit;
	@FXML
	TextField textFieldNit2;
	@FXML
	TextField textFieldNautorizacion;
	@FXML
	TextField textFieldNautorizacion2;
	@FXML
	TextField textfieldActividad;
	@FXML
	TextField textFieldNFactura;
	@FXML
	TextField textFieldNFactura2;
	@FXML
	TextField textFieldLlaveDosificacion;
	@FXML
	TextField textFieldLlaveDosificacion2;
	@FXML
	TextField textFieldExtra;
	@FXML
	Label labelNit;
	ObservableList<String> lista = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			File f = new File("ConfigFactura.ser");
			if (f.exists() && !f.isDirectory()) {
				read();
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void btnguardarPressed(ActionEvent event) throws IOException {
		
		if(checkDatos(new ActionEvent())){
			try{
				lista.clear();
				lista.add(textFieldNombreRest.getText().toString());
				lista.add(textFieldSucursal.getText());
				lista.add(textFieldDireccion.getText());
				lista.add(textFieldTelefono.getText());
				lista.add(textFieldNit.getText());
				lista.add(textFieldNit2.getText());
				lista.add(textFieldNautorizacion.getText());
				lista.add(textFieldNautorizacion2.getText());
				lista.add(textfieldActividad.getText());
				lista.add(textFieldNFactura.getText());
				lista.add(textFieldNFactura2.getText());
				lista.add(textFieldLlaveDosificacion.getText());
				lista.add(textFieldLlaveDosificacion2.getText());
				lista.add(textFieldExtra.getText());
				write();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Configuración de facturación");
				alert.setHeaderText("Éxito");
				alert.setContentText("Datos guardados");
				alert.showAndWait();
				Stage stage  = (Stage)textfieldActividad.getScene().getWindow();
				stage.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}	
		}
		
	}
	
	public void btnPruebasSFCPressed (ActionEvent event) throws IOException {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(
				"/application/PruebasSFCWindow.fxml"));
		Scene scene = new Scene(root, 630, 540);
		scene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
	
	public void write() throws IOException {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			// write object to file
			fout = new FileOutputStream("ConfigFactura.ser");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(new ArrayList<String>(lista));
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

	
	public void read() throws IOException, ClassNotFoundException {
		FileInputStream fin;
		try {
			fin = new FileInputStream("ConfigFactura.ser");
			ObjectInputStream ois = new ObjectInputStream(fin);
			List<String> list = (List<String>) ois.readObject();
			lista = FXCollections.observableList(list);
			textFieldNombreRest.setText(lista.get(0));
			textFieldSucursal.setText(lista.get(1));
			textFieldDireccion.setText(lista.get(2));
			textFieldTelefono.setText(lista.get(3));
			textFieldNit.setText(lista.get(4));
			textFieldNit2.setText(lista.get(5));
			textFieldNautorizacion.setText(lista.get(6));
			textFieldNautorizacion2.setText(lista.get(7));
			textfieldActividad.setText(lista.get(8));
			textFieldNFactura.setText(lista.get(9));
			textFieldNFactura2.setText(lista.get(10));
			textFieldLlaveDosificacion.setText(lista.get(11));
			textFieldLlaveDosificacion2.setText(lista.get(12));
			textFieldExtra.setText(lista.get(13));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		}

	}

	public boolean checkDatos (ActionEvent event){
		boolean output = true;
		String error="";
		
		if(textFieldNit.getText().isEmpty() || textFieldNit2.getText().isEmpty())
		{
			error += "Completa correctamente los datos de NIT.";
		}else{
			if(!(textFieldNit.getText().equals(textFieldNit2.getText())))
			{
				error += "Los datos de NIT no son iguales";
			}
		}
		if(textFieldNautorizacion.getText().isEmpty() || textFieldNautorizacion2.getText().isEmpty())
		{
			error+="\nCompleta correctamente los datos de Num. de Autorización";
		}else{
			if(!(textFieldNautorizacion.getText().equals(textFieldNautorizacion2.getText())))
			{
				error += "\nLos datos de Num. de autorizacion no son iguales";
			}
		}
		if(textFieldNFactura.getText().isEmpty() || textFieldNFactura2.getText().isEmpty())
		{
			error+="\nCompleta correctamente los datos de Num. de Factura";
		}else{
			if(!(textFieldNFactura.getText().equals(textFieldNFactura2.getText())))
			{
				error += "\nLos datos de Num. de factura no son iguales";
			}
		}
		
		if(textFieldLlaveDosificacion.getText().isEmpty() || textFieldLlaveDosificacion2.getText().isEmpty())
		{
			error+="\nCompleta correctamente los datos de Llave de dosificación";
		}else{
			if(!(textFieldLlaveDosificacion.getText().equals(textFieldLlaveDosificacion2.getText())))
			{
				error += "\nLos datos de Llave de dosificación no son iguales";
			}
		}
		
		if(error != "")
		{
			output = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar");
			alert.setContentText(error);
			alert.showAndWait();
		}
		
		
		return output;
	}
}
