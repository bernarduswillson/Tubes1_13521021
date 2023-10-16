public class HillClimbingBot {
    public int[] move(int[][] board, int player) {
        int[] move = new int[2];
        int currentScore = evaluateBoard(board, player);
        int bestScore = currentScore;
        int opponent = (player == 1) ? 2 : 1;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    int[][] tempBoard = copyBoard(board);
                    tempBoard[i][j] = player;

                    captureMarks(tempBoard, i, j, player, opponent);

                    int newScore = evaluateBoard(tempBoard, player);

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

    private int evaluateBoard(int[][] board, int player) {
        int playerCount = countMarks(board, player);
        return playerCount;
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
