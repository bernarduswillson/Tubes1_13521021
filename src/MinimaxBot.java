public class MinimaxBot {
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
        int result = checkWinner(board, player, opponent);

        if (result != 0 || depth == 4) {
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

    private int checkWinner(int[][] board, int player, int opponent) {
        int playerCount = countMarks(board, player);
        int opponentCount = countMarks(board, opponent);

        return playerCount - opponentCount;
    }

    private int countMarks(int[][] board, int player) {
        int count = 0;
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == player) {
                    count++;
                }
            }
        }
        return count;
    }

    private void captureMarks(int[][] board, int row, int col, int player, int opponent) {
        int[][] directions = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };
        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];
            int newRow = row + dx;
            int newCol = col + dy;

            while (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 && board[newRow][newCol] == opponent) {
                board[newRow][newCol] = player;
                newRow += dx;
                newCol += dy;
            }
        }
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 8);
        }
        return copy;
    }
}
