public class MinimaxBot extends Bot {
    @Override
    public int[] move(int[][] board, int player) {
        int[] move = new int[2];
        int bestScore = Integer.MIN_VALUE;
        int opponent = (player == 1) ? 2 : 1;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    int[][] tempBoard = copyBoard(board);
                    tempBoard[i][j] = player;

                    captureMarks(tempBoard, i, j, player, opponent);

                    int score = minimax(tempBoard, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, player, opponent);

                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        return move;
    }

    private int minimax(int[][] board, int depth, boolean isMaximizing, int alpha, int beta, int player, int opponent) {
        int result = evaluateBoard(board, player, opponent);

        if (depth == 4) {
            return result;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 0) {
                        int[][] tempBoard = copyBoard(board);
                        tempBoard[i][j] = player;

                        // Capture opponent's marks
                        captureMarks(tempBoard, i, j, player, opponent);

                        int score = minimax(tempBoard, depth + 1, false, alpha, beta, player, opponent);
                        bestScore = Math.max(score, bestScore);
                        alpha = Math.max(alpha, bestScore);

                        if (beta <= alpha) {
                            break; // Beta cutoff
                        }
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 0) {
                        int[][] tempBoard = copyBoard(board);
                        tempBoard[i][j] = opponent;

                        captureMarks(tempBoard, i, j, opponent, player);

                        int score = minimax(tempBoard, depth + 1, true, alpha, beta, player, opponent);
                        bestScore = Math.min(score, bestScore);
                        beta = Math.min(beta, bestScore);

                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return bestScore;
        }
    }

    @Override
    public int evaluateBoard(int[][] board, int player, int opponent) {
        int playerCount = countMarks(board, player);
        int opponentCount = countMarks(board, opponent);

        return playerCount - opponentCount;
    }
}
