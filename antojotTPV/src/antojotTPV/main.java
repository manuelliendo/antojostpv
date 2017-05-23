package antojotTPV;

import javax.swing.JFrame;

public class main {

	public static void main(String[] args) {
		
		Ventana prueba = new Ventana();
		prueba.setSize(680, 660);
		prueba.setVisible(true);
		prueba.pack();
		prueba.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
