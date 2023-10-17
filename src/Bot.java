public abstract class Bot {
    protected int[][] copyBoard(int[][] board) {
        int[][] copy = new int[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 8);
        }
        return copy;
    }

    protected void captureMarks(int[][] board, int row, int col, int player, int opponent) {
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
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

    protected int countMarks(int[][] board, int player) {
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

    protected abstract int[] move(int[][] board, int player);

    protected abstract int evaluateBoard(int[][] board, int player, int opponent);
}

