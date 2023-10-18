import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticBot extends Bot {

    private int ROW;
    private int COL;
    private String bot;
    private String opponent;
    private int botScore;

    public GeneticBot(int row, int col, String bot) {
        this.ROW = row;
        this.COL = col;
        this.bot = bot;
        this.opponent = bot.equals("O") ? "X" : "O";
        System.out.println("Bot: " + bot);
        System.out.println("Opponent: " + opponent);
    }

    public void updateScore(int botScore) {
        this.botScore = botScore;
    }

    @Override
    protected int[] move(int[][] board, int player) {
        List<int[]> population = generateInitialPopulation(100);
        int generation = 0;

        while (generation < 100) {
            List<int[]> offspring = generateOffspring(population);
            population.addAll(offspring);
            population = selectFittestPopulation(population, 100);
            generation++;
        }

        return getBestMove(population,board);
    }

    @Override
    protected int evaluateBoard(int[][] board, int player, int opponent) {
        // Your custom evaluation logic goes here
        // This is a placeholder, replace it with your actual logic
        int playerScore = countMarks(board, player);
        int opponentScore = countMarks(board, opponent);

        return playerScore - opponentScore;
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
        int row = random.nextInt(ROW);
        int col = random.nextInt(COL);
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
            move[0] = random.nextInt(ROW);
            move[1] = random.nextInt(COL);
        }
    }

    private List<int[]> selectFittestPopulation(List<int[]> population, int size) {
        List<int[]> fittestPopulation = new ArrayList<>(population);
        fittestPopulation.sort((m1, m2) -> utilityFunction(m2) - utilityFunction(m1));
        return fittestPopulation.subList(0, size);
    }

    private int utilityFunction(int[] move) {
        int row = move[0];
        int col = move[1];
        int utility = 100;
        

        return utility;
    }

    private boolean isTileClear(int[][] board, int row, int col) {
        return board[row][col] == 0;
    }

    private int[] getBestMove(List<int[]> population, int[][] board) {
        int[] bestMove = population.get(0);
        int bestOutcome = Integer.MIN_VALUE;
    
        for (int[] move : population) {
            if (!isTileClear(board, move[0], move[1])) {
                continue;
            }
            int outcome = utilityFunction(move);
            if (outcome > bestOutcome) {
                bestOutcome = outcome;
                bestMove = move;
            }
        }
    
        return bestMove;
    }
}
