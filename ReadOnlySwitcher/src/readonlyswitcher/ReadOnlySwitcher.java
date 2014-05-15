/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package readonlyswitcher;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author cruiserupce
 */
public class ReadOnlySwitcher extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {                
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.setTitle("ReadOnly Switcher for Digital Forensinc");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReadOnlySwitcher.fxml"));
        Parent root = (Parent) loader.load();
        ReadOnlySwitcherController controller = (ReadOnlySwitcherController) loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
       

        root.getStylesheets().add("/readonlyswitcher.css");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
