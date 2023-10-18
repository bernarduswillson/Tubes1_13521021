import java.util.Random;

public class SimulatedAnnealingBot extends Bot {
    private static final double INITIAL_TEMPERATURE = 1.0;
    private static final double FINAL_TEMPERATURE = 0.01;
    private static final double COOLING_RATE = 0.995;
    private static final int MAX_ITERATIONS = 1000;
    private Random random = new Random();

    @Override
    public int[] move(int[][] board, int player) {
        int[] currentMove = new int[2];
        int[] bestMove = new int[2];
        int currentScore = evaluateBoard(board, player, 0);
        int bestScore = currentScore;
        int opponent = (player == 1) ? 2 : 1;
        double temperature = INITIAL_TEMPERATURE;

        for (int iteration = 0; iteration < MAX_ITERATIONS && temperature > FINAL_TEMPERATURE; iteration++) {
            int i = random.nextInt(8);
            int j = random.nextInt(8);

            if (board[i][j] == 0) {
                int[][] tempBoard = copyBoard(board);
                tempBoard[i][j] = player;

                captureMarks(tempBoard, i, j, player, opponent);

                int newScore = evaluateBoard(tempBoard, player, 0);

                if (acceptNewSolution(currentScore, newScore, temperature)) {
                    currentScore = newScore;
                    currentMove[0] = i;
                    currentMove[1] = j;

                    if (newScore > bestScore) {
                        bestScore = newScore;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }

            temperature *= COOLING_RATE;
        }
        return bestMove;
    }

    private boolean acceptNewSolution(int currentScore, int newScore, double temperature) {
        if (newScore > currentScore) {
            return true;
        }
        double probability = Math.exp((newScore - currentScore) / temperature);
        return random.nextDouble() < probability;
    }

    @Override
    public int evaluateBoard(int[][] board, int player, int opponent) {
        int playerCount = countMarks(board, player);
        return playerCount;
    }
}