package application;
	
import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*MAIN ANTOJOS TPV*/
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double width = screenSize.getWidth();
			double height = screenSize.getHeight();
			Scene scene = new Scene(root,width,height-75);
			
			scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
//			primaryStage.setMaximized(true);
			primaryStage.setTitle("Puerta a Puerta | Terminal de punto de venta (TPV)");
			try{
				String dir = System.getProperty("user.dir");
//				System.out.println(dir + "\\icon.png");
			primaryStage.getIcons().add(new Image("file:" + dir + "\\icon.png"));
			
			}catch(Exception e){
				e.printStackTrace();
			}
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
