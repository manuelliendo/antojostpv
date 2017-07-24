package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PruebasSFCWindowController {

	@FXML
	TextField textfieldNautorizacion;
	@FXML
	TextField textfieldNFactura;
	@FXML
	TextField textfieldNit;
	@FXML
	TextField textfieldFecha;
	@FXML
	TextField textfieldMonto;
	@FXML
	TextField textfieldLlaveDos;
	@FXML
	Label labelCcontrol;
	public void BtnPruebasPressed (ActionEvent event){
		String cControl = ""; 
		cControl = EmisionFactura.CodigoControl(textfieldNFactura.getText(), textfieldNit.getText(), 
				textfieldFecha.getText(), textfieldMonto.getText(), 
				textfieldLlaveDos.getText(), textfieldNautorizacion.getText());
		labelCcontrol.setText(cControl);
	}
	
}
