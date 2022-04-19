/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.engine.model.board.Board;
import hr.algebra.utilities.ReflectionUtils;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author fran
 */
public class Chess extends Application {


    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("engine/view/Chess.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Chess game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Board board = Board.createStandardBoard();

        Class<?> clazz = hr.algebra.engine.model.pieces.Piece.class;
        StringBuilder builder = new StringBuilder();
        ReflectionUtils.ReadMembersInfo(clazz, builder, true);
        System.out.println(builder);
        System.out.println(board);

        launch(args);
    }
}