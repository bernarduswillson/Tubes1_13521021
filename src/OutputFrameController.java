import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.application.Platform;

import java.io.IOException;

/**
 * The OutputFrameController class. It controls button input from the users when
 * playing the game.
 *
 * @author Jedid Ahn
 *
 */
public class OutputFrameController {
    @FXML
    private GridPane gameBoard;

    @FXML
    private GridPane scoreBoard;

    @FXML
    private Label roundsLeftLabel;
    @FXML
    private Label playerXName;
    @FXML
    private Label playerOName;
    @FXML
    private HBox playerXBoxPane;
    @FXML
    private HBox playerOBoxPane;
    @FXML
    private Label playerXScoreLabel;
    @FXML
    private Label playerOScoreLabel;

    private boolean playerXTurn;
    private int playerXScore;
    private int playerOScore;
    private int roundsLeft;
    private boolean is2First;
    private String algorithm1;
    private String algorithm2;
    private Bot bot1;
    private Bot bot2;
    private boolean is1Human;
    private boolean is2Human;

    private static final int ROW = 8;
    private static final int COL = 8;
    private Button[][] buttons = new Button[ROW][COL];

    /**
     * Set the name of player X (player) to be name1, set the name of player O (bot)
     * to be name2,
     * and the number of rounds played to be rounds. This input is received from
     * the input frame and is output in the score board of the output frame.
     *
     * @param name1    Name of Player 1 (Player).
     * @param name2    Name of Player 2 (Bot).
     * @param rounds   The number of rounds chosen to be played.
     * @param is2First True if bot is first, false otherwise.
     *
     */
    void getInput(String name1, String name2, String rounds, String algorithm1, String algorithm2, boolean is2First) {
        this.playerXName.setText(name1);
        this.playerOName.setText(name2);
        this.roundsLeftLabel.setText(rounds);
        this.roundsLeft = Integer.parseInt(rounds);
        this.algorithm1 = algorithm1;
        this.algorithm2 = algorithm2;
        this.is2First = is2First;
        this.is1Human = false;
        this.is2Human = false;

        // Start bot for Player 1 (X)
        if (algorithm1.equals("Human")) {
            this.is1Human = true;
        } else if (algorithm1.equals("Hill Climbing")) {
            this.bot1 = new HillClimbingBot();
        } else if (algorithm1.equals("Minimax")) {
            this.bot1 = new MinimaxBot();
        } else if (algorithm1.equals("Simulated Annealing")) {
            this.bot1 = new SimulatedAnnealingBot();
        } else if (algorithm1.equals("Genetic")) {
            this.bot1 = new GeneticBot(8, 8, "X");
        }

        // Start bot for Player 2 (O)
        if (algorithm2.equals("Human")) {
            this.is2Human = true;
        } else if (algorithm2.equals("Hill Climbing")) {
            this.bot2 = new HillClimbingBot();
        } else if (algorithm2.equals("Minimax")) {
            this.bot2 = new MinimaxBot();
        } else if (algorithm2.equals("Simulated Annealing")) {
            this.bot2 = new SimulatedAnnealingBot();
        } else if (algorithm2.equals("Genetic")) {
            this.bot2 = new GeneticBot(8, 8, "O");
        }

        // Set the first player's turn based on is2First
        this.playerXTurn = !is2First;

        // If the first player is a bot, make the bot's move
        if (is2First || !(is1Human)) {
            moveBot();
        }
    }

    /**
     * Construct the 8x8 game board by creating a total of 64 buttons in a 2
     * dimensional array, and construct the 8x2 score board for scorekeeping
     * and then initialize turn and score.
     *
     */
    @FXML
    private void initialize() {
        // Construct game board with 8 rows.
        for (int i = 0; i < ROW; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / ROW);
            this.gameBoard.getRowConstraints().add(rowConst);
        }

        // Construct game board with 8 columns.
        for (int i = 0; i < COL; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / COL);
            this.gameBoard.getColumnConstraints().add(colConst);
        }

        // Style buttons and construct 8x8 game board.
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                this.buttons[i][j] = new Button();
                this.buttons[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                this.buttons[i][j].setCursor(Cursor.HAND);
                this.gameBoard.add(this.buttons[i][j], j, i);

                // Add ActionListener to each button such that when it is clicked, it calls
                // the selected coordinates method with its i and j coordinates.
                final int finalI = i;
                final int finalJ = j;
                this.buttons[i][j].setOnAction(event -> this.selectedCoordinates(finalI, finalJ));
            }
        }

        // Setting up the initial game board with 4 X's in bottom left corner and 4 O's
        // in top right corner.
        this.buttons[ROW - 2][0].setText("X");
        this.buttons[ROW - 1][0].setText("X");
        this.buttons[ROW - 2][1].setText("X");
        this.buttons[ROW - 1][1].setText("X");
        this.buttons[0][COL - 2].setText("O");
        this.buttons[0][COL - 1].setText("O");
        this.buttons[1][COL - 2].setText("O");
        this.buttons[1][COL - 1].setText("O");

        // Construct score board with 8 rows.
        for (int i = 0; i < ROW; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / ROW);
            this.scoreBoard.getRowConstraints().add(rowConst);
        }

        // Construct score board with 2 column.
        for (int i = 0; i < 2; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 2);
            this.scoreBoard.getColumnConstraints().add(colConst);
        }

        // Initialize turn and score for the game.
        this.playerXBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
        this.playerOBoxPane.setStyle("-fx-background-color: white; -fx-border-color: #D3D3D3;");
        this.playerXScoreLabel.setText("4");
        this.playerOScoreLabel.setText("4");

        this.playerXTurn = true;
        this.playerXScore = 4;
        this.playerOScore = 4;
    }

    /**
     * Process the coordinates of the button that the user selected on the game
     * board.
     *
     * @param i The row number of the button clicked.
     * @param j The column number of the button clicked.
     *
     */
    private void selectedCoordinates(int i, int j) {
        if (this.roundsLeft == 0) {
            // Disable the game board buttons to prevent further moves
            for (int a = 0; a < ROW; a++) {
                for (int b = 0; b < COL; b++) {
                    this.buttons[a][b].setDisable(true);
                }
            }
            return;
        }
        // Invalid when a button with an X or an O is clicked.
        if (!this.buttons[i][j].getText().equals(""))
            new Alert(Alert.AlertType.ERROR, "Invalid coordinates: Try again!").showAndWait();
        // Button must be blank.
        else {
            if (this.playerXTurn && is1Human) {
                // Changed background color to green to indicate next player's turn.
                this.playerXBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
                this.playerOBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
                this.buttons[i][j].setText("X"); // Mark the board with X.
                this.playerXScore++; // Increment the score of player X.

                // Update game board by changing surrounding cells to X if applicable.
                this.updateGameBoard(i, j);
                this.playerXTurn = false; // Alternate player's turn.

                if (is2First) {
                    this.roundsLeft--; // Decrement the number of rounds left after both Player X & Player O have
                                       // played.
                    this.roundsLeftLabel.setText(String.valueOf(this.roundsLeft));
                }

                if (is2First && this.roundsLeft == 0) {
                    this.endOfGame();
                }

                // Bot's turn
                this.moveBot();
            } else if (!this.playerXTurn && is1Human) {
                this.playerXBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
                this.playerOBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
                this.buttons[i][j].setText("O");
                this.playerOScore++;

                this.updateGameBoard(i, j);
                this.playerXTurn = true;

                if (!is2First) {
                    this.roundsLeft--; // Decrement the number of rounds left after both Player X & Player O have
                                       // played.
                    this.roundsLeftLabel.setText(String.valueOf(this.roundsLeft));
                }

                if (!is2First && this.roundsLeft == 0) { // Game has terminated.
                    this.endOfGame(); // Determine & announce the winner.
                }
            } else if (this.playerXTurn && is2Human) {
                this.playerXBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
                this.playerOBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
                this.buttons[i][j].setText("X");
                this.playerXScore++;

                this.updateGameBoard(i, j);
                this.playerXTurn = false;

                if (!is2First) {
                    this.roundsLeft--; // Decrement the number of rounds left after both Player X & Player O have
                                       // played.
                    this.roundsLeftLabel.setText(String.valueOf(this.roundsLeft));
                }

                if (!is2First && this.roundsLeft == 0) { // Game has terminated.
                    this.endOfGame(); // Determine & announce the winner.
                }
            } else if (!this.playerXTurn && is2Human) {
                this.playerXBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
                this.playerOBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
                this.buttons[i][j].setText("O");
                this.playerOScore++;

                this.updateGameBoard(i, j);
                this.playerXTurn = true;

                if (is2First) {
                    this.roundsLeft--; // Decrement the number of rounds left after both Player X & Player O have
                                       // played.
                    this.roundsLeftLabel.setText(String.valueOf(this.roundsLeft));
                }

                if (is2First && this.roundsLeft == 0) {
                    this.endOfGame();
                }

                // Bot's turn
                this.moveBot();
            } 
            else if (!is1Human && !is2Human) {
                // bot 1 turn
                if (this.playerXTurn) {
                    this.playerXBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
                    this.playerOBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
                    this.buttons[i][j].setText("X");
                    this.playerXScore++;

                    this.updateGameBoard(i, j);
                    this.playerXTurn = false;

                    if (is2First) {
                        this.roundsLeft--; // Decrement the number of rounds left after both Player X & Player O have
                                           // played.
                        this.roundsLeftLabel.setText(String.valueOf(this.roundsLeft));
                    }

                    if (is2First && this.roundsLeft == 0) {
                        this.endOfGame();
                    }

                    // bot 2 turn
                    this.moveBot();
                } else {
                    this.playerXBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
                    this.playerOBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
                    this.buttons[i][j].setText("O");
                    this.playerOScore++;

                    this.updateGameBoard(i, j);
                    this.playerXTurn = true;

                    if (!is2First) {
                        this.roundsLeft--; // Decrement the number of rounds left after both Player X & Player O have
                                           // played.
                        this.roundsLeftLabel.setText(String.valueOf(this.roundsLeft));
                    }

                    if (!is2First && this.roundsLeft == 0) { // Game has terminated.
                        this.endOfGame(); // Determine & announce the winner.
                    }

                    // bot 1 turn
                    this.moveBot();
                }
            }
        }
    }

    /**
     * Change adjacent cells to X's or O's.
     *
     * @param i The row number of the button clicked.
     * @param j The column number of the button clicked.
     *
     */
    private void updateGameBoard(int i, int j) {
        // Value of indices to control the lower/upper bound of rows and columns
        // in order to change surrounding/adjacent X's and O's only on the game board.
        // Four boundaries: First & last row and first & last column.

        int startRow, endRow, startColumn, endColumn;

        if (i - 1 < 0) // If clicked button in first row, no preceding row exists.
            startRow = i;
        else // Otherwise, the preceding row exists for adjacency.
            startRow = i - 1;

        if (i + 1 >= ROW) // If clicked button in last row, no subsequent/further row exists.
            endRow = i;
        else // Otherwise, the subsequent row exists for adjacency.
            endRow = i + 1;

        if (j - 1 < 0) // If clicked on first column, lower bound of the column has been reached.
            startColumn = j;
        else
            startColumn = j - 1;

        if (j + 1 >= COL) // If clicked on last column, upper bound of the column has been reached.
            endColumn = j;
        else
            endColumn = j + 1;

        // Search for adjacency for X's and O's or vice versa, and replace them.
        // Update scores for X's and O's accordingly.
        for (int x = startRow; x <= endRow; x++) {
            this.setPlayerScore(x, j);
        }

        for (int y = startColumn; y <= endColumn; y++) {
            this.setPlayerScore(i, y);
        }

        this.playerXScoreLabel.setText(String.valueOf(this.playerXScore));
        this.playerOScoreLabel.setText(String.valueOf(this.playerOScore));
    }

    private void setPlayerScore(int i, int j) {
        if (this.playerXTurn) {
            if (this.buttons[i][j].getText().equals("O")) {
                this.buttons[i][j].setText("X");
                this.playerXScore++;
                this.playerOScore--;
            }
        } else if (this.buttons[i][j].getText().equals("X")) {
            this.buttons[i][j].setText("O");
            this.playerOScore++;
            this.playerXScore--;
        }
    }

    /**
     * Determine and announce the winner of the game.
     *
     */
    private void endOfGame() {
        Platform.runLater(() -> {
            // Player X is the winner.
            if (this.playerXScore > this.playerOScore) {
                new Alert(Alert.AlertType.INFORMATION,
                        this.playerXName.getText() + " has won the game!").showAndWait();
                this.playerXBoxPane.setStyle("-fx-background-color: CYAN; -fx-border-color: #D3D3D3;");
                this.playerOBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
                this.playerXName.setText(this.playerXName.getText() + " (Winner!)");
            }

            // Player O is the winner,
            else if (this.playerOScore > this.playerXScore) {
                new Alert(Alert.AlertType.INFORMATION,
                        this.playerOName.getText() + " has won the game!").showAndWait();
                this.playerXBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
                this.playerOBoxPane.setStyle("-fx-background-color: CYAN; -fx-border-color: #D3D3D3;");
                this.playerOName.setText(this.playerOName.getText() + " (Winner!)");
            }

            // Player X and Player O tie.
            else {
                new Alert(Alert.AlertType.INFORMATION,
                        this.playerXName.getText() + " and " + this.playerOName.getText() + " have tied!")
                        .showAndWait();
                this.playerXBoxPane.setStyle("-fx-background-color: ORANGE; -fx-border-color: #D3D3D3;");
                this.playerOBoxPane.setStyle("-fx-background-color: ORANGE; -fx-border-color: #D3D3D3;");
            }

            // Disable the game board buttons to prevent from playing further.
            for (int i = 0; i < ROW; i++)
                for (int j = 0; j < COL; j++)
                    this.buttons[i][j].setDisable(true);
        });
    }

    /**
     * Close OutputFrame controlled by OutputFrameController if end game button is
     * clicked.
     *
     */
    @FXML
    private void endGame() {
        System.exit(0);
    }

    /**
     * Reopen InputFrame controlled by InputFrameController if play new game button
     * is clicked.
     *
     */
    @FXML
    private void playNewGame() throws IOException {
        // Close secondary stage/output frame.
        Stage secondaryStage = (Stage) this.gameBoard.getScene().getWindow();
        secondaryStage.close();

        // Reopen primary stage/input frame.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InputFrame.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Adjacency Gameplay");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void moveBot() {
        int[] botMove;

        if (getPlayerID() == 1) {
            botMove = bot1.move(getBoardState(), 1);
        } else if (getPlayerID() == 2) {
            botMove = bot2.move(getBoardState(), 2);
        } else {
            new Alert(Alert.AlertType.ERROR, "Invalid Player ID. Exiting.").showAndWait();
            System.exit(1);
            return;
        }

        int i = botMove[0];
        int j = botMove[1];

        System.out.println(i);
        System.out.println(j);

        // Create a PauseTransition with a 1-second duration
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            if (i >= 0 && i < ROW && j >= 0 && j < COL && buttons[i][j].getText().isEmpty()) {
                selectedCoordinates(i, j);
            } else {
                new Alert(Alert.AlertType.ERROR, "Bot Invalid Coordinates. Exiting.").showAndWait();
                System.exit(1);
            }
        });

        // Start the pause transition
        pause.play();
    }

    private int[][] getBoardState() {
        int[][] board = new int[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (buttons[i][j].getText().equals("X")) {
                    board[i][j] = 1;
                } else if (buttons[i][j].getText().equals("O")) {
                    board[i][j] = 2;
                } else {
                    board[i][j] = 0;
                }
            }
        }
        return board;
    }

    private int getPlayerID() {
        return playerXTurn ? 1 : 2;
    }

    private void makeBotMove(int i, int j) {
        if (i >= 0 && i < ROW && j >= 0 && j < COL && buttons[i][j].getText().isEmpty()) {
            selectedCoordinates(i, j);
        }
    }
}