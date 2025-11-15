package com.jbme.sudoku_solver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

/**
 * Launcher that invokes the other classes of the program: {@link Grille} and
 * {@link TableauSudoku}. This launcher will load a Sudoku grid in text format
 * located at the path provided as a command-line argument. That grid will be
 * transcribed into an array. Finally, the array will be filled.
 * The operation is performed twice using a variation of the algorithm. If
 * the two results differ, the program will display both possible solutions.
 * Otherwise, the single result is displayed with the message
 * "!!!No other solutions possible!!!".
 * 
 * @author Jean-Baptiste MEYRIEUX
 *
 */

@SpringBootApplication
public class SudokuSolverApplication {
	private static final Logger log = LoggerFactory.getLogger(SudokuSolverApplication.class);

	/**
	 * 
	 * @param args Grid path in text format
	 * @throws FileNotFoundException throw error if no file is found or compatible
	 */

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(SudokuSolverApplication.class, args);
		log.info("===== SUDOKU SOLVER STARTED =====");
		String path = args[0];
		log.info("Loading sudoku from: " + path);
		log.debug("===== Debug Mod =====");

		// grid Initialization
		Grid grille1 = new Grid();
		grille1.setPath(path);

		// Transform Grid in Array
		SudokuSolver solver = new SudokuSolver();
		solver.setGrid(grille1.read());

		// Display Grid
		System.out.println("\n-SUDOKU A REMPLIR-\n");
		displayGrid(solver.getGrid());

		// Search for the 1st Solution with ascending Algorithm (1)

		int[][] resultat1 = solver.fillSudokuGrid(1);
		if (resultat1 != null) {
			System.out.println("\n-----RESULTAT 1-----\n");
			displayGrid(resultat1);
		} else {
			System.out.println("\n!!!Pas de solutions possible!!!");
		}

		// Create a second identical array for the nd solution.
		// Search for 2nd solution with descending algorithm (2)
		SudokuSolver solver2 = new SudokuSolver();
		solver2.setGrid(grille1.read());

		int[][] resultat2 = solver2.fillSudokuGrid(2);
		if ((resultat2 != null) && (Arrays.deepEquals(resultat1, resultat2) == false)) {
			System.out.println("\n-----RESULTAT 2-----\n");
			displayGrid(resultat2);
		} else {
			System.out.println("\n!!!Pas d'autres solutions possible!!!");

		}
	}

	/**
	 * Method to Diplay the Grid.
	 * 
	 * @param grid Grid to display.
	 */
	public static void displayGrid(int[][] grid) {
		int[][] display = grid;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(display[i][j] + "|");
			}
			System.out.println("\n------------------");
		}

	}

}
