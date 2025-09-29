import java.util.Arrays;

public class SudokuSolver {
    private static final int GRID_SIZE = 9;
    public static void main(String[] args){
        int[][] board = {
            {7, 0, 2, 0, 5, 0, 6, 0, 0},
            {0, 0, 0, 0, 0, 3, 0, 0, 0},
            {1, 0, 0, 0, 0, 9, 5, 0, 0},
            {8, 0, 0, 0, 0, 0, 0, 9, 0},
            {0, 4, 3, 0, 0, 0, 7, 5, 0},
            {0, 9, 0, 0, 0, 0, 0, 0, 8},
            {0, 0, 9, 7, 0, 0, 0, 0, 5},
            {0, 0, 0, 2, 0, 0, 0, 0, 0},
            {0, 0, 7, 0, 4, 0, 2, 0, 3}
        };

        if (!validateBoard(board)) {
            System.out.println("Board is invalid (duplicates found). Fix them and try again.");
            printBoard(board);
            return;
        }
        
        if(solveBoard(board)){
            System.out.println("Solved Successfully");
        }else{
            System.out.println("Unsolvable");
        }
        printBoard(board);
    }

    private static boolean validateBoard(int[][] board) {
        boolean valid = true;

        // Rows
        for (int r = 0; r < GRID_SIZE; r++) {
            int[] seenPos = new int[10];
            Arrays.fill(seenPos, -1);
            for (int c = 0; c < GRID_SIZE; c++) {
                int v = board[r][c];
                if (v != 0) {
                    if (seenPos[v] != -1) {
                        System.out.printf("Duplicate %d in row %d (cols %d and %d)%n", v, r + 1, seenPos[v] + 1, c + 1);
                        valid = false;
                    } else {
                        seenPos[v] = c;
                    }
                }
            }
        }

        // Columns
        for (int c = 0; c < GRID_SIZE; c++) {
            int[] seenPos = new int[10];
            Arrays.fill(seenPos, -1);
            for (int r = 0; r < GRID_SIZE; r++) {
                int v = board[r][c];
                if (v != 0) {
                    if (seenPos[v] != -1) {
                        System.out.printf("Duplicate %d in column %d (rows %d and %d)%n", v, c + 1, seenPos[v] + 1, r + 1);
                        valid = false;
                    } else {
                        seenPos[v] = r;
                    }
                }
            }
        }

        // Boxes
        for (int br = 0; br < 3; br++) {
            for (int bc = 0; bc < 3; bc++) {
                int[] seenPos = new int[10]; // store previous position as (r*9 + c)
                Arrays.fill(seenPos, -1);
                for (int r = br * 3; r < br * 3 + 3; r++) {
                    for (int c = bc * 3; c < bc * 3 + 3; c++) {
                        int v = board[r][c];
                        if (v != 0) {
                            if (seenPos[v] != -1) {
                                int prev = seenPos[v];
                                int prevR = prev / 9;
                                int prevC = prev % 9;
                                System.out.printf("Duplicate %d in box (%d,%d) at cells (%d,%d) and (%d,%d)%n",
                                        v, br + 1, bc + 1, prevR + 1, prevC + 1, r + 1, c + 1);
                                valid = false;
                            } else {
                                seenPos[v] = r * 9 + c;
                            }
                        }
                    }
                }
            }
        }

        return valid;
    }

    private static void printBoard(int[][] board) {
        for(int row = 0; row < GRID_SIZE; row++){
            if(row % 3 == 0 && row != 0){
                System.out.println("----------------");
            }
            for(int column = 0; column < GRID_SIZE; column++){
                if(column % 3 == 0 && column != 0){
                    System.out.print(" | ");
                }
                System.out.print(board[row][column]);
            }
            System.out.println();
        }
    }

    private static boolean isNumberInRow_ (int[][] board, int number , int row){
        for (int i = 0; i < GRID_SIZE; i++){
            if(board[row][i] == number){
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInColumn_ (int[][] board, int number , int column){
        for (int i = 0; i < GRID_SIZE; i++){
            if(board[i][column] == number){
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInBox_(int[][] board, int number , int row, int column){
        int localBoxRow = row - (row % 3);
        int localBoxColumn = column - (column % 3);

        for( int i = localBoxRow; i < localBoxRow + 3; i++){
            for(int j = localBoxColumn; j < localBoxColumn + 3; j++){
                if(board[i][j] == number){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isValidPlacement(int[][] board, int number, int row, int column){
        return !isNumberInRow_(board, number, row) && !isNumberInColumn_(board, number, column) && !isNumberInBox_(board, number, row, column);
    }

    private static boolean solveBoard(int[][] board){
        for(int row = 0; row < GRID_SIZE; row++){
            for(int column = 0; column < GRID_SIZE; column++){
                if(board[row][column] == 0){
                    for(int numberUsed = 1; numberUsed <= GRID_SIZE; numberUsed++){
                        if(isValidPlacement(board, numberUsed, row, column)){
                            board[row][column] = numberUsed;
                            if(solveBoard(board)){
                                return true;
                            }
                            else{
                                board[row][column] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
}