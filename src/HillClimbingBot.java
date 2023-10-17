public class HillClimbingBot extends Bot {
    @Override
    public int[] move(int[][] board, int player) {
        int[] move = new int[2];
        int currentScore = evaluateBoard(board, player, 0);
        int bestScore = currentScore;
        int opponent = (player == 1) ? 2 : 1;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    int[][] tempBoard = copyBoard(board);
                    tempBoard[i][j] = player;

                    captureMarks(tempBoard, i, j, player, opponent);

                    int newScore = evaluateBoard(tempBoard, player, 0);

                    if (newScore > bestScore) {
                        bestScore = newScore;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        return move;
    }

    @Override
    public int evaluateBoard(int[][] board, int player, int opponent) {
        int playerCount = countMarks(board, player);
        return playerCount;
    }
}
