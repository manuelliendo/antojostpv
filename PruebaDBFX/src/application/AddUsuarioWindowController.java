package application;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AddUsuarioWindowController implements Initializable{
	
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	@FXML
	TextField textfieldNombre;
	@FXML
	TextField textfieldApellido;
	@FXML
	PasswordField textfieldContrasena;
	@FXML
	PasswordField textfieldContrasena2;
	@FXML
	ComboBox<String> comboboxCargo;
	ObservableList<String> listaCargos= FXCollections.observableArrayList("Administrador","Supervisor","Cajero");
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		PopulateCargos();
		
	}
	public void PopulateCargos(){
		comboboxCargo.setItems(listaCargos);
	}
	
	public int ComboboxSelected(){
		int salida=0;
		switch(comboboxCargo.getValue()){
		case "Administrador":
			salida=1;
			break;
		case "Supervisor":
			salida=2;
			break;
		case "Cajero":
			salida=3;
			break;
			
		}
		return salida;
	}
	
	public void BtnGuardarPressed(ActionEvent event) throws SQLException{
		pst = null;
		rs = null;
		conn = null;
		if (textfieldNombre.getText().isEmpty()
				|| textfieldApellido.getText().isEmpty()
				|| textfieldContrasena.getText().isEmpty()
				|| textfieldContrasena2.getText().isEmpty()
				|| comboboxCargo.getValue().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor ingrese todos los datos correctamente.");
			alert.showAndWait();
			
		} else if(!textfieldContrasena.getText().equals(textfieldContrasena2.getText())){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar");
			alert.setHeaderText("Error");
			alert.setContentText("Las contraseñas no coinciden.");
			alert.showAndWait();
		} 
		else {
			conn = ConnectionDB.Conectar();
			String query = "INSERT INTO Usuarios (Nombre,Apellido,Usuario,Password,Acceso_id) VALUES(?,?,?,?,?)";
			try {

				pst = conn.prepareStatement(query);
				pst.setString(1, textfieldNombre.getText());
				pst.setString(2, textfieldApellido.getText());
				pst.setString(3, textfieldNombre.getText()+textfieldApellido.getText());
				pst.setString(4, textfieldContrasena.getText());
				pst.setInt(5, ComboboxSelected());
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
				Stage stage = (Stage) textfieldNombre.getScene().getWindow();
				stage.close();
			}
		}
	}
	
}
