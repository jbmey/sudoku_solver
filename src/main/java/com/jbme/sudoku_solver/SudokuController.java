package com.jbme.sudoku_solver;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/sudoku")
public class SudokuController {
    private static final Logger log = LoggerFactory.getLogger(SudokuController.class);
     /**
     * REST controller to Solve a Sudoku grid
     * @param grid 9x9 array of integers (0 for empty cells)
     * @return SudokuResponse containing both solutions
     */
     @PostMapping("/solve")
    public SudokuResponse solveSudoku(@RequestBody int[][] grid) {
        log.info("Received Sudoku grid for solving");
        
        // First solution (ascending)
        SudokuSolver solver1 = new SudokuSolver();
        solver1.setGrid(copyGrid(grid));
        int[][] result1 = solver1.fillSudokuGrid(1);
        
        // Second solution (descending)
        SudokuSolver solver2 = new SudokuSolver();
        solver2.setGrid(copyGrid(grid));
        int[][] result2 = solver2.fillSudokuGrid(2);
        
        return new SudokuResponse(result1, result2);
    }

     /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public String health() {
        return "Sudoku Solver API is running";
    }

    /**
     * Helper method to deep copy grid
     */
    private int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copy[i][j] = grid[i][j];
            }
        }
        return copy;
    }



}
