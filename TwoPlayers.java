package example;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class TwoPlayers {
    private final int size = 75; // Размер каждой клетки (можно изменять)
    private final int field = 10; // Размер поля (можно изменять)
    private boolean playable = true; // Флаг что поле доступно для игры
    private String turn = "X"; // Очерель хода
    private final Tile[][] board = new Tile[field][field]; // Доска
    private final List<Combo> combos = new ArrayList<>();  // Список возможных комбмнаций для победы
    private final Pane root = new Pane();

    public void show() {
        Stage window = new Stage();
        window.setScene(new Scene(createContent()));
        window.setTitle("Tic-Tac-Toe " + "for two");
        window.showAndWait();
    }

    private Parent createContent() {
        root.setPrefSize(field * size, field * size);

        for (int i = 0; i < field; i++) {
            for (int j = 0; j < field; j++) {
                Tile tile = new Tile();
                tile.setTranslateY(i * size);
                tile.setTranslateX(j * size);
                root.getChildren().add(tile);
                board[j][i] = tile;
            }
        }
        // Вертикальные
        for (int x = 0; x < field; x++)
            for (int y = 0; y < field - 2; y++)
                combos.add(new Combo(board[x][y], board[x][y + 1], board[x][y + 2]));
        //  Горизонтальные
        for (int y = 0; y < field; y++)
            for (int x = 0; x < field - 2; x++)
                combos.add(new Combo(board[x][y], board[x + 1][y], board[x + 2][y]));
        // Диагональные
        for (int x = 0; x < field - 2; x++)
            for (int y = 0; y < field - 2; y++)
                combos.add(new Combo(board[x][y], board[x + 1][y + 1], board[x + 2][y + 2]));
        for (int x = 2; x < field; x++)
            for (int y = 0; y < field - 2; y++)
                combos.add(new Combo(board[x][y], board[x - 1][y + 1], board[x - 2][y + 2]));
        return root;
    }

    private void checkState(String player) {
        // ToDo
        for (Combo combo : combos)
            if (combo.isComplete()) {
                playable = false;
                playWinAnimation(combo);
                informDialog(player);
                return;
            }
        for (int i = 0; i < field; i++)
            for (int j = 0; j < field; j++)
                if (board[i][j].getValue().isEmpty()) {
                    return;
                }
        playable = false;
        informDialog("Draw");
    }

    public class Tile extends StackPane {
        private final Text text = new Text();

        public Tile() {
            Rectangle border = new Rectangle(size, size);
            border.setFill(null);
            border.setStroke(Color.BLACK);
            text.setFont(Font.font(64));
            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);
            click();
        }

        public void click() {
            setOnMouseClicked(event -> {
                if (!playable)
                    return;
                if (event.getButton() == MouseButton.PRIMARY && this.text.getText().isEmpty()) {
                    if (turn.equals("X")) {
                        this.drawX();
                        checkState(turn);
                        turn = "O";
                    } else {
                        this.drawO();
                        checkState(turn);
                        turn = "X";
                    }
                }
            });
        }

        private void drawX() {
            text.setText("X");
        }

        private void drawO() {
            text.setText("O");
        }

        public double getCenterX() {
            return getTranslateX() + size * 0.5;
        }

        public double getCenterY() {
            return getTranslateY() + size * 0.5;
        }

        public String getValue() {
            return text.getText();
        }
    }

    public static class Combo {
        private final Tile[] tiles = new Tile[3];

        public Combo(Tile t0, Tile t1, Tile t2) {
            this.tiles[0] = t0;
            this.tiles[1] = t1;
            this.tiles[2] = t2;
        }

        public boolean isComplete() {
            if (tiles[0].getValue().isEmpty())
                return false;
            return tiles[0].getValue().equals(tiles[1].getValue())
                    && tiles[0].getValue().equals(tiles[2].getValue());
        }
    }

    private void playWinAnimation(Combo combo) {
        Line line = new Line();
        line.setStartX(combo.tiles[0].getCenterX());
        line.setStartY(combo.tiles[0].getCenterY());
        line.setEndX(combo.tiles[2].getCenterX());
        line.setEndY(combo.tiles[2].getCenterY());
        root.getChildren().add(line);
    }

    private void informDialog(String str) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("The game is over");
        alert.setHeaderText("The game is over");
        if (str.equals("Draw"))
            alert.setContentText("Draw!!!");
        else {
            alert.setContentText("Player, playing " + str + " won!!!");
        }
        alert.showAndWait();
    }

}
