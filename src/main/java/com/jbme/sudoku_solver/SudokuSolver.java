package com.jbme.sudoku_solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Sudoku solver using recursive backtracking with Minimum Remaining Values (MRV) heuristic.
 * 
 * This class provides two solving algorithms:
 * - Ascending order (1-9): tries values from 1 to 9 for each cell
 * - Descending order (9-1): tries values from 9 to 1 for each cell
 * 
 * The different orderings help detect if a puzzle has multiple solutions.
 * 
 * @author Jean-Baptiste MEYRIEUX
 */
@Component
public class SudokuSolver {

    private static final Logger logger = LoggerFactory.getLogger(SudokuSolver.class);
    
    // Standard Sudoku grid size
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;
    private static final int EMPTY_CELL = 0;

    /**
     * The 9x9 Sudoku grid where 0 represents empty cells and 1-9 represent filled cells
     */
    private int[][] sudokuGrid;

    /**
     * Gets the current Sudoku grid
     * @return Copy of the current grid
     */
    public int[][] getGrid() {
        return sudokuGrid;
    }

    /**
     * Sets the Sudoku grid to solve
     * @param gridToSolve The 9x9 grid with 0 for empty cells and 1-9 for filled cells
     */
    public void setGrid(int[][] gridToSolve) {
        this.sudokuGrid = gridToSolve;
    }

    /**
     * Solves the Sudoku puzzle using recursive backtracking with MRV heuristic
     * 
     * @param searchDirection 1 for ascending order (1-9), 2 for descending order (9-1)
     * @return Solved grid if solution exists, null if no solution or invalid input
     */
    public int[][] fillSudokuGrid(int searchDirection) {
        if (!isValidInitialGrid()) {
            logger.warn("The provided Sudoku grid is invalid - contains duplicate values");
            return null;
        }
        
        logger.info("Starting Sudoku solving with {} order", 
                   searchDirection == 1 ? "ascending" : "descending");
        
        boolean isSolved = solveUsingMRVBacktracking(searchDirection);
        
        if (isSolved) {
            logger.info("Sudoku solved successfully");
            return sudokuGrid;
        } else {
            logger.warn("No solution found for this Sudoku puzzle");
            return null;
        }
    }

    /**
     * Recursive backtracking solver using Minimum Remaining Values (MRV) heuristic.
     * Always chooses the empty cell with the fewest possible candidates to minimize backtracking.
     * 
     * @param searchDirection 1 for ascending (1-9), 2 for descending (9-1)
     * @return true if puzzle is solved, false if no solution exists
     */
    private boolean solveUsingMRVBacktracking(int searchDirection) {
        // Find the empty cell with the minimum number of possible values (MRV heuristic)
        CellPosition cellWithFewestOptions = findCellWithMinimumCandidates();
        
        if (cellWithFewestOptions == null) {
            // No empty cells remaining - puzzle is solved!
            return true;
        }
        
        int row = cellWithFewestOptions.row;
        int column = cellWithFewestOptions.column;
        
        // Try values in the specified order
        if (searchDirection == 1) {
            // Ascending: try 1, 2, 3, ..., 9
            return tryValuesInRange(row, column, MIN_VALUE, MAX_VALUE, 1, searchDirection);
        } else {
            // Descending: try 9, 8, 7, ..., 1
            return tryValuesInRange(row, column, MAX_VALUE, MIN_VALUE, -1, searchDirection);
        }
    }

    /**
     * Tries values in a specified range for a given cell
     * 
     * @param row Cell row position
     * @param column Cell column position
     * @param startValue Starting value to try
     * @param endValue Ending value to try
     * @param increment 1 for ascending, -1 for descending
     * @param searchDirection Direction for recursive calls
     * @return true if a solution is found, false otherwise
     */
    private boolean tryValuesInRange(int row, int column, int startValue, int endValue, 
                                   int increment, int searchDirection) {
        for (int candidateValue = startValue; 
             (increment > 0 && candidateValue <= endValue) || (increment < 0 && candidateValue >= endValue); 
             candidateValue += increment) {
            
            if (isValueSafeToPlace(row, column, candidateValue)) {
                // Place the value tentatively
                sudokuGrid[row][column] = candidateValue;
                
                // Recursively try to solve the rest of the puzzle
                if (solveUsingMRVBacktracking(searchDirection)) {
                    return true; // Solution found!
                }
                
                // Backtrack: remove the value and try the next one
                sudokuGrid[row][column] = EMPTY_CELL;
            }
        }
        
        // No valid value found for this cell - backtrack
        return false;
    }

    /**
     * Finds the empty cell with the minimum number of valid candidates (MRV heuristic).
     * This reduces the branching factor and speeds up the search.
     * 
     * @return Position of cell with fewest candidates, or null if no empty cells
     */
    private CellPosition findCellWithMinimumCandidates() {
        CellPosition bestCell = null;
        int minimumCandidateCount = Integer.MAX_VALUE;
        
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                if (sudokuGrid[row][column] != EMPTY_CELL) {
                    continue; // Cell is already filled
                }
                
                boolean[] possibleValues = calculatePossibleValues(row, column);
                int candidateCount = countTrueBooleans(possibleValues);
                
                if (candidateCount == 0) {
                    // Dead end - no valid values for this cell
                    return new CellPosition(row, column);
                }
                
                if (candidateCount < minimumCandidateCount) {
                    minimumCandidateCount = candidateCount;
                    bestCell = new CellPosition(row, column);
                    
                    if (minimumCandidateCount == 1) {
                        // Found a cell with only one option - choose it immediately
                        return bestCell;
                    }
                }
            }
        }
        
        return bestCell;
    }

    /**
     * Calculates which values (1-9) can be legally placed in a specific cell
     * 
     * @param row Cell row position
     * @param column Cell column position
     * @return Array where index i is true if value i can be placed in the cell
     */
    private boolean[] calculatePossibleValues(int row, int column) {
        boolean[] isPossible = new boolean[MAX_VALUE + 1]; // Index 0 unused, 1-9 used
        
        // Initially, all values 1-9 are possible
        for (int value = MIN_VALUE; value <= MAX_VALUE; value++) {
            isPossible[value] = true;
        }
        
        // Eliminate values that already exist in the same row
        for (int col = 0; col < GRID_SIZE; col++) {
            int existingValue = sudokuGrid[row][col];
            if (existingValue != EMPTY_CELL) {
                isPossible[existingValue] = false;
            }
        }
        
        // Eliminate values that already exist in the same column
        for (int r = 0; r < GRID_SIZE; r++) {
            int existingValue = sudokuGrid[r][column];
            if (existingValue != EMPTY_CELL) {
                isPossible[existingValue] = false;
            }
        }
        
        // Eliminate values that already exist in the same 3x3 subgrid
        int subgridStartRow = (row / SUBGRID_SIZE) * SUBGRID_SIZE;
        int subgridStartColumn = (column / SUBGRID_SIZE) * SUBGRID_SIZE;
        
        for (int r = subgridStartRow; r < subgridStartRow + SUBGRID_SIZE; r++) {
            for (int c = subgridStartColumn; c < subgridStartColumn + SUBGRID_SIZE; c++) {
                int existingValue = sudokuGrid[r][c];
                if (existingValue != EMPTY_CELL) {
                    isPossible[existingValue] = false;
                }
            }
        }
        
        return isPossible;
    }

    /**
     * Checks if a value can be safely placed in a specific cell without violating Sudoku rules
     * 
     * @param row Cell row position
     * @param column Cell column position
     * @param value Value to test (1-9)
     * @return true if the value can be placed safely, false otherwise
     */
    private boolean isValueSafeToPlace(int row, int column, int value) {
        boolean[] possibleValues = calculatePossibleValues(row, column);
        return possibleValues[value];
    }

    /**
     * Validates that the initial Sudoku grid follows Sudoku rules.
     * Checks for duplicates in rows, columns, and 3x3 subgrids.
     * 
     * @return true if the grid is valid, false if it contains rule violations
     */
    public boolean isValidInitialGrid() {
        // Check all rows and columns for duplicates
        for (int i = 0; i < GRID_SIZE; i++) {
            if (!isGroupValid(getRow(i)) || !isGroupValid(getColumn(i))) {
                return false;
            }
        }
        
        // Check all 3x3 subgrids for duplicates
        for (int subgridRow = 0; subgridRow < GRID_SIZE; subgridRow += SUBGRID_SIZE) {
            for (int subgridColumn = 0; subgridColumn < GRID_SIZE; subgridColumn += SUBGRID_SIZE) {
                if (!isGroupValid(getSubgrid(subgridRow, subgridColumn))) {
                    return false;
                }
            }
        }
        
        return true;
    }

    /**
     * Checks if a group of 9 cells (row, column, or subgrid) contains no duplicate values
     * 
     * @param cellGroup Array of 9 cell values
     * @return true if no duplicates exist (ignoring zeros), false if duplicates found
     */
    private boolean isGroupValid(int[] cellGroup) {
        boolean[] valuesSeen = new boolean[MAX_VALUE + 1]; // Track which values we've seen
        
        for (int cellValue : cellGroup) {
            if (cellValue != EMPTY_CELL) { // Ignore empty cells
                if (valuesSeen[cellValue]) {
                    return false; // Duplicate found!
                }
                valuesSeen[cellValue] = true;
            }
        }
        
        return true; // No duplicates found
    }

    /**
     * Extracts a complete row from the grid
     * 
     * @param rowIndex Row to extract (0-8)
     * @return Array containing all 9 values from the specified row
     */
    private int[] getRow(int rowIndex) {
        return sudokuGrid[rowIndex];
    }

    /**
     * Extracts a complete column from the grid
     * 
     * @param columnIndex Column to extract (0-8)
     * @return Array containing all 9 values from the specified column
     */
    private int[] getColumn(int columnIndex) {
        int[] columnValues = new int[GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            columnValues[row] = sudokuGrid[row][columnIndex];
        }
        return columnValues;
    }

    /**
     * Extracts a 3x3 subgrid from the main grid
     * 
     * @param startRow Starting row of the subgrid (0, 3, or 6)
     * @param startColumn Starting column of the subgrid (0, 3, or 6)
     * @return Array containing all 9 values from the specified subgrid
     */
    private int[] getSubgrid(int startRow, int startColumn) {
        int[] subgridValues = new int[GRID_SIZE];
        int index = 0;
        
        for (int row = startRow; row < startRow + SUBGRID_SIZE; row++) {
            for (int column = startColumn; column < startColumn + SUBGRID_SIZE; column++) {
                subgridValues[index++] = sudokuGrid[row][column];
            }
        }
        
        return subgridValues;
    }

    /**
     * Counts the number of true values in a boolean array
     * 
     * @param booleanArray Array to count
     * @return Number of true values
     */
    private int countTrueBooleans(boolean[] booleanArray) {
        int count = 0;
        for (boolean value : booleanArray) {
            if (value) {
                count++;
            }
        }
        return count;
    }

	    /**
     * Simple class to represent a cell position in the grid
     */
    /**
     * Logs the complete sudoku grid solution to the console in a formatted way
     * 
     * @param grid The solved sudoku grid to log
     * @param solutionType Description of the solution type (e.g., "Solution 1 (Ascending)", "Solution 2 (Descending)")
     */
    public void logSolutionToConsole(int[][] grid, String solutionType) {
        if (grid == null) {
            logger.info("{}: No solution found", solutionType);
            return;
        }
        
        logger.info("=== {} ===", solutionType);
        StringBuilder gridOutput = new StringBuilder();
        gridOutput.append("\n");
        
        for (int row = 0; row < GRID_SIZE; row++) {
            if (row % 3 == 0 && row != 0) {
                gridOutput.append("      ------+-------+------\n");
            }
            
            gridOutput.append("      ");
            for (int col = 0; col < GRID_SIZE; col++) {
                if (col % 3 == 0 && col != 0) {
                    gridOutput.append("| ");
                }
                gridOutput.append(grid[row][col]).append(" ");
            }
            gridOutput.append("\n");
        }
        
        logger.info(gridOutput.toString());
    }

    /**
     * Logs both solutions from a SudokuResponse to the console
     * 
     * @param response The SudokuResponse containing both solutions
     */
    public void logResponseToConsole(SudokuResponse response) {
        logger.info("=== SUDOKU SOLVING RESULTS ===");
        
        if (response.getSolution1() != null) {
            logSolutionToConsole(response.getSolution1(), "Solution 1 (Ascending 1-9)");
        }
        
        if (response.getSolution2() != null) {
            logSolutionToConsole(response.getSolution2(), "Solution 2 (Descending 9-1)");
        }
        
        // Check if solutions are identical or different
        if (response.getSolution1() != null && response.getSolution2() != null) {
            boolean identical = areGridsIdentical(response.getSolution1(), response.getSolution2());
            if (identical) {
                logger.info("=== ANALYSIS: Both solutions are IDENTICAL - Unique solution found ===");
            } else {
                logger.info("=== ANALYSIS: Solutions are DIFFERENT - Multiple solutions exist ===");
            }
        } else if (response.getSolution1() == null && response.getSolution2() == null) {
            logger.warn("=== ANALYSIS: NO SOLUTIONS FOUND - Unsolvable puzzle ===");
        } else {
            logger.warn("=== ANALYSIS: Only one algorithm found a solution - Possible implementation issue ===");
        }
    }

    /**
     * Helper method to compare two grids for equality
     * 
     * @param grid1 First grid to compare
     * @param grid2 Second grid to compare
     * @return true if grids are identical, false otherwise
     */
    private boolean areGridsIdentical(int[][] grid1, int[][] grid2) {
        if (grid1 == null || grid2 == null) {
            return false;
        }
        
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid1[row][col] != grid2[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static class CellPosition {
        final int row;
        final int column;
        
        CellPosition(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }
}

