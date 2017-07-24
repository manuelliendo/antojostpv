package application;

import java.awt.Dimension;
import java.awt.RenderingHints.Key;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class MainController implements Initializable {

	@FXML
	TextField textFieldUsuario;
	@FXML
	TextField textFieldPassword;
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	int acceso = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		conn = ConnectionDB.Conectar();
	}

	@FXML
	public void Login(ActionEvent event) {
//		String query2 = "SELECT * FROM Usuarios";
		String query = "SELECT Usuario,Password,Acceso_id FROM Usuarios WHERE (Usuario=? and Password=?)";
		try {
			boolean UsuarioExiste = false;
			pst = conn.prepareStatement(query);
			pst.setString(1, textFieldUsuario.getText());
			pst.setString(2, textFieldPassword.getText());
			rs = pst.executeQuery();
			while(rs.next())
			{
				UsuarioExiste = true;
				acceso = rs.getInt("Acceso_id");
			}
			if(UsuarioExiste)
			{
				Usuario.setNombreUsuario(textFieldUsuario.getText());
				Usuario.setPermiso(acceso);
				Stage primaryStage  = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/application/MainMenu.fxml"));
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				double width = screenSize.getWidth();
				double height = screenSize.getHeight();
				Scene scene = new Scene(root,width,height-50);
				scene.getStylesheets().add(getClass().getResource("Pedidos.css").toExternalForm());
				primaryStage.setScene(scene);
//				primaryStage.setResizable(false);
				primaryStage.setMaximized(true);
				primaryStage.setTitle("Puerta a Puerta | Terminal de punto de venta (TPV)");
				try{
					String dir = System.getProperty("user.dir");
//					System.out.println(dir + "\\icon.png");
				primaryStage.getIcons().add(new Image("file:" + dir + "\\icon.png"));
				
				}catch(Exception e){
					e.printStackTrace();
				}
				primaryStage.show();
				
				Stage stage = (Stage) textFieldUsuario.getScene().getWindow();
			    stage.close();
			}
			else{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error de acceso");
				alert.setContentText("Usuario o contraseña incorrectos");
				alert.showAndWait();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		finally{
			try {
				rs.close();
				pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
	}

}
