package com.jbme.sudoku_solver;

public class SudokuResponse {
     private int[][] solution1;
    private int[][] solution2;
    private boolean multiplesSolutions;
    private boolean noSolution;
    
    public SudokuResponse(int[][] solution1, int[][] solution2) {
        this.solution1 = solution1;
        this.solution2 = solution2;
        this.multiplesSolutions = !gridEquals(solution1, solution2);
        this.noSolution = checkNoSolution(solution1, solution2);
    }
    
    private boolean gridEquals(int[][] grid1, int[][] grid2) {
        if (grid1 == null || grid2 == null) return grid1 == grid2;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid1[i][j] != grid2[i][j]) return false;
            }
        }
        return true;
    }

    private boolean checkNoSolution(int[][] grid1, int[][] grid2){
        if(grid1 == null && grid2 == null){
            return true;
        }else{
            return false;
        }
    }
    
    // Getters
    public int[][] getSolution1() { return solution1; }
    public int[][] getSolution2() { return solution2; }
    public boolean isMultiplesSolutions() { return multiplesSolutions; }
    public boolean isNoSolution() {return noSolution;}
}


