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

import java.util.Random;
import java.util.ArrayList;

public class OnePlayer {
    private final int size = 75; // Размер каждой клетки (можно изменять)
    private final int field = 10; // Размер поля (можно изменять)
    private int countTurn = 0; //  count turn computer
    private boolean playable = true; // Флаг что поле доаступно для игры
    private final Tile[][] board = new Tile[field][field];
    private final ArrayList<Combo> combos = new ArrayList<>();  // Список возможных комбмнаций
    private final Pane root = new Pane();

    // Координаты для первого хода компьютера
    Random random = new Random();
    private final int x = random.nextInt(field - 2) + 1;
    private final int y = random.nextInt(field - 2) + 1;

    public void show() {
        Stage window = new Stage();
        window.setScene(new Scene(createContent()));
        window.setTitle("Tic-Tac-Toe " + "for one");
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
        computerTurn();
        return root;
    }

    public void computerTurn() {
        if (!playable)
            return;

        this.countTurn++;
        if (this.countTurn == 1) {
            board[x][y].drawX();
            return;
        }
        if (this.countTurn == 2) {
            if (board[x - 1][y - 1].getValue().isEmpty() && board[x + 1][y + 1].getValue().isEmpty())
                board[x - 1][y - 1].drawX();
            else
                board[x - 1][y + 1].drawX();
            return;
        }
        // Win computer
        for (Combo combo : combos)
            if (combo.checkFor2("X")) {
                if (combo.tiles[0].getValue().isEmpty())
                    combo.tiles[0].drawX();
                else if (combo.tiles[1].getValue().isEmpty())
                    combo.tiles[1].drawX();
                else if (combo.tiles[2].getValue().isEmpty())
                    combo.tiles[2].drawX();
                return;
            }
        // Do not Win player
        for (Combo combo : combos)
            if (combo.checkFor2("O")) {
                if (combo.tiles[0].getValue().isEmpty())
                    combo.tiles[0].drawX();
                else if (combo.tiles[1].getValue().isEmpty())
                    combo.tiles[1].drawX();
                else if (combo.tiles[2].getValue().isEmpty())
                    combo.tiles[2].drawX();
                return;
            }
        // Continuation
        for(int i = 0; i < field; i++)
            for (int j = 0; j < field; j++)
                if (board[i][j].getValue().isEmpty()) {
                    board[i][j].drawX();
                    return;
                }
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
                if (event.getButton() == MouseButton.PRIMARY && this.getValue().isEmpty()) {
                    this.drawO();
                    checkState("You");
                    computerTurn();
                    checkState("Computer");
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

    public class Combo {
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

        public boolean checkFor2(String str) {
            return     (tiles[0].getValue().equals(str) && tiles[1].getValue().equals(str) && tiles[2].getValue().isEmpty())
                    || (tiles[0].getValue().equals(str) && tiles[2].getValue().equals(str) && tiles[1].getValue().isEmpty())
                    || (tiles[1].getValue().equals(str) && tiles[2].getValue().equals(str) && tiles[0].getValue().isEmpty());
        }
    }

    private void playWinAnimation(Combo combo) {
        Line line = new Line();
        line.setStartX(combo.tiles[0].getCenterX());
        line.setStartY(combo.tiles[0].getCenterY());
        line.setEndX(combo.tiles[2].getCenterX());
        line.setEndY(combo.tiles[2].getCenterY());
        root.getChildren().add(line); // Добавление линии
    }

    private void informDialog(String str) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("The game is over");
        alert.setHeaderText("The game is over");
        if (str.equals("Draw"))
            alert.setContentText("Draw!!!");
        else {
            alert.setContentText(str + " won!!!");
        }
        alert.showAndWait();
    }

}
