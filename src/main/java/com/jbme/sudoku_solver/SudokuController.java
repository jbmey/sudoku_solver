package com.jbme.sudoku_solver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/sudoku")
public class SudokuController {
    private static final Logger log = LoggerFactory.getLogger(SudokuController.class);

    @Autowired
    private SudokuSolver solver;

     /**
     * REST controller to Solve a Sudoku grid
     * @param grid 9x9 array of integers (0 for empty cells)
     * @return SudokuResponse containing both solutions
     */
     @PostMapping("/solve")
    public SudokuResponse solveSudoku(@RequestBody int[][] grid) {
        log.info("Received Sudoku grid for solving");
        
        // First solution (ascending)
        solver.setGrid(copyGrid(grid));
        int[][] result1 = solver.fillSudokuGrid(1);
        
        // Second solution (descending)
        solver.setGrid(copyGrid(grid));
        int[][] result2 = solver.fillSudokuGrid(2);
        
        SudokuResponse response = new SudokuResponse(result1, result2);
        
        // Log the complete response to console
        solver.logResponseToConsole(response);
        
        return response;
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
