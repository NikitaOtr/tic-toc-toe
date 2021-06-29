package example;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main (String[] arg) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tic-Tac-Toe");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(24));

        Font font = Font.font("Tahoma", FontWeight.NORMAL, 26);

        Label lab = new Label("Tic-Tac-Toe\n\n");
        lab.setFont(Font.font("Tahoma", FontWeight.BLACK, 36 ));

        Button btn1 = new Button("One Player");
        btn1.setMinSize(500,100);
        btn1.setFont(font);
        btn1.setOnAction((ActionEvent e) ->{
            primaryStage.close();
            OnePlayer window = new OnePlayer();
            window.show();
        });

        Button btn2 = new Button("Two Players");
        btn2.setMinSize(500,100);
        btn2.setFont(font);
        btn2.setOnAction((ActionEvent e) ->{
            primaryStage.close();
            TwoPlayers window = new TwoPlayers();
            window.show();
        } );

        root.getChildren().addAll(lab, btn1, btn2);

        Scene scene = new Scene(root, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
