package it.polito.tdp.formulaone;
	
import it.polito.tdp.formulaone.model.Model;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("FormulaOne.fxml")) ;
			BorderPane root = (BorderPane)loader.load();
			
			FormulaOneController controller = loader.getController() ;
			Model model = new Model() ;
			controller.setModel(model);
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			controller.caricaBox();
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

//praticamente da rifare da capo - Ricorsivo