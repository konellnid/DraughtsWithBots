package main;

import controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainView.fxml"));
        Parent root = loader.load();

        MainView mainView = loader.getController();
        MainViewController mainViewController = new MainViewController(mainView);
        mainView.setMainViewController(mainViewController);

        primaryStage.setTitle("Draughts");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
