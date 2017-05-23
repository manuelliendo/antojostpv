package application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
public class LoginFailedController {

	@FXML
	Button btnOk;
	public void btnOkPressed(){
		Stage stage = (Stage) btnOk.getScene().getWindow();
	    stage.close();
	}
}
