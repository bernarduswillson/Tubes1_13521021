public class MinimaxBot extends Bot {
    private long startTime;
    private boolean timeLimitReached;
    private int MAX_DEPTH;

    private long getTimeElapsed() {
        return System.nanoTime() - this.startTime;
    }
    
    public MinimaxBot(int rounds) {
        if (rounds > 4) {
            this.MAX_DEPTH = 4;
        } else {
            this.MAX_DEPTH = rounds;
        }
    }

    @Override
    public int[] move(int[][] board, int player) {
        int[] move = new int[2];
        int bestScore = Integer.MIN_VALUE;
        int opponent = (player == 1) ? 2 : 1;

        this.timeLimitReached = false;
        this.startTime = System.nanoTime();
        for (int i = 0; i < board.length && !this.timeLimitReached; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    int[][] tempBoard = copyBoard(board);
                    tempBoard[i][j] = player;

                    captureMarks(tempBoard, i, j, player, opponent);
                    
                    
                    int score = minimax(tempBoard, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, player, opponent);
                    if (this.timeLimitReached) {
                        break;
                    }
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        
        if (this.timeLimitReached) {
            System.out.println("Time limit reached");
            fallbackPlan(board, player, move);
        }
        
        return move;
    }

    private int minimax(int[][] board, int depth, boolean isMaximizing, int alpha, int beta, int player, int opponent) {
        /*
         * If the time limit has been reached, it will break out of tree
         * If not, it will check if the time elapsed is greater than 5 seconds
         */
        if (this.timeLimitReached) {
            return -1;
        }
        else {
            if (getTimeElapsed() / 1000000 > 5000) {
                this.timeLimitReached = true;
                return -1;
            }
        }

        int result = evaluateBoard(board, player, opponent);
        
        if (depth == MAX_DEPTH) {
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

    /*
     * fallbackPlan() is called when the time limit has been reached
     * It will use the Greedy algorithm to find the best move 
     * because it takes less than 1 ms.
     */
    private void fallbackPlan(int[][] board, int player, int[] move) {
        int currentScore = countMarks(board, player);
        int bestScore = currentScore;
        int opponent = (player == 1) ? 2 : 1;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    int[][] tempBoard = copyBoard(board);
                    tempBoard[i][j] = player;

                    captureMarks(tempBoard, i, j, player, opponent);

                    int newScore = countMarks(board, player);

                    if (newScore > bestScore) {
                        bestScore = newScore;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
    }
}
