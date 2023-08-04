import java.util.*;

/**
 * @Author: Filippos Papaspyrou
 * @version:
 * @Description:
 * @Date: 04/08/2023
 */
public class CellRemover {

    private Random random;

    private Map<Integer, Set<Integer>> rowsRandomPool, colsRandomPool, boxesRandomPool;

    public CellRemover() {
        random = new Random();
        // Initialize possibilities
        rowsRandomPool = new HashMap<>();
        colsRandomPool = new HashMap<>();
        boxesRandomPool = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            rowsRandomPool.put(i, new HashSet<>());
            colsRandomPool.put(i, new HashSet<>());
            boxesRandomPool.put(i, new HashSet<>());
        }
    }

    public int[][] removeCells(int[][] board, int numberOfCells) {
        Map<Integer, Integer> count = new HashMap<>();
        int[] removalIndex = new int[9 * 9];
        for (int i = 0; i < 9 * 9; i++) {
            removalIndex[i] = i;
        }
        int poolSize = 9 * 9;
        int randomIndex;
        int temp;
        for (int i = 0; i < numberOfCells; i++) {
            randomIndex = random.nextInt(poolSize);
            temp = removalIndex[randomIndex];
            int row = temp % 9;
            int col = temp / 9;
            int counter = count.getOrDefault(board[row][col], 0);
            if (counter>5) {
                i--;
                continue;
            }
            count.put(board[row][col], ++counter);
            board[row][col] = 0;
            removalIndex[randomIndex] = removalIndex[poolSize - 1];
            removalIndex[poolSize - 1] = temp;
            poolSize--;
        }
        return board;
    }

}
