package com.jbme.sudoku_solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Sudoku Grid.
 * 
 * @author Jean-Baptiste MEYRIEUX
 */

public class Grid {

    /**
     * Path to the text file containing the Sudoku grid.
     */
    private String path;

    /**
     * Constructor
     */
    public Grid() {

    }

    /**
     * "getters" for the sudoku grid path.
     * 
     * @return path of the sudoku grid.
     */
    public String getPath() {
        return path;
    }

    /**
     * "setters" for the sudoku grid path.
     * 
     * @param path path of the sudoku grid.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Method that retrieves a Sudoku grid in text format and generates a
     * two-dimensional array containing the Sudoku grid. Empty cells (.) will be
     * replaced by zeros.
     * 
     * @return sudokuGrid : an array containing the Sudoku grid. The two dimensions
     *         represent rows and columns.
     * @throws FileNotFoundException Return an error if no compatible file are
     *                               there.
     * 
     */
    public int[][] read() throws FileNotFoundException {

        /**
         * Array containing the Sudoku grid.
         *
         * <li>1er dimension = row
         * <li>2e dimension = column
         * 
         */
        int[][] sudokuGrid = new int[9][9];

        /**
         * row count.
         */
        int countLine = 0;

        File gridFile = new File(path);

        try (Scanner scan = new Scanner(gridFile)) {
            while (scan.hasNextLine()) {

                String nl = scan.nextLine();
                char ch = nl.charAt(0);

                if (ch != '#') { // If the first character is not a '#', this mean we are scanning the data.

                    for (int j = 0; j < 9; j++) {

                        if (nl.charAt(j) == '.') {// replacing the empty number with 0
                            sudokuGrid[countLine][j] = 0;

                        } else {
                            // storing number as int in the column of the grid
                            sudokuGrid[countLine][j] = Character.getNumericValue(nl.charAt(j));

                        }
                    }

                    countLine++; // next line
                }

            }
        }

        return sudokuGrid;

    }

}
