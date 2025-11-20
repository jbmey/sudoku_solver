package com.jbme.sudoku_solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class SudokuSolverTests {

    private SudokuSolver solver;

    // Valid Sudoku puzzle with unique solution
    private final int[][] VALID_PUZZLE = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    // Already solved puzzle
    private final int[][] SOLVED_PUZZLE = {
        {5, 3, 4, 6, 7, 8, 9, 1, 2},
        {6, 7, 2, 1, 9, 5, 3, 4, 8},
        {1, 9, 8, 3, 4, 2, 5, 6, 7},
        {8, 5, 9, 7, 6, 1, 4, 2, 3},
        {4, 2, 6, 8, 5, 3, 7, 9, 1},
        {7, 1, 3, 9, 2, 4, 8, 5, 6},
        {9, 6, 1, 5, 3, 7, 2, 8, 4},
        {2, 8, 7, 4, 1, 9, 6, 3, 5},
        {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };

    // Invalid puzzle - duplicate 5 in first row
    private final int[][] INVALID_PUZZLE_ROW = {
        {5, 3, 5, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    // Invalid puzzle - duplicate 5 in first column
    private final int[][] INVALID_PUZZLE_COLUMN = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {5, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    // Invalid puzzle - duplicate 3 in top-left 3x3 subgrid
    private final int[][] INVALID_PUZZLE_SUBGRID = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 3, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    // Empty grid
    private final int[][] EMPTY_GRID = {
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    @BeforeEach
    void setUp() {
        solver = new SudokuSolver();
    }

    @Test
    @DisplayName("Should solve valid puzzle with ascending order")
    void shouldSolveValidPuzzleAscending() {
        // Arrange
        solver.setGrid(deepCopy(VALID_PUZZLE));

        // Act
        int[][] result = solver.fillSudokuGrid(1);

        // Assert
        assertNotNull(result);
        assertTrue(isValidSudokuUsingHelper(result));
        assertTrue(isCompleteSolution(result));
    }

    @Test
    @DisplayName("Should solve valid puzzle with descending order")
    void shouldSolveValidPuzzleDescending() {
        // Arrange
        solver.setGrid(deepCopy(VALID_PUZZLE));

        // Act
        int[][] result = solver.fillSudokuGrid(2);

        // Assert
        assertNotNull(result);
        assertTrue(isValidSudokuUsingHelper(result));
        assertTrue(isCompleteSolution(result));
    }

    @Test
    @DisplayName("Should produce different solutions for ascending vs descending order")
    void shouldProduceDifferentSolutionsForDifferentDirections() {
        // Arrange
        solver.setGrid(deepCopy(VALID_PUZZLE));
        int[][] ascendingSolution = solver.fillSudokuGrid(1);

        solver.setGrid(deepCopy(VALID_PUZZLE));
        int[][] descendingSolution = solver.fillSudokuGrid(2);

        // Assert
        assertNotNull(ascendingSolution);
        assertNotNull(descendingSolution);
        
        assertTrue(isValidSudokuUsingHelper(ascendingSolution));
        assertTrue(isValidSudokuUsingHelper(descendingSolution));
    }

    @Test
    @DisplayName("Should return null for invalid puzzle with duplicate in row")
    void shouldReturnNullForInvalidPuzzleRow() {
        // Arrange
        solver.setGrid(INVALID_PUZZLE_ROW);

        // Act
        int[][] result = solver.fillSudokuGrid(1);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should return null for invalid puzzle with duplicate in column")
    void shouldReturnNullForInvalidPuzzleColumn() {
        // Arrange
        solver.setGrid(INVALID_PUZZLE_COLUMN);

        // Act
        int[][] result = solver.fillSudokuGrid(1);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should return null for invalid puzzle with duplicate in subgrid")
    void shouldReturnNullForInvalidPuzzleSubgrid() {
        // Arrange
        solver.setGrid(INVALID_PUZZLE_SUBGRID);

        // Act
        int[][] result = solver.fillSudokuGrid(1);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should handle already solved puzzle")
    void shouldHandleAlreadySolvedPuzzle() {
        // Arrange
        solver.setGrid(SOLVED_PUZZLE);

        // Act
        int[][] result = solver.fillSudokuGrid(1);

        // Assert
        assertNotNull(result);
        assertArrayEquals(SOLVED_PUZZLE, result);
        assertTrue(isValidSudokuUsingHelper(result));
    }

    @Test
    @DisplayName("Should solve empty grid")
    void shouldSolveEmptyGrid() {
        // Arrange
        solver.setGrid(EMPTY_GRID);

        // Act
        int[][] result = solver.fillSudokuGrid(1);

        // Assert
        assertNotNull(result);
        assertTrue(isValidSudokuUsingHelper(result));
        assertTrue(isCompleteSolution(result));
    }

    @Test
    @DisplayName("Should validate correct initial grid")
    void shouldValidateCorrectInitialGrid() {
        // Arrange
        solver.setGrid(VALID_PUZZLE);

        // Act & Assert
        assertTrue(solver.isValidInitialGrid());
    }

    @Test
    @DisplayName("Should invalidate grid with row duplicates")
    void shouldInvalidateGridWithRowDuplicates() {
        // Arrange
        solver.setGrid(INVALID_PUZZLE_ROW);

        // Act & Assert
        assertFalse(solver.isValidInitialGrid());
    }

    @Test
    @DisplayName("Should invalidate grid with column duplicates")
    void shouldInvalidateGridWithColumnDuplicates() {
        // Arrange
        solver.setGrid(INVALID_PUZZLE_COLUMN);

        // Act & Assert
        assertFalse(solver.isValidInitialGrid());
    }

    @Test
    @DisplayName("Should invalidate grid with subgrid duplicates")
    void shouldInvalidateGridWithSubgridDuplicates() {
        // Arrange
        solver.setGrid(INVALID_PUZZLE_SUBGRID);

        // Act & Assert
        assertFalse(solver.isValidInitialGrid());
    }

    @Test
    @DisplayName("Should get and set grid correctly")
    void shouldGetAndSetGridCorrectly() {
        // Arrange
        int[][] testGrid = deepCopy(VALID_PUZZLE);

        // Act
        solver.setGrid(testGrid);
        int[][] retrievedGrid = solver.getGrid();

        // Assert
        assertArrayEquals(testGrid, retrievedGrid);
        assertSame(testGrid, retrievedGrid); // Should be the same reference
    }

    @Test
    @DisplayName("Should preserve original grid when solving fails")
    void shouldPreserveOriginalGridWhenSolvingFails() {
        // Arrange
        int[][] originalGrid = deepCopy(INVALID_PUZZLE_ROW);
        int[][] expectedGrid = deepCopy(INVALID_PUZZLE_ROW);
        solver.setGrid(originalGrid);

        // Act
        solver.fillSudokuGrid(1);

        // Assert
        assertArrayEquals(expectedGrid, solver.getGrid());
    }

    // Helper methods

    /**
     * Creates a deep copy of a 2D array
     */
    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    /**
     * Validates that a Sudoku solution follows all rules using SudokuSolver's helper methods
     */
    private boolean isValidSudokuUsingHelper(int[][] grid) {
        // First, check that all cells are filled with values 1-9
        if (!isCompleteSolution(grid)) {
            return false;
        }

        // Create a temporary solver to use its validation methods
        SudokuSolver tempSolver = new SudokuSolver();
        tempSolver.setGrid(grid);
        
        // Use the solver's existing validation method
        return tempSolver.isValidInitialGrid();
    }

    /**
     * Checks if the solution is complete (no empty cells) and all values are 1-9
     */
    private boolean isCompleteSolution(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = grid[row][col];
                if (value < 1 || value > 9) {
                    return false;
                }
            }
        }
        return true;
    }
}