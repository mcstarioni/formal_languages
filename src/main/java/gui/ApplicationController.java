package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationController extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainWindowController controller = MainWindowController.create();
        primaryStage.setScene(new Scene(controller.getRoot()));
        primaryStage.setTitle("My Language Compiler");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public static void main(String... args)
    {
        launch(args);
    }
}
