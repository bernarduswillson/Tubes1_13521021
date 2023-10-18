import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticBot extends Bot {

    private int rows;
    private int cols;
    private String bot;
    private String opponent;


    public GeneticBot(int row, int col, String bot) {
        this.rows = row;
        this.cols = col;
        this.bot = bot;
        this.opponent = bot.equals("O") ? "X" : "O";
        System.out.println("Bot: " + bot);
        System.out.println("Opponent: " + opponent);
    }

    @Override
    protected int[] move(int[][] board, int player) {
        List<int[]> population = generateInitialPopulation(50);
        int generation = 0;

        while (generation < 50) {
            List<int[]> offspring = generateOffspring(population);
            population.addAll(offspring);
            population = selectFittestPopulation(population, 50);
            generation++;
        }

        return getBestMove(population,board,player);
    }

    @Override
    protected int evaluateBoard(int[][] board, int player, int opponent) {
        int playerScore = countMarks(board, player);

        return playerScore;
    }

    private List<int[]> generateInitialPopulation(int size) {
        List<int[]> population = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int[] move = generateRandomMove();
            population.add(move);
        }

        return population;
    }

    private int[] generateRandomMove() {
        Random random = new Random();
        int row = random.nextInt(rows);
        int col = random.nextInt(cols);
        return new int[]{row, col};
    }

    private List<int[]> generateOffspring(List<int[]> population) {
        List<int[]> offspring = new ArrayList<>();

        for (int i = 0; i < population.size(); i++) {
            int[] parent1 = selectParent(population);
            int[] parent2 = selectParent(population);
            int[] child = crossover(parent1, parent2);
            mutate(child);
            offspring.add(child);
        }

        return offspring;
    }

    private int[] selectParent(List<int[]> population) {
        Random random = new Random();
        int index = random.nextInt(population.size());
        return population.get(index);
    }

    private int[] crossover(int[] parent1, int[] parent2) {
        int[] child = new int[2];
        Random random = new Random();

        // Randomly select genes from parents
        child[0] = random.nextBoolean() ? parent1[0] : parent2[0];
        child[1] = random.nextBoolean() ? parent1[1] : parent2[1];

        return child;
    }

    private void mutate(int[] move) {
        Random random = new Random();
        double mutationRate = 0.1;

        if (random.nextDouble() < mutationRate) {
            move[0] = random.nextInt(rows);
            move[1] = random.nextInt(cols);
        }
    }

    private List<int[]> selectFittestPopulation(List<int[]> population, int size) {
        List<int[]> fittestPopulation = new ArrayList<>(population);
        fittestPopulation.sort((m1, m2) -> fitnessFunction(m2) - fitnessFunction(m1));
        return fittestPopulation.subList(0, size);
    }
    private boolean isTileClear(int[][] board, int row, int col) {
        return board[row][col] == 0;
    }

    private int fitnessFunction(int[] move) {
        int[][] board = new int[rows][cols];
        int player = bot.equals("O") ? 1 : 2;
        int score = 0;

        for (int[] row : board) {
            for (int cell : row) {
                if (cell == player) {
                    score++;
                }
            }
        }

        return score;
    }
    private int[] getBestMove(List<int[]> population, int[][] board,int player) {
        int[] bestMove = population.get(0);
        int bestOutcome = Integer.MIN_VALUE;
        int opponent = (player == 1) ? 2 : 1;
    
        for (int[] move : population) {
            if (!isTileClear(board, move[0], move[1])) {
                continue;
            }
            int outcome = evaluateBoard(board, player,opponent);
            if (outcome > bestOutcome) {
                bestOutcome = outcome;
                bestMove = move;
            }
        }
    
        return bestMove;
    }
}
